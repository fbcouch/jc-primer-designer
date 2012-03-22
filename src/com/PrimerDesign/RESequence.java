package com.PrimerDesign;

import java.util.ArrayList;

import org.biojava3.core.sequence.DNASequence;

public class RESequence {
	/*
	 * Special characters in RE alphabet:
	 * B = C or G or T
	 * D = A or G or T 
	 * H = A or C or T 
	 * K = G or T 
	 * M = A or C 
	 * N = A or C or G or T 
	 * R = A or G 
	 * S = C or G 
	 * V = A or C or G 
	 * W = A or T 
	 * Y = C or T 
	 */
	
	public String sName = "";
	public String sSite = "";
	
	public RESequence(String name, String site)
	{
		this.sName = name;
		this.sSite = site.toUpperCase();
	}
	
	/**
	 * getCutSites
	 * @param seq the sequence to be checked
	 * @return Returns the nucleotide number of the cut sites
	 */
	public ArrayList<Integer> getCutSites(String seq)
	{
		String sSeq1 = seq.toUpperCase();
		//String sSeq2 = seq.getReverseComplement().getSequenceAsString().toUpperCase();
		ArrayList<Integer> returnVals = new ArrayList<Integer>();
		//search sSeq1 first
		for(int x=0;x<sSeq1.length();x++){
			int y = 0;
			boolean isEqual = false;
			while(y < this.sSite.length())
			{
				if(x+y >= sSeq1.length()) {
					isEqual = false;
					break;
				}
				isEqual = SequenceUtils.ntEquals(sSeq1.charAt(x+y), this.sSite.charAt(y));
				if(!isEqual) break;
				y++;
			}
			if(isEqual) {
				returnVals.add(x);
			}
		}
		return returnVals;
	}
}
