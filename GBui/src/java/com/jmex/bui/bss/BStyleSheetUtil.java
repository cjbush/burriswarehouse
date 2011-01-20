//
// $Id: BStyleSheetUtil.java,v 1.8 2007/05/08 22:13:49 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2005, Michael Bayne, All Rights Reserved
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package com.jmex.bui.bss;

import com.jmex.bui.BStyleSheet;
import com.jmex.bui.provider.DefaultResourceProvider;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author timo
 * @since 27Apr07
 */
public class BStyleSheetUtil {
    public static BStyleSheet getStyleSheet(final String path) {
        return getStyleSheet(BStyleSheetUtil.class.getResourceAsStream(path));
    }

    public static BStyleSheet getStyleSheetFromFile(String fileName) {
        List<String> list = readFile(fileName);

        List<String> lst = new LinkedList<String>();

        for (final String aList : list) {
            lst.addAll(readFile(aList));
        }

        return processList(lst);
    }

    private static List<String> readFile(final String path) {
        return readFile(BStyleSheetUtil.class.getResourceAsStream(path));
    }

    private static List<String> readFile(final InputStream is) {
        List<String> readFile = new ArrayList<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(is));
            for (String inner = in.readLine(); inner != null; inner = in.readLine()) {
                readFile.add(inner);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return readFile;
    }

    public static void writeFile(final String filePath,
                                 final List content) {
        FileOutputStream fout = null;
        try {
            File file = new File(filePath);
            fout = new FileOutputStream(file);
            for (Object aContent : content) {
                fout.write((aContent + "\r").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * return a combined BStyleSheet from any number of URLs
     *
     * @param list URL varargs
     * @return BStyleSheet
     */
    public static BStyleSheet getStyleSheet(final URL... list) {
        return processList(loadMultipleBSS(list));
    }

    /**
     * return a combined BStyleSheet from any number of Strings
     *
     * @param list String varargs
     * @return BStyleSheet
     */
    public static BStyleSheet getStyleSheet(final String... list) {
        List<String> lst = loadMultipleBSS(list);

        StringBuilder sb = new StringBuilder();

        for (String s : lst) {
            sb.append(s);
        }

        return getStyleSheet(new StringReader(sb.toString()));
    }

    public static BStyleSheet getStyleSheet(final InputStream stin) {
        BStyleSheet style = null;
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(stin);
            style = getStyleSheet(in);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return style;
    }

    /**
     * create stylesheet from Reader
     *
     * @param reader Reader
     * @return BStyleSheet instance
     */
    public static BStyleSheet getStyleSheet(final Reader reader) {
        BStyleSheet sheet = null;

        sheet = new BStyleSheet(reader,
                                new DefaultResourceProvider());

        assert sheet != null;

        return sheet;
    }


    /**
     * @param list URL varargs
     * @return List<String>
     */
    private static List<String> loadMultipleBSS(final URL... list) {
        List<String> lst = new LinkedList<String>();

        try {
            for (URL s : list) {
                lst.addAll(readFile(s.openStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lst;
    }

    /**
     * @param list String varargs
     * @return List<String>
     */
    private static List<String> loadMultipleBSS(final String... list) {
        List<String> lst = new LinkedList<String>();

        for (String s : list) {
            lst.addAll(readFile(s));
        }

        return lst;
    }

    /**
     * @param list List of URLs and/or Strings
     * @return List<String>
     */
    private static List<String> loadMultipleBSS(final List list) {
        final List<String> styleList = new LinkedList<String>();

        if (list != null && list.size() > 0) {
            for (final Object aLst : list) {
                if (aLst instanceof URL) {
                    try {
                        styleList.addAll(readFile(((URL) aLst).openStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (aLst instanceof String) {
                    styleList.addAll(readFile((String) aLst));
                }
            }
        }

        return styleList;
    }

    /**
     * @param fileName String name of the file we want to put the output into
     * @param list     List of URLs and/or Strings that we want to combine
     */
    public static void combineMultipleBSS(final String fileName,
                                          final List list) {
        writeFile(fileName, loadMultipleBSS(list));
    }

    /**
     * @param fileName String name of the file we want to put the output into
     * @param list     Strings that we want to combine
     */
    public static void combineMultipleBSS(final String fileName,
                                          final String... list) {
        writeFile(fileName, loadMultipleBSS(list));
    }

    /**
     * @param fileName String name of the file we want to put the output into
     * @param list     URLs that we want to combine
     */
    public static void combineMultipleBSS(final String fileName,
                                          final URL... list) {
        writeFile(fileName, loadMultipleBSS(list));
    }

    private static BStyleSheet processList(final List<String> list) {
        final StringBuilder sb = new StringBuilder();

        for (String s : list) {
            sb.append(s).append("\n");
        }

        return getStyleSheet(new StringReader(sb.toString()));
    }
}