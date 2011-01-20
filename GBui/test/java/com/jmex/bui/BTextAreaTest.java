package com.jmex.bui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BTextAreaTest {
	private BTextArea textArea;

	@Before
	public void setUp() {
		textArea = new BTextArea();
	}
	
	@Test
	public void test_Get_set_text() throws Exception {
		textArea.setText("hello to you");
		assertEquals("hello to you", textArea.getText());
	}
	
	@Test
	public void test_Get_appended_text() throws Exception {
		textArea.setText("some text here");
		textArea.appendText("some more text to be added here");
		assertEquals("some text here\nsome more text to be added here", textArea.getText());
	}
}
