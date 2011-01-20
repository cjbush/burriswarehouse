package services;



import com.transoft.cfa.ServiceProxy;



public class LUTUndoLPickService extends ServiceProxy

{

	// Input Parameters

	private String  inDateTime;

	private String  inTermSerial;

	private String  inOperatorID;

	private int  inAssignID;

	private int  inWorkID;

	private String  inLocID;



	// Output Parameters

	private String  outQtyPicked;

	private String  outContQty;

	private int  outActvCont;

	private int  outTotConts;

	private int  outErrCode;

	private String  outErrMsg;



	// Results Parameters



	// default constructor

	public LUTUndoLPickService() {}



	/**

	* FOR INTERNAL USE ONLY.

	* This method is called automatically when this service is registered.

	* Do not call this method explicitly in your code.

	*/

	protected void init() throws Exception {

		service.init("LUTUndoLPick", 6, 6, 0);



		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);

		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);

		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);

		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);

		service.initInParameter(4, "inWorkID", NUMBER, 9, 0);

		service.initInParameter(5, "inLocID", CHARACTER, 8, 0);



		service.initOutParameter(0, "outQtyPicked", CHARACTER, 5, 0);

		service.initOutParameter(1, "outContQty", CHARACTER, 5, 0);

		service.initOutParameter(2, "outActvCont", NUMBER, 9, 0);

		service.initOutParameter(3, "outTotConts", NUMBER, 9, 0);

		service.initOutParameter(4, "outErrCode", NUMBER, 2, 0);

		service.initOutParameter(5, "outErrMsg", CHARACTER, 255, 0);

	}

	public void execute() throws Exception {

		service.setInputStr(0, inDateTime);

		service.setInputStr(1, inTermSerial);

		service.setInputStr(2, inOperatorID);

		service.setInputInt(3, inAssignID);

		service.setInputInt(4, inWorkID);

		service.setInputStr(5, inLocID);



		service.execute();



		outQtyPicked = service.getOutputStr(0);

		outContQty = service.getOutputStr(1);

		outActvCont = service.getOutputInt(2);

		outTotConts = service.getOutputInt(3);

		outErrCode = service.getOutputInt(4);

		outErrMsg = service.getOutputStr(5);

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



	public String getoutQtyPicked(){

		return outQtyPicked;

	}



	public String getoutContQty(){

		return outContQty;

	}



	public int getoutActvCont(){

		return outActvCont;

	}



	public int getoutTotConts(){

		return outTotConts;

	}



	public int getoutErrCode(){

		return outErrCode;

	}



	public String getoutErrMsg(){

		return outErrMsg;

	}

}

