# DAA-Issuer
This is Issuer in DAA protocol implemeted to web service

INIT
You have to acquire the IAIK library for yourself. One can download evaluation versions from https://jce.iaik.tugraz.at/crm/freeDownload.php. Download jce_full v5.3, ECCelerate 3.01 (including addons). Unpack the archives and install the jars into the local maven repository with the following commands:

mvn install:install-file -Dfile=iaik_jce_full.jar -DgroupId=at.tugraz.iaik -DartifactId=jce-full -Dversion=5.3 -Dpackaging=jar

mvn install:install-file -Dfile=iaik_eccelerate.jar -DgroupId=at.tugraz.iaik -DartifactId=eccelerate -Dversion=3.01 -Dpackaging=jar

mvn install:install-file -Dfile=iaik_eccelerate_addon.jar -DgroupId=at.tugraz.iaik -DartifactId=eccelerate-addon -Dversion=3.01 -Dpackaging=jar
