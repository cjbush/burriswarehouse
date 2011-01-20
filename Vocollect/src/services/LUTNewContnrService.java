package services;

import com.transoft.cfa.*;

public class LUTNewContnrService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	private String  inOperatorID;
	private int  inAssignID;
	private int  inWorkID;
	private int  inLogUndo;
	
	// Output Parameters
	private int  outContID;
	private int  outContSeq;
	private int  outErrCode;
	private String  outErrMsg;
	
	// Results Parameters
	
	// default constructor
	public LUTNewContnrService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTNewContnr", 6, 4, 0);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);
		service.initInParameter(4, "inWorkID", NUMBER, 9, 0);
		service.initInParameter(5, "inLogUndo", NUMBER, 1, 0);
	
		service.initOutParameter(0, "outContID", NUMBER, 9, 0);
		service.initOutParameter(1, "outContSeq", NUMBER, 4, 0);
		service.initOutParameter(2, "outErrCode", NUMBER, 2, 0);
		service.initOutParameter(3, "outErrMsg", CHARACTER, 255, 0);
	}
	public void execute() throws Exception {
		service.setInputStr(0, inDateTime);
		service.setInputStr(1, inTermSerial);
		service.setInputStr(2, inOperatorID);
		service.setInputInt(3, inAssignID);
		service.setInputInt(4, inWorkID);
		service.setInputInt(5, inLogUndo);
	
		service.execute();
	
		outContID = service.getOutputInt(0);
		outContSeq = service.getOutputInt(1);
		outErrCode = service.getOutputInt(2);
		outErrMsg = service.getOutputStr(3);
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
	
	public void setinLogUndo(int val) {
		inLogUndo = val;
	}
	
	public int getoutContID(){
		return outContID;
	}
	
	public int getoutContSeq(){
		return outContSeq;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
