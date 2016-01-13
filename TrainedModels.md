# Trained Models #

## Old models ##

  * [Version 1.2.x](TrainedModelsOld.md).
  * [Version 1.3.x](TrainedModels13.md).

## Dictionary ##

Here is a dictionary file used for tokenization and morphological analysisr.
  * [dictionary-1.4.0.zip](https://bitbucket.org/jdchoi77/models/downloads/dictionary-1.4.0.zip)

# English #

## OntoNotes ##

The [OntoNotes](http://www.bbn.com/ontonotes/) models are trained on a mixture of:

  * Broadcasting conversations: 10,826 sentences, 171,120 tokens.
  * Broadcasting news: 10,349 sentences, 206,057 tokens.
  * News magazines: 6,672 sentences, 163,627 tokens.
  * Newswires: 34,492 sentences, 876,399 tokens.
  * Religious texts: 21,419 sentences, 296,437 tokens.
  * Telephone conversations: 8,969 sentences, 85,466 tokens.
  * Web-texts: 12,452 sentences, 284,975 tokens.

| **Task** | **Mode** |  **Algorithm** | **HDD** | **RAM** | **Date** | **Download** |
|:---------|:---------|:---------------|:--------|:--------|:---------|:-------------|
| Part-of-speech tagging | `pos`    | AdaGrad        | 20.0MB  | -       | 07/15/2013 | [ontonotes-en-pos-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-pos-1.4.0.tgz) |
| Dependency parsing | `dep`    | AdaGrad        | 1.4GB   | -       | 07/15/2013 | [ontonotes-en-dep-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-dep-1.4.0.tgz) |
| Predicate identification | `pred`   | Liblinear      | 156.2KB | -       | 07/15/2013 | [ontonotes-en-pred-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-pred-1.4.0.tgz) |
| Roleset classification | `role`   | Liblinear      | 7.6MB   | -       | 07/15/2013 | [ontonotes-en-role-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-role-1.4.0.tgz) |
| Semantic role labeling | `srl`    | AdaGrad        | 27.4MB  | -       | 07/15/2013 | [ontonotes-en-srl-1.4.2.tgz](https://bitbucket.org/jdchoi77/models/downloads/ontonotes-en-srl-1.4.2.tgz) |

## Medical ##

The medical models are trained on a mixture of:

  * Clinical questions: 1,600 sentences, 30,138 tokens.
  * Medpedia articles: 2,796 sentences, 49,922 tokens.
  * MiPACQ clinical notes: 8,040 sentences, 107,663 tokens.
  * MiPACQ pathological notes: 1,225 sentences, 21,581 tokens.
  * Seattle group health clinical notes: 5,020 sentences, 61,124 tokens.
  * Seattle group health pathological notes: 2,294 sentences, 34,384 tokens.
  * SHARP clinical notes: 6,787 sentences, 94,205 tokens.
  * SHARP stratified: 4,316 sentences, 43,037 tokens.
  * SHARP stratified SGH: 4,963 sentences, 49,081 tokens.
  * TEMPREL clinical notes: 19,775 sentences, 266,979 tokens.
  * TEMPREL pathological notes: 4,335 sentences, 78,829 tokens.

| **Task** | **Mode** |  **Algorithm** | **HDD** | **RAM** | **Date** | **Download** |
|:---------|:---------|:---------------|:--------|:--------|:---------|:-------------|
| Part-of-speech tagging | `pos`    | AdaGrad        | 9.1MB   | -       | 12/29/2012 | [mayo-pos-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/mayo-pos-1.4.0.tgz) |
| Dependency parsing | `dep`    | AdaGrad        | 515.3MB | -       | 12/29/2012 | [mayo-dep-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/mayo-dep-1.4.0.tgz) |
| Predicate identification | `pred`   | Liblinear      | 35.2KB  | -       | 12/29/2012 | [mayo-pred-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/mayo-pred-1.4.0.tgz) |
| Roleset classification | `role`   | Liblinear      | 1.0MB   | -       | 12/29/2012 | [mayo-role-1.4.0.tgz](https://bitbucket.org/jdchoi77/models/downloads/mayo-role-1.4.0.tgz) |
| Semantic role labeling | `srl`    | AdaGrad        | 6.0MB   | -       | 12/29/2012 | [mayo-srl-1.4.2.tgz](https://bitbucket.org/jdchoi77/models/downloads/mayo-srl-1.4.2.tgz) |

## Sample ##

The sample models are built from our [sample data](http://clearnlp.googlecode.com/git/src/main/resources/sample-dev/trn).  These models are not for actual run but for unit-testing or debugging.

  * [sample-pos.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-pos-1.4.0.tgz), [sample-dep.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-dep-1.4.0.tgz),[sample-pred.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-pred-1.4.0.tgz),[sample-role.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-role-1.4.0.tgz),[sample-srl.jar](https://bitbucket.org/jdchoi77/models/downloads/sample-en-srl-1.4.0.tgz)