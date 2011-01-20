package com.jmex.bui;

import com.jmex.bui.bss.BStyleSheetUtil;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class MultipleStyleSheetTest {
    private static final String STYLE1_STRING = "/rsrc/style.bss";
    private static final String STYLE2_STRING = "/rsrc/style2.bss";

    private static final URL STYLE1 = MultipleStyleSheetTest.class.getResource(STYLE1_STRING);
    private static final URL STYLE2 = MultipleStyleSheetTest.class.getResource(STYLE2_STRING);

    public MultipleStyleSheetTest() {
        //load multiple bss files by string reference
        BStyleSheetUtil.combineMultipleBSS("stylesa.bss", STYLE1_STRING, STYLE2_STRING);

        //load multiple bss files by URL reference
        BStyleSheetUtil.combineMultipleBSS("stylesb.bss", STYLE1, STYLE2);

        //load multiple bss files by URL and/or String reference in a List
        List<Object> lst = new LinkedList<Object>();
        lst.add(STYLE1_STRING);
        lst.add(STYLE2);

        BStyleSheetUtil.combineMultipleBSS("stylesc.bss", lst);

        BStyleSheetUtil.getStyleSheetFromFile("/rsrc/styles.properties");
    }

    public static void main(String[] args) {
        new MultipleStyleSheetTest();
    }
}
