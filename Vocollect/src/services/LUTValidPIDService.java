package services;

import com.transoft.cfa.*;

public class LUTValidPIDService extends ServiceProxy
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
	private String  inPIDType;
	
	// Output Parameters
	private String  outPIDQty;
	private int  outErrCode;
	private String  outErrMsg;
	
	// Results Parameters
	
	// default constructor
	public LUTValidPIDService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTValidPID", 9, 3, 0);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);
		service.initInParameter(4, "inWorkID", NUMBER, 9, 0);
		service.initInParameter(5, "inLocID", CHARACTER, 8, 0);
		service.initInParameter(6, "inSequence", CHARACTER, 10, 0);
		service.initInParameter(7, "inCustPID", CHARACTER, 20, 0);
		service.initInParameter(8, "inPIDType", CHARACTER, 1, 0);
	
		service.initOutParameter(0, "outPIDQty", CHARACTER, 5, 0);
		service.initOutParameter(1, "outErrCode", NUMBER, 2, 0);
		service.initOutParameter(2, "outErrMsg", CHARACTER, 255, 0);
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
		service.setInputStr(8, inPIDType);
	
		service.execute();
	
		outPIDQty = service.getOutputStr(0);
		outErrCode = service.getOutputInt(1);
		outErrMsg = service.getOutputStr(2);
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
	
	public void setinPIDType(String val) {
		inPIDType = val;
	}
	
	public String getoutPIDQty(){
		return outPIDQty;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
