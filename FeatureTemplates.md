# Dependency Parsing #

## Cutoff ##

The cutoff element requires two attributes to be filled:

  * `label`: class labels that occur less than this value in the training data will not be used during training.
  * `feature`: features that occur less than this value in the training data will not be used during training.

Two cutoff elements need to be specified.  The first one is used for building the base model, and the second one is used for building models using bootstrapping.

```
<cutoff label="4" feature="2" note="base"/>
<cutoff label="4" feature="4" note="bootstrapping"/>
```

## N-gram features ##

The feature element requires two or more attributes.

  * `n`: the number of features to be joined (`n > 0`).
  * `fi ::= token:field`, where `i = [0, n)`

The followings show different representations of `token`.
  * `s`: the top of the stack.
  * `s-1`: the second top of the stack (similarly, `s-2`, ...).
  * `l`: the top of the stack.
  * `l±1`: the token whose ID is `i±1`, where `i` is the ID of the top of the stack (similarly, `l±2`, ...).
  * `b`: the front of the input buffer.
  * `b±1`: the token whose ID is `j±1`, where `j` is the ID of the front of the input buffer (similarly, `b±2`, ...).
  * `s_h`: the head of `s`.
  * `s_h2`: the grand-head of `s`.
  * `s_lmd`: the leftmost dependent of `s`.
  * `s_rmd`: the rightmost dependent of `s`.
  * `s_lmd2`: the 2nd-leftmost dependent of `s`.
  * `s_rmd2`: the 2nd-rightmost dependent of `s`.
  * `s_lns`: the left-nearest sibling of `s`.
  * `s_rns`: the right-nearest sibling of `s`.

The followings show different representations of `field`.

  * `f`: word-form.
  * `m`: lemma.
  * `p`: pos tag.
  * `d`: dependency label.
  * `n`: the distance between the top of the stack and the front of the input buffer.
  * `lv`: the left valency.
  * `rv`: the right valency.

The following example gives a joined feature between the pos tag of the top of the stack and the word form of the front of the input buffer.

```
<feature n="2" f0="s:p" f1="b:f"/>
```

## Binary features ##

Three kinds of binary features can be specified.

  * `l:b0`: this feature is turned on when the top of the stack is the leftmost token in a sentence.
  * `b:b1`: this feature is turned on when the front of the input buffer is the rightmost token in a sentence.
  * `l:b2`: this feature is turned on when the top of the stack is adjacent to the front of the input buffer.

```
<feature t="b" n="1" f0="l:b0" note="lambda is the leftmost token"/>
<feature t="b" n="1" f0="b:b1" note="beta is the righttmost token"/>
<feature t="b" n="1" f0="l:b2" note="lambda and beta are adjacent"/>
```