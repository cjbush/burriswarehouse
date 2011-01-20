package com.jmex.bui;

/**
 * Translates an object into a viewable plain text label.
 * 
 * @author gpelcha
 */
public interface LabelProvider {
	String getText(Object object);
}
