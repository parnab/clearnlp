# Tokenizer and Sentence Segmenter #

Our tokenizer takes a raw text and splits words by their morphological aspects.  It also gives an option of grouping tokens into sentences.  Our tokenizer is based on the [LDC](http://www.ldc.upenn.edu/) tokenizer used for creating English Treebanks (as 2012) although it uses more robust heuristics.  Here are some key features about our tokenizer.

  * Emoticons are recognized as one unit (e.g., `:-)`, `^_^`).
  * Hyperlinks are recognized as one unit (`google.com`, `jinho@gmail.com`, `index.html`).
  * Numbers consisting of punctuation are recognized as one unit (e.g., `0.1`, `2/3`).
  * Repeated punctuation are grouped together (e.g., `---`, `...`).
  * Abbreviations are recognized as one unit (e.g., `Prof.`, `Ph.D`).
  * File extensions are not tokenized (e.g., `clearnlp.zip`, `tokenizer.doc`).
  * Units are tokenized (e.g., `1 kg`, `2 cm`).
  * Usernames including periods are recognized as one unit (e.g., `jinho.choi`).

Currently our tokenizer supports only English.

## How to run ##

```
com.googlecode.clearnlp.run.Tokenizer -d <filename> -i <filepath> [-ie <regex> -oe <string> -if <string> -of <string> -l <language> -twit]

 -d <filename>   : name of a dictionary file (required)
 -i <filepath>   : input path (required)
 -ie <regex>     : input file extension (default: .*)
 -oe <string>    : output file extension (default: tok)
 -if <string>    : input format (default: raw)
 -of <string>    : input format (default: line)
 -l <language>   : language (default: en)
 -twit <boolean> : if set, do not tokenize special punctuation used in twitter
```

  * A dictionary file can be found here: [TrainedModels](TrainedModels.md).
  * The input path can point to either a file or a directory.  When the input path points to a file, only the specific file is processed.  When the input path points to a directory, all files with the input file extension (`-ie`) under the specific directory are processed.
  * The input file extension can be either a string (e.g., `txt`) or a [regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html) specifying the extension of input files.  The default value (`.*`) implies files with any extension.  This option is used only when the input path (`-i`) points to a directory.
  * The output file extension gets appended to input filenames, and used to generate corresponding output files.
  * The input format can be either "raw" or "line".  If the raw format is chosen, the tokenizer automatically groups tokens into sentences.  If the line format is chosen, the tokenizer treats each line as one sentence.  See the [DataFormat](DataFormat.md) for more details about data formats.
  * The output format can be either "line" or "tok".  See the [DataFormat](DataFormat.md) for more details about data formats.
  * The language indicates the language of constituent trees to be converted.  Currently, our conversion supports only English (`en`).
  * If the `twit` option is used, special punctuation such as `&` and `#` used in twitter are not tokenized.

The following command takes a sample input file ([iphone5.txt](http://clearnlp.googlecode.com/git/src/main/resources/sample/iphone5.txt)) and generates an output file ([iphone5.txt.tok](http://clearnlp.googlecode.com/git/src/main/resources/sample/iphone5.txt.tok)) using the dictionary file ([TrainedModels](TrainedModels.md)), and the default output file extension (`-oe tok`).

```
 java com.googlecode.clearnlp.run.Tokenizer -d dictionary-xxx.zip -i iphone5.txt
```