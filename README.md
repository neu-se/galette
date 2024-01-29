# Galette

Galette is a system for performing dynamic taint tracking in the Java Virtual Machine (JVM).
Galette uses Java bytecode instrumentation to associate labels, also called "taint tags",
with program data and to propagate these labels along information "flows" at runtime.

Galette requires Java 11+ to build, but it can also be used on Java 8.

## Building Galette

### Requirements

* Java Development Kit (JDK) 11+
* [Apache Maven](https://maven.apache.org/) 3.6.0+

### Steps

1. Clone or download this repository.
2. Ensure that some version of the JDK 11+ is installed.
   We recommend using a JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/),
   the [Adoptium Working Group](https://adoptium.net/temurin/releases/), or [Amazon](https://aws.amazon.com/corretto/).
3. Set the JAVA_HOME environmental variable to the path of this JDK installation.
   On Linux and Mac, this can be done by running `export JAVA_HOME=<PATH-TO-JDK>`, where &lt;PATH-TO-JDK&gt; is the path
   of the JDK installation.
4. Ensure that you have installed Apache Maven 3.6.0+.
   Directions for [downloading](https://maven.apache.org/download.cgi)
   and [installing](https://maven.apache.org/install.html) Maven are available on the project page for Maven.
5. In the root directory of this project (the one where this README file is located), run `mvn -DskipTests install`.

## Running Galette's Tests

Once you have built Galette according to the directions described above, you can run the tests and examples.
Although Galette currently requires Java 11+ to build, it can also be used on Java 8.
If you would like to run Galette's tests on Java 8, build Galette using Java 11+, then change the
`JAVA_HOME` environmental variable to the path of a JDK 8 installation before running the tests.
To run the root directory of this project, run `mvn -pl :galette-integration-tests verify`.
The first time you run this command, Maven will invoke the Galette Maven plugin to create
Galette-instrumented Java installations.
These instrumented Java installations are cached for future use and will not be recreated unless one of the
Galette JARs, the configuration used to create them, or the value of `JAVA_HOME` changes.
Once the Galette Maven plugin finishes creating the instrumented Java installations, the tests will run.
These tests demonstrate how Galette can be used and are a good reference when first learning Galette.

## Creating an Instrumented Java Installation

In order to track the flow of information through classes in the Java Class Library (JCL), such as `java.lang.String`
and `java.util.List`, Galette must instrument the bytecode of JCL classes.
Therefore, the first step when using Galette is to create an instrumented Java installation
(i.e., Java Development Kit or Java Runtime Environment).
A Java installation can be downloaded from [Oracle](https://www.oracle.com/java/technologies/downloads/) or
the [Adoptium Working Group](https://adoptium.net/temurin/releases/).
Once you have obtained a Java installation, it can be instrumented using Galette's
Maven plugin.

**Important note on Oracle's Java installations:**
Oracle's Java installations require that the JAR that contains the cryptography routines `jce.jar` be signed by
Oracle for export control purposes.
Galette instrumentation will break these signatures.
Therefore, it is not possible to use Galette with Oracle's Java installation *and* use the
cryptography functionality.

To create a Galette-instrumented Java installation as part of your Maven build, add the
`galette-maven-plugin` in your pom:

```
<build>
    ...
    <plugins>
        ...
        <plugin>
            <groupId>edu.neu.ccs.prl.galette</groupId>
            <artifactId>galette-maven-plugin</artifactId>
           <version>VERSION</version>
        </plugin>
        ...
    </plugins>
    ...
</build>
```

[//]: # (TODO describe plugin option and how to run without adding to build)

## Running Your Application with Galette

[//]: # (TODO)

## Interacting with Galette

[//]: # (TODO)

## License

This software release is licensed under the BSD 3-Clause License.

Copyright (c) 2024, Katherine Hough and Jonathan Bell.

All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
   disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

## Acknowledgements

Galette makes use of the following libraries:

* [ASM](http://asm.ow2.org/), (c) INRIA, France
  Telecom, [license](http://asm.ow2.org/license.html)
* [Apache Harmony](https://harmony.apache.org), (c) The Apache Software
  Foundation, [license](http://www.apache.org/licenses/LICENSE-2.0)