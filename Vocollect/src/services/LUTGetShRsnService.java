package services;



import com.transoft.cfa.ServiceProxy;



public class LUTGetShRsnService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;



	// Output Parameters



	// Results Parameters

	private int  outShortCode;

	private String  outShortDesc;

	private int  outErrCode;

	private String  outErrMsg;



	// default constructor

	public LUTGetShRsnService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("LUTGetShRsn", 3, 0, 4);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);



		service.initResultParameter(0, "outShortCode", NUMBER, 2, 0);

		service.initResultParameter(1, "outShortDesc", CHARACTER, 40, 0);

		service.initResultParameter(2, "outErrCode", NUMBER, 2, 0);

		service.initResultParameter(3, "outErrMsg", CHARACTER, 255, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);



		service.execute();



	}

	public boolean fetch() throws Exception {

		boolean more = service.fetch();

		if (more) {

			outShortCode = service.getResultInt(0);

			outShortDesc = service.getResultStr(1);

			outErrCode = service.getResultInt(2);

			outErrMsg = service.getResultStr(3);

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



	// Result row accessor methods



	public int getoutShortCode(){

		return outShortCode;

	}



	public String getoutShortDesc(){

		return outShortDesc;

	}



	public int getoutErrCode(){

		return outErrCode;

	}



	public String getoutErrMsg(){

		return outErrMsg;

	}

}

