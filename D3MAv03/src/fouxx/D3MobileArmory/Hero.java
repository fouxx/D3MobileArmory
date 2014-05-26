package fouxx.D3MobileArmory;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Hero implements Serializable{
	String ID;
	String name;
	String gender;
	String level;
	String heroClass;
	String mode;
	
	String downloaded;
	String btag;
	
	String damage;
	String toughness;
	String healing;
	String a_str, a_dex, a_int, a_vit;
	String life;
	String resource;
	
	Hero(){}
	
	Hero(String ID, String name, String gender, String level, String heroClass, String mode, String downloaded, String btag){
		this.ID = ID;
		this.name = name;
		this.gender = gender;
		this.level = level;
		this.heroClass = heroClass;
		this.mode = mode;
		this.downloaded = downloaded;
		this.btag = btag;
	}
	
	void setDetails(String damage, String toughness, String healing, String a_str, String a_dex, String a_int, String a_vit){
		this.damage = damage;
		this.toughness = toughness;
		this.healing = healing;
		this.a_str = a_str;
		this.a_dex = a_dex;
		this.a_int = a_int;
		this.a_vit = a_vit;
	}
	
	@Override
	public String toString(){
		return name+" level: "+level;
	}
	
	String getID(){
		return ID;
	}
}
