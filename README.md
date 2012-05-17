OpenDDR-Java
==============
Dear OpenDDR user, 

In the follow the description of directory tree:
* lib: contains necessary libraries to run OpenDDR-Simple-API;
* src: contains java source code and the oddr.properties file. Please configure the oddr.properties file with the right path of the resources.


A basic explanation of the properties in oddr.properties:
* __oddr.ua.device.builder.path__: Path of the file that explain how to identify the devices. In this, for each builder, are specified the devices id that the builder handle and the identification rules
* __oddr.ua.device.datasource.path__: Path of the device datasource
* __oddr.ua.device.builder.patch.paths__: Path of the patch file for the builder file
* __oddr.ua.device.datasource.patch.paths__: Path of the patch file for the device data source
* __oddr.ua.browser.datasource.path__: Path of the browser data source
* __oddr.ua.operatingSystem.datasource.path__: Path of the operating system data source
* __ddr.vocabulary.core.path__: Path of the W3C vocabulary file
* __oddr.vocabulary.path__: Path of OpenDDR vocabulary
* __oddr.limited.vocabulary.path__: Path of the reduced vocabulary. This vocabulary is usefull to limitate the memory load. It can be safely left unspecified.
* __oddr.vocabulary.device__: IRI of the default vocabulary. It is the target namespace specified in a vocabulary
* __oddr.threshold__: Identification threshold. It is used to balance the request evaluation time and identification matching.

This is a java example on how to integrate OpenDDR in a servlet Filter:
<pre><code>
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
</code></pre>
