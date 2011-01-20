package code.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import code.world.WarehouseWorld;

/**
 * <b>File:</b> CVStoXMLParser.java<br/>
 * <b>Description:</b> Generates environment.xml. This class reads in all cvs files located
 * in data/placement/cvs data/environment/ and generates an xml representation of the 
 * information contained therein.
 *
 */
public class CSVtoXMLParser {

	static String FILE_PATH = "src" + File.separatorChar + "data" + File.separatorChar + "placement" + File.separatorChar + "csv data" + File.separatorChar + "environment" + File.separatorChar; //our path

	/**
	 * Reads in warehouse environment xml file. Since the warehouse environment information 
	 * is not located in an cvs file, this information is read in separately.
	 * 
	 * @param fileName
	 * @return String containing environment xml information
	 * @throws Exception
	 */
    public static String readOriginalEnvironment(String fileName) throws Exception
    {
        System.out.println("Reading original environment:  "+fileName);

        Scanner inFile = new Scanner(new File(FILE_PATH+fileName+".xml"));
        String finalString = "";

        inFile.useDelimiter("\n");

        while (inFile.hasNext())
        {
            String s = inFile.next();
            
            s = ridOfR(s);

            if (s.equals("</environment>"))
            {
                break;
            }

            finalString += s+"\n";
        }

        System.out.println("Finished reading original environment");
        return finalString;
    }

    public static void unParse(String fileName) throws Exception
    {
        System.out.println("unParsing:  "+fileName);

        String fileInput = fileName;

        Scanner inFile = new Scanner(new File(FILE_PATH+fileInput+".csv"));
        String finalString = "";

        inFile.useDelimiter("\n");
        String objName = inFile.next();
        objName = objName.substring(0,objName.indexOf(","));
        String format = inFile.next();
        format = format.substring(0,format.indexOf(","));

        finalString += objName + ",,,,,\n";
        finalString += format + ",,,,,\n";

        while (inFile.hasNext())
        {
            String s = inFile.next();

            if (s.equals("\r") || s.equals("\n") || s.equals("") || s.substring(0,1).equals(","))
            {
                finalString += "\n";
                continue;
            }

            Scanner pattern = new Scanner(s);
            pattern.useDelimiter(",");

            String x = pattern.next();
            String z = Double.toString(-Double.parseDouble(pattern.next()));
            String y = pattern.next();

            String rx = pattern.next();
            String ry = pattern.next();
            String rz = pattern.next();

            if (!(Double.parseDouble(x) > 0 && Double.parseDouble(x) < 11.48 && Math.abs(Double.parseDouble(z)) > 27.86 && Math.abs(Double.parseDouble(z)) < 40.93))
            {
                finalString += x+","+Double.toString(Math.abs(Double.parseDouble(z)))+","+y+","+rx+","+ry+","+rz+"\n";
            }
            else
            {
                finalString += "\n";
                System.out.println("Got a bad value");
            }
        }

        inFile.close(); //close that file

        PrintStream outFile = new PrintStream(new FileOutputStream(FILE_PATH+fileName+"Input.csv"));
        outFile.print(finalString);

        System.out.println("Finished unParsing "+fileName);

        outFile.close();
    }

