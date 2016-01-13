# Dependency Parser #

<font color='red'><b>This page is deprecated since the version 1.3.0</b></font>.

Our dependency parser uses a transition-based parsing approach that performs transitions from both Nivre's arc-eager and Convington's algorithms.  If training data contains only projective trees, it learns transitions only from Nivre's arc-eager algorithm and gives a worst-case parsing complexity of O(n).  If training data contains both projective and non-projective trees, it learns transitions from both algorithms and selectively performs non-projective parsing, which gives a linear-time parsing speed on average for the generation of non-projective trees.  See Chapter 5 of the following paper for more details about our parsing approach.

  * [Optimization of Natural Language Processing Components for Robustness and Scalability](https://dl.dropbox.com/u/15060914/publications/thesis-choijd.pdf), Jinho D. Choi, Ph.D. Thesis, University of Colorado Boulder, Computer Science and Cognitive Science, 2012.

Additionally, our parser uses a bootstrapping technique that significantly improves parsing accuracy.  See the following paper for more details about the bootstrapping technique.

  * [Getting the Most out of Transition-based Dependency Parsing](http://aclweb.org/anthology-new/P/P11/P11-2121.pdf), Jinho D. Choi, Martha Palmer, Proceedings of the 49th Annual Meeting of the Association for Computational Linguistics: Human Language Technologies (ACL:HLT'11), 687-692, Portland, Oregon, 2011.

Our parser takes about 1.2 milliseconds for parsing a sentence on an Intel Xeon 2.57GHz machine and shows state-of-the-art accuracy compared to other transition-based parsing approaches.

## Training ##

```
java com.googlecode.clearnlp.run.DEPTrain -c <filename> -f <filename> -i <filename> -m <filename> [-n <integer> -sb]

 -c <filename>  : configuration file (required)
 -f <filename>  : feature template file (required)
 -i <directory> : input directory containing training files (required)
 -m <filename>  : model file (output; required)
 -n <integer>   : the bootstrapping level (default: 2)
 -sb <boolean>  : if set, save all intermediate bootstrapping models
```

  * A sample configuration file can be found here: [config\_en\_dep.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_dep.xml).  See the descriptions below for more details about the configuration file.
  * A sample feature template file can be found here: [feature\_en\_dep.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_dep.xml).  See the descriptions below for more details about the feature template file.
  * All files under the input directory are used for training.
  * The model file is the name of an output file to save a trained model.
  * The bootstrapping level indicates the number of iterations for bootstrapping.  In general, bootstrapping once always helps, bootstrapping twice usually helps, and bootstrapping three or more times may or may not help (depending on the training data).
  * If the `-sb` option is set, it saves all intermediate models during bootstrapping.

The following command trains all files under the input directory ([trn/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/trn)) and generates a model file '`sample-dep.jar`' using the configuration file ([config\_en\_dep.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_dep.xml)) and the feature template file ([feature\_en\_dep.xml](http://clearnlp.googlecode.com/git/src/main/resources/feature/feature_en_dep.xml)).  By default, the bootstrapping level of 2 is chosen and only the last model is saved.  Each input file consists of multiple columns; we need only the first 7 columns (ID, form, lemma, pos, feats, head ID, and deprel) as indicated in the configuration file.
```
java com.googlecode.clearnlp.run.DEPTrain -c config_en_dep.xml -f feature_en_dep.xml -i trn/ -m sample-dep.jar
```

## Predicting ##

Our pre-trained models are available at the [TrainedModels](TrainedModels.md) page.

```
java com.googlecode.clearnlp.run.DEPPredict -c <filename> -m <filename> -i <filepath> [-ie <regex> -oe <string>]

 -c <filename> : configuration file (required)
 -m <filename> : model file (required)
 -i <filepath> : input path (required)
 -ie <regex>   : input file extension (default: .*)
 -oe <string>  : output file extension (default: parsed)
```
  * A sample configuration file can be found here: [config\_en\_dep.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_dep.xml).  See the descriptions below for more details about the configuration file.
  * A model file contains the trained model (see above).
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `dep`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.

The following command parses all files whose extension is "`dep`" under the input directory ([dev/](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev)) and generates output files (`*.parsed`) to the same directory using the configuration file ([config\_en\_dep.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_dep.xml)) and the model file ([sample-dep.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-dep.jar)).

```
java -XX:+UseConcMarkSweepGC com.googlecode.clearnlp.run.DEPPredict -c config_en_dep.xml -m sample-dep.jar -i dev/ -ie dep
```

## Evaluating ##

```
java com.googlecode.clearnlp.run.DEPEvaluate -g <filename> -s <filename> -gh <integer> -gd <integer> -sh <integer> -sd <integer>

 -g <filename> : gold-standard file (required)
 -s <filename> : system file (required)
 -gh <integer> : column index of gold head ID (required)
 -gd <integer> : column index of gold dependency label (required)
 -sh <integer> : column index of system head ID (required)
 -sd <integer> : column index of system dependency label (required)
```

  * The gold-standard file contains gold-standard head IDs and dependency labels.
  * The system file contains system-generated head IDs and dependency labels.
  * The '`gh`' option specifies the column index of head IDs in the gold-standard file (starting at 1).
  * The '`gd`' option specifies the column index of dependency labels in the gold-standard file (starting at 1).
  * The '`sh`' option specifies the column index of head IDs in the system-generated file (starting at 1).
  * The '`sd`' option specifies the column index of dependency labels in the system-generated file (starting at 1).

The following command evaluates a system-generated file ([dev/bc-p2.5\_a2e-dev.dep.parsed](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep.parsed)) against a gold-standard file ([dev/bc-p2.5\_a2e-dev.dep](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/dev/bc-p2.5_a2e-dev.dep)).  Both the gold-standard and system-generated files contain head IDs and dependency labels in the 6th and 7th columns, respectively.

```
java com.googlecode.clearnlp.run.DEPEvaluate -g dev/bc-p2.5_a2e-dev.dep -s dev/bc-p2.5_a2e-dev.dep.parsed -gh 6 -gd 7 -sh 6 -sd 7
```

## Configuration file format ##

The following shows a sample configuration file ([config\_en\_dep.xml](http://clearnlp.googlecode.com/git/src/main/resources/configure/config_en_dep.xml)).

```
<configuration>
    <language>en</language>
    <dictionary>model/dictionary-1.1.0.zip</dictionary>
    <pos_model>model/ontonotes-en-pos-1.1.0g.jar</pos_model>

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
        <algorithm name="liblinear" solver="0" cost="0.1" eps="0.1" bias="-1"/>
        <threads>8</threads>
    </train>
</configuration>
```

  * The language element specifies the language for a [tokenizer](Tokenizer.md) and a [morphological analyzer](MPAnalyzer.md): `en` - English.
  * The dictionary element specifies the path to a dictionary file (see [TrainedModels](TrainedModels.md)) used for the tokenizer and the morphological analyzer.
  * The pos\_model element specifies the path to a model file (see [TrainedModels](TrainedModels.md)) for a [part-of-speech tagger](POSTagger.md).
  * The reader element specifies the input [data format](DataFormat.md):
    * For training, it accepts only the "dep" type, which requires the id, form, lemma, pos, feats, head\_id, and deprel fields to be specified.  These fields indicate the indices of the columns containing token IDs, word forms, lemmas, POS tags, extra features, head IDs, and dependency labels, respectively (starting at 1).  See the [CoNLL'09 shared task](http://ufal.mff.cuni.cz/conll2009-st/task-description.html) page for more detail about these fields.
    * For predicting, it accepts the "raw", "line", "tok", "pos", or "dep" type.  When the "raw" or "line" type is used, no field needs to be specified.  When the "tok" type is used, the form field must be specified.  When the "pos" type is used, the form and pos fields must be specified.  When the "dep" type is used, the id, form, lemma, pos, and feats fields must be specified.
    * For the following example, token IDs, word forms, lemmas, pos tags, extra features, head IDs, and dependency labels are in the 1st ~ 7th columns, respectively; thus, the above configuration can be used to read this example.

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

  * The train element specifies an algorithm used for training.  See the [LibLinear](LibLinear.md) page for more details about the Liblinear hyperparameters.  The thread element specifies the number of threads to be used for training.

## Feature template format ##

Please contact the owner of ClearNLP for more details about the feature template format.