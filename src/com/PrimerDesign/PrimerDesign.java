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
			//System.out.println(this.dsTemplate.charAt(i) + " =? " + this.dsMutant.charAt(i));
			if(this.dsTemplate.charAt(i) != this.dsMutant.charAt(i)) this.mutations.add(i);//if(!RESequence.ntEquals(this.dsTemplate.charAt(i), this.dsMutant.charAt(i))) this.mutations.add(i);
		}
		
		//next question: is there a possible mutation within +/- MUTANT_GROUP_SIZE?
		ArrayList<CutSite> possibleSites = null;
		for(int i=0;i<this.mutations.size(); i++) {
			// TODO fix this to add the mutations to the master list
			System.out.println("Mutation @ " + this.mutations.get(i));
			possibleSites = findRemovableRESites(this.dsMutant, this.mutations.get(i), MUTANT_GROUP_SIZE, this.mutantCutSites);
			//now, these mutations must be silent...so lets implement a method to do just that :)
			ArrayList<String> degenerateSequences = null;
			for(int j=0;j<possibleSites.size();j++)
			{
				int start = possibleSites.get(i).site;
				start -= start % 3;
				int end = start + possibleSites.get(i).enzyme.sSite.length();
				if(end % 3 != 0) end += (3 - (end % 3));
				degenerateSequences = getDegenerateSequence(this.dsMutant.substring(start, end));
				// TODO check the degenerate sequences to make mutations of only the ones that are actually mutations and get rid of the restriction site...
			}
		}
		
		
	}
	
	private ArrayList<String> getDegenerateSequence(String sequence) {
		//for each codon, reverse translate
		int position = 0;
		ArrayList<ArrayList<String>> possibilities = new ArrayList<ArrayList<String>>();
		while(position + 3 <= sequence.length())
		{
			String ambigCodon = SequenceUtils.reverseTranslate(SequenceUtils.translateCodon(sequence.substring(position, position + 3)));
			
			possibilities.add(getDegenerateCodons(ambigCodon));
			
			position += 3;
		}
		
		return getAllSequences(possibilities, 0);
	}
	
	private ArrayList<String> getAllSequences(ArrayList<ArrayList<String>> codonTable, int index)
	{
		if(index == codonTable.size() - 1) return codonTable.get(index);
		ArrayList<String> returnVal = new ArrayList<String>();
		ArrayList<String> postStrings = new ArrayList<String>();
		for(int x=index+1;x<codonTable.size();x++) {
			postStrings = getAllSequences(codonTable, x);
		}
		
		for(int x=0;x<codonTable.get(index).size();x++)
		{
			for(int y=0;y<postStrings.size();y++) {
				returnVal.add(codonTable.get(index).get(x) + postStrings.get(y));
			}
		}
		
		return returnVal;
	}

	private ArrayList<String> getDegenerateCodons(String ambigCodon) {
		ArrayList<String> returnVal = new ArrayList<String>();
		int pos = 0;
		String allow1 = "";
		String allow2 = "";
		String allow3 = "";
		while(pos + 3 <= ambigCodon.length())
		{
			allow1 += SequenceUtils.ntAllowable(ambigCodon.charAt(0));
			allow2 += SequenceUtils.ntAllowable(ambigCodon.charAt(1));
			allow3 += SequenceUtils.ntAllowable(ambigCodon.charAt(2));
			pos +=3;
		}
		
		for(int x=0;x<allow1.length();x++)
		{
			for(int y=0;y<allow2.length();y++)
			{
				for(int z=0;z<allow3.length();z++)
				{
					String testCodon = allow1.substring(x,x+1) + allow2.substring(y,y+1) + allow3.substring(z,z+1);
					if(SequenceUtils.translateCodon(testCodon) == SequenceUtils.translateCodon(ambigCodon)) returnVal.add(testCodon);
				}
			}
		}
		return returnVal;
	}

	public ArrayList<CutSite> findRemovableRESites(String pMutantSequence, int pMutationSite, int pRange, ArrayList<CutSite> pCutSites) {
		ArrayList<CutSite> returnVal = (ArrayList<CutSite>) pCutSites.clone();
		CutSite site = null;
		for(int i=0;i<pCutSites.size();i++) {
			site = pCutSites.get(i);
			if(site.site < pMutationSite - pRange || site.site > pMutationSite + pRange) returnVal.remove(site);
		}
		System.out.println(returnVal.toString());
		return returnVal;
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
		
		System.out.println("Template:");
		
		
		PrimerDesign pdMain = new PrimerDesign(seq1, seq2);
		
	}
}
