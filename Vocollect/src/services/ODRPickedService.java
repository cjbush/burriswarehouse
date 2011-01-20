package services;



import com.transoft.cfa.ServiceProxy;



public class ODRPickedService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;

	private int  inAssignID;

	private int  inWorkID;

	private String  inLocID;

	private String  inSequence;

	private int  inQtyPicked;

	private int  inContType;

	private int  inContCpcty;

	private int  inPickedSts;

	private int  inFullCsSts;

	private int  inNewCont;



	// Output Parameters



	// Results Parameters



	// default constructor

	public ODRPickedService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("ODRPicked", 13, 0, 0);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);

		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);

		service.initInParameter(4, "inWorkID", NUMBER, 9, 0);

		service.initInParameter(5, "inLocID", CHARACTER, 8, 0);

		service.initInParameter(6, "inSequence", CHARACTER, 10, 0);

		service.initInParameter(7, "inQtyPicked", NUMBER, 5, 0);

		service.initInParameter(8, "inContType", NUMBER, 1, 0);

		service.initInParameter(9, "inContCpcty", NUMBER, 5, 0);

		service.initInParameter(10, "inPickedSts", NUMBER, 1, 0);

		service.initInParameter(11, "inFullCsSts", NUMBER, 1, 0);

		service.initInParameter(12, "inNewCont", NUMBER, 1, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);

		service.setInputInt(3, inAssignID);

		service.setInputInt(4, inWorkID);

		service.setInputStr(5, inLocID);

		service.setInputStr(6, inSequence);

		service.setInputInt(7, inQtyPicked);

		service.setInputInt(8, inContType);

		service.setInputInt(9, inContCpcty);

		service.setInputInt(10, inPickedSts);

		service.setInputInt(11, inFullCsSts);

		service.setInputInt(12, inNewCont);



		service.execute();



	}



	// Property accessor methods



	public void setinDateTime(String val) {

		inDateTime = val;

	}



	public void setinTermSerial(String val) {

		inTermSerial = val;

	}



	public void setinOperatorID(String val) {

		inOperatorID = val;

	}



	public void setinAssignID(int val) {

		inAssignID = val;

	}



	public void setinWorkID(int val) {

		inWorkID = val;

	}



	public void setinLocID(String val) {

		inLocID = val;

	}



	public void setinSequence(String val) {

		inSequence = val;

	}



	public void setinQtyPicked(int val) {

		inQtyPicked = val;

	}



	public void setinContType(int val) {

		inContType = val;

	}



	public void setinContCpcty(int val) {

		inContCpcty = val;

	}



	public void setinPickedSts(int val) {

		inPickedSts = val;

	}



	public void setinFullCsSts(int val) {

		inFullCsSts = val;

	}



	public void setinNewCont(int val) {

		inNewCont = val;

	}

}

