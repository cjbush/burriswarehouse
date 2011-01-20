package code.util;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class RoomCVStoXMLParser {

	private static String FILE_PATH = "src" + File.separatorChar + "data" + File.separatorChar + "placement" + File.separatorChar + "csv data" + File.separatorChar + "rooms" + File.separatorChar;
	private static int NUM_FIELDS = 6;
	
	
	public static void main(String[] args)
	{
		try 
		{
			convert("RoomCoordinates.csv", "rooms.xml");
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
        String finalString = "<rooms>\n";
        
        inFile.useDelimiter("\n");
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
	        	finalString += "\t<room>\n";
	        	
	        	finalString += makeNode("name", elements[0], 2);
	        	finalString += makeNode("temperature", elements[1], 2);
	        	finalString += makeNode("x1", elements[2], 2);
	        	finalString += makeNode("z1", elements[3], 2);
	        	finalString += makeNode("x2", elements[4], 2);
	        	finalString += makeNode("z2", elements[5], 2);
	        	
	        	finalString += "\t</room>\n";
	        }
	        
        }
        
        finalString += "</rooms>";
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
