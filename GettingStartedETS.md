# How to run ClearNLP from scratch #
## Written by Chris Brew (cbrew@ets.org) ##

If you do the following steps, you should get a program that runs ClearNLP components and transmutes raw text into dependency trees. I did this on my home machine, which is a 2.66 GHz Intel Core Duo iMac, running MacOS 10.8 with 4Mb of RAM, and also on the servers at work, which have more memory than this and run CentOS.

1) Create a new project using Maven.

```
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DgroupId=org.ets.nlp.cnlp -DartifactId=cnlp
```

Here, the `org.ets.nlp` part is there because I work for the NLP group at the Educational Testing Service, and should preferably be replaced by something that describes your affiliation.

Maven rushes off and begins to make a project in a file called `cnlp`. Accept all the defaults that it offers.

2) Edit the `pom.xml` that Maven has just generated. The [installation](Installation.md) page tells you the repository and dependency to use. When you are done, your `pom.xml` should look something like this:

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.ets.nlp.cnlp</groupId>
  <artifactId>cnlp</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>cnlp</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <repositories>
    <repository>
      <id>sonatype-oss-public</id>
      <url>https://oss.sonatype.org/content/groups/public/</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
  </repositories>
  <dependencies>
    <dependency>
      <groupId>com.googlecode.clearnlp</groupId>
      <artifactId>clearnlp</artifactId>
      <version>1.1.0</version>
   </dependency>
   <dependency>
     <groupId>junit</groupId>
     <artifactId>junit</artifactId>
     <version>3.8.1</version>
     <scope>test</scope>
   </dependency>
  </dependencies>
</project>
```

Again, I would prefer it if you used your own affiliation, not `org.ets.nlp`, so the groupId element needs to change. Because you are using Maven, no need to download the ClearNLP source code. Maven does something semi-magical behind the scenes and pulls down ClearNLP and its dependencies.

3) Download models from [TrainedModels](TrainedModels.md).  Then

```
cd cnlp
mkdir models
```

Do the downloads. I used these ones:
  * [dictionary-1.1.0.zip](https://bitbucket.org/jdchoi77/models/downloads/dictionary-1.1.0.zip)
  * [ontonotes-en-dep-1.1.0b3.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-pos-1.1.0g.jar)
  * [ontonotes-en-pos-1.1.0g.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-dep-1.1.0b3.jar)

Copy the models into models, and get hold of some text file, such as the [iphone5.txt](http://clearnlp.googlecode.com/git/src/main/resources/sample/iphone5.txt) in the source distribution of ClearNLP. The ClearNLP git repository doesn't include the models, but you can get them by following links from the [TrainedModels](TrainedModels.md) page.

4) Edit `src/main/java/org/ets/nlp/cnlp/App.java` to look like the demo file at [DEPParser.java](https://code.google.com/p/clearnlp/source/browse/src/main/java/com/googlecode/clearnlp/demo/DemoDEPParser.java). The result I got was:

```
package org.ets.nlp.cnlp;

import java.io.BufferedReader;
import java.util.List;
import com.googlecode.clearnlp.dependency.DEPParser;
import com.googlecode.clearnlp.dependency.DEPTree;
import com.googlecode.clearnlp.engine.EngineGetter;
import com.googlecode.clearnlp.engine.EngineProcess;
import com.googlecode.clearnlp.morphology.AbstractMPAnalyzer;
import com.googlecode.clearnlp.pos.POSTagger;
import com.googlecode.clearnlp.reader.AbstractReader;
import com.googlecode.clearnlp.segmentation.AbstractSegmenter;
import com.googlecode.clearnlp.tokenization.AbstractTokenizer;
import com.googlecode.clearnlp.util.UTInput;
import com.googlecode.clearnlp.util.pair.Pair;

public class App
{
        final String language = AbstractReader.LANG_EN;
        
        public App(String dictionaryFile, String posModelFile, String depModelFile, String inputFile) throws Exception
        {
                AbstractTokenizer tokenizer = EngineGetter.getTokenizer(language, dictionaryFile);
                AbstractMPAnalyzer analyzer = EngineGetter.getMPAnalyzer(language, dictionaryFile);
                Pair<POSTagger[],Double> taggers = EngineGetter.getPOSTaggers(posModelFile);
                DEPParser parser = EngineGetter.getDEPParser(depModelFile);
                
                String sentence = "I'd like to meet Mr. Choi.";
                parse(tokenizer, analyzer, taggers, parser, sentence);
                parse(tokenizer, analyzer, taggers, parser, UTInput.createBufferedFileReader(inputFile));
        }
        
        public void parse(AbstractTokenizer tokenizer, AbstractMPAnalyzer analyzer, Pair<POSTagger[],Double> taggers, DEPParser parser, String sentence)
        {
                DEPTree tree = EngineProcess.getDEPTree(tokenizer, taggers, analyzer, parser, sentence);
                System.out.println(tree.toStringDEP());
        }
        
        public void parse(AbstractTokenizer tokenizer, AbstractMPAnalyzer analyzer, Pair<POSTagger[],Double> taggers, DEPParser parser, BufferedReader reader)
        {
                AbstractSegmenter segmenter = EngineGetter.getSegmenter(language, tokenizer);
                DEPTree tree;
                
                for (List<String> tokens : segmenter.getSentences(reader))
                {
                        tree = EngineProcess.getDEPTree(taggers, analyzer, parser, tokens);
                        System.out.println(tree.toStringDEP()); 
                }
        }

        public static void main(String[] args)
        {
                String dictionaryFile = args[0];    // e.g., dictionary-1.1.0.zip
                String posModelFile   = args[1];    // e.g., ontonotes-en-pos-1.1.0g.jar
                String depModelFile   = args[2];    // e.g., ontonotes-en-dep-1.1.0b3.jar
                String inputFile      = args[3];

                try
                {
                        new App(dictionaryFile, posModelFile, depModelFile, inputFile);
                }
                catch (Exception e) {e.printStackTrace();}
        }
}
```

The things I changed are (a) the package name (b) the places where Java insists that the program be called `App` rather than `DemoDEPParser`. This aspect of Java source file organization is annoying, but everybody is used to it by now. Would perhaps have been better to change the filename, and not call it `App`.

5) Run the program with Maven

```
mvn compile
mvn exec:java -Dexec.mainClass=org.ets.nlp.cnlp.App -Dexec.args="models/dictionary-1.1.0.zip models/ontonotes-en-pos-1.1.0g.jar models/ontonotes-en-dep-1.1.0b3.jar iphone5.txt" 
```

The first time I tried this, it didn't work for me, because Java needs to give license to grab a lot of memory. You have to set an environment variable to tell Maven about this. The way you do that is to say.

export MAVEN\_OPTS="-XX:+UseConcMarkSweepGC -Xmx4g"

-Xmx4g tells Java it is allowed to use 4GB of RAM for heap. This is a bit scary if your poor little machine only has 4GB of RAM, but it worked for me. The option about [-XX:+UseConcMarkSweepGC](http://www.oracle.com/technetwork/java/javase/gc-tuning-6-140523.html) is a way of reducing the peak memory load, possibly at the cost of some other aspect of performance. Standard advice for Java developers doing NLP is to accept the fact that Java is memory-hungry and just buy some more RAM. If you can't do that, `-XX:+UseConcMarkSweepGC` can help.