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
	
	public static final int MUTANT_GROUP_SIZE = 15;
	
	public String dsTemplate;
	public String dsMutant;
	
	public ArrayList<RESequence> enzymes = new ArrayList<RESequence>();
	public ArrayList<CutSite> templateCutSites = new ArrayList<CutSite>();
	public ArrayList<CutSite> mutantCutSites = new ArrayList<CutSite>();
	
	public ArrayList<Integer> mutations = new ArrayList<Integer>();
	public ArrayList<Mutation> newMutations = new ArrayList<Mutation>();
	
	public PrimerDesign(String seq1, String seq2)
	{
		this.dsTemplate = seq1;
		this.dsMutant = seq2;
		loadRestrictionEnzymes();
		calcRestrictionSites();
		detectMutations();
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
				//System.out.println(this.templateCutSites.get(this.templateCutSites.size()-1));
			}
			
			sites = enzymes.get(i).getCutSites(this.dsMutant);
			for(int j=0;j<sites.size();j++)
			{
				this.mutantCutSites.add(new CutSite(enzymes.get(i),sites.get(j)));
				//System.out.println(this.mutantCutSites.get(this.mutantCutSites.size()-1));
			}
			
			//does the number of sites for this enzyme change?
			ArrayList<CutSite> tmpl = CutSite.getSites(this.templateCutSites, enzymes.get(i));
			ArrayList<CutSite> mut = CutSite.getSites(this.mutantCutSites, enzymes.get(i));
			if(tmpl.size() != mut.size()) {
				// yes...there is a mutation here...
				for(int j=0;j<tmpl.size();j++)
				{
					boolean contains = false;
					for(int k=0;k<mut.size();k++)
					{
						if(mut.get(k).equals(tmpl.get(j))) contains = true;
					}
					if(/*!mut.contains(tmpl.get(j))*/ !contains){
						//this site has been deleted
						//TODO: add a mutation
						System.out.println("Site Removed: " + tmpl.get(j));
					}
				}
				
				for(int j=0;j<mut.size();j++)
				{
					boolean contains = false;
					for(int k=0;k<tmpl.size();k++)
					{
						if(tmpl.get(k).equals(mut.get(j))) contains = true;
					}
					if(!contains) {
						//this site added
						//TODO: add a mutation
						System.out.println("Site added: " + mut.get(j));
					}
				}
			}
		}
		
	}
	
	public void detectMutations()
	{
		//iterate through both sequences and detect any differences...assume for now that they both start at the same place
		int size = (this.dsTemplate.length()>=this.dsMutant.length()?this.dsMutant.length():this.dsTemplate.length());
		for(int i=0;i<size;i++) {
			//just using a simple comparison so that i can use the extended alphabet to make mutations easier
			if(this.dsTemplate.charAt(i) == this.dsMutant.charAt(i)) this.mutations.add(i);//if(!RESequence.ntEquals(this.dsTemplate.charAt(i), this.dsMutant.charAt(i))) this.mutations.add(i);
		}
		
		//next question: is there a possible mutation within +/- MUTANT_GROUP_SIZE?
		for(int i=0;i<this.mutations.size(); i++) {
			//for(int j=0;j<this.mutant)
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String seq1, seq2;
		
		if(args.length == 0 || args[0].equals(""))
		{
			System.out.println("Please enter the template sequence:");
			seq1 = new String(input.nextLine());
		}
		else
		{
			seq1 = new String(args[0]);
			System.out.println("Template sequence read from CLI.");
		}
		
		if(!(args.length > 1) || args[1].equals(""))
		{
			System.out.println("Please enter the mutated sequence:");
			seq2 = new String(input.nextLine());
		}
		else
		{
			seq2 = new String(args[1]);
			System.out.println("Mutated sequence read from CLI.");
		}
		
		PrimerDesign pdMain = new PrimerDesign(seq1, seq2);
		
	}
}
