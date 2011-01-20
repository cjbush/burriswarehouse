package services;



import com.transoft.cfa.ServiceProxy;



public class ODRUpdateStsService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;

	private int  inAssignID;

	private String  inLocID;

	private String  inSequence;

	private int  inUpdateID;

	private String  inUpdateTo;



	// Output Parameters



	// Results Parameters



	// default constructor

	public ODRUpdateStsService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("ODRUpdateSts", 8, 0, 0);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);

		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);

		service.initInParameter(4, "inLocID", CHARACTER, 8, 0);

		service.initInParameter(5, "inSequence", CHARACTER, 10, 0);

		service.initInParameter(6, "inUpdateID", NUMBER, 1, 0);

		service.initInParameter(7, "inUpdateTo", CHARACTER, 1, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);

		service.setInputInt(3, inAssignID);

		service.setInputStr(4, inLocID);

		service.setInputStr(5, inSequence);

		service.setInputInt(6, inUpdateID);

		service.setInputStr(7, inUpdateTo);



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



	public void setinLocID(String val) {

		inLocID = val;

	}



	public void setinSequence(String val) {

		inSequence = val;

	}



	public void setinUpdateID(int val) {

		inUpdateID = val;

	}



	public void setinUpdateTo(String val) {

		inUpdateTo = val;

	}

}

