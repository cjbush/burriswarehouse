package services;

import com.transoft.cfa.*;

public class LUTGetCntTpService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	private String  inOperatorID;
	
	// Output Parameters
	
	// Results Parameters
	private int  outContCode;
	private String  outContDesc;
	private int  outErrCode;
	private String  outErrMsg;
	
	// default constructor
	public LUTGetCntTpService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTGetCntTp", 3, 0, 4);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
	
		service.initResultParameter(0, "outContCode", NUMBER, 2, 0);
		service.initResultParameter(1, "outContDesc", CHARACTER, 20, 0);
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
			outContCode = service.getResultInt(0);
			outContDesc = service.getResultStr(1);
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
	
	public int getoutContCode(){
		return outContCode;
	}
	
	public String getoutContDesc(){
		return outContDesc;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
