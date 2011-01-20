/**
 * $ID$
 * $COPYRIGHT$
 */
package com.jmex.bui

import com.jmex.bui.BButtonScrollingList
import com.jmex.bui.event.ActionEvent
import com.jmex.bui.event.ActionListener

/**
 *
 * @author timo
 * @since Dec 13, 2008 12:24:23 PM
 */
class SLTWBActionListener implements ActionListener {
  BButtonScrollingList list

  public void actionPerformed(ActionEvent event) {
    if (event.action == "removeAll") {
      removeAll()
    } else if (event.action == "removeValues") {
      removeValues()
    } else if (event.action == "populate") {
      populate()
    }
  }

  public void populate() {
    for (int i = 0; i < 100; i++) {
      list.addValue("Item #" + i, true);
    }
  }

  private void removeAll() {
    list.removeAll();
  }

  private void removeValues() {
    list.removeValues();
  }
}