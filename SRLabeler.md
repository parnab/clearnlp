# Semantic Role Labeler #

<font color='red'><b>This page is deprecated since the version 1.3.0</b></font>.

Our semantic role labeler uses a higher-order argument pruning algorithm that significantly improves recall from the first-order argument pruning algorithm, yet keeps a similar labeling complexity in practice.  See Chapter 6 of the following paper for more details about our labeling approach.

  * [Optimization of Natural Language Processing Components for Robustness and Scalability](https://dl.dropbox.com/u/15060914/publications/thesis-choijd.pdf), Jinho D. Choi, Ph.D. Thesis, University of Colorado Boulder, Computer Science and Cognitive Science, 2012.

Additionally, our labeler uses a bootstrapping technique as our [dependency parser](DEPParser.md).  This bootstrapping technique improves labeling accuracy, not so significantly but marginally enough.

Our labeler takes about 0.45 milliseconds for labeling a predicate on an Intel Xeon 2.57GHz machine and shows state-of-the-art accuracy compared to other dependency-based labeling approaches.

## Training ##

```
java com.googlecode.clearnlp.run.SRLTrain -c <filename> -f <filename> -i <filename> -m <filename> [-n <integer> -sb]

 -c <filename>  : configuration file (required)
 -f <filename>  : feature template file (required)
 -i <directory> : input directory containing training files (required)
 -m <filename>  : model file (output; required)
 -n <integer>   : the bootstrapping level (default: 2)
 -sb <boolean>  : if set, save all intermediate bootstrapping models
```

  * A sample configuration file can be found here: [config\_en\_srl.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_srl.xml).  See the descriptions below for more details about the configuration file.
  * A sample feature template file can be found here: [feature\_en\_srl.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_srl.xml).  See the descriptions below for more details about the feature template file.
  * All files under the input directory are used for training.
  * The model file is the name of an output file to save a trained model.
  * The bootstrapping level indicates the number of iterations for bootstrapping.  In general, bootstrapping once always helps, bootstrapping twice usually helps, and bootstrapping three or more times may or may not help (depending on the training data).
  * If the `-sb` option is set, it saves all intermediate models during bootstrapping.

The following command trains all files under the input directory ([trn/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/trn)) and generates a model file '`sample-srl.jar`' using the configuration file ([config\_en\_srl.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_srl.xml)) and the feature template file ([feature\_en\_srl.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_srl.xml)) with the bootstrapping level of 1.  Each input file consists of multiple columns; we need only the first 8 columns (ID, form, lemma, pos, feats, head ID, deprel, and sheads) as indicated in the configuration file.
```
java com.googlecode.clearnlp.run.SRLTrain -c config_en_srl.xml -f feature_en_srl.xml -i trn/ -m sample-srl.jar -n 1
```

## Predicting ##

Our pre-trained models are available at the [TrainedModels](TrainedModels.md) page.

```
java com.googlecode.clearnlp.run.SRLPredict -c <filename> -m <filename> -i <filepath> [-ie <regex> -oe <string>]

 -c <filename> : configuration file (required)
 -m <filename> : model file (required)
 -i <filepath> : input path (required)
 -ie <regex>   : input file extension (default: .*)
 -oe <string>  : output file extension (default: labeled)
```
  * A sample configuration file can be found here: [config\_en\_srl.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_srl.xml).  See the descriptions below for more details about the configuration file.
  * A model file contains the trained model (see above).
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `dep`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.

The following command parses all files whose extension is "`dep`" under the input directory ([dev/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev)) and generates output files (`*.labeled`) to the same directory using the configuration file ([config\_en\_srl.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_srl.xml)) and the model file ([sample-srl.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-srl.jar)).

```
java -XX:+UseConcMarkSweepGC com.googlecode.clearnlp.run.SRLPredict -c config_en_dep.xml -m sample-dep.jar -i dev/ -ie dep
```

## Evaluating ##

```
java com.googlecode.clearnlp.run.SRLEvaluate -g <filename> -s <filename> -gi <integer> -si <integer>

 -g <filename> : gold-standard file (required)
 -s <filename> : system file (required)
 -gi <integer> : column index of gold semantic heads (required)
 -si <integer> : column index of system semantic heads (required)
```

  * The gold-standard file contains gold-standard semantic heads.
  * The system file contains system-generated semantic heads.
  * The '`gi`' option specifies the column index of semantic heads in the gold-standard file (starting at 1).
  * The '`si`' option specifies the column index of semantic heads in the system-generated file (starting at 1).

The following command evaluates a system-generated file ([dev/bc-p2.5\_a2e-dev.dep.labeled](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep.labeled)) against a gold-standard file ([dev/bc-p2.5\_a2e-dev.dep](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep)).  The gold-standard and system-generated files contain semantic heads in the 9th and 8th columns, respectively.

```
java com.googlecode.clearnlp.run.SRLEvaluate -g dev/bc-p2.5_a2e-dev.dep -s dev/bc-p2.5_a2e-dev.dep.labeled -gi 9 -si 8
```

## Configuration file format ##

The following shows a sample configuration file ([config\_en\_srl.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_srl.xml)).

```
<configuration>
    <language>en</language>
    <dictionary>model/dictionary-1.1.0.zip</dictionary>
    <pos_model>model/ontonotes-en-pos-1.1.0g.jar</pos_model>
    <dep_model>model/ontonotes-en-dep-1.1.0b3.jar</dep_model>
    <pred_model>model/ontonotes-en-pred-1.2.0.jar</pred_model>

    <reader type="srl">
        <column index="1" field="id"/>
        <column index="2" field="form"/>
        <column index="3" field="lemma"/>
        <column index="4" field="pos"/>
        <column index="5" field="feats"/>
        <column index="6" field="headId"/>
        <column index="7" field="deprel"/>
        <column index="9" field="sheads"/>
    </reader>
    
    <train>
        <algorithm name="liblinear" solver="0" cost="0.1" eps="0.1" bias="-1"/>
        <algorithm name="liblinear" solver="0" cost="0.1" eps="0.1" bias="0.1"/>
        <threads>8</threads>
    </train>
</configuration>
```

  * The language element specifies the language for a [tokenizer](Tokenizer.md) and a [morphological analyzer](MPAnalyzer.md): `en` - English.
  * The dictionary element specifies the path to a dictionary file (see [TrainedModels](TrainedModels.md)) used for the tokenizer and the morphological analyzer.
  * The pos\_model element specifies the path to a model file (see [TrainedModels](TrainedModels.md)) for a [part-of-speech tagger](POSTagger.md).
  * The dep\_model element specifies the path to a model file (see [TrainedModels](TrainedModels.md)) for a [dependency parser](DEPParser.md).
  * The pred\_model element specifies the path to a model file (see [TrainedModels](TrainedModels.md)) for a [predicate identifier](PredIdentifier.md).
  * The reader element specifies the input [data format](DataFormat.md):
    * For training, it accepts only the "srl" type, which requires the id, form, lemma, pos, feats, head\_id, deprel, and sheads fields to be specified.  These fields indicate the indices of the columns containing token IDs, word forms, lemmas, POS tags, extra features, head IDs, dependency labels, and semantic heads, respectively (starting at 1).  See the [CoNLL'09 shared task](http://ufal.mff.cuni.cz/conll2009-st/task-description.html) page and our [DataFormat](DataFormat.md) page for more detail about these fields.
    * For predicting, it accepts the "raw", "line", "tok", "pos", "dep", or "srl" type.  When the "raw" or "line" type is used, no field needs to be specified.  When the "tok" type is used, the form field must be specified.  When the "pos" type is used, the form and pos fields must be specified.  When the "dep" or "srl" type is used, the id, form, lemma, pos, feats, head ID, and deprel fields must be specified.  When the "srl" type is used, the feats field must contain the identified predicate information (e.g., "pb=buy.01").
    * For the following example, token IDs, word forms, lemmas, pos tags, extra features, head IDs, dependency labels are in the 1st ~ 7th columns, and semantic heads are in the 9th column, respectively; thus, the above configuration can be used to read this example.

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

  * The train element specifies algorithms used for training the left-argument and right-argument models (see the paper above for the positional feature separation).  See the [LibLinear](LibLinear.md) page for more details about the Liblinear hyperparameters.  The thread element specifies the number of threads to be used for training.

## Feature template format ##

Please contact the owner of ClearNLP for more details about the feature template format.