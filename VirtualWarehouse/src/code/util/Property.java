package code.util;

public class Property {
	private String name;
	private String value;
	private String comment;
	private boolean encryptOnOutput;
	private boolean encryptedProperty;
	
	public Property(String name, String value, String comment, boolean encryptOnOutput, boolean encryptedProperty){
		this.name = name.trim();
		this.value = value.trim();
		this.comment = comment.trim();
		this.encryptOnOutput = encryptOnOutput;
		this.encryptedProperty = encryptedProperty;
		if(encryptedProperty){
			this.value = Encryption.decrypt(value);
			String test = "";
		}		
	}
	
	public String toString(){
		String value = ""+this.value;
		String name = ""+this.name;
		if(encryptOnOutput){
			value = Encryption.encrypt(value);
			name = "@"+name;
		}
		else if(encryptedProperty){
			name = "@"+name;
			value = Encryption.encrypt(value);
		}
		if(comment.equals("")){
			return name+" = "+value;
		}
		return name.trim()+" = "+value.trim()+"\t\t\t#"+comment.trim();
	}
	
	public String toRealString(){
		return name.trim()+" = "+value.trim()+"\t\t\t#"+comment.trim();
	}
	
	public String getValue(){
		return value;
	}
	
	public String getName(){
		return name;
	}
}
