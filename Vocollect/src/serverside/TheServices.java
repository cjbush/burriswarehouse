package serverside;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

import services.BurrisVocApplication;
import services.LUTCallHelpService;
import services.LUTCkOperStsService;
import services.LUTCntReviewService;
import services.LUTConfigService;
import services.LUTGetAssignService;
import services.LUTGetCntTpService;
import services.LUTGetLocInvService;
import services.LUTGetPIDQtyService;
import services.LUTGetPicksService;
import services.LUTGetShRsnService;
import services.LUTInvestgtdService;
import services.LUTNewContService;
import services.LUTNewContnrService;
import services.LUTPassAssgnService;
import services.LUTPckRegionService;
import services.LUTPickedService;
import services.LUTPrintService;
import services.LUTRequestWkService;
import services.LUTReviewPckService;
import services.LUTRgWkPermService;
import services.LUTSignOffService;
import services.LUTSignOnService;
import services.LUTStopAssgnService;
import services.LUTUndoLPickService;
import services.LUTUpdateStsService;
import services.LUTValid128Service;
import services.LUTValidPIDService;
import services.LUTVarWeightService;
import services.ODRPickedService;
import services.ODRUpdateStsService;
import services.ODRVarWeightService;

import com.burris.tca.services.BurrisJavaApplication;
import com.burris.tca.services.ElsPerformanceServiceService;
import com.transoft.cfa.ServiceProxy;

//added for Cedarville database connection
import java.sql.*;
import database.*;
import java.util.Random;

/**
 * <p>Title: TheServices</p>
 * <p>Description: This class provides the thread with a call method for each Talkman task.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Burris Logistics</p>
 * <p>Modifications: 1-4-05: put try-catch around serv.setinWorkID in LutPrint
 * <p>	2/10 - Matt Kent: heavy changes to refer to Cedarville's database rather than Burris'
 * @author Josh Krupka
 * @version 1.0
 */

public class TheServices {

   public static final int THREAD_SLEEP_LENGTH = 2000;
   public static final String LF = String.valueOf( (char) 10);
   public static final String CR = String.valueOf( (char) 13);
   public static final String DELIMITER = String.valueOf((char)128);
   public static final int LUT_CONFIGURATION = 200;
   public static final int LUT_SIGN_ON = 201;
   public static final int LUT_REGION_PERMISSIONS_FOR_WORK_TYPE = 202;
   public static final int LUT_PICKING_REGION = 203;
   public static final int LUT_REQUEST_WORK = 204;
   public static final int LUT_GET_ASSIGNMENT = 205;
   public static final int LUT_GET_PICKS = 206;
   public static final int ODR_PICKED = 207;
   public static final int LUT_PICKED = 208;
   public static final int ODR_VARIABLE_WEIGHT = 209;
   public static final int ODR_UPDATE_STATUS = 210;
   public static final int LUT_UPDATE_STATUS = 211;
   public static final int LUT_CHECK_OPERATOR_STATUS = 212;
   public static final int LUT_PASS_ASSIGNMENT = 213;
   public static final int LUT_UNDO_LAST_PICK = 214;
   public static final int LUT_REVIEW_CASE_PICKS = 215;
   public static final int LUT_NEW_CONTAINER = 216;
   public static final int LUT_GET_LOCATION_TO_INVESTIGATE = 217;
   public static final int LUT_INVESTIGATED = 218;
   public static final int LUT_PRINT = 219;
   public static final int LUT_CONTAINER_REVIEW = 220;
   public static final int LUT_STOP_ASSIGNMENT = 221;
   public static final int LUT_SIGN_OFF = 222;
   public static final int LUT_VALIDATE_PID = 223;
   public static final int LUT_GET_SHORT_REASONS = 224;
   public static final int LUT_GET_PID_QTY = 225;
   public static final int LUT_CALL_HELP = 226;
   public static final int ODR_DEBUG_INFO = 227;
   public static final int LUT_VALID_128 = 228;
   public static final int LUT_VARIABLE_WEIGHT = 229;
   public static final int LUT_HOW_AM_I_DOING = 230;
   public static final int LUT_GET_CONTAINER_TYPES = 231;

