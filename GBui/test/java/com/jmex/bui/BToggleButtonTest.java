package com.jmex.bui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.event.SelectionListener;
import com.jmex.bui.event.StateChangedEvent;
import com.jmex.bui.event.StateChangedEvent.SelectionState;

public class BToggleButtonTest {
	private SelectionListener listener;
	private BToggleButton button;

	@Before
	public void setUp() throws Exception {
		listener = EasyMock.createStrictMock(SelectionListener.class);
		button = new BToggleButton("button");
	}
	
	@Test
	public void test_Can_listen_to_selection_events() throws Exception {
		listener.stateChanged((StateChangedEvent)EasyMock.anyObject());
		
		EasyMock.replay(listener);
		button.addSelectionListener(listener);
		button.setSelected(true);
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Adding_listener_twice_sends_one_event() throws Exception {
		listener.stateChanged((StateChangedEvent)EasyMock.anyObject());
		
		EasyMock.replay(listener);
		button.addSelectionListener(listener);
		button.addSelectionListener(listener);
		button.setSelected(true);
		EasyMock.verify(listener);
	}
	
	
	@Test
	public void test_No_event_sent_when_listener_is_removed() throws Exception {
		EasyMock.replay(listener);
		button.addSelectionListener(listener);
		button.removeSelectionListener(listener);
		button.setSelected(true);
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_MouseEventSelectionWorks() {
		StateChangedEvent event = new StateChangedEvent(button, -1, SelectionState.Selected);
		listener.stateChanged(event);
		
		EasyMock.replay(listener);
		button.addSelectionListener(listener);
		button.dispatchEvent(new MouseEvent(button, System.currentTimeMillis(), 0, MouseEvent.MOUSE_PRESSED, MouseEvent.BUTTON1, 0, 0));
		button.dispatchEvent(new MouseEvent(button, System.currentTimeMillis(), 0, MouseEvent.MOUSE_RELEASED, MouseEvent.BUTTON1, 0, 0));
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Sanity_check_No_listeners_added() throws Exception {
		EasyMock.replay(listener);
		button.setSelected(true);
		EasyMock.verify(listener);
	}
}	
