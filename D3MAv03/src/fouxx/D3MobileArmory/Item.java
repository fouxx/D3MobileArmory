package fouxx.D3MobileArmory;

public class Item {
	String slot;
	String heroID;
	
	String name;
	String icon;
	String color;
	String tooltip;
	String level;
	String accountBound;
	String flavorText;
	String type;
	String armor;
	String DPS;
	String attackSpeed;
	String damage;
	String blockChance;
	String primaryAtr;
	String secondaryAtr;
	String passive;
	
	public Item() { }
	
	public Item(String slot, String heroID,
			String name, 		 String icon, 			String color, 	String tooltip, 	String level,
			String accountBound, String flavorText, 	String type, 	String armor,
			String DPS, 		 String attackSpeed, 	String damage, 	String blockChance,
			String primaryAtr, 	 String secondaryAtr, 	String passive){
		
		this.slot = slot;
		this.heroID = heroID;
		
		this.name = name;
		this.icon = icon;
		this.color = color;
		this.tooltip = tooltip;
		this.level = level;
		this.accountBound = accountBound;
		this.flavorText = flavorText;
		this.type = type;
		this.armor = armor;
		this.DPS = DPS;
		this.attackSpeed = attackSpeed;
		this.damage = damage;
		this.blockChance = blockChance;
		this.primaryAtr = primaryAtr;
		this.secondaryAtr = secondaryAtr;
		this.passive = passive;
	}
	
	public Item(String slot, String heroID){
		this(slot, heroID, 
				"empty", "", "", "", "",
				"", "", "", "",
				"", "", "", "",
				"", "", "");
	}
	
	@Override
	public String toString(){
		return DPS;		
	}
}
