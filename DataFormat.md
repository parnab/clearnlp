# Data Format #

ClearNLP supports 6 data formats.  Note that ClearNLP assumes all input files to be in UTF-8.  For the tok, pos, dep, and srl formats, each field is delimited by a tab character (`'\t'`) and each sentence is delimited by a blank line (`'\n'`).

## Raw format (raw) ##

The raw format accepts texts in any format.

```
I'd like to meet Mr. Choi. He's the owner of ClearNLP.
```

## Line format (line) ##

The line format requires one sentence per line.

```
I'd like to meet Mr. Choi. 
He's the owner of ClearNLP.
```

## Token format (tok) ##

The token format requires 1 field.

  * FORM: word form

```
I
'd
like
to
meet
Mr.
Choi
.

He
's
the
owner
of
ClearNLP
.
```

## Part-of-speech format (pos) ##

The part-of-speech format requires 2 fields.

  * FORM: word form
  * POS: part-of-speech tag

```
I           PRP
'd          MD
like        VB
to          TO
meet        VB
Mr.         NNP
Choi        NNP
.           .

He          PRP
's          VBZ
the         DT
owner       NN
of          IN
ClearNLP    NNP
.           .
```

## Dependency format (dep) ##

The dependency format requires 7 fields.

  * ID: current token ID (starting at 1)
  * FORM: word form
  * LEMMA: lemma
  * POS: part-of-speech tag
  * FEATS: features (different features are delimited by '`|`', keys and values are delimited by '`=`', and '`_`' indicates no feature)
  * HEAD: head token ID
  * DEPREL: dependency label

```
1    I           i           PRP    _    3    nsubj
2    'd          would       MD     _    3    aux
3    like        like        VB     _    0    root
4    to          to          TO     _    5    aux
5    meet        meet        VB     _    3    xcomp
6    Mr.         mr.         NNP    _    7    nn
7    Choi        choi        NNP    _    5    dobj
8    .           .           .      _    3    punct

1    He          he          PRP    _    2    nsubj
2    's          is          VBZ    _    0    root
3    the         the         DT     _    4    nn
4    owner       owner       NN     _    2    attr
5    of          of          IN     _    4    prep
6    ClearNLP    clearnlp    NNP    _    5    pobj
7    .           .           .      _    .    punct
```

## Semantic role format (srl) ##

The semantic role labeling format requires 8 fields.

  * ID: current token ID (starting at 1)
  * FORM: word form
  * LEMMA: lemma
  * POS: part-of-speech tag
  * FEATS: features (different features are delimited by '`|`', keys and values are delimited by '`=`', and '`_`' indicates no feature)
  * HEAD: head token ID
  * DEPREL: dependency label
  * SHEADS: semantic heads ('`_`' indicates no semantic head)

```
SHEADS ::= _ | SHEAD(;SHEAD)*
SHEAD  ::= HEAD:LABEL
```

The roleset ID of each predicate is indicated in the FEATS field (e.g., `pb=like.02`).

```
1    I           i           PRP    _             3    nsubj     3:A0;5:A0
2    'd          would       MD     _             3    aux       3:AM-MOD
3    like        like        VB     pb=like.02    0    root
4    to          to          TO     _             5    aux
5    meet        meet        VB     pb=meet.01    3    xcomp     3:A1
6    Mr.         mr.         NNP    _             7    nn
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