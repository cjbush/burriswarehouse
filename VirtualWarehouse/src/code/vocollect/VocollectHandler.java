package code.vocollect;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

import code.app.VirtualWarehouse;
import code.hud.MessageBox;
import code.model.player.Player;

/** 
 * This is the client-side component of the Vocollect client/server communication socket.
 * It also contains methods to access data and use in the Warehouse application.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class VocollectHandler extends Thread {

	private MessageBox mb;
	private static Status status;
	static Socket sock = null;
	static PrintWriter out;
	private String aisle;
	private String slot;

	//these might mess up if you run more than one pick job without quitting the program...untested
	private int currentPickJob;
	private int numPickJobs;
	private ArrayList<String> pickJobs = new ArrayList<String>(); //for local use
	private static ArrayList<String> pickList = new ArrayList<String>(); //for public use
	
	
	// rebuild workspace
	public enum Status {
		OFF, CONFIGURATION, SIGNON, PICKINGSTARTED, PICKINGDONE,
		PICK1DONE, PICK2DONE, PICK3DONE, PICK4DONE, PICK5DONE, PICK6DONE, PICK7DONE, PICK8DONE, PICK9DONE
		//This limits us to 9 picks per job...better way to do this?
	}

	public VocollectHandler(MessageBox messageBox) {
		this.mb = messageBox;
		setStatus(Status.OFF);
		currentPickJob = 0;
		numPickJobs = 0;
		// must have the ability to ask for the map. This is done in the Vocollect Handler, because
		// that's how I want to trigger it. Potentially...
		KeyBindingManager.getKeyBindingManager().set("askForMap", KeyInput.KEY_Q);
    }

    public void run() {    	
        //String hostName = "192.168.1.5";
    	String hostName = "joseph.cedarville.edu";
    	//String hostName = "localhost";
    	//String hostName = "163.11.210.22";
    	
        int portNum = 42211; // shouldn't this be 9090??

        
		try {
			sock = new Socket(hostName, portNum);
		} catch (Exception e) {
			System.out.println("Problem opening socket");
			e.printStackTrace();
			System.exit(1);
		} 

        //Get input/output streams
        BufferedReader networkIn = null;
        
			try {
				networkIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
			} catch (IOException e) {
				System.out.println("Problem opening network input stream");
				e.printStackTrace();
			}

        while(true) {
        	String line = "";
        	int messageNumber = -1;
        	boolean gotALine = false;
        	try {
				if (networkIn.ready()) {
					line = networkIn.readLine();	
					gotALine = true;
				}
			} catch (IOException e) {
				System.out.println("Problem reading line from network");
				e.printStackTrace();
			}
			
			if (gotALine) {
				//split line into array so we can parse it
				String[] inputItems = line.split(" ");

				//find out what message we're dealing with
				messageNumber = Integer.valueOf(inputItems[0]);

				switch (messageNumber) {
				case 0: //LUTConfiguration
					setStatus(Status.CONFIGURATION);
					
					//piece together the Customer Name (it could have spaces, and we split on spaces)
					String custName = "";
					for (int i=1; i<inputItems.length; i++) { //start at 1 since 0 is the message code
						custName = custName + inputItems[i] + " ";
					}
					
					//set message box
					mb.setHeadingText("Vocollect System");
					mb.setBodyText("Welcome to the " + custName + "picking system! Make sure the Talkman is turned on and then follow Vocollect prompts to sign on.");
					
					break;
				case 1: //LUTSignOn
					setStatus(Status.SIGNON);
					
					//set message box
					mb.setBodyText("Welcome, " + inputItems[1] + "! Follow Vocollect prompts to set picking region. Use region 1 when prompted.");
					break;
				case 2: //LUTRegionPermissionsForWorkType 

					break;
				case 3: //LUTGetCntTp

					break;

				case 4: //LUTPickingRegion
					
					//piece together the Customer Name (it could have spaces, and we split on spaces)
					String pickRgn = "";
					for (int i=1; i<inputItems.length; i++) { //start at 1 since 0 is the message code
						pickRgn = pickRgn + " " + inputItems[i];
					}
					
					mb.setBodyText("Picking region set as" + pickRgn + ". Say \"ready\" to receive work.");
					break;
				case 5: //LUTGetAssignment
					
					break;
				case 6: //LUTGetPicks
					numPickJobs = ((inputItems.length)-1)/2; //minus 1 gets rid of 6, divided by 2 since two items for each pick
					setStatus(Status.PICKINGSTARTED);
					
					//load pick jobs into our array list so we can use them
					for (int i = 1; i<inputItems.length; i++) {
						pickJobs.add(inputItems[i]);
					}
					
					//create list for VirtualWarehouse class
					for (int i = 1; i<inputItems.length; i+=2) {
						for (int j = 0; j<Integer.parseInt(inputItems[i+1]); j++) {
							//strip 00 off of end
							String addString = inputItems[i].substring(0, 5);
							pickList.add(addString);
						}
					}
					
					//set message box...this is also our first pick
					String toSplit = pickJobs.remove(0);
					aisle = toSplit.substring(0, 2); //grab the first and second characters from the string
					slot = toSplit.substring(2, 5);  //grab the third through fifth characters from the string
					int numToPick = Integer.parseInt(pickJobs.remove(0)); //parse to an int since it's a string in form "00002"
					
				
					mb.setBodyText("Use printer 1 when prompted. Picking started. Follow Vocollect prompts to aisle " + aisle + ", slot " + slot + ", and pick " + numToPick + " items.");
					
					
					break;
				case 7: //LUTPrint

					break;
				case 8: //LUTPicked
					//add one to current pick counter since we just completed a job
					currentPickJob++;
					Player.ach.advance();
					//was this the last job?
					if (currentPickJob >= numPickJobs) {
						setStatus(Status.PICKINGDONE);
						mb.setBodyText("Picking complete. Say \"ready\" and follow Vocollect prompts to delivery location.");
						
					} else { //set status based on what job we just finished
						switch (currentPickJob) {
						case 1:
							setStatus(Status.PICK1DONE);
							break;
						case 2:
							setStatus(Status.PICK2DONE);
							break;
						case 3:
							setStatus(Status.PICK3DONE);
							break;
						case 4:
							setStatus(Status.PICK4DONE);
							break;
						case 5:
							setStatus(Status.PICK5DONE);
							break;
						case 6:
							setStatus(Status.PICK6DONE);
							break;
						case 7:
							setStatus(Status.PICK7DONE);
							break;
						case 8:
							setStatus(Status.PICK8DONE);
							break;
						case 9:
							setStatus(Status.PICK9DONE);
							break;
						}
						
						if (pickJobs.size() >= 2) {	//make sure there's still a job left in the list
							toSplit = pickJobs.remove(0);
							aisle = toSplit.substring(0, 2); //grab the first and second characters from the string
							slot = toSplit.substring(2, 5);  //grab the third through fifth characters from the string
							numToPick = Integer.parseInt(pickJobs.remove(0)); //parse to an int since it's a string in form "00002"

							mb.setBodyText("Pick " + currentPickJob + " finished. Follow Vocollect prompts to aisle " + aisle + ", slot " + slot + ", and pick " + numToPick + " items.");
						
							
						
						} else {
							mb.setBodyText("Error in pick reporting. Follow Vocollect prompts.");
						}
					}
					break;
				case 9: //LUTSignOff
					setStatus(Status.OFF);
					break;
				}

			}

        }
             
    }

    private void setStatus(Status status) {
		this.status = status;
	}

	public static Status getStatus() {
		return status;
	}

//	private static void setNextBin(String nextBin2) {
//		nextBin = nextBin2;
//	}
//
//	public static String getNextBin() {
//		return nextBin;
//	}
//
//	private static void setNextPickCount(int nextPickCount2) {
//		nextPickCount = nextPickCount2;
//	}
//
//	public static int getNextPickCount() {
//		return nextPickCount;
//	}
	
	public static ArrayList<String> getPickList() {
		return pickList;
	}
	
	public static void close() {
		out.println("DONE");
	}

	public static void main(String[] args) throws IOException {
        VocollectHandler myC = new VocollectHandler(null);
        myC.start();
    }

//	private void setPickList(ArrayList<String> pickList) {
//		this.pickList = pickList;
//	}

	public String getSlot(){
		return slot;
	}
	public String getAisle(){
		return aisle;
	}


}



