# How To Install #

## Without Maven ##

  1. Create a directory called `clearnlp`.
  1. Download the [library](http://clearnlp.googlecode.com/files/clearnlp-lib.zip) file and uncompress it.  Place all `jar` files under the `clearnlp` directory.
  1. Download the current version of [ClearNLP](https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=com.googlecode.clearnlp&a=clearnlp&v=1.4.2&e=jar) `jar` file and put it under the `clearnlp` directory.
  1. Add all `jar` files in the `clearnlp` directory to your Java classpath.  If you are using the bash shell, it is something like the followings:
```
export CLEARNLP=some_path/clearnlp
export CLASSPATH=$CLEARNLP/args4j-2.0.23.jar:$CLEARNLP/commons-compress-1.5.jar:$CLEARNLP/hppc-0.5.2.jar: \\
                 $CLEARNLP/jregex1.2_01.jar:$CLEARNLP/slf4j-api-1.7.5.jar:$CLEARNLP/slf4j-log4j12-1.7.5.jar: \\
                 $CLEARNLP/guava-14.0.1.jar:$CLEARNLP/clearnlp-lib-1.4.2.jar:.
```
  1. Put the [log4j configuration file](https://clearnlp.googlecode.com/git/src/main/resources/configure/log4j.properties) under the `clearnlp` directory.
  1. Type the following command on a terminal.  If you see the version info below, ClearNLP is successfully installed on your machine.
```
$ java com.googlecode.clearnlp.run.Version
```
```
ClearNLP version 1.4.x
Webpage: clearnlp.com
Owner  : Jinho D. Choi
Contact: support@clearnlp.com
```

## With Maven ##

ClearNLP can be retrieved from either the [Sonatype Nexus](https://oss.sonatype.org/index.html#nexus-search;quick~clearnlp) or the [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cclearnlp).  Add the following lines to your `pom.xml` with the group ID '`com.googlecode.clearnlp`', the artifact ID '`clearnlp`', and the current version number.

```
<repositories>
...
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
...
</repositories>

<dependencies>
...
  <dependency>
    <groupId>com.googlecode.clearnlp</groupId>
    <artifactId>clearnlp</artifactId>
    <version>1.4.2</version>
  </dependency>
...
</dependencies>
```

Put the [log4j configuration file](https://clearnlp.googlecode.com/git/src/main/resources/configure/log4j.properties) under the directory you run ClearNLP from.

## Instructions From Users ##

The followings show wonderful instructions from users who have made ClearNLP work for their systems.
  * [How to run ClearNLP from scratch](GettingStartedETS.md) from Chris Brew at ETS (for version 1.1.0).