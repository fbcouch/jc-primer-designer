package com.PrimerDesign;

import java.util.ArrayList;

public class Mutation {
	public String sequence = "";
	public int seqIndex = 0;
	public RESequence enzyme = new RESequence("","");
	public boolean isAddedRE = false; // true if adding a site, false if removing
	
	public Mutation (int pSite, String pSequence, RESequence pEnzyme)
	{
		this.sequence = pSequence;
		this.seqIndex = pSite;
		this.enzyme = pEnzyme;
		this.isAddedRE = (pEnzyme.getCutSites(pSequence).size() > 0);
	}
}
