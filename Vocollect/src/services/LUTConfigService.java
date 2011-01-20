package services;

import com.transoft.cfa.ServiceProxy;

public class LUTConfigService extends ServiceProxy
{
	// Input Parameters
	private String  inDateTime;
	private String  inTermSerial;
	
	// Output Parameters
	private String  outWhse;
	private String  outCustName;
	private int  outNCInvest;
	private int  outNoCtTrk;
	private String  outAddShrt;
	private String  outRgnZone;
	private String  outCtCount;
	private int  outOrdPicking;
	private int  outUseLUT;
	private int  outErrCode;
	private String  outErrMsg;
	
	// Results Parameters
	
	// default constructor
	public LUTConfigService() {}
	
	/**
	* FOR INTERNAL USE ONLY.
	* This method is called automatically when this service is registered.
	* Do not call this method explicitly in your code.
	*/
	protected void init() throws Exception {
		service.init("LUTConfig", 2, 11, 0);
	
		service.initInParameter(0, "inDateTime", CHARACTER, 17, 0);
		service.initInParameter(1, "inTermSerial", CHARACTER, 12, 0);
	
		service.initOutParameter(0, "outWhse", CHARACTER, 3, 0);
		service.initOutParameter(1, "outCustName", CHARACTER, 40, 0);
		service.initOutParameter(2, "outNCInvest", NUMBER, 1, 0);
		service.initOutParameter(3, "outNoCtTrk", NUMBER, 1, 0);
		service.initOutParameter(4, "outAddShrt", CHARACTER, 1, 0);
		service.initOutParameter(5, "outRgnZone", CHARACTER, 1, 0);
		service.initOutParameter(6, "outCtCount", CHARACTER, 1, 0);
		service.initOutParameter(7, "outOrdPicking", NUMBER, 1, 0);
		service.initOutParameter(8, "outUseLUT", NUMBER, 1, 0);
		service.initOutParameter(9, "outErrCode", NUMBER, 2, 0);
		service.initOutParameter(10, "outErrMsg", CHARACTER, 255, 0);
	}
	public void execute() throws Exception {
		service.setInputStr(0, inDateTime);
		service.setInputStr(1, inTermSerial);
	
		service.execute();
	
		outWhse = service.getOutputStr(0);
		outCustName = service.getOutputStr(1);
		outNCInvest = service.getOutputInt(2);
		outNoCtTrk = service.getOutputInt(3);
		outAddShrt = service.getOutputStr(4);
		outRgnZone = service.getOutputStr(5);
		outCtCount = service.getOutputStr(6);
		outOrdPicking = service.getOutputInt(7);
		outUseLUT = service.getOutputInt(8);
		outErrCode = service.getOutputInt(9);
		outErrMsg = service.getOutputStr(10);
	}
	
	// Property accessor methods
	
	public void setinDateTime(String val) {
		inDateTime = val;
	}
	
	public void setinTermSerial(String val) {
		inTermSerial = val;
	}
	
	public String getoutWhse(){
		return outWhse;
	}
	
	public String getoutCustName(){
		return outCustName;
	}
	
	public int getoutNCInvest(){
		return outNCInvest;
	}
	
	public int getoutNoCtTrk(){
		return outNoCtTrk;
	}
	
	public String getoutAddShrt(){
		return outAddShrt;
	}
	
	public String getoutRgnZone(){
		return outRgnZone;
	}
	
	public String getoutCtCount(){
		return outCtCount;
	}
	
	public int getoutOrdPicking(){
		return outOrdPicking;
	}
	
	public int getoutUseLUT(){
		return outUseLUT;
	}
	
	public int getoutErrCode(){
		return outErrCode;
	}
	
	public String getoutErrMsg(){
		return outErrMsg;
	}
}
