Dear OpenDDR user, 

Directory Tree
==============

The directory setup is the typical setup for a Maven (http://maven.apache.org/) style project, with a few exceptions in order to keep you from having to use maven.  The directories are as follows:

    src/main/java:  Java source files
    src/main/resources:  the oddr.properties template file *and* XML files needed running
    src/test/java:  Unit tests go here ... crickets ...

Building
========

There are a couple of steps you need to do before you can compile a usable version.  The first thing you need to do is to get the device XML files.  They are not distributed in the source code.  With a bit of searching, you can find the files on the http://openddr.org/ web site.  A few of the file names are _BrowserDataSource.xml_, _coreVocabulary.xml_, and _DeviceDataSource.xml_.  There are about 9 XML files in all (as of this writing). See _oddr.properites_ for a full list.

Once you have the XML files, put them into the _/src/main/resources_ directory.  From there, they will be compiled into the final OpenDDR-Java jar (don't put them there if you are going to use the _full system path_ way).

Before you compile, have a look at the _pom.xml_ file.

In an older version of OpenDDR, the path to the XML files was the full system path to the XML files.  If you like that behaviour, set the property _oddr.resourcePath_ to the directory where you are storing the XML files.  Be sure to begin and end the path with a slash.  Leaving the value with the default will compile the XML files into the jar.

Ok, finally you can use maven to build the project by using the following:

  $ mvn package

Doing _package_ will create a jar from the source code, and also copy all the other jar files you will need into the _dist_ directory.  You can then copy all the jar files into your project's classpath and you should be good to go.

Oh, and lastly:

  $ mvn clean

will remove any temporary directories.

*Note* if you are into maven, you will notice that within the _pom.xml_ the jar files are obtained from the local file system (the _/src/main/lib_ directory).  This is not as it should be. However, I am currently forking this project, and don't want to assume moving everything to maven is ok.

If you desire to use maven in the "right way", then you will need to edit the dependency section of the _pom.xml_ file.



oddr.properties - the "XML files"
=================================

The following is a basic explanation of the properties in oddr.properties.

oddr.ua.device.builder.path
---------------------------
Path of the file that explain how to identify the devices. In this, for each builder, are specified the devices id that the builder handle and the identification rules.

oddr.ua.device.datasource.path
------------------------------
Path of the device datasource

oddr.ua.device.builder.patch.paths
----------------------------------
Path of the patch file for the builder file.

oddr.ua.device.datasource.patch.paths
-------------------------------------
Path of the patch file for the device data source

oddr.ua.browser.datasource.path
-------------------------------
Path of the browser data source

oddr.ua.operatingSystem.datasource.path
----------------------------------------
Path of the operating system data source

oddr.vocabulary.core.path
-------------------------
Path of the W3C vocabulary file

oddr.vocabulary.path
---------------------
Path of OpenDDR vocabulary

oddr.limited.vocabulary.path
-----------------------------
Path of the reduced vocabulary. This vocabulary is useful to limit the memory load. It can be safely left unspecified.

oddr.vocabulary.device
----------------------
IRI of the default vocabulary. It is the target namespace specified in a vocabulary

oddr.threshold
--------------
Identification threshold. It is used to balance the request evaluation time and identification matching.


Grails Injected Service
=======================

To use OpenDDR in a grails web application, you can do the following after having copied all the jars from the _Building_ section into your classpath (for example, the top level _lib_ directory).

1) Edit _resources.groovy_

    Properties deviceProps = new Properties()
    deviceProps.load(this.class.classLoader.getResource('oddr.properties').openStream())
    deviceService(
        org.openddr.simpleapi.oddr.DeviceService,
        org.openddr.simpleapi.oddr.ODDRVocabularyService.ODDR_LIMITED_VOCABULARY_IRI,
        deviceProps
    )

This will add the service _deviceService_ that you can call from your services, or (more likely) a filter.

*Note* after adding the above you will notice an increased slowness when starting up.

2) Add a filter in _conf_ - _DetectionFilters.groovy_ for example.  The important parts are shown below

    ...
    class DetectionFilters {

        DeviceService deviceService

        def filters = {
            all(controller:'*', action:'*') {
                before = {
                    Evidence evidence = new ODDRHTTPEvidence();
                    evidence.put('User-Agent', request.getHeader('user-agent'))

                    def vendorRef = deviceService.newPropertyRef('vendor')
                    def modelRef = deviceService.newPropertyRef('model')
                    def displayWidthRef = deviceService.newPropertyRef('displayWidth')
                    def displayHeightRef = deviceService.newPropertyRef('displayHeight')
                    PropertyRef[] propertyRefs = [vendorRef, modelRef, displayWidthRef, displayHeightRef]

                    PropertyValues propertyValues = deviceService.getPropertyValues(evidence, propertyRefs)
                    PropertyValue vendor = propertyValues.getValue(vendorRef)
                    PropertyValue model = propertyValues.getValue(modelRef)
                    PropertyValue displayWidth = propertyValues.getValue(displayWidthRef)
                    PropertyValue displayHeight = propertyValues.getValue(displayHeightRef)

                    request.setAttribute('vendor', vendor?.getString())
                    request.setAttribute('model', model?.getString())
                    request.setAttribute('displayWidth', displayWidth?.getInteger())
                    request.setAttribute('displayHeight', displayHeight?.getInteger())
                }
            }
        }
        ...

You should now be able to inspect _request.vendor_, _request.displayWidth_, etc from any request.

Servlet Filter Example
======================

This is a java example on how to integrate OpenDDR in a servlet Filter:

	package org.openddr.samplewebapp.filter;

	import java.io.IOException;
	import java.util.Properties;
	import javax.servlet.Filter;
	import javax.servlet.FilterChain;
	import javax.servlet.FilterConfig;
	import javax.servlet.ServletContext;
	import javax.servlet.ServletException;
	import javax.servlet.ServletRequest;
	import javax.servlet.ServletResponse;
	import javax.servlet.http.HttpServletRequest;
	import org.openddr.simpleapi.oddr.ODDRService;
	import org.openddr.simpleapi.oddr.model.ODDRHTTPEvidence;
	import org.w3c.ddr.simple.Evidence;
	import org.w3c.ddr.simple.PropertyRef;
	import org.w3c.ddr.simple.PropertyValue;
	import org.w3c.ddr.simple.PropertyValues;
	import org.w3c.ddr.simple.Service;
	import org.w3c.ddr.simple.ServiceFactory;
	import org.w3c.ddr.simple.exception.NameException;

	public class IdentificationFilter implements Filter {

	    private Service identificationService = null;

	    public void init(FilterConfig filterConfig) throws ServletException {
            System.out.println("Initialize filter");
            Properties initializationProperties = new Properties();
            ServletContext context = filterConfig.getServletContext();

            try {
                initializationProperties.load(context.getResourceAsStream("/WEB-INF/oddr.properties"));
                identificationService = ServiceFactory.newService("org.openddr.simpleapi.oddr.ODDRService", initializationProperties.getProperty(ODDRService.ODDR_VOCABULARY_IRI), initializationProperties);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
	    }

	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            PropertyRef vendorRef;
            PropertyRef modelRef;
            PropertyRef displayWidthRef;
            PropertyRef displayHeightRef;

            try {
                vendorRef = identificationService.newPropertyRef("vendor");
                modelRef = identificationService.newPropertyRef("model");
                displayWidthRef = identificationService.newPropertyRef("displayWidth");
                displayHeightRef = identificationService.newPropertyRef("displayHeight");

            } catch (NameException ex) {
                throw new RuntimeException(ex);
            }

            PropertyRef[] propertyRefs = new PropertyRef[] {vendorRef, modelRef, displayWidthRef, displayHeightRef};
            Evidence e = new ODDRHTTPEvidence();
            e.put("User-Agent", ((HttpServletRequest)request).getHeader("User-Agent"));

            try {
                PropertyValues propertyValues = identificationService.getPropertyValues(e, propertyRefs);
                PropertyValue vendor = propertyValues.getValue(vendorRef);
                PropertyValue model = propertyValues.getValue(modelRef);
                PropertyValue displayWidth = propertyValues.getValue(displayWidthRef);
                PropertyValue displayHeight = propertyValues.getValue(displayHeightRef);

                if (vendor.exists() && model.exists() && displayWidth.exists() && displayHeight.exists()) {
                    ((HttpServletRequest)request).setAttribute("vendor", vendor.getString());
                    ((HttpServletRequest)request).setAttribute("model", model.getString());
                    ((HttpServletRequest)request).setAttribute("displayWidth", displayWidth.getInteger());
                    ((HttpServletRequest)request).setAttribute("displayHeight", displayHeight.getInteger());

                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            chain.doFilter(request, response);
	    }

	    public void destroy() {
		    System.out.println("Destroy Filter");
	    }
	}
