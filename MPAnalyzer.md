# Morphological Analyzer #

<font color='red'><b>This page is deprecated since the version 1.3.0</b></font>.

Our morphological analyzer takes word-forms and their part-of-speech tags as input, and generates lemmas of those word-forms as output.  Currently, our morphological analyzer supports only English.  It is a dictionary-based analyzer based on the [WordNet morphy](http://wordnet.princeton.edu/man/morphy.7WN.html) (v3.0) although it uses a slightly larger dictionary gathered from various sources.  Furthermore, our analyzer normalizes numbers and redundant punctuation (see examples below), which is found to be useful for several NLP tasks such as POS tagging, dependency parsing, semantic-role labeling, etc.

## How to run ##

```
java com.googlecode.clearnlp.run.MPAnalyze -c <filename> -i <filepath> [-ie <regex> -oe <string>]

-c  <filename> : configuration file (input; required)
-i  <filepath> : input path (input; required)
-ie <regex>    : input file extension (default: .*)
-oe <string>   : output file extension (default: morph)
```

  * A sample configuration file can be found here: [config\_en\_morph.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_morph.xml).  See the descriptions below for more details about the configuration file.
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `txt`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.

The following command takes a sample input file ([morph-sample.txt](http://clearnlp.googlecode.com/git/src/main/resources/sample/morph-sample.txt)) and generates an output file ([morph-sample.txt.morph](http://clearnlp.googlecode.com/git/src/main/resources/sample/morph-sample.txt.morph)) using the default output file extension (`-oe morph`).  The input file consists of two columns (delimited by tabs) where the 1st and 2nd columns show word-forms and POS tags, respectively.  The output file consists of an additional column showing predicted lemmas.

```
java com.googlecode.clearnlp.run.MPAnalyze -c config_en_morph.xml -i morph-sample.txt
```

## Normalization ##

Ordinals and cardinals are normalized to `#ord#` and `#crd#`, respectively.

```
1st         CD    #ord#
12nd        CD    #ord#
23rd        CD    #ord#
34th        CD    #ord#
first       CD    #ord#
third       CD    #ord#
fourth      CD    #ord#
zero        CD    #crd#
ten         CD    #crd#
eleven      CD    #crd#
fourteen    CD    #crd#
```

Numeric expressions are normalized to 0.

```
10%                    XX    0
$10                    XX    0
A.01                   XX    a.0
A:01                   XX    a:0
A/01                   XX    a/0
.01                    XX    0
12.34                  XX    0
12,34,56               XX    0
12-34-56               XX    0
12/34/46               XX    0
$10.23,45:67-89/10%    XX    0
```

Redundant punctuation chracters are collapsed.

```
.!?-*=~,                            XX    .!?-*=~,
..!!??--**==~~,,                    XX    ..!!??--**==~~,,
...!!!???---***===~~~,,,            XX    ..!!??--**==~~,,
....!!!!????----****====~~~~,,,,    XX    ..!!??--**==~~,,
```

URLs are normalized to `#url#`.

```
http://www.google.com         XX    #url#
www.google.com                XX    #url#
mailto:somebody@google.com    XX    #url#
some-body@google+.com         XX    #url#
```

## Configuration file format ##

The following shows a sample configuration file ([config\_en\_morph.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_morph.xml)).

```
<configuration>
    <language>en</language>
    <dictionary>model/dictionary-1.1.0.zip</dictionary>

    <reader type="pos">
        <column index="1" field="form"/>
        <column index="2" field="pos"/>
    </reader>
</configuration>
```

  * The language element specifies the language for our morphological analyzer: `en` - English.
  * The dictionary element specifies the path to a dictionary file: [TrainedModels](TrainedModels.md).
  * The reader element specifies the input [data format](DataFormat.md); it accepts only the "pos" type, which requires both the form and pos fields to be specified.  The form and pos fields indicate the indices of the columns containing word forms and pos tags, respectively (starting at 1).