package com.example.d3ma;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Player implements Serializable{
	String btag;
	String paragonSC;
	String paragonHC;
	
	public Player() {}
	
	public Player(String btag, String paragonSC, String paragonHC){
		super();
		this.btag = btag;
		this.paragonSC = paragonSC;
		this.paragonHC = paragonHC;
	}
	
	@Override
    public String toString() {
        return btag+"\nParagon level: "+paragonSC+" | "+paragonHC;
    }
	
	String getBtag(){
		return btag;
	}
	
	String getParagonSC(){
		return paragonSC;
	}
	
	String getParagonHC(){
		return paragonHC;
	}
	
	void setBtag(String btag){
		this.btag = btag;
	}
	
	void setParagonSC(String paragonSC){
		this.paragonSC = paragonSC;
	}
	
	void setParagonHC(String paragonHC){
		this.paragonHC = paragonHC;
	}
}
