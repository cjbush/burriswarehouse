/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.util;

import com.jmex.bui.BStyleConstants;
import com.jmex.bui.BStyleSheet;
import com.jmex.bui.Rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * @author torr
 * @since Oct 9, 2008 - 11:48:34 AM
 */
public class TokReader implements BStyleConstants {
    private static StreamTokenizer tokenize(final Reader reader) {
        final StreamTokenizer tok = new StreamTokenizer(new BufferedReader(reader));

        tok.lowerCaseMode(true);
        tok.slashSlashComments(true);
        tok.slashStarComments(true);

        tok.eolIsSignificant(false);

        tok.wordChars('#', '#');
        tok.wordChars('_', '_');

        return tok;
    }

    private static Rule startRule(StreamTokenizer tok) throws IOException {
        if (tok.ttype != StreamTokenizer.TT_WORD) {
            fail(tok, "style-class");
        }

        Rule rule = new Rule();
        rule.styleClass = tok.sval;

        switch (tok.nextToken()) {
            case START_BRACE:
                return rule;

            case COLON:
                if (tok.nextToken() != StreamTokenizer.TT_WORD) {
                    fail(tok, "pseudo-class");
                }
                rule.pseudoClass = tok.sval;
                if (tok.nextToken() != START_BRACE) {
                    fail(tok, "${START_BRACE}");
                }
                return rule;

            default:
                fail(tok, "${START_BRACE} or ${COLON}");
                return null; // not reachable
        }
    }

    private static boolean parseProperty(StreamTokenizer tok,
                                         Rule rule,
                                         BStyleSheet style) throws IOException {
        if (tok.nextToken() == END_BRACE) {
            return false;
        } else if (tok.ttype != StreamTokenizer.TT_WORD) {
            fail(tok, "property-name");
        }

        int sline = tok.lineno();
        String name = tok.sval;

        if (tok.nextToken() != ':') {
            fail(tok, ":");
        }

        ArrayList<Comparable> args = new ArrayList<Comparable>();
        while (tok.nextToken() != SEMI_COLON && tok.ttype != END_BRACE) {
            switch (tok.ttype) {
                case SINGLE_QUOTE:
                case DOUBLE_QUOTE:
                case StreamTokenizer.TT_WORD:
                    args.add(tok.sval);
                    break;
                case StreamTokenizer.TT_NUMBER:
                    args.add(tok.nval);
                    break;
                default:
                    System.err.println(
                            "Unexpected token: '" + (char) tok.ttype + "'. Line " + tok.lineno() + ".");
                    break;
            }
        }

        try {
            rule.properties.put(name, style.createProperty(name, args));
//             System.out.println("  " + name + " -> " + rule.get(name));
        } catch (Exception e) {
            System.err.println(
                    "Failure parsing property '" + name + "' line " + sline + ": " + e.getMessage());
            if (!(e instanceof IllegalArgumentException)) {
                e.printStackTrace(System.err);
            }
        }
        return true;
    }

    private static void fail(StreamTokenizer tok,
                             String expected) throws IOException {
        String err = "Parse failure line: " + tok.lineno() +
                     " expected: '" + expected + "' found: '";
        switch (tok.ttype) {
            case StreamTokenizer.TT_WORD:
                err += tok.sval;
                break;
            case StreamTokenizer.TT_NUMBER:
                err += tok.nval;
                break;
            case StreamTokenizer.TT_EOF:
                err += "EOF";
                break;
            default:
                err += (char) tok.ttype;
        }
        throw new IOException(err + "'");
    }

    public static void parse(Reader reader, BStyleSheet sheet) throws IOException {
        final StreamTokenizer tok = tokenize(reader);

        while (tok.nextToken() != StreamTokenizer.TT_EOF) {
            Rule rule = TokReader.startRule(tok);
            while (TokReader.parseProperty(tok, rule, sheet)) {
            }

            sheet._rules.put(makeFQClass(rule.styleClass, rule.pseudoClass), rule);
        }
    }

    public static String makeFQClass(String styleClass,
                                     String pseudoClass) {
        return (pseudoClass == null) ? styleClass : (styleClass + ":" + pseudoClass);
    }
}
