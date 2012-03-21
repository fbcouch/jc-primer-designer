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
				isEqual = RESequence.ntEquals(sSeq1.charAt(x+y), this.sSite.charAt(y));
				if(!isEqual) break;
				y++;
			}
			if(isEqual) {
				returnVals.add(x);
			}
		}
		return returnVals;
	}

	public static boolean ntEquals(char nt1, char nt2) {
		
		
		if(nt1 == nt2) return true; //already the same...
		String nta1 = RESequence.ntAllowable(nt1);
		String nta2 = RESequence.ntAllowable(nt2);
		
		//boolean overlap = false;
		for(int x=0;x<nta1.length();x++)
		{
			if(nta2.contains(nta1.substring(x,x+1))) return true;
		}
		
		
		return false;
	}
	
	private static String ntAllowable(char nt)
	{
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
		switch(nt) {
		case 'B':
			return "CGT";
		case 'D':
			return "AGT";
		case 'H':
			return "ACT";
		case 'K':
			return "GT";
		case 'M':
			return "AC";
		case 'N':
			return "ACGT";
		case 'R':
			return "AG";
		case 'S':
			return "CG";
		case 'V':
			return "ACG";
		case 'W':
			return "AT";
		case 'Y':
			return "CT";
		}
		return String.valueOf(nt);
	}
}
