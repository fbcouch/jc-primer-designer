package com.PrimerDesign;

import java.util.ArrayList;

public class CutSite
{
	public RESequence enzyme;
	public int site;
	
	public CutSite(RESequence pEnzyme, int pSite) {
		enzyme = pEnzyme;
		site = pSite;
	}
	
	@Override
	public String toString()
	{
		return (enzyme.sName.equals("")?"N/A":enzyme.sName) + ": " + site;
	}
	
	public static int countSites(ArrayList<CutSite> pSites, RESequence pEnzyme)
	{
		int returnVal = 0;
		for(int i=0;i<pSites.size();i++)
		{
			if(pSites.get(i).enzyme.sName.equals(pEnzyme.sName)) returnVal++;
		}
		
		return returnVal;
	}
}