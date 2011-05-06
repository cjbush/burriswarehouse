package code.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ConfigurationManager {
	public static HashMap<String,Property> properties = new HashMap<String, Property>();
	
	public static void read(String filename){
		read(filename, true);
	}
	
	public static void read(String filename, boolean writeBack){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String fileWriteback = "";
			String line = null;
			while((line=reader.readLine())!=null){
				if(!line.equals("") && line.charAt(0) != '#'){
					Property p = parseLine(line);
					properties.put(p.getName().trim(), p);
					fileWriteback += p.toString() + "\n";
				}
				else{
					fileWriteback += line + "\n";
				}
			}
			reader.close();
			if(writeBack){
				write(filename, fileWriteback);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	
	public static Property parseLine(String line){
		boolean encryptOnWriteBack = false;
		boolean encryptedProperty = false;
		String name = line.substring(0, line.indexOf('=')).trim();
		String value = line.substring(line.indexOf('=')+1).trim();
		String comment = "";
		if(line.indexOf('#') != -1){
			comment = line.substring(line.indexOf('#')+1);
			value = line.substring(line.indexOf('=')+1, line.indexOf('#')-1);
		}
		if(line.charAt(0) == '!'){
			encryptOnWriteBack = true;
			name = line.substring(1, line.indexOf('='));
		}
		else if(line.charAt(0) == '@'){
			encryptedProperty = true;
			name = line.substring(1, line.indexOf('='));
		}
		value = value.replaceAll("\\t", "");
		name = name.replaceAll("\\t", "");
		Property p = new Property(name, value, comment, encryptOnWriteBack, encryptedProperty);
		return p;
	}
	
	public static void write(String filename, String output) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
		writer.write(output);
		writer.close();
	}
	
	public static String get(String name){
		return properties.get(name).getValue();
	}
	
	public static void main(String[] args){
		read("app.cfg");
	}
}
