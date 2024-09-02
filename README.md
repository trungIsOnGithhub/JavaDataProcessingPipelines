#### ETL Java

## Overview
- Text Mining with TF-IDF in MapReduce implmentation, using Cascading library in Java.

## Some Details about Cascading
- Cascading offer a core notion of **WorkFlow**. For a typical *MapReduce job* with **mapper** and **reducer**, Cascading let us focus on the whole application, developing application as a *DAG(Directed Acyclic Graph) of MapReduce job steps*.
- To implement those pipelines, Cascading offer some concepts like *Source, Sink, Tap, Pipes* and many other data types.
- With Cascading, you can package your entire MapReduce application, including its orchestration and testing, within a single JAR file.

## Using Gradle Build Tools
- To build project, use below command if Gradle is already installed
```sh
gradle clean jar
```
## Using Gradle Wrapper
- It is a recommended way to use Gradle on other's code distribution. Wrapper script invokes a declared version of Gradle, download it beforehand if necessary.
- Just replace the ```gradle``` command with ```gradlew``` or ```gradle.bat``` to use Gradle Wrapper.
- Typical:
```sh
gradle build
```
- Using Wrapper:
```sh
./gradlew build
# or .\gradlew.bat build
```