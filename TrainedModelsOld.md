# Trained Models (until v1.2.x) #

Please make sure to use the [-XX:+UseConcMarkSweepGC](http://www.oracle.com/technetwork/java/javase/gc-tuning-6-140523.html) option for JVM, which reduces the memory usage into a half.  For instance, if you are running our [dependency parser](DEPParser.md), your command would be something like this.

```
java com.googlecode.clearnlp.run.DEPPredict ...
```

Add the `-XX:+UseConcMarkSweepGC ` option to this command.  You also need to add `-Xmx` option to increase the heap size for JVM.

```
java -XX:+UseConcMarkSweepGC -Xmx4g com.googlecode.clearnlp.run.DEPPredict ...
```

## Dictionaries ##

Here is a dictionary file used for our [tokenizer](Tokenizer.md) and [morphological analyzer](MPAnalyzer.md).
  * [dictionary-1.2.0.zip](https://bitbucket.org/jdchoi77/models/downloads/dictionary-1.2.0.zip)

## OntoNotes ##

The [OntoNotes](http://www.bbn.com/ontonotes/) models are trained on a mixture of:

  * Broadcasting conversations: 11,846 sentences, 178,592 tokens.
  * Broadcasting news: 9,126 sentences, 172,859 tokens.
  * News magazines: 6,905 sentences, 164,217 tokens.
  * Newswires: 39,173 sentences, 982,033 tokens.
  * Telephone conversations: 11,284 sentences, 95,856 tokens.
  * Web-texts: 13,362 sentences, 303,190 tokens.

| **Language** | **Task** | **Algorithm** | **HDD** | **RAM** | **Date** | **Download** |
|:-------------|:---------|:--------------|:--------|:--------|:---------|:-------------|
| English      | Part-of-speech tagging | Liblinear L2-L1 SVM | 39.3MB  | 1.3GB   | 10/17/2012 | [ontonotes-en-pos-1.1.0g.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-pos-1.1.0g.jar) |
| English      | Dependency parsing | Liblinear L2-L1 SVM | 143.9MB | 2.0GB   | 10/17/2012 | [ontonotes-en-dep-1.1.0b3.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-dep-1.1.0b3.jar) |
| English      | Predicate identification | Liblinear L2-L1 SVM | 1.5MB   | 0.1GB   | 10/29/2012 | [ontonotes-en-pred-1.2.0.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-pred-1.2.0.jar) |
| English      | Semantic role labeling | Liblinear L2-L1 SVM | 41.8MB  | 0.8GB   | 10/29/2012 | [ontonotes-en-srl-1.2.0b3.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-srl-1.2.0b3.jar) |

## Medical ##

The medical models are trained on a mixture of:

  * Clinical questions: 1,700 sentences, 31,920 tokens.
  * Cohort queries: 678 sentences, 8,432 tokens.
  * Medpedia articles: 3,483 sentences, 52,489 tokens.
  * MiPACQ clinical notes: 9,621 sentences, 123,856 tokens.
  * MiPACQ pathological notes: 1814 sentences, 23,842 tokens.
  * Seattle group health clinical notes: 7,218 sentences, 75,663 tokens.
  * Seattle group health pathological notes: 2,631 sentences, 38,199 tokens.
  * SHARP clinical notes: 7,818 sentences, 102,877 tokens.
  * TEMPREL clinical notes: 15,059 sentences, 168,605 tokens.
  * TEMPREL pathological notes: 3,905 sentences, 46,412 tokens.

| **Language** | **Task** | **Algorithm** | **HDD** | **RAM** | **Date** | **Download** |
|:-------------|:---------|:--------------|:--------|:--------|:---------|:-------------|
| English      | Part-of-speech tagging | Liblinear L2-L1 SVM | 15.0MB  | 0.7GB   | 10/17/2012 | [medical-en-pos-1.1.0g.jar](https://bitbucket.org/jdchoi77/models/downloads/medical-en-pos-1.1.0g.jar) |
| English      | Dependency parsing | Liblinear L2-L1 SVM | 60.2MB  | 1.0GB   | 10/17/2012 | [medical-en-dep-1.1.0b3.jar](https://bitbucket.org/jdchoi77/models/downloads/medical-en-dep-1.1.0b3.jar) |
| English      | Predicate identification | Liblinear L2-L1 SVM | 0.4MB   | <0.1GB  | 10/29/2012 | [medical-en-pred-1.2.0.jar](https://bitbucket.org/jdchoi77/models/downloads/medical-en-pred-1.2.0.jar) |
| English      | Semantic role labeling | Liblinear L2-L1 SVM | 11.1MB  | 0.3GB   | 10/29/2012 | [medical-en-srl-1.2.0b1.jar](https://bitbucket.org/jdchoi77/models/downloads/medical-en-srl-1.2.0b1.jar) |

## Craft ##

The medical models are trained on the [Craft corpus](http://bionlp-corpora.sourceforge.net/CRAFT/index.shtml) using 20,138 sentences and 522,467 tokens.

  * Chemical Entities of Biological Interest (ChEBI)
  * Cell Type Ontology (CL)
  * Entrez Gene
  * Gene Ontology (biological process, cellular component, and molecular function)
  * NCBI Taxonomy
  * Protein Ontology
  * Sequence Ontology

| **Language** | **Task** | **Algorithm** | **HDD** | **RAM** | **Date** | **Download** |
|:-------------|:---------|:--------------|:--------|:--------|:---------|:-------------|
| English      | Part-of-speech tagging | Liblinear L2-L1 SVM | 11.5MB  | 0.7GB   | 10/17/2012 | [craft-en-pos-1.1.0g.jar](https://bitbucket.org/jdchoi77/models/downloads/craft-en-pos-1.1.0g.jar) |
| English      | Dependency parsing | Liblinear L2-L1 SVM | 49.2MB  | 1.0GB   | 10/17/2012 | [craft-en-dep-1.1.0b1.jar](https://bitbucket.org/jdchoi77/models/downloads/craft-en-dep-1.1.0b1.jar) |

## Sample ##

The sample models are built from our sample data ([dev](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev)).  These models are not for actual run but for unit-testing or debugging.

  * [sample-pos.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-pos.jar), [sample-dep.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-dep.jar), [sample-pred.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-pred.jar), [sample-srl.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-srl.jar).