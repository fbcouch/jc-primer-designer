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
		return (enzyme.sName.equals("")?"N/A":enzyme.sName) + " @ " + site;
	}
	
	public boolean equals(CutSite pCutSite)
	{
		return this.enzyme.sName.equals(pCutSite.enzyme.sName) && this.site == pCutSite.site;
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

	public static ArrayList<CutSite> getSites(ArrayList<CutSite> pSites, RESequence pEnzyme) {
		ArrayList<CutSite> returnVal = new ArrayList<CutSite>();
		for(int i=0;i<pSites.size();i++)
		{
			if(pSites.get(i).enzyme.sName.equals(pEnzyme.sName)) returnVal.add(pSites.get(i));
		}
		return returnVal;
	}
}