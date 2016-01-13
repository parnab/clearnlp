# Trained Models (v1.3.x) #

## Dictionary ##

Here is a dictionary file used for tokenization and morphological analysisr.
  * [dictionary-1.3.1.zip](https://bitbucket.org/jdchoi77/models/downloads/dictionary-1.3.1.zip)

## OntoNotes ##

The [OntoNotes](http://www.bbn.com/ontonotes/) models are trained on a mixture of:

  * Broadcasting conversations: 11,846 sentences, 173,289 tokens.
  * Broadcasting news: 10,658 sentences, 206,901 tokens.
  * News magazines: 6,905 sentences, 164,217 tokens.
  * Newswires: 34,944 sentences, 878,223 tokens.
  * Telephone conversations: 11,274 sentences, 90,403 tokens.
  * Web-texts: 13,362 sentences, 303,182 tokens.

| **Task** | **Mode** |  **Algorithm** | **HDD** | **RAM** | **Date** | **Download** |
|:---------|:---------|:---------------|:--------|:--------|:---------|:-------------|
| Part-of-speech tagging | `pos`    | AdaGrad        | 21.7MB  | 781MB   | 12/29/2012 | [ontonotes-en-pos-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-pos-1.3.0.tgz) |
| Dependency parsing | `dep`    | AdaGrad        | 64.4MB  | 1.8GB   | 12/29/2012 | [ontonotes-en-dep-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-dep-1.3.0.tgz) |
| Predicate identification | `pred`   | Liblinear      | 1.7MB   | 82MB    | 12/29/2012 | [ontonotes-en-pred-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-pred-1.3.0.tgz) |
| Roleset classification | `role`   | Liblinear      | 8.2MB   | 237MB   | 12/29/2012 | [ontonotes-en-role-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-role-1.3.0.tgz) |
| Semantic role labeling | `srl`    | AdaGrad        | 25.2MB  | 654MB   | 12/29/2012 | [ontonotes-en-srl-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-srl-1.3.0.tgz) |
| VerbNet classification | `sense_vn` | Liblinear      | 1MB     | 65MB    | 02/26/2012 | [ontonotes-en-sense-vn-1.3.1b.tgz](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-sense-vn-1.3.1b.tgz) |

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

| **Task** | **Mode** |  **Algorithm** | **HDD** | **RAM** | **Date** | **Download** |
|:---------|:---------|:---------------|:--------|:--------|:---------|:-------------|
| Part-of-speech tagging | `pos`    | AdaGrad        | 7.8MB   | 360MB   | 12/29/2012 | [mayo-en-pos-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/mayo-en-pos-1.3.0.tgz) |
| Dependency parsing | `dep`    | AdaGrad        | 24.2MB  | 645MB   | 12/29/2012 | [mayo-en-dep-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/mayo-en-dep-1.3.0.tgz) |
| Predicate identification | `pred`   | Liblinear      | 450.1KB | 31MB    | 12/29/2012 | [mayo-en-pred-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/mayo-en-pred-1.3.0.tgz) |
| Roleset classification | `role`   | Liblinear      | 1.1MB   | 81MB    | 12/29/2012 | [mayo-en-role-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/mayo-en-role-1.3.0.tgz) |
| Semantic role labeling | `srl`    | AdaGrad        | 5.8MB   | 219MB   | 12/29/2012 | [mayo-en-srl-1.3.0.jar](https://bitbucket.org/jdchoi77/models/downloads/mayo-en-srl-1.3.0.tgz) |

## Sample ##

The sample models are built from our [sample data](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/trn).  These models are not for actual run but for unit-testing or debugging.

  * [sample-pos.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-pos-1.3.0.tgz), [sample-dep.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-dep-1.3.0.tgz),[sample-pred.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-pred-1.3.0.tgz),[sample-role.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-role-1.3.0.tgz),[sample-srl.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-srl-1.3.0.tgz)