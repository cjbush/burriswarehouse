package com.jmex.bui;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.jmex.bui.event.FocusEvent;
import com.jmex.bui.event.FocusListener;


public class BTextFieldTest {
	private BTextField textField;

	@Before
	public void setUp() {
		textField = new BTextField();
	}
	
	@Test
	public void test_Listener_called_on_focus_gained_event() throws Exception {
		FocusListener listener = EasyMock.createStrictMock(FocusListener.class);
		listener.focusGained((FocusEvent)EasyMock.anyObject());
		textField.addFocusListener(listener);
		
		EasyMock.replay(listener);
		textField.dispatchEvent(new FocusEvent(textField, 1l, FocusEvent.FOCUS_GAINED));
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Listener_called_on_focus_lost_event() throws Exception {
		FocusListener listener = EasyMock.createStrictMock(FocusListener.class);
		listener.focusLost((FocusEvent)EasyMock.anyObject());
		textField.addFocusListener(listener);
		
		EasyMock.replay(listener);
		textField.dispatchEvent(new FocusEvent(textField, 1l, FocusEvent.FOCUS_LOST));
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Adding_then_removing_the_listener_recieves_no_events() throws Exception {
		FocusListener listener = EasyMock.createStrictMock(FocusListener.class);
		textField.addFocusListener(listener);
		textField.removeFocusListener(listener);
		
		EasyMock.replay(listener);
		textField.dispatchEvent(new FocusEvent(textField, 1l, FocusEvent.FOCUS_LOST));
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Adding_two_of_the_same_listener_receives_only_one_event() throws Exception {
		FocusListener listener = EasyMock.createStrictMock(FocusListener.class);
		listener.focusLost((FocusEvent)EasyMock.anyObject());
		
		textField.addFocusListener(listener);
		textField.addFocusListener(listener);
		
		EasyMock.replay(listener);
		textField.dispatchEvent(new FocusEvent(textField, 1l, FocusEvent.FOCUS_LOST));
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Works_without_any_listeners_added() throws Exception {
		textField.dispatchEvent(new FocusEvent(textField, 1l, FocusEvent.FOCUS_LOST));
	}
}
