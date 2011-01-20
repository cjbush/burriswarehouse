package services;

import com.transoft.cfa.*;

public class LUTSignOnService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	private String  inOperatorID;
	private String  inPassword;
	
	// Output Parameters
	private String  outWorkType;
	private String  outWorkSel;
	private int  outNCInvest;
	private String  outPass;
	private String  outPickType;
	private String  outCurrZone;
	private String  outCurrAisle;
	private String  outCurrSlot;
	private String  outSequence;
	private String  outCurrSeq;
	private String  outSavePass;
	private String  outSavePkTp;
	private String  outReturn;
	private String  outGBSStat;
	private String  outPassWIP;
	private String  outHoldQty;
	private String  outAddShrt;
	private String  out1stAssgnmt;
	private String  outOrder;
	private String  outWorkQty;
	private String  outWorkWt;
	private String  outCurQty;
	private String  outShortQty;
	private int  outErrCode;
	private String  outErrMsg;
	
	// Results Parameters
	
	// default constructor
	public LUTSignOnService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTSignOn", 4, 25, 0);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
		service.initInParameter(3, "inPassword", CHARACTER, 18, 0);
	
		service.initOutParameter(0, "outWorkType", CHARACTER, 1, 0);
		service.initOutParameter(1, "outWorkSel", CHARACTER, 1, 0);
		service.initOutParameter(2, "outNCInvest", NUMBER, 1, 0);
		service.initOutParameter(3, "outPass", CHARACTER, 1, 0);
		service.initOutParameter(4, "outPickType", CHARACTER, 1, 0);
		service.initOutParameter(5, "outCurrZone", CHARACTER, 50, 0);
		service.initOutParameter(6, "outCurrAisle", CHARACTER, 50, 0);
		service.initOutParameter(7, "outCurrSlot", CHARACTER, 50, 0);
		service.initOutParameter(8, "outSequence", CHARACTER, 10, 0);
		service.initOutParameter(9, "outCurrSeq", CHARACTER, 10, 0);
		service.initOutParameter(10, "outSavePass", CHARACTER, 1, 0);
		service.initOutParameter(11, "outSavePkTp", CHARACTER, 1, 0);
		service.initOutParameter(12, "outReturn", CHARACTER, 2, 0);
		service.initOutParameter(13, "outGBSStat", CHARACTER, 1, 0);
		service.initOutParameter(14, "outPassWIP", CHARACTER, 1, 0);
		service.initOutParameter(15, "outHoldQty", CHARACTER, 5, 0);
		service.initOutParameter(16, "outAddShrt", CHARACTER, 1, 0);
		service.initOutParameter(17, "out1stAssgnmt", CHARACTER, 1, 0);
		service.initOutParameter(18, "outOrder", CHARACTER, 1, 0);
		service.initOutParameter(19, "outWorkQty", CHARACTER, 5, 0);
		service.initOutParameter(20, "outWorkWt", CHARACTER, 5, 0);
		service.initOutParameter(21, "outCurQty", CHARACTER, 5, 0);
		service.initOutParameter(22, "outShortQty", CHARACTER, 5, 0);
		service.initOutParameter(23, "outErrCode", NUMBER, 2, 0);
		service.initOutParameter(24, "outErrMsg", CHARACTER, 255, 0);
	}
	public void execute() throws Exception {
		service.setInputStr(0, inDateTime);
		service.setInputStr(1, inTermSerial);
		service.setInputStr(2, inOperatorID);
		service.setInputStr(3, inPassword);
	
		service.execute();
	
		outWorkType = service.getOutputStr(0);
		outWorkSel = service.getOutputStr(1);
		outNCInvest = service.getOutputInt(2);
		outPass = service.getOutputStr(3);
		outPickType = service.getOutputStr(4);
		outCurrZone = service.getOutputStr(5);
		outCurrAisle = service.getOutputStr(6);
		outCurrSlot = service.getOutputStr(7);
		outSequence = service.getOutputStr(8);
		outCurrSeq = service.getOutputStr(9);
		outSavePass = service.getOutputStr(10);
		outSavePkTp = service.getOutputStr(11);
		outReturn = service.getOutputStr(12);
		outGBSStat = service.getOutputStr(13);
		outPassWIP = service.getOutputStr(14);
		outHoldQty = service.getOutputStr(15);
		outAddShrt = service.getOutputStr(16);
		out1stAssgnmt = service.getOutputStr(17);
		outOrder = service.getOutputStr(18);
		outWorkQty = service.getOutputStr(19);
		outWorkWt = service.getOutputStr(20);
		outCurQty = service.getOutputStr(21);
		outShortQty = service.getOutputStr(22);
		outErrCode = service.getOutputInt(23);
		outErrMsg = service.getOutputStr(24);
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
	
	public void setinPassword(String val) {
		inPassword = val;
	}
	
	public String getoutWorkType(){
		return outWorkType;
	}
	
	public String getoutWorkSel(){
		return outWorkSel;
	}
	
	public int getoutNCInvest(){
		return outNCInvest;
	}
	
	public String getoutPass(){
		return outPass;
	}
	
	public String getoutPickType(){
		return outPickType;
	}
	
	public String getoutCurrZone(){
		return outCurrZone;
	}
	
	public String getoutCurrAisle(){
		return outCurrAisle;
	}
	
	public String getoutCurrSlot(){
		return outCurrSlot;
	}
	
	public String getoutSequence(){
		return outSequence;
	}
	
	public String getoutCurrSeq(){
		return outCurrSeq;
	}
	
	public String getoutSavePass(){
		return outSavePass;
	}
	
	public String getoutSavePkTp(){
		return outSavePkTp;
	}
	
	public String getoutReturn(){
		return outReturn;
	}
	
	public String getoutGBSStat(){
		return outGBSStat;
	}
	
	public String getoutPassWIP(){
		return outPassWIP;
	}
	
	public String getoutHoldQty(){
		return outHoldQty;
	}
	
	public String getoutAddShrt(){
		return outAddShrt;
	}
	
	public String getout1stAssgnmt(){
		return out1stAssgnmt;
	}
	
	public String getoutOrder(){
		return outOrder;
	}
	
	public String getoutWorkQty(){
		return outWorkQty;
	}
	
	public String getoutWorkWt(){
		return outWorkWt;
	}
	
	public String getoutCurQty(){
		return outCurQty;
	}
	
	public String getoutShortQty(){
		return outShortQty;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
