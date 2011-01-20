package services;



import com.transoft.cfa.ServiceProxy;



public class LUTRequestWkService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;

	private String  inWorkIDVal;

	private String  inWorkIDType;

	private int  inWorkIDPart;



	// Output Parameters



	// Results Parameters

	private int  outWorkID;

	private int  outErrCode;

	private String  outErrMsg;



	// default constructor

	public LUTRequestWkService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("LUTRequestWk", 6, 0, 3);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);

		service.initInParameter(3, "inWorkIDVal", CHARACTER, 9, 0);

		service.initInParameter(4, "inWorkIDType", CHARACTER, 1, 0);

		service.initInParameter(5, "inWorkIDPart", NUMBER, 1, 0);



		service.initResultParameter(0, "outWorkID", NUMBER, 9, 0);

		service.initResultParameter(1, "outErrCode", NUMBER, 2, 0);

		service.initResultParameter(2, "outErrMsg", CHARACTER, 255, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);

		service.setInputStr(3, inWorkIDVal);

		service.setInputStr(4, inWorkIDType);

		service.setInputInt(5, inWorkIDPart);



		service.execute();



	}

	public boolean fetch() throws Exception {

		boolean more = service.fetch();

		if (more) {

			outWorkID = service.getResultInt(0);

			outErrCode = service.getResultInt(1);

			outErrMsg = service.getResultStr(2);

		}

		return more;

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



	public void setinWorkIDVal(String val) {

		inWorkIDVal = val;

	}



	public void setinWorkIDType(String val) {

		inWorkIDType = val;

	}



	public void setinWorkIDPart(int val) {

		inWorkIDPart = val;

	}



	// Result row accessor methods



	public int getoutWorkID(){

		return outWorkID;

	}



	public int getoutErrCode(){

		return outErrCode;

	}



	public String getoutErrMsg(){

		return outErrMsg;

	}

}

