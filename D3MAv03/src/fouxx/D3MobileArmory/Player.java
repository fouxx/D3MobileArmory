package fouxx.D3MobileArmory;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Player implements Serializable{
	final String btag;
	String paragonSC;
	String paragonHC;
	
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
}
