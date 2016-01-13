# Constituent-to-Dependency Converter #

Our constituent-to-dependency converter takes the [Penn Treebank](http://www.cis.upenn.edu/~treebank/) style constituent trees as input and generates the C<font size='1'>LEAR</font> style dependency trees as output.  Here are some of the key features of the C<font size='1'>LEAR</font> dependency conversion.

  * Generates the [Stanford dependency](http://nlp.stanford.edu/software/stanford-dependencies.shtml) labels.
  * Produces long-distance dependencies, including non-projective dependencies, by remapping empty categories in constituent trees.
  * Allows users to customize their own headrules.
  * Includes secondary dependencies caused by several linguistic phenomena (e.g., right node raising, gapping).
  * Preserves function tags as features of individual dependency nodes.

The C<font size='1'>LEAR</font> dependency conversion has been tested on various Treebanks and shown robust results across different corpora.  See the technical report for more details about this conversion.

  * [Guidelines for the Clear Style Constituent to Dependency Conversion](https://dl.dropbox.com/u/15060914/publications/ics-12.pdf), Jinho D. Choi, Martha Palmer, Technical report 01-12: Institute of Cognitive Science, University of Colorado Boulder, Boulder, CO, 2012.

Currently our converter supports only English.  If you'd like to contribute for other languages, please contact the owner of ClearNLP.

## How to run ##

```
java com.googlecode.clearnlp.run.C2DConvert -h <filename> -d <filename> -i <filepath> [-ie <regex> -oe <string> -l <language> -m <string>]

-h  <filename> : name of a headrule file (required)
-d  <filename> : name of a dictionary file (required)
-i  <filepath> : input path (required)
-ie <regex>    : input file extension (default: .*)
-oe <string>   : output file extension (default: dep)
-l  <language> : language (default: en)
-m  <string>   : merge labels (default: null)
```

  * A headrule file can be found here: [headrule\_en\_stanford.txt](http://clearnlp.googlecode.com/git/src/main/resources/headrule/headrule_en_stanford.txt).  See the [headrule file format](HeadruleFormat.md) page for more details about this file.
  * A dictionary file is used for generating lemmas and can be found here: [TrainedModels](TrainedModels.md).  See [MPAnalyzer](MPAnalyzer.md) for more details about morphological analysis.
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `parse`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.
  * The language indicates the language of constituent trees to be converted.  Currently, our conversion supports only English (`en`).
  * The `-m` option merges the specified dependency labels to user defined labels.  The followings show the format of this option.  For instance, if `-m` is specified as "`SBJ=nsubj,csubj,nsubjpass,csubjpass|OBJ=dobj,iobj`", it relabels `{nsubj`, `csubj`, `nsubjpass`, `csubjpass}` to `SBJ` and `{dobj`, `iobj}` to `OBJ`.
```
LABEL  ::= <new_label>=<old_label>[,<old_label>]*
LABELS ::= LABEL[|LABEL]*
```

The following command takes a sample input file ([wsj\_0001.parse](http://clearnlp.googlecode.com/git/src/main/resources/sample/wsj_0001.parse)) and generates an output file ([wsj\_0001.parse.dep](http://clearnlp.googlecode.com/git/src/main/resources/sample/wsj_0001.parse.dep)) using the headrule file ([headrule\_en\_stanford.txt](http://clearnlp.googlecode.com/git/src/main/resources/headrule/headrule_en_stanford.txt)), the dictionary file ([TrainedModels](TrainedModels.md)), and the default output file extension (`-oe dep`).

```
java com.googlecode.clearnlp.run.C2DConvert -h headrule_en_stanford.txt -d dictionary-1.1.0.zip -i wsj_0001.parse
```

## Input file ##

A sample input file can be found here: [wsj\_0001.parse](http://clearnlp.googlecode.com/git/src/main/resources/sample/wsj_0001.parse).  Each tree must start with an empty clause (example 1) or a `TOP` clause (example 2).

  * Example 1
```
((S (NP-SBJ (NP (NNP Pierre)
                (NNP Vinken))
            (, ,)
            (ADJP (NML (CD 61)
                       (NNS years))
                  (JJ old))
            (, ,))
    (VP (MD will)
        (VP (VB join)
            (NP (DT the)
                (NN board))
            (PP-CLR (IN as)
                    (NP (DT a)
                        (JJ nonexecutive)
                        (NN director)))
            (NP-TMP (NNP Nov.)
                    (CD 29))))
    (. .)))
```

  * Example 2
```
(TOP (S (NP-SBJ (NNP Mr.)
                (NNP Vinken))
        (VP (VBZ is)
            (NP-PRD (NP (NN chairman))
                    (PP (IN of)
                        (NP (NP (NNP Elsevier)
                                (NNP N.V.))
                            (, ,)
                            (NP (DT the)
                                (NNP Dutch)
                                (VBG publishing)
                                (NN group))))))
        (. .)))
```

## Output file ##

A sample output file can be found here: [wsj\_0001.parse.dep](http://clearnlp.googlecode.com/git/src/main/resources/sample/wsj_0001.parse.dep).  Each field is delimited by a tab character and each tree is delimited by a blank line.  See the [data format](DataFormat.md) page for more details about the format of the converted dependency trees.

```
1	Pierre	pierre	NNP	_	2	nn	_
2	Vinken	vinken	NNP	_	9	nsubj	_
3	,	,	,	_	2	punct	_
4	61	0	CD	_	5	num	_
5	years	year	NNS	_	6	npadvmod	_
6	old	old	JJ	_	2	amod	_
7	,	,	,	_	2	punct	_
8	will	will	MD	_	9	aux	_
9	join	join	VB	_	0	root	_
10	the	the	DT	_	11	det	_
11	board	board	NN	_	9	dobj	_
12	as	as	IN	syn=CLR	9	prep	_
13	a	a	DT	_	15	det	_
14	nonexecutive	nonexecutive	JJ	_	15	amod	_
15	director	director	NN	_	12	pobj	_
16	Nov.	nov.	NNP	sem=TMP	9	npadvmod	_
17	29	0	CD	_	16	num	_
18	.	.	.	_	9	punct	_

1	Mr.	mr.	NNP	_	2	nn	_
2	Vinken	vinken	NNP	_	3	nsubj	_
3	is	be	VBZ	_	0	root	_
4	chairman	chairman	NN	syn=PRD	3	attr	_
5	of	of	IN	_	4	prep	_
6	Elsevier	elsevier	NNP	_	7	nn	_
7	N.V.	n.v.	NNP	_	5	pobj	_
8	,	,	,	_	7	punct	_
9	the	the	DT	_	12	det	_
10	Dutch	dutch	NNP	_	12	nn	_
11	publishing	publish	VBG	_	12	amod	_
12	group	group	NN	_	7	appos	_
13	.	.	.	_	3	punct	_
```

## Dependency labels ##

Here is a list of C<font size='1'>LEAR</font> dependency labels.  The Italicized labels do not exist in the Stanford dependency approach but added in our approach to improve robustness across different corpora.

| **Label**   | **Description**             | **Label**   | **Description**             | **Label**    | **Description**               |
|:------------|:----------------------------|:------------|:----------------------------|:-------------|:------------------------------|
| acomp       | adjectival complement       | dobj        | direct object               | _oprd_       | object predicate              |
| advcl       | adverbial clause modifier   | expl        | expletive                   | parataxis    | parataxis                     |
| advmod      | adverbial modifier          | _hmod_      | modifier in hyphenation     | partmod      | participial modifier          |
| agent       | agent                       | _hyph_      | hyphen                      | pcomp        | complement of a preposition   |
| amod        | adjectival modifier         | infmod      | infinitival modifier        | pobj         | object of a preposition       |
| appos       | appositional modifier       | _intj_      | interjection                | poss         | possession modifier           |
| attr        | attribute                   | iobj        | indirect object             | possessive   | possessive modifier           |
| aux         | auxiliary                   | mark        | marker                      | preconj      | pre-correlative conjunction   |
| auxpass     | auxiliary (passive)         | _meta_      | meta modifier               | predet       | predeterminer                 |
| cc          | coordinating conjunction    | neg         | negation modifier           | prep         | prepositional modifier        |
| ccomp       | clausal complement          | _nmod_      | modifier of nominal         | prt          | particle                      |
| complm      | complementizer              | nn          | noun compound modifier      | punct        | punctuation                   |
| conj        | conjunct                    | npadvmod    | noun phrase as advmod       | quantmod     | quantifier phrase modifier    |
| csubj       | clausal subject             | nsubj       | nominal subject             | rcmod        | relative clause modifier      |
| csubjpass   | clausal subject (passive)   | nsubjpass   | nominal subject (passive)   | root         | root                          |
| dep         | unclassified dependent      | num         | numeric modifier            | xcomp        | open clausal complement       |
| det         | determiner                  | number      | number compound modifier    |              |                               |