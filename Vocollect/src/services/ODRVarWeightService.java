package services;



import com.transoft.cfa.ServiceProxy;



public class ODRVarWeightService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;

	private int  inAssignID;

	private int  inWorkID;

	private String  inLocID;

	private String  inSequence;

	private String  inCustPID;

	private double  inVarWeight;

	private int  inTransSts;



	// Output Parameters



	// Results Parameters



	// default constructor

	public ODRVarWeightService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("ODRVarWeight", 10, 0, 0);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);

		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);

		service.initInParameter(4, "inWorkID", NUMBER, 9, 0);

		service.initInParameter(5, "inLocID", CHARACTER, 8, 0);

		service.initInParameter(6, "inSequence", CHARACTER, 10, 0);

		service.initInParameter(7, "inCustPID", CHARACTER, 20, 0);

		service.initInParameter(8, "inVarWeight", NUMBER, 6, 2);

		service.initInParameter(9, "inTransSts", NUMBER, 1, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);

		service.setInputInt(3, inAssignID);

		service.setInputInt(4, inWorkID);

		service.setInputStr(5, inLocID);

		service.setInputStr(6, inSequence);

		service.setInputStr(7, inCustPID);

		service.setInputDbl(8, inVarWeight);

		service.setInputInt(9, inTransSts);



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



	public void setinCustPID(String val) {

		inCustPID = val;

	}



	public void setinVarWeight(double val) {

		inVarWeight = val;

	}



	public void setinTransSts(int val) {

		inTransSts = val;

	}

}

