package services;

import com.transoft.cfa.*;

public class LUTGetPicksService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	private String  inOperatorID;
	private int  inAssignID;
	private int  inShortSkip;
	private int  inGoBack;
	private int  inReqOrder;
	
	// Output Parameters
	
	// Results Parameters
	private String  outAssgnSts;
	private int  outBaseItem;
	private String  outSequence;
	private String  outLocID;
	private String  outRegionID;
	private String  outZone;
	private String  outAisle;
	private String  outSlot;
	private String  outPickQty;
	private String  outItemNum;
	private int  outVarWt;
	private double  outVarWtMin;
	private double  outVarWtMax;
	private double  outVarWtTot;
	private String  outQtyPicked;
	private int  outChkDigits;
	private String  outItemDesc;
	private String  outItemSize;
	private String  outItemUPC;
	private int  outWorkID;
	private String  outDlvyLoc;
	private int  outFullCase;
	private String  outDummy;
	private String  outStore;
	private String  outGetCusPID;
	private String  outAccumCs;
	private String  outPIDType;
	private String  outUOM;
	private int  outErrCode;
	private String  outErrMsg;
	
	// default constructor
	public LUTGetPicksService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTGetPicks", 7, 0, 30);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
		service.initInParameter(2, "inOperatorID", CHARACTER, 18, 0);
		service.initInParameter(3, "inAssignID", NUMBER, 9, 0);
		service.initInParameter(4, "inShortSkip", NUMBER, 1, 0);
		service.initInParameter(5, "inGoBack", NUMBER, 1, 0);
		service.initInParameter(6, "inReqOrder", NUMBER, 1, 0);
	
		service.initResultParameter(0, "outAssgnSts", CHARACTER, 1, 0);
		service.initResultParameter(1, "outBaseItem", NUMBER, 1, 0);
		service.initResultParameter(2, "outSequence", CHARACTER, 10, 0);
		service.initResultParameter(3, "outLocID", CHARACTER, 8, 0);
		service.initResultParameter(4, "outRegionID", CHARACTER, 30, 0);
		service.initResultParameter(5, "outZone", CHARACTER, 50, 0);
		service.initResultParameter(6, "outAisle", CHARACTER, 50, 0);
		service.initResultParameter(7, "outSlot", CHARACTER, 50, 0);
		service.initResultParameter(8, "outPickQty", CHARACTER, 5, 0);
		service.initResultParameter(9, "outItemNum", CHARACTER, 22, 0);
		service.initResultParameter(10, "outVarWt", NUMBER, 1, 0);
		service.initResultParameter(11, "outVarWtMin", NUMBER, 6, 2);
		service.initResultParameter(12, "outVarWtMax", NUMBER, 6, 2);
		service.initResultParameter(13, "outVarWtTot", NUMBER, 6, 2);
		service.initResultParameter(14, "outQtyPicked", CHARACTER, 5, 0);
		service.initResultParameter(15, "outChkDigits", NUMBER, 5, 0);
		service.initResultParameter(16, "outItemDesc", CHARACTER, 30, 0);
		service.initResultParameter(17, "outItemSize", CHARACTER, 8, 0);
		service.initResultParameter(18, "outItemUPC", CHARACTER, 18, 0);
		service.initResultParameter(19, "outWorkID", NUMBER, 9, 0);
		service.initResultParameter(20, "outDlvyLoc", CHARACTER, 30, 0);
		service.initResultParameter(21, "outFullCase", NUMBER, 1, 0);
		service.initResultParameter(22, "outDummy", CHARACTER, 16, 0);
		service.initResultParameter(23, "outStore", CHARACTER, 30, 0);
		service.initResultParameter(24, "outGetCusPID", CHARACTER, 1, 0);
		service.initResultParameter(25, "outAccumCs", CHARACTER, 5, 0);
		service.initResultParameter(26, "outPIDType", CHARACTER, 1, 0);
		service.initResultParameter(27, "outUOM", CHARACTER, 2, 0);
		service.initResultParameter(28, "outErrCode", NUMBER, 2, 0);
		service.initResultParameter(29, "outErrMsg", CHARACTER, 255, 0);
	}
	public void execute() throws Exception {
		service.setInputStr(0, inDateTime);
		service.setInputStr(1, inTermSerial);
		service.setInputStr(2, inOperatorID);
		service.setInputInt(3, inAssignID);
		service.setInputInt(4, inShortSkip);
		service.setInputInt(5, inGoBack);
		service.setInputInt(6, inReqOrder);
	
		service.execute();
	
	}
	public boolean fetch() throws Exception {
		boolean more = service.fetch();
		if (more) {
			outAssgnSts = service.getResultStr(0);
			outBaseItem = service.getResultInt(1);
			outSequence = service.getResultStr(2);
			outLocID = service.getResultStr(3);
			outRegionID = service.getResultStr(4);
			outZone = service.getResultStr(5);
			outAisle = service.getResultStr(6);
			outSlot = service.getResultStr(7);
			outPickQty = service.getResultStr(8);
			outItemNum = service.getResultStr(9);
			outVarWt = service.getResultInt(10);
			outVarWtMin = service.getResultDbl(11);
			outVarWtMax = service.getResultDbl(12);
			outVarWtTot = service.getResultDbl(13);
			outQtyPicked = service.getResultStr(14);
			outChkDigits = service.getResultInt(15);
			outItemDesc = service.getResultStr(16);
			outItemSize = service.getResultStr(17);
			outItemUPC = service.getResultStr(18);
			outWorkID = service.getResultInt(19);
			outDlvyLoc = service.getResultStr(20);
			outFullCase = service.getResultInt(21);
			outDummy = service.getResultStr(22);
			outStore = service.getResultStr(23);
			outGetCusPID = service.getResultStr(24);
			outAccumCs = service.getResultStr(25);
			outPIDType = service.getResultStr(26);
			outUOM = service.getResultStr(27);
			outErrCode = service.getResultInt(28);
			outErrMsg = service.getResultStr(29);
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
	
	public void setinShortSkip(int val) {
		inShortSkip = val;
	}
	
	public void setinGoBack(int val) {
		inGoBack = val;
	}
	
	public void setinReqOrder(int val) {
		inReqOrder = val;
	}
	
	// Result row accessor methods
	
	public String getoutAssgnSts(){
		return outAssgnSts;
	}
	
	public int getoutBaseItem(){
		return outBaseItem;
	}
	
	public String getoutSequence(){
		return outSequence;
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
	
	public String getoutPickQty(){
		return outPickQty;
	}
	
	public String getoutItemNum(){
		return outItemNum;
	}
	
	public int getoutVarWt(){
		return outVarWt;
	}
	
	public double getoutVarWtMin(){
		return outVarWtMin;
	}
	
	public double getoutVarWtMax(){
		return outVarWtMax;
	}
	
	public double getoutVarWtTot(){
		return outVarWtTot;
	}
	
	public String getoutQtyPicked(){
		return outQtyPicked;
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
	
	public int getoutWorkID(){
		return outWorkID;
	}
	
	public String getoutDlvyLoc(){
		return outDlvyLoc;
	}
	
	public int getoutFullCase(){
		return outFullCase;
	}
	
	public String getoutDummy(){
		return outDummy;
	}
	
	public String getoutStore(){
		return outStore;
	}
	
	public String getoutGetCusPID(){
		return outGetCusPID;
	}
	
	public String getoutAccumCs(){
		return outAccumCs;
	}
	
	public String getoutPIDType(){
		return outPIDType;
	}
	
	public String getoutUOM(){
		return outUOM;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
