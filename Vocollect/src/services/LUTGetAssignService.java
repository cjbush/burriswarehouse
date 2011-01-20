package services;

import com.transoft.cfa.*;

public class LUTGetAssignService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	private String  inOperatorID;
	private String  inAssignReq;
	private String  inMaxAssign;
	private int  inContType;
	private String  inWorkIDType;
	
	// Output Parameters
	
	// Results Parameters
	private int  outAssignID;
	private int  outAssignNo;
	private int  outIsChase;
	private int  outWorkID;
	private int  outPosition;
	private String  outTotItems;
	private String  outItNotPckd;
	private String  outBINotPckd;
	private double  outTotCube;
	private double  outGoalTime;
	private String  outRouteID;
	private int  outPBRNoRem;
	private String  outDlvyLoc;
	private int  outContID;
	private int  outActvCont;
	private int  outPosCounter;
	private int  outTotConts;
	private int  outPassAssgn;
	private int  outIndivPick;
	private String  outNewPick;
	private String  outCntTrack;
	private int  outErrCode;
	private String  outErrMsg;
	
	// default constructor
	public LUTGetAssignService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTGetAssign", 7, 0, 23);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
		service.initInParameter(3, "inAssignReq", CHARACTER, 1, 0);
		service.initInParameter(4, "inMaxAssign", CHARACTER, 1, 0);
		service.initInParameter(5, "inContType", NUMBER, 1, 0);
		service.initInParameter(6, "inWorkIDType", CHARACTER, 1, 0);
	
		service.initResultParameter(0, "outAssignID", NUMBER, 9, 0);
		service.initResultParameter(1, "outAssignNo", NUMBER, 5, 0);
		service.initResultParameter(2, "outIsChase", NUMBER, 1, 0);
		service.initResultParameter(3, "outWorkID", NUMBER, 9, 0);
		service.initResultParameter(4, "outPosition", NUMBER, 9, 0);
		service.initResultParameter(5, "outTotItems", CHARACTER, 4, 0);
		service.initResultParameter(6, "outItNotPckd", CHARACTER, 4, 0);
		service.initResultParameter(7, "outBINotPckd", CHARACTER, 4, 0);
		service.initResultParameter(8, "outTotCube", NUMBER, 4, 1);
		service.initResultParameter(9, "outGoalTime", NUMBER, 4, 1);
		service.initResultParameter(10, "outRouteID", CHARACTER, 18, 0);
		service.initResultParameter(11, "outPBRNoRem", NUMBER, 1, 0);
		service.initResultParameter(12, "outDlvyLoc", CHARACTER, 30, 0);
		service.initResultParameter(13, "outContID", NUMBER, 9, 0);
		service.initResultParameter(14, "outActvCont", NUMBER, 9, 0);
		service.initResultParameter(15, "outPosCounter", NUMBER, 9, 0);
		service.initResultParameter(16, "outTotConts", NUMBER, 9, 0);
		service.initResultParameter(17, "outPassAssgn", NUMBER, 1, 0);
		service.initResultParameter(18, "outIndivPick", NUMBER, 1, 0);
		service.initResultParameter(19, "outNewPick", CHARACTER, 1, 0);
		service.initResultParameter(20, "outCntTrack", CHARACTER, 1, 0);
		service.initResultParameter(21, "outErrCode", NUMBER, 2, 0);
		service.initResultParameter(22, "outErrMsg", CHARACTER, 255, 0);
	}
	public void execute() throws Exception {
		service.setInputStr(0, inDateTime);
		service.setInputStr(1, inTermSerial);
		service.setInputStr(2, inOperatorID);
		service.setInputStr(3, inAssignReq);
		service.setInputStr(4, inMaxAssign);
		service.setInputInt(5, inContType);
		service.setInputStr(6, inWorkIDType);
	
		service.execute();
	
	}
	public boolean fetch() throws Exception {
		boolean more = service.fetch();
		if (more) {
			outAssignID = service.getResultInt(0);
			outAssignNo = service.getResultInt(1);
			outIsChase = service.getResultInt(2);
			outWorkID = service.getResultInt(3);
			outPosition = service.getResultInt(4);
			outTotItems = service.getResultStr(5);
			outItNotPckd = service.getResultStr(6);
			outBINotPckd = service.getResultStr(7);
			outTotCube = service.getResultDbl(8);
			outGoalTime = service.getResultDbl(9);
			outRouteID = service.getResultStr(10);
			outPBRNoRem = service.getResultInt(11);
			outDlvyLoc = service.getResultStr(12);
			outContID = service.getResultInt(13);
			outActvCont = service.getResultInt(14);
			outPosCounter = service.getResultInt(15);
			outTotConts = service.getResultInt(16);
			outPassAssgn = service.getResultInt(17);
			outIndivPick = service.getResultInt(18);
			outNewPick = service.getResultStr(19);
			outCntTrack = service.getResultStr(20);
			outErrCode = service.getResultInt(21);
			outErrMsg = service.getResultStr(22);
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
	
	public void setinAssignReq(String val) {
		inAssignReq = val;
	}
	
	public void setinMaxAssign(String val) {
		inMaxAssign = val;
	}
	
	public void setinContType(int val) {
		inContType = val;
	}
	
	public void setinWorkIDType(String val) {
		inWorkIDType = val;
	}
	
	// Result row accessor methods
	
	public int getoutAssignID(){
		return outAssignID;
	}
	
	public int getoutAssignNo(){
		return outAssignNo;
	}
	
	public int getoutIsChase(){
		return outIsChase;
	}
	
	public int getoutWorkID(){
		return outWorkID;
	}
	
	public int getoutPosition(){
		return outPosition;
	}
	
	public String getoutTotItems(){
		return outTotItems;
	}
	
	public String getoutItNotPckd(){
		return outItNotPckd;
	}
	
	public String getoutBINotPckd(){
		return outBINotPckd;
	}
	
	public double getoutTotCube(){
		return outTotCube;
	}
	
	public double getoutGoalTime(){
		return outGoalTime;
	}
	
	public String getoutRouteID(){
		return outRouteID;
	}
	
	public int getoutPBRNoRem(){
		return outPBRNoRem;
	}
	
	public String getoutDlvyLoc(){
		return outDlvyLoc;
	}
	
	public int getoutContID(){
		return outContID;
	}
	
	public int getoutActvCont(){
		return outActvCont;
	}
	
	public int getoutPosCounter(){
		return outPosCounter;
	}
	
	public int getoutTotConts(){
		return outTotConts;
	}
	
	public int getoutPassAssgn(){
		return outPassAssgn;
	}
	
	public int getoutIndivPick(){
		return outIndivPick;
	}
	
	public String getoutNewPick(){
		return outNewPick;
	}
	
	public String getoutCntTrack(){
		return outCntTrack;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