    /**
     * Parses the given cvs file and generates an xml string based on the contents of the
     * file.
     * 
     * @param fileName
     * @return String containing xml representing the contents of the cvs file.
     * @throws Exception
     */
    public static String parse(String fileName) throws Exception
    {
        System.out.println("Parsing:  "+fileName);

        String fileInput = fileName;

        Scanner inFile = new Scanner(new File(FILE_PATH+fileInput+".csv"));
        String finalString = "";
        
        inFile.useDelimiter("\n");
        String firstLine = inFile.next();
        String[] names = firstLine.split(",");
        String objName = "";
        String folderName = "";
        
        if(names.length > 1)
        {
        	objName = names[0];
        	folderName = names[1];
        }
        else
        {
        	System.out.println("error");
        }
        
        /*String objName = inFile.next();
        
        if (objName.indexOf(",") > -1)
        {
            objName = objName.substring(0,objName.indexOf(","));
        }
        
        String folderName = inFile.next();
        
        if (folderName.indexOf(",") > -1)
        {
        	folderName = folderName.substring(0,folderName.indexOf(","));
        }*/
        
        String format = inFile.next();
        
        if (format.indexOf(",") > -1)
        {
            format = format.substring(0,format.indexOf(","));
        }

        inFile.next(); //skip the headings
        
        while (inFile.hasNext())
        {
            String s = inFile.next();

            if (s.equals("\r") || s.equals("\n") || s.equals("") || s.substring(0,1).equals(","))
            {
                continue;
            }
            int level = 2;

            Scanner pattern = new Scanner(s);
            pattern.useDelimiter(",");

            String tagName = "object";
            //assumes all racks and only racks have the prefix "rack" in their name field
            if (objName.startsWith("rack"))
            {
            	tagName = "rack";
            }
            //assumes all pallets and only pallets have the prefix "pallet" in their name field
            else if (objName.startsWith("pallet"))
            {
            	tagName = "pallet";
            }
            //assumes all stacked pallets and only stacked pallets have the prefix "stackedPallet" in their name field
            else if (objName.startsWith("stackedPallets"))
            {
            	tagName = "stackedPallets";
            }
            
            finalString += "\t<" + tagName + ">\n";

            finalString += makeNode("name",objName,level);
            finalString += makeNode("fileName",objName+"."+format,level);
            finalString += makeNode("folderName",folderName,level);
            finalString += makeNode("format",format,level);

            finalString += makeNode("translationX",pattern.next(),level);
            finalString += makeNode("translationZ",Double.toString(-Double.parseDouble(pattern.next())),level);
            finalString += makeNode("translationY",pattern.next(),level);

            finalString += makeNode("scale","1",level);
            finalString += makeNode("rotationX",pattern.next(),level);
            finalString += makeNode("rotationY",pattern.next(),level);
            finalString += makeNode("rotationZ",pattern.next(),level);
            finalString += makeNode("rotationW","0",level);

            //special case for racks
            if (objName.startsWith("rack"))
            {
            	 finalString += makeNode("aisle",pattern.next(),level);
            	 finalString += makeNode("label",pattern.next(),level);

            	 finalString += makeNode("binNumber1",pattern.next(),level);
            	 finalString += makeNode("binNumber2",pattern.next(),level);
            	 //while (pattern.hasNext())
            	 //{
            	 //	 finalString += makeNode("binNumber",pattern.next(),level);
            	 //}
            }
            
            //special case for pallet stacks - the height of the stack
            if (objName.startsWith("stackedPallets"))
            {
            	finalString += makeNode("stackHeight",pattern.next(),level);
            }
            
            finalString += "\t</" + tagName + ">\n";
        }

        inFile.close(); //close that file

        System.out.println("Finished parsing "+fileName);

        return finalString;
    }

    
    /**
     * Gets rid of any trailing \r characters in the given string.
     */
    private static String ridOfR(String s)
    {
        if (s.indexOf("\r") > -1)
        {
            s = s.substring(0,s.indexOf("\r"));
        }
        return s;
    }

    
    /**
     * Creates an xml node.
     * 
     * @param nodeName; the type of the node
     * @param element; the information contained the the node
     * @param deep; the number of tabs to insert before the node
     * @return the String xml node
     */
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

    //Main, calls the magic.
    public static void main(String [] args)
    {
        try
        {
            String fileName = "environment";

            String outString = readOriginalEnvironment("wareEnvironment");

            //String outString = "<dots>\n";

            File actual = new File(FILE_PATH+".");
            for( File f : actual.listFiles())
            {
                String s = f.getName();
                if (s.substring(s.length()-3,s.length()).equals("csv"))
                {
                    outString += parse(s.substring(0,s.length()-4));
                }
            }

            /*
            unParse("Data1");
            unParse("Data2");
            unParse("Data3");
            unParse("Data4");
            unParse("Data5");
            unParse("Data6");
            unParse("Data7");
            unParse("Data8");
            unParse("Data9");
            */

            
            outString += "</environment>";

            PrintStream outFile = new PrintStream(new FileOutputStream(FILE_PATH+fileName+".xml"));
            outFile.print(outString);
            outFile.close();
            
            System.out.println("Parsing complete - check " + FILE_PATH+fileName+".xml" + " for output.");
            //System.out.println("To use this XML file in the application, place it at " + WarehouseWorld.ENV_PLACEMENT_FILE);
        }
        catch (Exception e)
        {
            System.out.println("Oops, we got an error:  "+e.toString());
            e.printStackTrace();
        }
    }
	
}
