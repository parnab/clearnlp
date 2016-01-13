# Predicate Identifier #

<font color='red'><b>This page is deprecated since the version 1.3.0</b></font>.

Our predicate identifier checks if a word token is a predicate.  It is used as a pre-processing step to our [semantic role labeler](SRLLabeler.md), which finds argument structures of identified predicates.

## Training ##

```
java com.googlecode.clearnlp.run.PredTrain -c <filename> -f <filename> -i <filename> -m <filename>
 -c <filename>  : the configuration file (required)
 -f <filename>  : the feature file for predicate identification (required)
 -i <directory> : the directory containg training files (required)
 -m <filename>  : the model file (output; required)
```

  * A sample configuration file can be found here: [config\_en\_pred.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pred.xml).  See the descriptions below for more details about the configuration file.
  * A sample feature template file can be found here: [feature\_en\_pred.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_pred.xml).  See the descriptions below for more details about the feature template file.
  * All files under the input directory are used for training.
  * The model file is the name of an output file to save a trained model.

The following command trains all files under the input directory ([trn/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/trn)) and generates a model file '`sample-pred.jar`' using the configuration file ([config\_en\_pred.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pred.xml)) and the feature template file ([feature\_en\_pred.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_pred.xml)).  Each input file consists of multiple columns; we need only the first 7 columns (ID, form, lemma, pos, feats, head ID, and deprel) as indicated in the configuration file.
```
java com.googlecode.clearnlp.run.PredTrain -c config_en_pred.xml -f feature_en_pred.xml -i trn/ -m sample-pred.jar
```

## Predicting ##

Our pre-trained models are available at the [TrainedModels](TrainedModels.md) page.

```
java com.googlecode.clearnlp.run.PredPredict -c <filename> -m <filename> -i <filepath> [-ie <regex> -oe <string>]

 -c <filename> : configuration file (required)
 -m <filename> : model file (required)
 -i <filepath> : input path (required)
 -ie <regex>   : input file extension (default: .*)
 -oe <string>  : output file extension (default: parsed)
```
  * A sample configuration file can be found here: [config\_en\_pred.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pred.xml).  See the descriptions below for more details about the configuration file.
  * A model file contains the trained model (see above).
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `dep`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.

The following command parses all files whose extension is "`dep`" under the input directory ([dev/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev)) and generates output files (`*.pred`) to the same directory using the configuration file ([config\_en\_pred.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pred.xml)) and the model file (`sample-dep.jar`).

```
java -XX:+UseConcMarkSweepGC com.googlecode.clearnlp.run.PredPredict -c config_en_pred.xml -m sample-pred.jar -i dev/ -ie dep
```

## Evaluating ##

```
java com.googlecode.clearnlp.run.PredEvaluate -g <filename> -s <filename> -gi <integer> -si <integer>

 -g <filename> : gold-standard file (required)
 -s <filename> : system-generated file (required)
 -gi <integer> : column index of extra features in a gold-standard file (required)
 -si <integer> : column index of extra features in a system-generated file (required)
```

  * The gold-standard file contains gold-standard head IDs and dependency labels.
  * The system file contains system-generated head IDs and dependency labels.
  * The extra features column contains identified predicates in the form of "pb=lemma.XX" (e.g., pb=buy.01).

The following command evaluates a system-generated file ([dev/bc-p2.5\_a2e-dev.dep.pred](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep.pred)) against a gold-standard file ([dev/bc-p2.5\_a2e-dev.dep](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep)).  Both the gold-standard and system-generated files contain identified predicates in the 5th column.

```
java com.googlecode.clearnlp.run.DEPEvaluate -g dev/bc-p2.5_a2e-dev.dep -s dev/bc-p2.5_a2e-dev.dep.pred -gi 5 -si 5
```

## Configuration file format ##

The following shows a sample configuration file ([config\_en\_pred.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_pred.xml)).

```
<configuration>
    <reader type="dep">
        <column index="1" field="id"/>
        <column index="2" field="form"/>
        <column index="3" field="lemma"/>
        <column index="4" field="pos"/>
        <column index="5" field="feats"/>
        <column index="6" field="headId"/>
        <column index="7" field="deprel"/>
    </reader>

    <train>
        <algorithm name="liblinear" solver="0" cost="0.4" eps="0.2" bias="-1"/>
        <threads>1</threads>
    </train>
</configuration>
```

  * The reader element specifies the input [data format](DataFormat.md):
    * For both training and predicting, it accepts only the "dep" type, which requires the id, form, lemma, pos, feats, head\_id, and deprel fields to be specified.  These fields indicate the indices of the columns containing token IDs, word forms, lemmas, POS tags, extra features, head IDs, and dependency labels, respectively (starting at 1).  See the [CoNLL'09 shared task](http://ufal.mff.cuni.cz/conll2009-st/task-description.html) page for more detail about these fields.
    * For the following example, token IDs, word forms, lemmas, pos tags, extra features, head IDs, and dependency labels are in the 1st ~ 7th columns, respectively; thus, the above configuration can be used to read this example.

```
1    I       i        PRP    _             3    nsubj    3:A0;5:A0
2    'd      would    MD     _             3    aux      3:AM-MOD
3    like    like     VB     pb=like.02    0    root
4    to      to       TO     _             5    aux
5    meet    meet     VB     pb=meet.01    3    xcomp    3:A1
6    Mr.     mr.      NNP    _             7    nn
7    Choi    choi     NNP    _             5    dobj     5:A1
8    .       .        .      _             3    punct
```

  * The train element specifies an algorithm used for training.  See the [LibLinear](LibLinear.md) page for more details about the Liblinear hyperparameters.  The thread element specifies the number of threads to be used for training.

## Feature template format ##

Please contact the owner of ClearNLP for more details about the feature template format.