   //number of output fields, used in case LUT fails and an error string needs to be generated
   public static final int NUM_OUT_LUT_CONFIGURATION = 10;
   public static final int NUM_OUT_LUT_SIGN_ON = 24;
   public static final int NUM_OUT_LUT_REGION_PERMISSIONS_FOR_WORK_TYPE = 4;
   public static final int NUM_OUT_LUT_PICKING_REGION = 27;
   public static final int NUM_OUT_LUT_REQUEST_WORK = 3;
   public static final int NUM_OUT_LUT_GET_ASSIGNMENT = 22;
   public static final int NUM_OUT_LUT_GET_PICKS = 29;
   public static final int NUM_OUT_LUT_SHORT_REASONS = 4;
   public static final int NUM_OUT_LUT_VALIDATE_PID = 3;
   public static final int NUM_OUT_LUT_GET_PID_QUANTITY = 2;
   public static final int NUM_OUT_LUT_PICKED = 2;
   public static final int NUM_OUT_LUT_VALID_128 = 3;
   public static final int NUM_OUT_LUT_VARIABLE_WEIGHT = 2;
   public static final int NUM_OUT_LUT_UPDATE_STATUS = 2;
   public static final int NUM_OUT_LUT_CHECK_OPERATOR_STATUS = 2;
   public static final int NUM_OUT_LUT_PASS_ASSIGNMENT = 2;
   public static final int NUM_OUT_LUT_UNDO_LAST_PICK = 6;
   public static final int NUM_OUT_LUT_REVIEW_CASE_PICKS = 5;
   public static final int NUM_OUT_LUT_NEW_CONTAINER = 2;
   public static final int NUM_OUT_LUT_GET_LOCATION_TO_INVESTIGATE = 12;
   public static final int NUM_OUT_LUT_INVESTIGATED = 2;
   public static final int NUM_OUT_LUT_PRINT = 2;
   public static final int NUM_OUT_LUT_CONTAINER_REVIEW = 7;
   public static final int NUM_OUT_LUT_STOP_ASSIGNMENT = 2;
   public static final int NUM_OUT_LUT_SIGN_OFF = 2;
   public static final int NUM_OUT_LUT_CALL_HELP = 2;
   public static final int NUM_OUT_LUT_HOW_AM_I_DOING = 5;
   public static final int NUM_OUT_LUT_GET_CON_TYPES = 4;
   
   public static final int CRITICAL_ERROR_NUM = 98;
   public static final String CRITICAL_ERROR_MSG = "Transaction with host failed to complete.  If encountered again then contact system administrator.";
   public static final String CRITICAL_SEQ = CRITICAL_ERROR_NUM + "," + CRITICAL_ERROR_MSG;

   public static final int TIMEOUT_ERROR_NUM = 96;
   public static final String TIMEOUT_ERROR_MSG = "The transaction has timed out.";

   public static final int HOST_STOPPED_TRANSACTION_ERROR_NUM = 97;

   private static Logger logger = Logger.getLogger(TheServices.class);
   public HashMap<String, Integer> procNameToNumHash;
   protected boolean doubleTerminate;
   private BurrisVocApplication tca;
   private int numOut;
   
   //added for Cedarville database/Vocollect connection
   private DatabaseHandler db;
   private CedarvilleConnect cc;

