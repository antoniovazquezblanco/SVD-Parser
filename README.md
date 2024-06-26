# SVD Parser

[![Build](https://github.com/antoniovazquezblanco/SVD-Parser/actions/workflows/main.yml/badge.svg)](https://github.com/antoniovazquezblanco/SVD-Parser/actions/workflows/main.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.antoniovazquezblanco/svd-parser)](https://central.sonatype.com/artifact/io.github.antoniovazquezblanco/svd-parser)


A Java library to parse CMSIS SVD files.


## Development

Install Eclipse. Once in Eclipse click `File > Open Projects from File System...`. On the import dialog select `SVD-Parser` folder and import the project.

You are ready for building, testing and developing.


## Installing

The package is published to [Maven Central](https://central.sonatype.com/artifact/io.github.antoniovazquezblanco/svd-parser) and [Github package repository](https://github.com/antoniovazquezblanco/SVD-Parser/packages/2011818).

Those pages provide installation snippets, visit them for more information.


## Usage

Parse from a file and print all peripherals:

```java
File file = new File("my.svd");
SvdDevice device = SvdDevice.fromFile(file);
for (SvdPeripheral peripheral : device.getPeripherals()) {
    System.out.println(peripheral.toString());
}
```
