#!/bin/sh

# TODO: do not copy .svn directory in .jar

WORKING_DIR=makejar
JAR_NAME=net.kernub.gwtcipher.client.jar

# cleaning first
echo "\t *** Cleaning first..."
ant clean

# build a compiled version
echo "\t *** Build a compiled version..."
ant
if [ $? -ne 0 ]; then
	echo "\t *** Error building GWTCipher. Abording."
	exit 1
fi

# create working directory
echo "\t *** Create working directory..."
mkdir -p ${WORKING_DIR}
if [ $? -ne 0 ]; then
	echo "\t *** Error creating working directory. Abording."
	exit 1
fi;

echo "\t *** Copying compiled *.class files to working directory (${WORKING_DIR})"
cp -R war/WEB-INF/classes/net ${WORKING_DIR}/

echo "\t *** Removing server directory, and replacing GWTCipher.gwt.xml"
rm -rf ${WORKING_DIR}/net/kernub/gwtcipher/server
rm -rf ${WORKING_DIR}/net/kernub/gwtcipher/GWTCipher.gwt.xml
cat <<EOF >${WORKING_DIR}/net/kernub/gwtcipher/GWTCipher.gwt.xml
<module>
  <!-- Inherit the core Web Toolkit stuff.                        -->
  <inherits name='com.google.gwt.user.User'/>
  <inherits name='com.google.gwt.http.HTTP'/>

	<!-- Inherit theme 																							-->
  <inherits name='com.google.gwt.user.theme.standard.Standard'/>
</module>
EOF

echo "\t *** Copying source *.java files to working directory (${WORKING_DIR})"
cp -R src/net/kernub/gwtcipher/client/* ${WORKING_DIR}/net/kernub/gwtcipher/client/

echo "\t *** Creating .jar file and indexing"
cd ${WORKING_DIR} && jar -cvf ../${JAR_NAME} net/ && cd -
if [ $? -ne 0 ]; then
	echo " *** Error while creating .jar file. Abording."
	exit 1
fi
jar i ${JAR_NAME}
if [ $? -ne 0 ]; then
	echo " *** Error while indexing .jar file."
fi

echo "\t *** Removing working directory"
rm -rf ${WORKING_DIR}

echo "\t *** ${JAR_NAME} is ready"
exit 0


