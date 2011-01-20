package services;

import com.transoft.cfa.*;

public class LUTPckRegionService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	private String  inOperatorID;
	private String  inPckRegion;
	private String  inWorkType;
	
	// Output Parameters
	private int  outRegnNum;
	private String  outRegnName;
	private int  outAutoAssgn;
	private int  outMaxWorkID;
	private int  outSkpAAllow;
	private int  outSkpSAllow;
	private int  outPickSkips;
	private int  outCpSpkWkID;
	private int  outMaxQtyPck;
	private int  outPrtLabels;
	private int  outPrtChsLbl;
	private int  outSpkSlotDs;
	private int  outPckPrompt;
	private int  outSpkWorkID;
	private int  outAllowSOff;
	private int  outContType;
	private String  outContCpcty;
	private int  outDlvPrvCnt;
	private int  outPassAssgn;
	private int  outDelivery;
	private int  outVerifyQty;
	private String  outSpkWordID;
	private String  outWorkIDFld;
	private int  outWorkIDLen;
	private int  outGoBacks;
	private int  outErrCode;
	private String  outErrMsg;
	
	// Results Parameters
	
	// default constructor
	public LUTPckRegionService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTPckRegion", 5, 27, 0);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
		service.initInParameter(3, "inPckRegion", CHARACTER, 9, 0);
		service.initInParameter(4, "inWorkType", CHARACTER, 1, 0);
	
		service.initOutParameter(0, "outRegnNum", NUMBER, 9, 0);
		service.initOutParameter(1, "outRegnName", CHARACTER, 30, 0);
		service.initOutParameter(2, "outAutoAssgn", NUMBER, 1, 0);
		service.initOutParameter(3, "outMaxWorkID", NUMBER, 2, 0);
		service.initOutParameter(4, "outSkpAAllow", NUMBER, 1, 0);
		service.initOutParameter(5, "outSkpSAllow", NUMBER, 1, 0);
		service.initOutParameter(6, "outPickSkips", NUMBER, 1, 0);
		service.initOutParameter(7, "outCpSpkWkID", NUMBER, 1, 0);
		service.initOutParameter(8, "outMaxQtyPck", NUMBER, 4, 0);
		service.initOutParameter(9, "outPrtLabels", NUMBER, 1, 0);
		service.initOutParameter(10, "outPrtChsLbl", NUMBER, 1, 0);
		service.initOutParameter(11, "outSpkSlotDs", NUMBER, 1, 0);
		service.initOutParameter(12, "outPckPrompt", NUMBER, 1, 0);
		service.initOutParameter(13, "outSpkWorkID", NUMBER, 1, 0);
		service.initOutParameter(14, "outAllowSOff", NUMBER, 1, 0);
		service.initOutParameter(15, "outContType", NUMBER, 1, 0);
		service.initOutParameter(16, "outContCpcty", CHARACTER, 5, 0);
		service.initOutParameter(17, "outDlvPrvCnt", NUMBER, 1, 0);
		service.initOutParameter(18, "outPassAssgn", NUMBER, 1, 0);
		service.initOutParameter(19, "outDelivery", NUMBER, 1, 0);
		service.initOutParameter(20, "outVerifyQty", NUMBER, 1, 0);
		service.initOutParameter(21, "outSpkWordID", CHARACTER, 30, 0);
		service.initOutParameter(22, "outWorkIDFld", CHARACTER, 30, 0);
		service.initOutParameter(23, "outWorkIDLen", NUMBER, 9, 0);
		service.initOutParameter(24, "outGoBacks", NUMBER, 1, 0);
		service.initOutParameter(25, "outErrCode", NUMBER, 2, 0);
		service.initOutParameter(26, "outErrMsg", CHARACTER, 255, 0);
	}
	public void execute() throws Exception {
		service.setInputStr(0, inDateTime);
		service.setInputStr(1, inTermSerial);
		service.setInputStr(2, inOperatorID);
		service.setInputStr(3, inPckRegion);
		service.setInputStr(4, inWorkType);
	
		service.execute();
	
		outRegnNum = service.getOutputInt(0);
		outRegnName = service.getOutputStr(1);
		outAutoAssgn = service.getOutputInt(2);
		outMaxWorkID = service.getOutputInt(3);
		outSkpAAllow = service.getOutputInt(4);
		outSkpSAllow = service.getOutputInt(5);
		outPickSkips = service.getOutputInt(6);
		outCpSpkWkID = service.getOutputInt(7);
		outMaxQtyPck = service.getOutputInt(8);
		outPrtLabels = service.getOutputInt(9);
		outPrtChsLbl = service.getOutputInt(10);
		outSpkSlotDs = service.getOutputInt(11);
		outPckPrompt = service.getOutputInt(12);
		outSpkWorkID = service.getOutputInt(13);
		outAllowSOff = service.getOutputInt(14);
		outContType = service.getOutputInt(15);
		outContCpcty = service.getOutputStr(16);
		outDlvPrvCnt = service.getOutputInt(17);
		outPassAssgn = service.getOutputInt(18);
		outDelivery = service.getOutputInt(19);
		outVerifyQty = service.getOutputInt(20);
		outSpkWordID = service.getOutputStr(21);
		outWorkIDFld = service.getOutputStr(22);
		outWorkIDLen = service.getOutputInt(23);
		outGoBacks = service.getOutputInt(24);
		outErrCode = service.getOutputInt(25);
		outErrMsg = service.getOutputStr(26);
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
	
	public void setinPckRegion(String val) {
		inPckRegion = val;
	}
	
	public void setinWorkType(String val) {
		inWorkType = val;
	}
	
	public int getoutRegnNum(){
		return outRegnNum;
	}
	
	public String getoutRegnName(){
		return outRegnName;
	}
	
	public int getoutAutoAssgn(){
		return outAutoAssgn;
	}
	
	public int getoutMaxWorkID(){
		return outMaxWorkID;
	}
	
	public int getoutSkpAAllow(){
		return outSkpAAllow;
	}
	
	public int getoutSkpSAllow(){
		return outSkpSAllow;
	}
	
	public int getoutPickSkips(){
		return outPickSkips;
	}
	
	public int getoutCpSpkWkID(){
		return outCpSpkWkID;
	}
	
	public int getoutMaxQtyPck(){
		return outMaxQtyPck;
	}
	
	public int getoutPrtLabels(){
		return outPrtLabels;
	}
	
	public int getoutPrtChsLbl(){
		return outPrtChsLbl;
	}
	
	public int getoutSpkSlotDs(){
		return outSpkSlotDs;
	}
	
	public int getoutPckPrompt(){
		return outPckPrompt;
	}
	
	public int getoutSpkWorkID(){
		return outSpkWorkID;
	}
	
	public int getoutAllowSOff(){
		return outAllowSOff;
	}
	
	public int getoutContType(){
		return outContType;
	}
	
	public String getoutContCpcty(){
		return outContCpcty;
	}
	
	public int getoutDlvPrvCnt(){
		return outDlvPrvCnt;
	}
	
	public int getoutPassAssgn(){
		return outPassAssgn;
	}
	
	public int getoutDelivery(){
		return outDelivery;
	}
	
	public int getoutVerifyQty(){
		return outVerifyQty;
	}
	
	public String getoutSpkWordID(){
		return outSpkWordID;
	}
	
	public String getoutWorkIDFld(){
		return outWorkIDFld;
	}
	
	public int getoutWorkIDLen(){
		return outWorkIDLen;
	}
	
	public int getoutGoBacks(){
		return outGoBacks;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