   /**
    * Creates a instance of this class which will use voc as the tca instance to
    * request instances of services.
    * @param voc BurrisVocApplication
    */
   public TheServices(BurrisVocApplication voc, CedarvilleConnect cc) {
      tca = voc;
      procNameToNumHash = new HashMap<String, Integer>();
      loadNameToNumHash();
      doubleTerminate = true;
      
      //added for Cedarville database/Vocollect connection
      db = new DatabaseHandler("joseph.cedarville.edu","talkman","warehouse","vwburr15");
      //db = new DatabaseHandler("192.168.1.5","talkman","warehouse","vwburr15");
      this.cc = cc;
   }
   /**
    * @return int
    */
   public int getNumOut() {
      return numOut;
   }
   /**
    * @param num int
    */
   public void setNumOut(int num) {
      numOut = num;
   }
   /**
    * Loads a hash table with the Talkman task names as keys and a predefined number
    * as the corresponding value.
    */
   private void loadNameToNumHash() {
      procNameToNumHash.put("prTaskLUTConfiguration", new Integer(LUT_CONFIGURATION));
      procNameToNumHash.put("prTaskLUTSignOn", new Integer(LUT_SIGN_ON));
      procNameToNumHash.put("prTaskLUTRegionPermissionsForWorkType", new Integer(LUT_REGION_PERMISSIONS_FOR_WORK_TYPE));
      procNameToNumHash.put("prTaskLUTPickingRegion", new Integer(LUT_PICKING_REGION));
      procNameToNumHash.put("prTaskLUTRequestWork", new Integer(LUT_REQUEST_WORK));
      procNameToNumHash.put("prTaskLUTGetAssignment", new Integer(LUT_GET_ASSIGNMENT));
      procNameToNumHash.put("prTaskLUTGetPicks", new Integer(LUT_GET_PICKS));
      procNameToNumHash.put("prTaskODRPicked", new Integer(ODR_PICKED));
      procNameToNumHash.put("prTaskLUTPicked", new Integer(LUT_PICKED));
      procNameToNumHash.put("prTaskODRVariableWeight", new Integer(ODR_VARIABLE_WEIGHT));
      procNameToNumHash.put("prTaskLUTVariableWeight", new Integer(LUT_VARIABLE_WEIGHT));
      procNameToNumHash.put("prTaskODRUpdateStatus", new Integer(ODR_UPDATE_STATUS));
      procNameToNumHash.put("prTaskLUTUpdateStatus", new Integer(LUT_UPDATE_STATUS));
      procNameToNumHash.put("prTaskLUTCheckOperatorStatus", new Integer (LUT_CHECK_OPERATOR_STATUS));
      procNameToNumHash.put("prTaskLUTPassAssignment", new Integer(LUT_PASS_ASSIGNMENT));
      procNameToNumHash.put("prTaskLUTUndoLastPick", new Integer(LUT_UNDO_LAST_PICK));
      procNameToNumHash.put("prTaskLUTReviewCasePicks", new Integer(LUT_REVIEW_CASE_PICKS));
      procNameToNumHash.put("prTaskLUTNewContainer", new Integer(LUT_NEW_CONTAINER));
      procNameToNumHash.put("prTaskLUTGetLocationToInvestigate", new Integer(LUT_GET_LOCATION_TO_INVESTIGATE));
      procNameToNumHash.put("prTaskLUTInvestigated", new Integer(LUT_INVESTIGATED));
      procNameToNumHash.put("prTaskLUTPrint", new Integer(LUT_PRINT));
      procNameToNumHash.put("prTaskLUTContainerReview", new Integer (LUT_CONTAINER_REVIEW));
      procNameToNumHash.put("prTaskLUTStopAssignment", new Integer(LUT_STOP_ASSIGNMENT));
      procNameToNumHash.put("prTaskLUTSignOff", new Integer(LUT_SIGN_OFF));
      procNameToNumHash.put("prTaskLUTValidatePid", new Integer(LUT_VALIDATE_PID));
      procNameToNumHash.put("prTaskLUTGetShortReasons", new Integer(LUT_GET_SHORT_REASONS));
      procNameToNumHash.put("prTaskLUTGetPidQuantity", new Integer(LUT_GET_PID_QTY));
      procNameToNumHash.put("prTaskLUTCallHelp", new Integer(LUT_CALL_HELP));
      procNameToNumHash.put("prTaskODRDebugInfo", new Integer(ODR_DEBUG_INFO));
      procNameToNumHash.put("prTaskLUTValid128", new Integer(LUT_VALID_128));
      procNameToNumHash.put("prTaskLUTHowAmIDoing", LUT_HOW_AM_I_DOING);
      procNameToNumHash.put("prTaskLUTGetCntTp", LUT_GET_CONTAINER_TYPES);
   }

   /**
    * The methods all follow this style.  All the inputs from the Talkman are stored
    * in params, in the same order they were sent.  The methods will set the inputs,
    * execute the service, and put the outputs in sb, formatted with a delimiter to be
    * replaced, this allows for commas in the text.
    * @param params ArrayList  The inputs from the Talkman
    * @param sb StringBuffer  The StringBuffer in which to put the string to return to the Talkman
    * @throws Exception
    */
   public void callLutCkOperSts(ArrayList params, StringBuffer sb) throws Exception{
	   //per Burris, don't need to return anything here...just be able to accept it
  }
   
  public void callLutCntReview(ArrayList params, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutCntReview shouldn't be needed");
  }

