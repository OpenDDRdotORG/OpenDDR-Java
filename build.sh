#!/bin/bash
# Quick and dirty build shell script
mkdir build
find ./src -name "*.java" > sources_list.txt
javac -classpath `find lib -name '*.jar' | xargs echo | tr ' ' ':'` @sources_list.txt -d build
jar -cf OpenDDR.jar -C build/ .
