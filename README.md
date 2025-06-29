# Galette

Galette is a system for performing dynamic taint tracking in the Java Virtual Machine (JVM).
Galette uses Java bytecode instrumentation to associate labels, also called "taint tags",
with program data and to propagate these labels along information "flows" at runtime.

Galette requires Java 17 to build, but it can also be used on Java 8 through 21.

## Building Galette

### Requirements

* Java Development Kit (JDK) 17
* [Apache Maven](https://maven.apache.org/) 3.6.0+

### Steps

1. Clone or download this repository.
2. Ensure that some version of the JDK 17 is installed.
   We recommend using a JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/),
   the [Adoptium Working Group](https://adoptium.net/temurin/releases/), or [Amazon](https://aws.amazon.com/corretto/).
3. Set the JAVA_HOME environmental variable to the path of this JDK installation.
   On Linux and Mac, this can be done by running `export JAVA_HOME=<PATH-TO-JDK>`, where &lt;PATH-TO-JDK&gt; is the path
   of the JDK installation.
4. Ensure that you have installed Apache Maven 3.6.0+.
   Directions for [downloading](https://maven.apache.org/download.cgi)
   and [installing](https://maven.apache.org/install.html) Maven are available on the project page for Maven.
5. In the root directory of this project (the one where this README file is located), run `mvn -DskipTests install`.
   This will compile Galette's source code, package the source code into JARs, and install the produced JARs into your local Maven repository.

## Running Galette's Tests

Once you have built Galette according to the directions described above, you can run the tests and examples.
Although Galette currently requires Java 17 to build, it can also be used on Java 8.
If you would like to run Galette's tests on Java 8, build Galette using Java 17, then change the
`JAVA_HOME` environmental variable to the path of a JDK 8 installation before running the tests.
To run the root directory of this project, run `mvn -pl :galette-integration-tests verify`.
The first time you run this command, Maven will invoke the Galette Maven plugin to create
Galette-instrumented Java installations.
These instrumented Java installations are cached for future use and will not be recreated unless one of the
Galette JARs, the configuration used to create them, or the value of `JAVA_HOME` changes.
Once the Galette Maven plugin finishes creating the instrumented Java installations, the tests will run.
These tests demonstrate how Galette can be used and are a good reference when first learning Galette.

## Creating an Instrumented Java Installation

To track the flow of information through classes in the Java Class Library (JCL), such as `java.lang.String`
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

### Using the Galette Maven Plugin
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

See the documentation for the 
[InstrumentMojo](galette-maven-plugin/src/main/java/edu/neu/ccs/prl/galette/plugin/InstrumentMojo.java)
for more information about the Galette Maven plugin.

### Using the Galette Instrument JAR

Galette-instrumented Java installations can also be created using the Galette instrument JAR by running:

```shell
<JAVA_HOME>/bin/java -jar <GALETTE_INSTRUMENT_JAR> <JAVA_HOME> <OUTPUT_DIRECTORY>
```

Where:
- `<JAVA_HOME>` is the path of the Java installation to be instrumented.
- `<GALETTE_INSTRUMENT_JAR>` is the path of Galette's instrument JAR.
  If you built Galette according to instructions in this manual, this will be
  `galette-instrument/target/galette-instrument-1.0.0-SNAPSHOT.jar`.
- `<OUTPUT_DIRECTORY>` is the location to which the instrumented Java installation should be written.

## Running Your Application with Galette
Once you have created an instrumented Java installation, you can use Galette to track information flows in any Java application.
All Java applications are run by invoking the `java` executable.
This invocation comes in two forms.

The first form specifies the class to be invoked:
```shell
java [ <OPTIONS> ] <CLASS> [ <ARGUMENT> ... ]
```

The second form specifies a JAR file.
```shell
java [ <OPTIONS> ] -jar <JAR> [ <ARGUMENT> ... ]
```

Where:
- `<CLASS>` is the name of the class to be invoked.
- `<ARGUMENT>` is an argument passed to the main function.
- `<OPTIONS>` specifies command-line options used to configure the Java runtime.
- `<JAR>` is the name of the jar file to be invoked.

When using Galette you will modify these commands in two ways.
First, you will invoke the `java` executable in the instrumented Java installation that you created instead of the 
original `java` executable.
Second, you will add Java options that will configure the Java runtime to load Galette as a Java programming language agent.

For class-based `java` invocations, use:
```shell
<INSTRUMENTED_JAVA_HOME>/bin/java [ <OPTIONS> ] -Xbootclasspath/a:<GALETTE_AGENT_JAR> -javaagent:<GALETTE_AGENT_JAR> <CLASS> [ <ARGUMENT> ... ]
```

For JAR-based invocations, use:
```shell
<INSTRUMENTED_JAVA_HOME>/bin/java [ <OPTIONS> ] -Xbootclasspath/a:<GALETTE_AGENT_JAR> -javaagent:<GALETTE_AGENT_JAR> -jar <JAR> [ <ARGUMENT> ... ]
```

Where:
- `<CLASS>` is the name of the class to be invoked.
- `<ARGUMENT>` is an argument passed to the main function.
- `<OPTIONS>` specifies command-line options used to configure the Java runtime.
- `<JAR>` is the name of the jar file to be invoked.
- `<GALETTE_AGENT_JAR>` is the path of Galette's agent JAR.
If you built Galette according to instructions in this manual, this will be
`galette-agent/target/galette-agent-1.0.0-SNAPSHOT.jar`.
- `<INSTRUMENTED_JAVA_HOME>` is the path of the Galette-instrumented Java installation that you created.


## Interacting with Galette
The [edu.neu.ccs.prl.galette.internal.runtime.Tainter](galette-agent/src/main/java/edu/neu/ccs/prl/galette/internal/runtime/Tainter.java)
class provides methods that can be used to associate taint tags with values and to access the taint tag associated with a value.

To associate a taint tag with a value, start by creating an instance of 
[`edu.neu.ccs.prl.galette.internal.runtime.Tag`](galette-agent/src/main/java/edu/neu/ccs/prl/galette/internal/runtime/Tag.java).
Now, call the static method `Tainter#setTag`, passing it the value and the tag you created:
```java
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;

public class SetExample {
    public static int set(int x) {
        Tag tag = Tag.of("my_label");
        return Tainter.setTag(x, tag);
    }
}
```

To access the taint tag associated with a value, call the static method `Tainter#getTag`, 
passing it the value whose tag you want to retrieve:
```java
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;

public class GetExample {
    public static void get(int x) {
        Tag tag = Tainter.getTag(x);
        Object[] labels = Tag.getLabels(tag);
        assert(labels[0].equals("my_label"));
    }
}
```

For more examples, see the tests in the [`galette-integration-tests`](galette-integration-tests) module.

## Acknowledgements

Galette makes use of the following libraries:

* [ASM](http://asm.ow2.org/), (c) INRIA, France
  Telecom, [license](http://asm.ow2.org/license.html)
* [Apache Harmony](https://harmony.apache.org), (c) The Apache Software
  Foundation, [license](http://www.apache.org/licenses/LICENSE-2.0)