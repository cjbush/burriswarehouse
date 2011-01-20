package services;

import com.transoft.cfa.*;

public class LUTGetLocInvService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	private String  inOperatorID;
	
	// Output Parameters
	private String  outLocID;
	private String  outRegionID;
	private String  outZone;
	private String  outAisle;
	private String  outSlot;
	private String  outItemNum;
	private int  outChkDigits;
	private String  outItemDesc;
	private String  outItemSize;
	private String  outItemUPC;
	private int  outErrCode;
	private String  outErrMsg;
	
	// Results Parameters
	
	// default constructor
	public LUTGetLocInvService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTGetLocInv", 3, 12, 0);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
	
		service.initOutParameter(0, "outLocID", CHARACTER, 8, 0);
		service.initOutParameter(1, "outRegionID", CHARACTER, 30, 0);
		service.initOutParameter(2, "outZone", CHARACTER, 50, 0);
		service.initOutParameter(3, "outAisle", CHARACTER, 50, 0);
		service.initOutParameter(4, "outSlot", CHARACTER, 50, 0);
		service.initOutParameter(5, "outItemNum", CHARACTER, 22, 0);
		service.initOutParameter(6, "outChkDigits", NUMBER, 5, 0);
		service.initOutParameter(7, "outItemDesc", CHARACTER, 30, 0);
		service.initOutParameter(8, "outItemSize", CHARACTER, 8, 0);
		service.initOutParameter(9, "outItemUPC", CHARACTER, 18, 0);
		service.initOutParameter(10, "outErrCode", NUMBER, 2, 0);
		service.initOutParameter(11, "outErrMsg", CHARACTER, 255, 0);
	}
	public void execute() throws Exception {
		service.setInputStr(0, inDateTime);
		service.setInputStr(1, inTermSerial);
		service.setInputStr(2, inOperatorID);
	
		service.execute();
	
		outLocID = service.getOutputStr(0);
		outRegionID = service.getOutputStr(1);
		outZone = service.getOutputStr(2);
		outAisle = service.getOutputStr(3);
		outSlot = service.getOutputStr(4);
		outItemNum = service.getOutputStr(5);
		outChkDigits = service.getOutputInt(6);
		outItemDesc = service.getOutputStr(7);
		outItemSize = service.getOutputStr(8);
		outItemUPC = service.getOutputStr(9);
		outErrCode = service.getOutputInt(10);
		outErrMsg = service.getOutputStr(11);
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
	
	public String getoutLocID(){
		return outLocID;
	}
	
	public String getoutRegionID(){
		return outRegionID;
	}
	
	public String getoutZone(){
		return outZone;
	}
	
	public String getoutAisle(){
		return outAisle;
	}
	
	public String getoutSlot(){
		return outSlot;
	}
	
	public String getoutItemNum(){
		return outItemNum;
	}
	
	public int getoutChkDigits(){
		return outChkDigits;
	}
	
	public String getoutItemDesc(){
		return outItemDesc;
	}
	
	public String getoutItemSize(){
		return outItemSize;
	}
	
	public String getoutItemUPC(){
		return outItemUPC;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
