package code.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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
public class CSVAisleModifier {

	static String FILE_PATH = "src" + File.separatorChar + "data" + 
				                      File.separatorChar + "placement" + 
				                      File.separatorChar + "csv data" + 
				                      File.separatorChar + "environment" + 
				                      File.separatorChar; //our path

    /**
     * Parses the given csv file and modifies it to generate aisle names
     * 
     * @param fileName
     * @return String containing the newly modified csv file.
     * @throws Exception
     */
    public static String modify(String fileName) throws Exception {
        System.out.println("Parsing:  " + fileName);

        String fileInput = fileName;

        Scanner inFile = new Scanner(new File(fileName));
        String finalString = "";
        
        inFile.useDelimiter("\n");

        // read file info
        String firstLine = inFile.next();
        String[] names = firstLine.split(",");

        if (names.length >= 1 && !names[0].startsWith("rack")){
            System.out.println("Error:  " + fileName + " is not rack data");
            return "";
        }
        finalString += firstLine + "\n";

        // read format
        String format = inFile.next();
        finalString += format + "\n";

        // read heading
        String heading = inFile.next();
        finalString += heading + "\n";

        Boolean startOfAisle = true;
        String  aisle        = "";
        int     bin1         = 0;
        int     bin2         = 0;
        
        while (inFile.hasNext()) {
            String s = inFile.next();

            if (s.equals("\r") || s.equals("\n") || s.equals("") || 
            	s.substring(0,1).equals(",")) {
            	finalString += s + "\n";
            	startOfAisle = true;
                continue;
            }

            Scanner pattern = new Scanner(s);
            pattern.useDelimiter(",");

            if(startOfAisle) {
            	finalString += s + "\n";

            	// skip over the next eight fields
            	for (int i = 0; i < 8; i++) {
            		pattern.next();
            	}
            	
            	// grab the bin labels
            	String label1 = pattern.next();
            	String label2 = pattern.next();

            	// save the aisle and bin numbering
            	aisle  = label1.substring(0, 2);
            	bin1   = Integer.parseInt(label1.substring(2, 5));
            	bin2   = Integer.parseInt(label2.substring(2, 5));
            	
            	startOfAisle = false;
            } else {
            	// write the next eight fields
            	for (int i = 0; i < 8; i++) {
            		finalString += pattern.next() + ",";
            	}
            	
            	bin1 += 4;
            	bin2 += 4;
            	
            	String num1 = Integer.toString(bin1);
            	String num2 = Integer.toString(bin2);

            	int len1 = num1.length();
            	int len2 = num2.length();

            	// prepend leading zeros to the bin numbers, if necessary
            	if (len1 == 1) {
            		num1 = "00" + num1;
            	} else if (len1 == 2) {
            		num1 = "0" + num1;
            	}

            	if (len2 == 1) {
            		num2 = "00" + num2;
            	} else if (len2 == 2) {
            		num2 = "0" + num2;
            	}

            	finalString += aisle + num1 + "," + aisle + num2 + ",\n";
            }
        }

        inFile.close(); //close that file

        System.out.println("Finished parsing " + fileName);

        return finalString;
    }

    //Main, calls the magic.
    public static void main(String [] args)
    {
    	// set up for reading the file name
		String fileName = "";
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		// get the file name from the user
    	try {
    		// prompt for the filename
    		System.out.print("Which file would you like to modify:  ");
    		
    		// read in user input 
    		fileName = reader.readLine(); 
    		System.out.println("The file name is " + fileName);
    	}
    	catch (Exception e) {
    		System.out.println("Oops");
    		return;
    	}

    	// construct the file's qaulified path name
        String file = FILE_PATH + fileName + ".csv";
        String finalString = "";

        // parse and modify the file's aisle designations
        try {
        	finalString = modify(file);
        }
        catch(Exception e) {
        	System.out.println("Error reading " + fileName + " for modifications");
    		return;
        }

        // write the output file
        try {
        	PrintStream outFile = new PrintStream(
        						  new FileOutputStream(FILE_PATH + 
        											   fileName  + "_new" + ".csv"));
        	outFile.print(finalString);
        	outFile.close();
        }
        catch (Exception e) {
        	System.out.println("Error writing " + fileName + "_new.csv" 
        									    + " for modifications");
    		return;
        }

        // announce the results
        System.out.println("Modifications to " + file + " complete.");
    }	
}
