# EDI Testing

Project tests interlok-edi-legacy features

## What it does

This project contains a single Interlok instance that allows you to run transformations both from xml to edi and the reverse.

We have two workflows, the first allows you to drop your XML file into the __./xml-in__ directory, which will run the XML-to-EDI transformation, the resulting EDI will be produced to the __./edi-out__ directory, the second workflow will allow you to run the reverse transformation, by dropping an EDI file into the __./edi-in__ directory and the resulting xml transformation produced into the __./xml-out__ directory.

For ease of testing both a sample XML and EDI input files have been provided; __ordersD96a.xml__ and __ordersD96a.edi__ respectively.

![edi diagram](/edi.png "edi diagram")
 
## Getting started

* `./gradlew clean build`
* `(cd ./build/distribution && java -jar lib/interlok-boot.jar)`

Do note this instance will require a license.

