package fouxx.D3MobileArmory;

public enum ItemType {
	
	HEAD("head"), 
	TORSO("torso"), 
	FEET("feet"), 
	HANDS("hands"), 
	SHOULDERS("shoulders"), 
	LEGS("legs"), 
	BRACERS("bracers"), 
	MAINHAND("mainHand"), 
	OFFHAND("offHand"), 
	WAIST("waist"), 
	RIGHTFINGER("rightFinger"), 
	LEFTFINGER("leftFinger"), 
	NECK("neck");
	
	String name;
	
	ItemType(String name){
		this.name = name;
	}
	
	//public ItemType 
}
