package com.PrimerDesign;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.biojava3.core.sequence.DNASequence;

import com.CSVUtils.CSVFileReader;


/**
 * This class should do several things:
 * 1. Load the Restriction Enzymes
 * 2. Load the DNA sequences (before & after mutagenesis)
 * 3. Calculate possible RE changes
 * 4. Score and output the RE changes
 */
public class PrimerDesign {
	public DNASequence dsTemplate;
	public DNASequence dsMutant;
	
	public ArrayList<RESequence> enzymes = new ArrayList<RESequence>();
	public ArrayList<CutSite> templateCutSites = new ArrayList<CutSite>();
	public ArrayList<CutSite> mutantCutSites = new ArrayList<CutSite>();
	
	public PrimerDesign(DNASequence seq1, DNASequence seq2)
	{
		this.dsTemplate = seq1;
		this.dsMutant = seq2;
		loadRestrictionEnzymes();
		calcRestrictionSites();
		
	}
	
	public void loadRestrictionEnzymes()
	{
		CSVFileReader reader = new CSVFileReader("restriction enzymes.csv");
		reader.ReadFile();
		ArrayList<String> rawValues = reader.getFileValues();
		StringTokenizer st = null;
		for(int i=0;i<rawValues.size();i++)
		{
			String name = "", sequence = "";
			st = new StringTokenizer(rawValues.get(i), ",");
			
			if(st.hasMoreTokens()) name = st.nextToken();
			if(st.hasMoreTokens()) sequence = st.nextToken();
			if(!(name.equals("") && sequence.equals(""))) this.enzymes.add(new RESequence(name,sequence));
		}
	}
	
	public void calcRestrictionSites()
	{
			
		for(int i=0;i<enzymes.size();i++)
		{
			ArrayList<Integer> sites = enzymes.get(i).getCutSites(this.dsTemplate);
			for(int j=0;j<sites.size();j++)
			{
				this.templateCutSites.add(new CutSite(enzymes.get(i),sites.get(j)));
				System.out.println(this.templateCutSites.get(this.templateCutSites.size()-1));
			}
			
			sites = enzymes.get(i).getCutSites(this.dsMutant);
			for(int j=0;j<sites.size();j++)
			{
				this.mutantCutSites.add(new CutSite(enzymes.get(i),sites.get(j)));
				System.out.println(this.mutantCutSites.get(this.mutantCutSites.size()-1));
			}
		}
		
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		DNASequence seq1, seq2;
		
		if(args.length == 0 || args[0].equals(""))
		{
			System.out.println("Please enter the template sequence:");
			seq1 = new DNASequence(input.nextLine());
		}
		else
		{
			seq1 = new DNASequence(args[0]);
			System.out.println("Template sequence read from CLI.");
		}
		
		if(!(args.length > 1) || args[1].equals(""))
		{
			System.out.println("Please enter the mutated sequence:");
			seq2 = new DNASequence(input.nextLine());
		}
		else
		{
			seq2 = new DNASequence(args[1]);
			System.out.println("Mutated sequence read from CLI.");
		}
		
		PrimerDesign pdMain = new PrimerDesign(seq1, seq2);
		
	}
}
