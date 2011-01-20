package code.util;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class VehicleCVStoXMLParser {

	private static String FILE_PATH = "src" + File.separatorChar + "data" + File.separatorChar + "placement" + File.separatorChar + "csv data" + File.separatorChar + "vehicles" + File.separatorChar;
	private static int NUM_FIELDS = 8;
	
	
	public static void main(String[] args)
	{
		try 
		{
			convert("VehicleCoordinates.csv", "vehicles.xml");
		}
		catch (Exception e) {
			System.out.println("Parsing and Writing Failed");
			e.printStackTrace();
		}
		
	}
	
	
    public static void convert(String inputFile, String outputFile) throws Exception
    {
        System.out.println("Parsing:  "+inputFile);

        String fileInput = inputFile;

        Scanner inFile = new Scanner(new File(FILE_PATH+fileInput));
        String finalString = "<vehicles>\n";
        
        inFile.useDelimiter("\n");
        String[] typeLine = inFile.next().split(",");
        String type = typeLine[0];
        
        inFile.next();						//skip titles
        
        String room;
        String[] elements;
        
        while(inFile.hasNext())
        {
	        room = inFile.next();		//set string to first set  
	        //ridOfR(room);						//get rid of any \r in string
	        
	        elements = room.split(",");
	        
	        if(elements.length == NUM_FIELDS)
	        {
	        	finalString += "\t<" + type + ">\n";
	        	
	        	finalString += makeNode("name", elements[0], 2);
	        	finalString += makeNode("scale", elements[1], 2);
	        	finalString += makeNode("translationX", elements[2], 2);
	        	finalString += makeNode("translationY", elements[3], 2);
	        	finalString += makeNode("translationZ", elements[4], 2);
	        	finalString += makeNode("rotationX", elements[5], 2);
	        	finalString += makeNode("rotationY", elements[6], 2);
	        	finalString += makeNode("rotationZ", elements[7], 2);
	        	
	        	finalString += "\t</" + type + ">\n";
	        }
	        
        }
        
        finalString += "</vehicles>";
        inFile.close(); //close that file
        
        PrintStream outFile = new PrintStream( new File(FILE_PATH + outputFile) );
        outFile.print(finalString);

        System.out.println("Finished parsing "+inputFile);
    }
    
    
    private static String makeNode(String nodeName, String element, int deep)
    {
        String s = "";
        String tab = "";

        element = ridOfR(element);

        for (int i=0;i<deep;i++)
        {
            tab += "\t";
        }

        s += tab+"<"+nodeName+">"+element+"</"+nodeName+">"+"\n";

        return s;
    }
    
    private static String ridOfR(String s)
    {
        if (s.indexOf("\r") > -1)
        {
            s = s.substring(0,s.indexOf("\r"));
        }
        return s;
    }
}
