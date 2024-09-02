#### ETL Java

## Overview
- Text Mining with TF-IDF in MapReduce implmentation, using Cascading library in Java.
- TF-IDF calculates a metric for each token indicating how **important** that token is to a document within a collection of documents.
- Tokens which appear in most documents tend to have very low TF-IDF weights.
- On the other hand, tokens which less common but appear multiple times in a few documents tend to have very high TF-IDF weights.
- Play an important role in the TF-IDF algorithm  drive the indexing in some text search engines
- TF-IDF is calculated based on:
1. term count in a given document
2. document frequency how appears across all documents
3. number of terms: total number of terms in a given document
4. document count: total number of documents


## Scrubing Operation for Data Stream
- In my example, I handled the data from a file in a stream to process data with lower memory usage.
- Scrubbing operation, as the data is in form of stream, is placed in the pipe to usually clean up the token stream for the whole pipeline.
![word_count](/assets/wc-with-scrub-pipeline.png)
*Image from [Cascading documentation]('http://docs.cascading.org')*

## Opertaion in Building Pipelines
1. ```SumBy```
2. ```CoGroup```
3. ```ExpressionFunction```
4. ```Each```

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

## Reference
1. [Cascading Documentation](http://docs.cascading.org/cascading/1.2/javadoc/allclasses-noframe.html)