  public void callLutConfig(ArrayList params, StringBuffer sb) throws Exception{
	  
	  	int currID = 1;
	  	String currTable = "LUTConfig";
	  	
	  	String custName = db.getResult(currTable, currID, "outCustName");
	  	
    	sb.append(db.getResult(currTable, currID, "outWhse")).append(DELIMITER);
    	sb.append(custName).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outNCInvest")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outNoCtTrk")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outAddShrt")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outRgnZone")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outCtCount")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outOrdPicking")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outUseLUT")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outErrMsg"));

    	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	    	cc.writeMessage("0 " + custName);
	  	}
  }

  public void callLutGetAssign(ArrayList params, StringBuffer sb) throws Exception {

	  //get number of pick jobs in table so we know how to randomize
	  int size = db.getSize("LUTGetAssign");
	  
	  //pick a random job out of the list
	  Random generator = new Random();
	  int currID = generator.nextInt(size) + 1; //random int between 1 and size

	  String currTable = "LUTGetAssign";
	  
	  String assignID = db.getResult(currTable, currID, "outAssignID");
	  
	  sb.append(assignID).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outAssignNo")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outIsChase")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outWorkID")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPosition")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outTotItems")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outItNotPckd")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outBINotPckd")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outTotCube")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outGoalTime")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outRouteID")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPBRNoRem")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outDlvyLoc")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outContID")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outActvCont")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPosCounter")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outTotConts")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPassAssgn")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outIndivPick")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outNewPick")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outCntTrack")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("5 " + assignID);
	  	}
  }

  public void callLutGetLocInv(ArrayList params, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutGetLocInv shouldn't be needed");
  }

  public void callLutGetPicks(ArrayList params, StringBuffer sb) throws Exception {
	  
	  //begin building string to send to Virtual Warehouse program
	  String ccString = "6";
	  
	  int currID = 1;
	  String currTable = "LUTGetPicks";
	  
	  int assignID = Integer.parseInt((String)params.get(3)); //fourth parameter is assignID
	  
	  //This will break if assignID is not in the LUTGetPicks table!!!!
	  
	  ArrayList outAssgnSts = db.getSet(currTable, assignID, "outAssgnSts");
	  ArrayList outBaseItem = db.getSet(currTable, assignID, "outBaseItem");
	  ArrayList outSequence = db.getSet(currTable, assignID, "outSequence");
	  ArrayList outLocID = db.getSet(currTable, assignID, "outLocID");
	  ArrayList outRegionID = db.getSet(currTable, assignID, "outRegionID");
	  ArrayList outZone = db.getSet(currTable, assignID, "outZone");
	  ArrayList outAisle = db.getSet(currTable, assignID, "outAisle");
	  ArrayList outSlot = db.getSet(currTable, assignID, "outSlot");
	  ArrayList outPickQty = db.getSet(currTable, assignID, "outPickQty");
	  ArrayList outItemNum = db.getSet(currTable, assignID, "outItemNum");
	  ArrayList outVarWt = db.getSet(currTable, assignID, "outVarWt");
	  ArrayList outVarWtMin = db.getSet(currTable, assignID, "outVarWtMin");
	  ArrayList outVarWtMax = db.getSet(currTable, assignID, "outVarWtMax");
	  ArrayList outVarWtTot = db.getSet(currTable, assignID, "outVarWtTot");
	  ArrayList outQtyPicked = db.getSet(currTable, assignID, "outQtyPicked");
	  ArrayList outChkDigits = db.getSet(currTable, assignID, "outChkDigits");
	  ArrayList outItemDesc = db.getSet(currTable, assignID, "outItemDesc");
	  ArrayList outItemSize = db.getSet(currTable, assignID, "outItemSize");
	  ArrayList outItemUPC = db.getSet(currTable, assignID, "outItemUPC");
	  ArrayList outWorkID = db.getSet(currTable, assignID, "outWorkID");
	  ArrayList outDlvyLoc = db.getSet(currTable, assignID, "outDlvyLoc");
	  ArrayList outFullCase = db.getSet(currTable, assignID, "outFullCase");
	  ArrayList outDummy = db.getSet(currTable, assignID, "outDummy");
	  ArrayList outStore = db.getSet(currTable, assignID, "outStore");
	  ArrayList outGetCusPID = db.getSet(currTable, assignID, "outGetCusPID");
	  ArrayList outAccumCs = db.getSet(currTable, assignID, "outAccumCs");
	  ArrayList outPIDType = db.getSet(currTable, assignID, "outPIDType");
	  ArrayList outUOM = db.getSet(currTable, assignID, "outUOM");
	  ArrayList outErrCode = db.getSet(currTable, assignID, "outErrCode");
	  ArrayList outErrMsg = db.getSet(currTable, assignID, "outErrMsg");
	  
	  //build remainder of string for Virtual Warehouse program
	  for (int i = 0; i < outLocID.size(); i++) {
		  ccString += " ";
		  ccString += outLocID.get(i);
		  ccString += " ";
		  ccString += outPickQty.get(i);
	  }
	  
	  while (!outAssgnSts.isEmpty()) {
		  sb.append((String)outAssgnSts.remove(0)).append(DELIMITER);
		  sb.append((String)outBaseItem.remove(0)).append(DELIMITER);
		  sb.append((String)outSequence.remove(0)).append(DELIMITER);
		  sb.append((String)outLocID.remove(0)).append(DELIMITER);
		  sb.append((String)outRegionID.remove(0)).append(DELIMITER);
		  sb.append((String)outZone.remove(0)).append(DELIMITER);
		  sb.append((String)outAisle.remove(0)).append(DELIMITER);
		  sb.append((String)outSlot.remove(0)).append(DELIMITER);
		  sb.append((String)outPickQty.remove(0)).append(DELIMITER);
		  sb.append((String)outItemNum.remove(0)).append(DELIMITER);
		  sb.append((String)outVarWt.remove(0)).append(DELIMITER);
		  sb.append((String)outVarWtMin.remove(0)).append(DELIMITER);
		  sb.append((String)outVarWtMax.remove(0)).append(DELIMITER);
		  sb.append((String)outVarWtTot.remove(0)).append(DELIMITER);
		  sb.append((String)outQtyPicked.remove(0)).append(DELIMITER);
		  sb.append((String)outChkDigits.remove(0)).append(DELIMITER);
		  sb.append((String)outItemDesc.remove(0)).append(DELIMITER);
		  sb.append((String)outItemSize.remove(0)).append(DELIMITER);
		  sb.append((String)outItemUPC.remove(0)).append(DELIMITER);
		  sb.append((String)outWorkID.remove(0)).append(DELIMITER);
		  sb.append((String)outDlvyLoc.remove(0)).append(DELIMITER);
		  sb.append((String)outFullCase.remove(0)).append(DELIMITER);
		  sb.append((String)outDummy.remove(0)).append(DELIMITER);
		  sb.append((String)outStore.remove(0)).append(DELIMITER);
		  sb.append((String)outGetCusPID.remove(0)).append(DELIMITER);
		  sb.append((String)outAccumCs.remove(0)).append(DELIMITER);
		  sb.append((String)outPIDType.remove(0)).append(DELIMITER);
		  sb.append((String)outUOM.remove(0)).append(DELIMITER);
		  sb.append((String)outErrCode.remove(0)).append(DELIMITER);
		  sb.append((String)outErrMsg.remove(0));
		  sb.append(CR + LF);
		  doubleTerminate = false; //what does this do?
	  }
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage(ccString);
	  	}
  }
  
  public void callLutGetShortReasons(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutGetShortReasons shouldn't be needed");
  }
  
  public void callLutInvestgtd(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutInvestgtd shouldn't be needed");
  }

  public void callLutNewContnr(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutNewContnr shouldn't be needed");
  }

  public void callLutPassAssgn(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutPassAssgn shouldn't be needed");
  }

  public void callLutPckRegion(ArrayList p, StringBuffer sb) throws Exception {
 	
	  int currID = 1;
	  String currTable = "LUTPckRegion";
	  
	  String regnName = db.getResult(currTable, currID, "outRegnName");
	  
	  sb.append(db.getResult(currTable, currID, "outRegnNum")).append(DELIMITER);
	  sb.append(regnName).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outAutoAssgn")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outMaxWorkID")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outSkpAAllow")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outSkpSAllow")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPickSkips")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outCpSpkWkID")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outMaxQtyPck")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPrtLabels")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPrtChsLbl")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outSpkSlotDs")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPckPrompt")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outSpkWorkID")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outAllowSOff")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outContType")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outContCpcty")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outDlvPrvCnt")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outPassAssgn")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outDelivery")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outVerifyQty")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outSpkWordID")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outWorkIDFld")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outWorkIDLen")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outGoBacks")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("4 " + regnName);
	  	}
}
  
  public void callLutPicked(ArrayList p, StringBuffer sb) throws Exception {
	  int currID = 1;
	  String currTable = "LUTPicked";

	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("8 " + p.get(5) + " " + p.get(7));
	  	}
  }

  public void callLutPrint(ArrayList p, StringBuffer sb) throws Exception {
	  int currID = 1;
	  String currTable = "LUTPrint";

	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("7");
	  	}
  }

  public void callLutRequestWk(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutRequestWk shouldn't be needed");
  }

  public void callLutReviewPck(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutReviewPck shouldn't be needed");
  }

  public void callLutRgWkPerm(ArrayList p, StringBuffer sb) throws Exception {

	  int currID = 1;
	  String currTable = "LUTRgWkPerm";
	  
	  sb.append(db.getResult(currTable, currID, "outRegnNum")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outRegnName")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("2");
	  	}
  }

  public void callLutSignOff(ArrayList p, StringBuffer sb) throws Exception {
	  
	  int currID = 1;
	  String currTable = "LUTSignOff";

	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("9 " + p.get(2));
	  	}
  }

  public void callLutSignOn(ArrayList params, StringBuffer sb) throws Exception {
	  
	  //This currently returns the same information every time
	  //It doesn't do anything with the operator ID and password sent by the Talkman
	  
	  	int currID = 1;
	  	String currTable = "LUTSignOn";
	  	
    	sb.append(db.getResult(currTable, currID, "outWorkType")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outWorkSel")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outNCInvest")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outPass")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outPickType")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outCurrZone")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outCurrAisle")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outCurrSlot")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outSequence")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outCurrSeq")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outSavePass")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outSavePkTp")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outReturn")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outGBSStat")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outPassWIP")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outHoldQty")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outAddShrt")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "out1stAssgnmt")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outOrder")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outWorkQty")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outWorkWt")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outCurQty")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outShortQty")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
    	sb.append(db.getResult(currTable, currID, "outErrMsg"));   
  
    	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("1 " + params.get(2));
	  	}	
  }

  public void callLutStopAssgn(ArrayList p, StringBuffer sb) throws Exception {
	  
	  int currID = 1;
	  String currTable = "LUTStopAssgn";

	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));

  }

  public void callLutUndoLPick(ArrayList p, StringBuffer sb) throws Exception{
	  //Per Burris, don't need this call
	  throw new BadCallException("LutUndoLPick shouldn't be needed");
  }

  public void callLutUpdateSts(ArrayList p, StringBuffer sb) throws Exception {
	  
	  int currID = 1;
	  String currTable = "LUTUpdateSts";
	  
	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  }
  
  public void callLutValidatePid(ArrayList p, StringBuffer sb) throws Exception {
	  //This one is optional, to be implemented if we get a chance
	  //Only used if pallet ID collection is being used
	  //The flag that controls this is from getoutGetCusPID in GetPicks

  }
  public void callOdrPicked(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("OdrPicked shouldn't be needed");
  }

  public void callOdrUpdateSts(ArrayList p, StringBuffer sb)  throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("OdrUpdateSts shouldn't be needed");
  }

  public void callLutVarWeight(ArrayList p, StringBuffer sb) throws Exception {

	  //This one is optional, to be implemented if we get a chance

  }

  public void callOdrVarWeight(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("OdrVarWeight shouldn't be needed");
  }

  public void callLutGetPidQty(ArrayList p, StringBuffer sb) throws Exception {
	  
	  //This one is optional, to be implemented if we get a chance
	  
  }
  
  public void callLutCallHelp(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutCallHelp shouldn't be needed");
  }

  public void callLutValid128(ArrayList p, StringBuffer sb) throws Exception {
	  
	  //This one is optional, to be implemented if we get a chance
	  
  }  
  
  public void callLutHowAmIDoing(ArrayList p, StringBuffer sb, BurrisJavaApplication javaTca) throws Exception {  

	  //Had to change line 341+ of ServerThread.java to make this work
	  
	  //Per Burris, for this one "you could just stub something out"
	  //Thus this returns values from the database that are based on sample logs
	  //It returns the same thing every time

	  int i = 0;
	  String dateTime = (String) p.get(i++);
	  String serial = (String) p.get(i++);
	  String operator = (String) p.get(i++);
	  String loc = ServerSide.getLocations().get(serial);
	  boolean isDisabled = Boolean.parseBoolean(ServerSide.getProps().getProperty(
			  ServerSide.HOWAMIDOING_DISABLED_PREFIX + loc, "false"));
	  if (!isDisabled) {
		  int currID = 1;
		  String currTable = "LUTHowAmIDoing";
		  
		  sb.append(db.getResult(currTable, currID, "outPerformance")).append(DELIMITER);
		  sb.append(db.getResult(currTable, currID, "outIdeal")).append(DELIMITER);
		  sb.append(db.getResult(currTable, currID, "outBestTime")).append(DELIMITER);
		  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
		  sb.append(db.getResult(currTable, currID, "outErrMsg"));  
	  }

	  else {
		  sb.append(0).append(DELIMITER);
		  sb.append(0).append(DELIMITER);
		  sb.append(" ").append(DELIMITER);
		  sb.append(99).append(DELIMITER);
		  sb.append("Performance information is not available for your location.");
	  }

  }
  
  public void callLutGetCntTp(ArrayList p, StringBuffer sb) throws Exception {

	  //Random generator is really just for fun. There are three container types
	  //in the database, but Burris says they don't really use this method
	  Random generator = new Random();
	  int currID = generator.nextInt(3) + 1; //random int between 1 and 3
	  
	  String currTable = "LUTGetCntTp";
	  
	  sb.append(db.getResult(currTable, currID, "outContCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outContDesc")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrCode")).append(DELIMITER);
	  sb.append(db.getResult(currTable, currID, "outErrMsg"));
	  
  	//if we have a connection from the Virtual Warehouse program, send a message
	  	if (cc.hasConnection()) {
	  		cc.writeMessage("3");
	  	}
	  
  }

  public void callLutNewContService(ArrayList p, StringBuffer sb) throws Exception {
	  //Per Burris, don't need this call
	  throw new BadCallException("LutNewContService shouldn't be needed");
	  }
  
  public void setErrorOutput(StringBuffer sb, String servName, int errorNum, String errorMsg) {
     if (procNameToNumHash.containsKey(servName)) {
        setErrorOutput(sb, ((Integer)procNameToNumHash.get(servName)).intValue(), errorNum, errorMsg);
     }
     else {
        setErrorOutput(sb, NUM_OUT_LUT_PICKING_REGION, errorNum, errorMsg);
     }
  }
  /**
   * This method creates a string to return to the client in case of an error in which
   * the tca fails.  It strings together the necessary number of commas based on the
   * number of outputs for the requested service, then the errorNum, then the errorMsg.
   *
   * @param sb StringBuffer  Where the resulting string is stored.
   * @param servNum int  The number corresponding to the requested service.
   * @param errorNum int  The errorNum to send back to the Talkman.
   * @param errorMsg String  The errorMsg to send back to the Talkman.
   */
  public void setErrorOutput(StringBuffer sb, int servNum, int errorNum, String errorMsg) {
         for (int i = 2; i < numOut; i++) {
            sb.append(DELIMITER);
         }
         sb.append(errorNum).append(DELIMITER).append(errorMsg);
  }

  protected void formatOutput(StringBuffer sb) {
     String out = sb.toString();
     String temp = "";
     boolean addDelimiter = false;
     sb.delete(0, sb.length());
     if (out.length() > 1 && out.substring(out.length()-1, out.length()).equals(DELIMITER)) {
        addDelimiter = true;
     }
     String[] outArray = out.split(DELIMITER);
     for (int i = 0; i < outArray.length; i++) {
        if (outArray[i].indexOf(",") >= 0 ) {
           temp = "\"" + outArray[i] + "\"";
           outArray[i] = temp;
        }
        sb.append(outArray[i]);
        if (i < (outArray.length - 1)) {
           sb.append(",");
        }
     }
     if (addDelimiter) {
        sb.append(",");
     }
  }
  
  @SuppressWarnings("deprecation")
public  void dropService(ServiceProxy sp) {
	  try {
		  sp.drop();
	  }
	  catch(Exception ex) {
          System.err.println("Error dropping service:");
          ex.printStackTrace();		  
	  }
  }

  public static void main(String[] args) {

  }

}
