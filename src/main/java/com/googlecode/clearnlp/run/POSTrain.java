/**
* Copyright (c) 2009-2012, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package com.googlecode.clearnlp.run;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.kohsuke.args4j.Option;
import org.w3c.dom.Element;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.googlecode.clearnlp.classification.model.StringModel;
import com.googlecode.clearnlp.classification.train.StringTrainSpace;
import com.googlecode.clearnlp.feature.xml.POSFtrXml;
import com.googlecode.clearnlp.pos.POSLib;
import com.googlecode.clearnlp.pos.POSNode;
import com.googlecode.clearnlp.pos.POSTagger;
import com.googlecode.clearnlp.reader.POSReader;
import com.googlecode.clearnlp.util.UTFile;
import com.googlecode.clearnlp.util.UTInput;
import com.googlecode.clearnlp.util.UTXml;
import com.googlecode.clearnlp.util.list.SortedDoubleArrayList;
import com.googlecode.clearnlp.util.map.Prob1DMap;
import com.googlecode.clearnlp.util.pair.Pair;


/**
 * Trains a part-of-speech tagging model.
 * @since v0.1
 * @author Jinho D. Choi ({@code choijd@colorado.edu})
 */
public class POSTrain extends AbstractRun
{
	static final int MODEL_SIZE = 2;
	
	protected final int FLAG_DOMAIN  = 0;
	protected final int FLAG_GENERAL = 1;
	protected final int FLAG_DYNAMIC = 2;
	
	@Option(name="-i", usage="the directory containg training files (input; required)", required=true, metaVar="<directory>")
	protected String s_trainDir;
	@Option(name="-c", usage="the configuration file (input; required)", required=true, metaVar="<filename>")
	protected String s_configXml;
	@Option(name="-f", usage="the feature file (input; required)", required=true, metaVar="<filename>")
	protected String s_featureXml;
	@Option(name="-m", usage="the model file (output; required)", required=true, metaVar="<filename>")
	protected String s_modelFile;
	@Option(name="-t", usage="the similarity threshold (default: -1)", required=false, metaVar="<double>")
	protected double d_threshold = -1;
	@Option(name="-flag", usage="0|1|2 (default: 1)\n0: train only a domain-specific model\n1: train only a generalized model\n2: train both models using dynamic model selection", required=false, metaVar="<int>")
	protected int i_flag = FLAG_GENERAL;
	
	public POSTrain() {}
	
