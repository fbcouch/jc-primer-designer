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
	
	@Override
	public boolean equals(Object pCutSite)
	{
		return (pCutSite instanceof CutSite ? this.enzyme.sName.equals(((CutSite)pCutSite).enzyme.sName) && this.site == ((CutSite)pCutSite).site : false);
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