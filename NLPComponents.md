<font size='5'><b>NLP Componenets</b></font>

From the version 1.3.0, all components using statistical models are inherited from [AbstractComponent](https://code.google.com/p/clearnlp/source/browse/src/main/java/com/googlecode/clearnlp/component/AbstractComponent.java).  Our statistical models currently support part-of-speech tagging, morphological analysis, dependency parsing, and semantic role labeling (including predicate identification and roleset classification).



# Decoding #

```
java com.googlecode.clearnlp.nlp.NLPDecode -c <filename> -z <mode> -i <filepath> [-ie <regex> -oe <string>]

 -c  <filename> : configuration file (required)
 -i  <filepath> : input path (required)
 -ie <regex>    : input file extension (default: .*)
 -oe <string>   : output file extension (default: labeled)
 -z  <mode>     : mode (pos|morph|dep|srl)
```

  * A sample configuration file can be found here: [config\_en\_decode.xml](https://code.google.com/p/clearnlp/source/browse/src/main/resources/configure/config_en_decode.xml) (see [configuration](NLPComponents#Configuration.md) for more details).
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `dep`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.
  * The mode specifies which component to run.
    * `pos`: part-of-speech tagging
    * `morph`: morphological analysis
    * `dep`: dependency parsing
    * `srl`: semantic role labeling

Our decoder automatically figures out the necessary steps for each mode.  The following command takes a raw text ([iphone5.txt](https://code.google.com/p/clearnlp/source/browse/src/main/resources/sample/iphone5.txt))  and performs all steps up to semantic role labeling, that are part-of-speech tagging, morphological analysis, and dependency parsing, using the configuration file ([config\_en\_decode.xml](https://code.google.com/p/clearnlp/source/browse/src/main/resources/configure/config_en_decode.xml)).  The configuration file requires to specify paths to statistical models, which can be found [here](TrainedModels.md).

```
java -XX:+UseConcMarkSweepGC -Xmx4g com.googlecode.clearnlp.nlp.NLPDecode -c config_en_decode.xml -i iphone5.txt -z srl
```

Make sure to use the [-XX:+UseConcMarkSweepGC](http://www.oracle.com/technetwork/java/javase/gc-tuning-6-140523.html) option for JVM, which reduces the memory usage into a half.  After this command, you should see an output file ([iphone5.txt.cnlp](https://code.google.com/p/clearnlp/source/browse/src/main/resources/sample/iphone5.txt.cnlp)) containing all processed information (see [data format](NLPComponents#Data_format.md) for more details about the output).

# Training #

All training modules are already implemented into ClearNLP; however, optimization can be tricky.  If you want to train new models, please contact the owner of ClearNLP.
  * <img src='https://dl.dropbox.com/u/15060914/img/jinho_email.png' height='13'></li></ul>

<h1>Configuration #

For decoding, two elements, `<reader>` and `<models>`, must be specified in a configuration file.

```
<configuration>
    <reader type="column">
        <column index="1" field="id"/>
        <column index="2" field="form"/>
        <column index="3" field="lemma"/>
        <column index="4" field="pos"/>
        <column index="5" field="feats"/>
        <column index="6" field="headId"/>
        <column index="7" field="deprel"/>
        <column index="8" field="sheads"/>
    </reader>

    <models>
        <language>en</language>
        <dictionary>model/dictionary-1.4.0.zip</dictionary>
        <model mode="pos"   path="model/ontonotes-en-pos-1.4.0.tgz"/>
        <model mode="dep"   path="model/ontonotes-en-dep-1.4.0.tgz"/>
        <model mode="morph" path="model/dictionary-1.4.0.zip"/>
        <model mode="pred"  path="model/ontonotes-en-pred-1.4.0.tgz"/>
        <model mode="role"  path="model/ontonotes-en-role-1.4.0.tgz"/>
        <model mode="srl"   path="model/ontonotes-en-srl-1.4.2.tgz"/>
    </models>
</configuration>
```

  * The `<reader>` element contains information about [data format](NLPComponents#Data_format.md).
    * The _type_ attribute specifies the type of data format: `raw|line|column`.
      * The `raw` type accepts texts in any format: `<reader type="raw">`.
      * The `line` type requires each sentence to be in one line: `<reader type="line">`.
      * The `column` type requires each field to be in one column: `<reader type="column">`.
    * When the `column` type is used, `<column>` elements need to be specified.
      * The _index_ attribute specifies the index of a field, starting at 1.
      * The _field_ attribute specifies the name of the field: `id|form|lemma|pos|feats|headId|deprel|sheads`.
        * `id` - token ID, starting at 1
        * `form` - word form
        * `lemma` - lemma
        * `pos` - part-of-speech tag
        * `feats` - features
        * `headId` - head token ID
        * `deprel` - dependency label
        * `sheads` - semantic heads
  * The `<models>` element contains information about statistical models, which can be downloaded from [here](TrainedModels.md).
    * The `<language>` element specifies the language of the models: `en` - English.
    * The `<dictionary>` element specifies the path to a dictionary file.
    * The `<model>` element contains information about individual models.
      * The _mode_ attribute specifies a component: `pos|dep|morph|pred|role|srl`.
      * The _path_ attribute specifies the path to a model file used by the component.

# Data Format #

Three types of data format are supported: `raw`, `line`, or `column`.

  * The `raw` type accepts texts in any format.
```
I'd like to meet Dr. Choi. He's the owner of ClearNLP.
```

  * The `line` type requires each sentence to be in one line.
```
I'd like to meet Dr. Choi. 
He's the owner of ClearNLP.
```

  * The `column` type requires each field to be in one column.  You must specify at least one column containing word forms.
```
1    I           i           PRP    _             3    nsubj     3:A0;5:A0
2    'd          would       MD     _             3    aux       3:AM-MOD
3    like        like        VB     pb=like.02    0    root
4    to          to          TO     _             5    aux
5    meet        meet        VB     pb=meet.01    3    xcomp     3:A1
6    Dr.         dr.         NNP    _             7    nn
7    Choi        choi        NNP    _             5    dobj      5:A1
8    .           .           .      _             3    punct

1    He          he          PRP    _             2    nsubj    2:A1
2    's          is          VBZ    pb=be.01      0    root
3    the         the         DT     _             4    nn
4    owner       owner       NN     _             2    attr     2:A2
5    of          of          IN     _             4    prep
6    ClearNLP    clearnlp    NNP    _             5    pobj
7    .           .           .      _             .    punct
```

> The example above consists of 8 columns containing:
    * `id` - token ID, starting at 1
    * `form` - word form
    * `lemma` - lemma
    * `pos` - part-of-speech tag
    * `feats` - features (different features are delimited by '`|`', keys and values are delimited by '`=`')
    * `headId` - head token ID
    * `deprel` - dependency label
    * `sheads` - semantic heads (different heads are delimited by '`;`', head IDs and semantic roles are delimited by '`:`')
    * '`_`' indicates no value for the field.

# Components #

For more details about our components, please read our previous description pages.

  * [Part-of-speech tagger](POSTagger.md)
  * [Morphological analyzer](MPAnalyzer.md)
  * [Dependency parser](DEPParser.md)
  * [Semantic role labeler](SRLabeler.md)