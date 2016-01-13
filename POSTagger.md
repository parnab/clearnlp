# Part-of-speech Tagger #

<font color='red'><b>This page is deprecated since the version 1.3.0</b></font>.

Our part-of-speech tagger uses dynamic model selection that dynamically selects either a domain-specific or a generalized model.  Our tagger tags about 39K tokens per second on an Intel Xeon 2.57GHz machine and shows state-of-the-art accuracy, especially on out-of-domain data.  See the following paper for more details about our tagging approach.

  * [Fast and Robust Part-of-Speech Tagging Using Dynamic Model Selection](http://aclweb.org/anthology-new/P/P12/P12-2071.pdf), Jinho D. Choi, Martha Palmer, Proceedings of the 50th Annual Meeting of the Association for Computational Linguistics (ACL'12), 363-367, Jeju, Korea, 2012.

## Training ##

```
java com.googlecode.clearnlp.run.POSTrain -c <filename> -f <filename> -i <filepath> -m <filename> [-s <integer> -t <double>]

 -c <filename>  : configuration file (required)
 -f <filename>  : feature template file (required)
 -i <directory> : input directory containg training files (required)
 -m <filename>  : model file (output; required)
 -s <integer>   : model type - 0|1|2 (default: 1)
                  0: train only a domain-specific model
                  1: train only a generalized model
                  2: train both models using dynamic model selection
 -t <double>    : similarity threshold (default: -1)
```

  * A sample configuration file can be found here: [config\_en\_pos.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pos.xml).  See the descriptions below for more details about the configuration file.
  * A sample feature template file can be found here: [feature\_en\_pos.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_pos.xml).  See the descriptions below for more details about the feature template file.
  * All files under the input directory are used for training.  We recommend you to split your training data into several files with respect to their contents; this will improve robustness of tagging out-of-domain data.  If you are not sure how to split your data, just split them randomly into 10 folds; even this will help improving the robustness.
  * The model file is the name of an output file to save trained models.
  * When the model type is set to 0, 1, or 2, it trains a domain-specific model, a generalized model, or both models, respectively.  If you know your future data are from the same sources as your training data, we recommend you to train a domain-specific model.  If you are not sure about your future data, we recommend you to train a generalized model.  If you think your future data contain a mixture of both kinds of data, we recommend you to train both models using dynamic model selection.
  * The threshold is used for dynamic model selection; this is a threshold for cosine similarities between the training data and input sentences (as described in the paper).  If the threshold is set to be a negative value, it finds an appropriate threshold automatically via cross-validation.

The following command trains all files under the input directory ([trn/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/trn)) and generates a model file [sample-pos.jar`' using the configuration file ([config\_en\_pos.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pos.xml)) and the feature template file ([feature\_en\_pos.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_pos.xml)).  By default, only a generalized model is trained; in fact, our [pre-trained models](TrainedModels.md) include only generalized models.  We recently found out that a generalized model was sufficient enough for most practical purposes because it is not likely that you'd be tagging data that are from the same sources as our pre-trained models.  Each input file consists of multiple columns; we need only the 2nd (form) and the 4th (pos) columns as indicated in the configuration file.
```
java com.googlecode.clearnlp.run.POSTrain -c config_en_pos.xml -f feature_en_pos.xml -i trn/ -m sample-pos.jar
```

## Predicting ##

Our pre-trained models are available at the [TrainedModels](TrainedModels.md) page.

```
java com.googlecode.clearnlp.run.POSPredict -c <filename> -m <filename> -i <filepath> [-ie <regex> -oe <string>]

 -c <filename> : configuration file (required)
 -m <filename> : model file (required)
 -i <filepath> : input path (required)
 -ie <regex>   : input file extension (default: .*)
 -oe <string>  : output file extension (default: tagged)
```
  * A sample configuration file can be found here: [config\_en\_pos.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pos.xml).  See the descriptions below for more details about the configuration file.
  * A model file contains the trained models (see above).
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `txt`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.

The following command tags all files whose extension is "`dep`" under the input directory ([dev/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev)) and generates output files (`*.tagged`) to the same directory using the configuration file ([config\_en\_pos.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pos.xml)) and the model file ([sample-pos.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-pos.jar)).

```
java -XX:+UseConcMarkSweepGC com.googlecode.clearnlp.run.POSPredict -c config_en_pos.xml -m sample-pos.jar -i dev/ -ie dep
```

## Evaluating ##

```
java com.googlecode.clearnlp.run.POSEvaluate -g <filename> -s <filename> -gi <integer> -si <integer>

 -g <filename> : gold-standard file (required)
 -s <filename> : system file (required)
 -gi <integer> : column index of POS tags in a gold-standard file (required)
 -si <integer> : column index of POS tags in a system-generated file (required)
```

  * The gold-standard file contains gold-standard POS tags.
  * The system file contains system-generated POS tags.
  * The '`gi`' option specifies the column index of POT tags in the gold-standard file (starting at 1).
  * The '`si`' option specifies the column index of POT tags in the system-generated file (starting at 1).

The following command evaluates a system-generated file ([dev/bc-p2.5\_a2e-dev.dep.tagged](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep.tagged)) against a gold-standard file ([dev/bc-p2.5\_a2e-dev.dep](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep)).  The gold-standard and system-generated files contain POS tags in the 4th and 2nd columns, respectively.

```
java com.googlecode.clearnlp.run.POSEvaluate -g dev/bc-p2.5_a2e-dev.dep -s dev/bc-p2.5_a2e-dev.dep.tagged -gi 4 -si 2
```

## Configuration file format ##

The following shows a sample configuration file ([config\_en\_pos.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pos.xml)).

```
<configuration>
    <language>en</language>
    <dictionary>model/dictionary-1.1.0.zip</dictionary>

    <reader type="pos">
        <column index="2" field="form"/>
        <column index="4" field="pos"/>
    </reader>

    <train>
        <algorithm name="liblinear" solver="0" cost="0.1" eps="0.1" bias="0.7" note="domain-specific"/>
        <algorithm name="liblinear" solver="0" cost="0.2" eps="0.1" bias="0.6" note="generalized"/>
        <threads>8</threads>
    </train>
</configuration>
```

  * The language element specifies the language for a [tokenizer](Tokenizer.md) and a [morphological analyzer](MPAnalyzer.md): `en` - English.
  * The dictionary element specifies the path to a dictionary file (see [TrainedModels](TrainedModels.md)) used for the tokenizer and the morphological analyzer.
  * The reader element specifies the input [data format](DataFormat.md):
    * For training, it accepts only the "pos" type, which requires both the form and pos fields to be specified.  The form and pos fields indicate the indices of the columns containing word forms and pos tags, respectively (starting at 1).
    * For predicting, it accepts the "raw", "line", "tok", or "pos" type.  When the "raw" or "line" type is used, no field needs to be specified.  When the "tok" or "pos" type is used, the form field must be specified.
    * For the following example, word forms and pos tags are in the 2nd and 4th columns, respectively; thus, the above configuration can be used to read this example.

```
1    I       i        PRP    _             3    nsubj    5:xsubj    3:A0;5:A0
2    'd      would    MD     _             3    aux      _          3:AM-MOD
3    like    like     VB     pb=like.02    0    root     _          _
4    to      to       TO     _             5    aux      _          _
5    meet    meet     VB     pb=meet.01    3    xcomp    _          3:A1
6    Mr.     mr.      NNP    _             7    nn       _          _
7    Choi    choi     NNP    _             5    dobj     _          5:A1
8    .       .        .      _             3    punct    _          _
```

  * The train element specifies algorithms used for training domain-specific (1st) and generalized (2nd) models.  Note that algorithms for both models need to be specified even if dynamic model selection is not used for training.  See the [LibLinear](LibLinear.md) page for more details about the Liblinear hyperparameters.  The thread element specifies the number of threads to be used for training.

## Feature template format ##

Please contact the owner of ClearNLP for more details about the feature template format.