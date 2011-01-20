/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui

import com.steadystate.css.parser.CSSOMParser
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.CSSRule
import org.w3c.dom.css.CSSStyleRule
import org.w3c.dom.css.CSSStyleSheet

/**
 *
 *
 * @author torr
 * @since Oct 8, 2008 - 4:11:03 PM
 *
 */

CSSOMParser parser = new CSSOMParser();
def iss = BuiStyleSheetTest.getClass().getResourceAsStream("/rsrc/colors.css")
def br = new BufferedReader(new InputStreamReader(iss))
def is = new InputSource(br);
CSSStyleSheet stylesheet = parser.parseStyleSheet(is, null, null);

def listing = [:]

def rules = stylesheet.cssRules
def size = rules.getLength()

for (int i = 0; i < size; i++) {
    CSSRule rule = rules.item(i);
    switch (rule.getType()) {
        case CSSRule.UNKNOWN_RULE:
//                addUnknownRule(node, (CSSUnknownRule) rule);
            println "CSSUnknownRule"
            break;
        case CSSRule.STYLE_RULE:
//                addStyleRule(node, (CSSStyleRule) rule);
            def styleName = rule.getSelectorText()
            def style = ((CSSStyleRule) rule).getStyle();
            if (style) {
//                        println styleName
                def styles = []
                int len = style.getLength()
                for (int j = 0; j < len; j++) {
                    String name = style.item(j);
                    String value = style.getPropertyValue(name);
                    String prio = style.getPropertyPriority(name);

                    if (prio.equals("")) {
                        styles << ["$name": "$value"]
                    } else {
                        styles << "$name : $value ! $prio)"
                    }
                }

                styleName.split(",").each {
                    listing.put(it.trim(), styles)
                }
            }
//                    println "CSSStyleRule"
            break;
        case CSSRule.CHARSET_RULE:
//                addCharsetRule(node, (CSSCharsetRule) rule);
            println "CSSCharsetRule"
            break;
        case CSSRule.IMPORT_RULE:
//                addImportRule(node, (CSSImportRule) rule);
            println "CSSImportRule"
            break;
        case CSSRule.MEDIA_RULE:
//                addMediaRule(node, (CSSMediaRule) rule);
            println "CSSMediaRule"
            break;
        case CSSRule.FONT_FACE_RULE:
//                addFontFaceRule(node, (CSSFontFaceRule) rule);
            println "CSSFontFaceRule"
            break;
        case CSSRule.PAGE_RULE:
//                addPageRule(node, (CSSPageRule) rule);
            println "CSSPageRule"
            break;
    }

}

listing.each {
    println it
}