	public POSTrain(String[] args)
	{
		initArgs(args);
		
		try
		{
			run(s_configXml, s_featureXml, s_trainDir, s_modelFile, d_threshold, i_flag);
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	public void run(String configXml, String featureXml, String trainDir, String modelFile, double threshold, int flag) throws Exception
	{
		Element     eConfig = UTXml.getDocumentElement(new FileInputStream(configXml));
		POSReader    reader = (POSReader)getReader(eConfig);
		POSFtrXml       xml = new POSFtrXml(new FileInputStream(featureXml));
		String[] trainFiles = UTFile.getSortedFileList(trainDir);
		POSTagger[] taggers = null;
		
		if (flag == FLAG_DYNAMIC)
		{
			if (threshold < 0)	threshold = crossValidate(trainFiles, reader, xml, eConfig);
			taggers = getTrainedTaggers(eConfig, reader, xml, trainFiles, null);
		}
		else
		{
			taggers    = new POSTagger[1];
			taggers[0] = getTrainedTagger(eConfig, reader, xml, trainFiles, null, flag);
		}
		
		saveModels(modelFile, featureXml, taggers, threshold);
	}
	
	/** @return a single POS tagging model using {@code modId}. */
	public POSTagger getTrainedTagger(Element eConfig, POSReader reader, POSFtrXml xml, String[] trnFiles, IntOpenHashSet sDev, int modId) throws Exception
	{
		Pair<Set<String>, Map<String,String>> p;
		StringTrainSpace space;
		Set<String> sLemmas;
		StringModel model;
		
		sLemmas = getLemmaSet  (reader, xml, modId, trnFiles, sDev);
		p       = getLexica    (reader, xml, modId, sLemmas, trnFiles, sDev);
		space   = getTrainSpace(reader, xml, modId, sLemmas, p.o1, p.o2, trnFiles, sDev); 
		model   = (StringModel)getModel(UTXml.getFirstElementByTagName(eConfig, TAG_TRAIN), space, modId);
		
		return new POSTagger(xml, sLemmas, p.o1, p.o2, model);
	}
	
	/** @return both domain-specific and generalized models. */
	public POSTagger[] getTrainedTaggers(Element eConfig, POSReader reader, POSFtrXml xml, String[] trnFiles, IntOpenHashSet sDev) throws Exception
	{
		POSTagger[] taggers = new POSTagger[MODEL_SIZE];
		int modId;
		
		for (modId=0; modId<MODEL_SIZE; modId++)
		{
			System.out.printf("===== Training model %d =====\n", modId);
			taggers[modId] = getTrainedTagger(eConfig, reader, xml, trnFiles, sDev, modId);
		}
		
		return taggers;
	}
	
	/** Saves POS tagging models. */
	public void saveModels(String modelFile, String featureXml, POSTagger[] taggers, double threshold) throws Exception
	{
		JarArchiveOutputStream zout = new JarArchiveOutputStream(new FileOutputStream(modelFile));
		PrintStream fout;
		
		if (taggers.length > 1)
		{
			zout.putArchiveEntry(new JarArchiveEntry(ENTRY_CONFIGURATION));
			fout = new PrintStream(zout);
			fout.println(threshold);
			fout.close();
			zout.closeArchiveEntry();			
		}
		
		zout.putArchiveEntry(new JarArchiveEntry(ENTRY_FEATURE));
		IOUtils.copy(new FileInputStream(featureXml), zout);
		zout.closeArchiveEntry();
		
		zout.putArchiveEntry(new JarArchiveEntry(ENTRY_MODEL));
		fout = new PrintStream(new BufferedOutputStream(zout));
		fout.println(taggers.length);
		for (POSTagger tagger : taggers) tagger.saveModel(fout);
		fout.close();
		zout.closeArchiveEntry();		
		
		zout.close();
	}

	/** Called by {@link POSTrain#getTrainedTaggers(Element, POSReader, POSFtrXml, String[], IntOpenHashSet)}. */
	private Set<String> getLemmaSet(POSReader reader, POSFtrXml xml, int modId, String[] trnFiles, IntOpenHashSet sDev) throws Exception
	{
		int dfCutoff = xml.getDocumentFrequency(modId);
		Prob1DMap map = new Prob1DMap();
		int i, size = trnFiles.length;
		Set<String> set;
		POSNode[] nodes;
		String    lemma;
		
		System.out.println("Collecting n-gram set:");
		System.out.println("- document frequency cutoff: "+dfCutoff);
		
		for (i=0; i<size; i++)
		{
			if (sDev != null && sDev.contains(i))	continue;
			reader.open(UTInput.createBufferedFileReader(trnFiles[i]));
			set = new HashSet<String>();
			
			while ((nodes = reader.next()) != null)
			{
				POSLib.normalizeForms(nodes);
				
				for (POSNode node : nodes)
					set.add(node.lemma);
			}
			
			reader.close();
			for (String s : set)	map.add(s);
		}
		
		set = new HashSet<String>();
		
		for (ObjectCursor<String> cur : map.keys())
		{
			lemma = cur.value;
			
			if (map.get(lemma) > dfCutoff)
				set.add(lemma);
		}
		
		System.out.printf("- lemma reduction: %d -> %d\n", map.size(), set.size());
		return set;
	}
	
	/** Called by {@link POSTrain#getTrainedTaggers(Element, POSReader, POSFtrXml, String[], IntOpenHashSet)}. */
	private Pair<Set<String>,Map<String,String>> getLexica(POSReader reader, POSFtrXml xml, int xmlId, Set<String> sLemmas, String[] trnFiles, IntOpenHashSet sDev)
	{
		POSTagger tagger = new POSTagger(sLemmas);
		int i, size = trnFiles.length;
		POSNode[] nodes;
		int featureCutoff = xml.getFeatureCutoff(xmlId);
		double ambiguityThreshold = xml.getAmbiguityThreshold(xmlId);
		
		System.out.println("Collecting lexica:");
		System.out.println("- lexica cutoff: "+featureCutoff);
		System.out.println("- ambiguity class threshold: "+ambiguityThreshold);
		
		for (i=0; i<size; i++)
		{
			if (sDev != null && sDev.contains(i))	continue;
			reader.open(UTInput.createBufferedFileReader(trnFiles[i]));
			
			while ((nodes = reader.next()) != null)
				tagger.tag(nodes);
			
			reader.close();
		}
		
		Set<String> sForms = tagger.getFormSet(featureCutoff);
		Map<String,String> mAmbi = tagger.getAmbiguityMap(ambiguityThreshold);
		System.out.println("- # of word-forms: "+sForms.size());
		System.out.println("- # of word-forms with ambiguity classes: "+mAmbi.size());
		
		return new Pair<Set<String>,Map<String,String>>(sForms, mAmbi);
	}
	
	/** Called by {@link POSTrain#getTrainedTaggers(Element, POSReader, POSFtrXml, String[], IntOpenHashSet)}. */
	private StringTrainSpace getTrainSpace(POSReader reader, POSFtrXml xml, int modId, Set<String> sLemmas, Set<String> sForms, Map<String, String> ambiguityMap, String[] trnFiles, IntOpenHashSet sDev)
	{
		StringTrainSpace space = new StringTrainSpace(false, xml.getLabelCutoff(modId), xml.getFeatureCutoff(modId));
		POSTagger tagger = new POSTagger(xml, sLemmas, sForms, ambiguityMap, space);
		int i, size = trnFiles.length;
		POSNode[] nodes;
		
		System.out.println("Collecting training instances:");
		
		for (i=0; i<size; i++)
		{
			if (sDev != null && sDev.contains(i))	continue;
			reader.open(UTInput.createBufferedFileReader(trnFiles[i]));
			
			while ((nodes = reader.next()) != null)
				tagger.tag(nodes);
			
			reader.close();
			System.out.print(".");
		}
		
		System.out.println();
		return space;
	}
	
	public double crossValidate(String[] trnFiles, POSReader reader, POSFtrXml xml, Element eConfig) throws Exception
	{
		SortedDoubleArrayList list = new SortedDoubleArrayList();
		int devId, size = trnFiles.length;
		POSTagger[] taggers;
		IntOpenHashSet sDev = new IntOpenHashSet();
		
		
		for (devId=0; devId<size; devId++)
		{
			System.out.printf("<== Cross validation %d ==>\n", devId);
			sDev.clear();	sDev.add(devId);
			
			taggers = getTrainedTaggers(eConfig, reader, xml, trnFiles, sDev);
			crossValidatePredict(trnFiles[devId], reader, taggers, list);
		}
		
	/*	PrintStream fout = UTOutput.createPrintBufferedFileStream("cosine-sims.txt");
		for (int i=0; i<list.size(); i++)	fout.println(list.get(i));
		fout.close();*/
		
		int n = (int)Math.round(list.size() * 0.05);
		double threshold = (double)Math.ceil(list.get(n)*1000)/1000;
		
		System.out.println("Out-of-domain validation:");
		System.out.println("- threshold: "+threshold);
	
		return threshold;
	}
	
	/** Called by {@link POSTrain#crossValidate(String[], POSReader, POSFtrXml, Element)}. */
	private void crossValidatePredict(String devFile, POSReader reader, POSTagger[] taggers, SortedDoubleArrayList list)
	{
		int[] local   = new int[MODEL_SIZE];
		int[] correct = new int[MODEL_SIZE];
		int modId, total = 0;
		POSNode[] nodes;
		String[]  gold;
		double sim;
		
		System.out.println("Predicting: "+devFile);
		reader.open(UTInput.createBufferedFileReader(devFile));
		
		while ((nodes = reader.next()) != null)
		{
			gold = POSLib.getLabels(nodes);
			total += gold.length;
			
			for (modId=0; modId<MODEL_SIZE; modId++)
			{
				taggers[modId].tag(nodes);
				local[modId] = countCorrect(nodes, gold);
				correct[modId] += local[modId];
			}
			
			if (local[0] > local[1])
			{
				sim = taggers[0].getCosineSimilarity(nodes);
				if (sim > 0)	list.add(sim);
			}
		}
		
		reader.close();
		
		double accuracy;
		
		for (modId=0; modId<MODEL_SIZE; modId++)
		{
			accuracy = 100d * correct[modId] / total;
			System.out.printf("- accuracy %d: %7.5f (%d/%d)\n", modId, accuracy, correct[modId], total);
		}
	}
	
	/** Called by {@link POSTrain#crossValidatePredict(String, POSReader, POSTagger[], SortedDoubleArrayList)}. */
	private int countCorrect(POSNode[] nodes, String[] gold)
	{
		int i, correct = 0, n = nodes.length;
		
		for (i=0; i<n; i++)
		{
			if (gold[i].equals(nodes[i].pos))
				correct++;
		}
		
		return correct;
	}
		
	static public void main(String[] args)
	{
		new POSTrain(args);
	}
}
