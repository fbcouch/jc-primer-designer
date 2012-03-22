package com.PrimerDesign;

public class SequenceUtils {
	public static boolean ntEquals(char nt1, char nt2) {
		if(nt1 == 'U') nt1 = 'T';
		if(nt2 == 'U') nt2 = 'U';
		
		if(nt1 == nt2) return true; //already the same...
		String nta1 = SequenceUtils.ntAllowable(nt1);
		String nta2 = SequenceUtils.ntAllowable(nt2);
		
		//boolean overlap = false;
		for(int x=0;x<nta1.length();x++)
		{
			if(nta2.contains(nta1.substring(x,x+1))) return true;
		}
		
		
		return false;
	}
	
	public static String ntAllowable(char nt)
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
		if(nt == 'U') nt = 'T';
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
	
	/**
	 * IUPAC Compressed Codon Table
	 * A = GCN
	 * R = CGN, MGR
	 * N = AAY
	 * D = GAY
	 * C = UGY
	 * Q = CAR
	 * E = GAR
	 * G = GGN
	 * H = CAY
	 * I = AUH
	 * L = YUR, CUN
	 * K = AAR
	 * M = AUG
	 * F = UUY
	 * P = CCN
	 * S = UCN, AGY
	 * T = ACN
	 * W = UGG
	 * Y = UAY
	 * V = GUN
	 * STOP = UAR, URA
	 */
	public static String translateSequence(String dnaSequence)
	{
		String returnVal = "";
		int position = 0;
		while(position + 3 < dnaSequence.length()) {
			returnVal += translateCodon(dnaSequence.substring(position, position + 3));
			position += 3;
		}
		
		return returnVal;
	}
	
	public static char translateCodon(String codon)
	{
		if(threeNtEquals(codon, "GCN")) return 'A';
		if(threeNtEquals(codon, "CGN") || threeNtEquals(codon, "MGR")) return 'R';
		if(threeNtEquals(codon, "AAY")) return 'N';
		if(threeNtEquals(codon, "GAY")) return 'D';
		if(threeNtEquals(codon, "UGY")) return 'C';
		if(threeNtEquals(codon, "CAR")) return 'Q';
		if(threeNtEquals(codon, "GAR")) return 'E';
		if(threeNtEquals(codon, "GGN")) return 'G';
		if(threeNtEquals(codon, "CAY")) return 'H';
		if(threeNtEquals(codon, "AUH")) return 'I';
		if(threeNtEquals(codon, "YUR") || threeNtEquals(codon, "CUN")) return 'L';
		if(threeNtEquals(codon, "AAR")) return 'K';
		if(threeNtEquals(codon, "AUG")) return 'M';
		if(threeNtEquals(codon, "UUY")) return 'F';
		if(threeNtEquals(codon, "CCN")) return 'P';
		if(threeNtEquals(codon, "UCN") || threeNtEquals(codon, "AGY")) return 'S';
		if(threeNtEquals(codon, "ACN")) return 'T';
		if(threeNtEquals(codon, "UGG")) return 'W';
		if(threeNtEquals(codon, "UAY")) return 'Y';
		if(threeNtEquals(codon, "GUN")) return 'V';
		if(threeNtEquals(codon, "UAR") || threeNtEquals(codon, "URA")) return 'Z'; // using Z as stop
		System.out.println(codon);
		return 'X';
	}
	
	public static boolean threeNtEquals(String nt1, String nt2)
	{
		if (!(nt1.length() >= 3 && nt2.length() >= 3)) return false;
		return (ntEquals(nt1.charAt(0), nt2.charAt(0)) && ntEquals(nt1.charAt(1), nt2.charAt(1)) && ntEquals(nt1.charAt(2), nt2.charAt(2)));
	}

	public static String reverseTranslate(char aminoacid) {
		switch(aminoacid)
		{
		case 'A': return "GCN";
		case 'R': return "CGNMGR";
		case 'N': return "AAY";
		case 'D': return "GAY";
		case 'C': return "UGY";
		case 'Q': return "CAR";
		case 'E': return "GAR";
		case 'G': return "GGN";
		case 'H': return "CAY";
		case 'I': return "AUH";
		case 'L': return "YURCUN";
		case 'K': return "AAR";
		case 'M': return "AUG";
		case 'F': return "UUY";
		case 'P': return "CCN";
		case 'S': return "UCNAGY";
		case 'T': return "ACN";
		case 'W': return "UGG";
		case 'Y': return "UAY";
		case 'V': return "GUN";
		case 'Z': return "UARURA";
		}
		
		return "NNN";
	}
}
