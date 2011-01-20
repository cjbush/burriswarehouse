package vocollect;

import services.BurrisVocApplication;
import services.LUTConfigService;
import services.LUTSignOnService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Communications {
  public Communications() {
  }
  public static void main(String[] args) {
    Communications communications1 = new Communications();
    BurrisVocApplication voc = new BurrisVocApplication();
    String custName = "";
    String slot = "";
    try {
//      voc.connect(AppInfo.tcbmachine, AppInfo.tcbport);
      voc.connect("eider", 7050);
      LUTConfigService configService = voc.getLUTConfigService();
      configService.setinDateTime("05-27-04 17:32:27");
      configService.setinTermSerial("64133649");
      configService.execute();
      custName = configService.getoutCustName();
      System.out.println("custName = " + custName);

      LUTSignOnService signOnService = voc.getLUTSignOnService();
      signOnService.setinDateTime("05-27-04 17:32:27");
      signOnService.setinOperatorID("jkrupka");
      signOnService.setinPassword("1234");
      signOnService.setinTermSerial("64133649");
      signOnService.execute();
      slot = signOnService.getoutCurrSlot();
      System.out.println("Curr slot = " + slot);
      System.out.println("curr aisle = " + signOnService.getoutCurrAisle());
      voc.disconnect();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      try {
        voc.disconnect();
      }
      catch (Exception ex1) {
      }
    }
  }

}
