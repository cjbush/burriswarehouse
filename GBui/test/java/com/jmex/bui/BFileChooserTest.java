/**
 * $Id$
 * $Copyright$
 */
package com.jmex.bui;

import com.jmex.bui.base.BaseTest;
import com.jmex.bui.layout.GroupLayout;

import javax.swing.*;
import java.io.File;

/**
 * temporary solution to implementing a file chooser within gbui the actual solution should include a file chooser that
 * can be skinned as the developer/designer wishes just as the rest of their game can be
 *
 * @author torr
 * @since Jan 7, 2010 - 11:51:37 AM
 */
public final class BFileChooserTest extends BaseTest {
    @Override
    protected void createWindows() {
        final BWindow window = new BDecoratedWindow(BuiSystem.getStyle(), null);
        window.setLayoutManager(GroupLayout.makeVStretch());

        JFrame frame = new JFrame();
        JFileChooser jc = new JFileChooser();

        frame.add(jc);
        frame.pack();
        frame.setVisible(true);

        BuiSystem.getRootNode().addWindow(window);
        window.setSize(400, 400);
        window.setLocation(25, 25);

        int returnVal = jc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName() + ".\n");
            frame.setVisible(false);
        } else {
            System.out.println("Open command cancelled by user.\n");
        }
    }

    public static void main(final String[] args) {
        new BFileChooserTest().start();
    }
}