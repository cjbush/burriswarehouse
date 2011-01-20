package services;



import com.transoft.cfa.ServiceProxy;



public class LUTReviewPckService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;

	private int  inAssignID;

	private int  inWorkID;



	// Output Parameters



	// Results Parameters

	private String  outItemDesc;

	private String  outQtyPicked;

	private String  outLocID;

	private int  outErrCode;

	private String  outErrMsg;



	// default constructor

	public LUTReviewPckService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("LUTReviewPck", 5, 0, 5);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);

		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);

		service.initInParameter(4, "inWorkID", NUMBER, 9, 0);



		service.initResultParameter(0, "outItemDesc", CHARACTER, 30, 0);

		service.initResultParameter(1, "outQtyPicked", CHARACTER, 5, 0);

		service.initResultParameter(2, "outLocID", CHARACTER, 8, 0);

		service.initResultParameter(3, "outErrCode", NUMBER, 2, 0);

		service.initResultParameter(4, "outErrMsg", CHARACTER, 255, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);

		service.setInputInt(3, inAssignID);

		service.setInputInt(4, inWorkID);



		service.execute();



	}

	public boolean fetch() throws Exception {

		boolean more = service.fetch();

		if (more) {

			outItemDesc = service.getResultStr(0);

			outQtyPicked = service.getResultStr(1);

			outLocID = service.getResultStr(2);

			outErrCode = service.getResultInt(3);

			outErrMsg = service.getResultStr(4);

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



	public void setinAssignID(int val) {

		inAssignID = val;

	}



	public void setinWorkID(int val) {

		inWorkID = val;

	}



	// Result row accessor methods



	public String getoutItemDesc(){

		return outItemDesc;

	}



	public String getoutQtyPicked(){

		return outQtyPicked;

	}



	public String getoutLocID(){

		return outLocID;

	}



	public int getoutErrCode(){

		return outErrCode;

	}



	public String getoutErrMsg(){

		return outErrMsg;

	}

}

