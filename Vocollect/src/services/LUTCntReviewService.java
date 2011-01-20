package services;



import com.transoft.cfa.ServiceProxy;



public class LUTCntReviewService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;

	private int  inAssignID;

	private int  inWorkID;

	private int  inContID;



	// Output Parameters



	// Results Parameters

	private int  outCurrCont;

	private String  outContDesc;

	private String  outItemQty;

	private String  outLocID;

	private String  outPickTime;

	private int  outErrCode;

	private String  outErrMsg;



	// default constructor

	public LUTCntReviewService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("LUTCntReview", 6, 0, 7);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);

		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);

		service.initInParameter(4, "inWorkID", NUMBER, 9, 0);

		service.initInParameter(5, "inContID", NUMBER, 9, 0);



		service.initResultParameter(0, "outCurrCont", NUMBER, 9, 0);

		service.initResultParameter(1, "outContDesc", CHARACTER, 30, 0);

		service.initResultParameter(2, "outItemQty", CHARACTER, 5, 0);

		service.initResultParameter(3, "outLocID", CHARACTER, 8, 0);

		service.initResultParameter(4, "outPickTime", CHARACTER, 17, 0);

		service.initResultParameter(5, "outErrCode", NUMBER, 2, 0);

		service.initResultParameter(6, "outErrMsg", CHARACTER, 255, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);

		service.setInputInt(3, inAssignID);

		service.setInputInt(4, inWorkID);

		service.setInputInt(5, inContID);



		service.execute();



	}

	public boolean fetch() throws Exception {

		boolean more = service.fetch();

		if (more) {

			outCurrCont = service.getResultInt(0);

			outContDesc = service.getResultStr(1);

			outItemQty = service.getResultStr(2);

			outLocID = service.getResultStr(3);

			outPickTime = service.getResultStr(4);

			outErrCode = service.getResultInt(5);

			outErrMsg = service.getResultStr(6);

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



	public void setinContID(int val) {

		inContID = val;

	}



	// Result row accessor methods



	public int getoutCurrCont(){

		return outCurrCont;

	}



	public String getoutContDesc(){

		return outContDesc;

	}



	public String getoutItemQty(){

		return outItemQty;

	}



	public String getoutLocID(){

		return outLocID;

	}



	public String getoutPickTime(){

		return outPickTime;

	}



	public int getoutErrCode(){

		return outErrCode;

	}



	public String getoutErrMsg(){

		return outErrMsg;

	}

}

