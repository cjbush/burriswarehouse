/**
 * 
 */
package com.jmex.bui;

class DummyObject {
	private final String label;

	public DummyObject(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	@Override
	public String toString() {
		return "DummyObject: " + label;
	}
}