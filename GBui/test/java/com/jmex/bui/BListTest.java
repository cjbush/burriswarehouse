package com.jmex.bui;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.jmex.bui.event.SelectionListener;
import com.jmex.bui.event.StateChangedEvent;
import com.jmex.bui.event.StateChangedEvent.SelectionState;

public class BListTest {
	private static final String ITEM_A = "one";
	private static final String ITEM_B = "two";
	private BList list;

	@Before
	public void setUp() {
		list = new BList(); 
		list.addValue(ITEM_A);
	}
	
	@Test
	public void test_Can_select_an_item_in_the_list() throws Exception {
		list.setSelectedValue(ITEM_A);
		assertEquals(ITEM_A, list.getSelectedValue());
	}
	
	@Test
	public void test_Remove_all_removes_all_values() throws Exception {
		list.addValue(ITEM_B);
		list.setSelectedValue(ITEM_B);
		list.removeAll();
		assertEquals(null, list.getSelectedValue());
	}
	
	@Test
	public void test_Remove_index_removes_the_value() throws Exception {
		list.addValue(ITEM_B);
		list.setSelectedValue(ITEM_B);
		list.remove(1);
		assertEquals(null, list.getSelectedValue());
	}
	
	@Test
	public void test_Selection_listener_is_called() throws Exception {
		SelectionListener listener = EasyMock.createStrictMock(SelectionListener.class);
		list.addValue(ITEM_B);
		list.addSelectionListener(listener);
		listener.stateChanged(new StateChangedEvent(list, -1, SelectionState.Selected));
		
		EasyMock.replay(listener);
		list.setSelectedValue(ITEM_A);
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Selection_listener_is_NOT_called_when_removed() throws Exception {
		SelectionListener listener = EasyMock.createStrictMock(SelectionListener.class);
		list.addSelectionListener(listener);
		list.removeSelectionListener(listener);
		
		EasyMock.replay(listener);
		list.setSelectedValue(ITEM_A);
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Adding_the_same_listener_twice_calls_only_once() throws Exception {
		SelectionListener listener = EasyMock.createStrictMock(SelectionListener.class);
		list.addSelectionListener(listener);
		list.addSelectionListener(listener);
		listener.stateChanged(new StateChangedEvent(list, -1, SelectionState.Selected));
		
		EasyMock.replay(listener);
		list.setSelectedValue(ITEM_A);
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Selection_listener_is_called_on_selection_changed() throws Exception {
		SelectionListener listener = EasyMock.createStrictMock(SelectionListener.class);
		list.addValue(ITEM_B);
		list.addSelectionListener(listener);
		listener.stateChanged(new StateChangedEvent(list, -1, SelectionState.Selected));
		listener.stateChanged(new StateChangedEvent(list, -1, SelectionState.Unselected));
		listener.stateChanged(new StateChangedEvent(list, -1, SelectionState.Selected));
		
		EasyMock.replay(listener);
		list.setSelectedValue(ITEM_A);
		list.setSelectedValue(ITEM_B);
		EasyMock.verify(listener);
	}
	
	@Test
	public void test_Add_many_values() throws Exception {
		list.addAllValues(Arrays.asList(ITEM_B, "item C", "item D"));
		
		assertEquals(ITEM_A, list.getValue(0));
		assertEquals(ITEM_B, list.getValue(1));
		assertEquals("item C", list.getValue(2));
		assertEquals("item D", list.getValue(3));
	}

	@Test
	public void test_Unable_to_add_components_to_a_list() throws Exception {
		try {
			list.add(new BLabel("not allowed to be added"));
			fail();
		} catch (IllegalStateException ise) {
			assertEquals("BList does not support adding of components", ise.getMessage());
		}
	}
	
	@Test
	public void test_Remove_component_from_a_list() throws Exception {
		list.addValue("bobo");
		list.remove(list.getComponent(0));
		assertEquals(1, list.getComponentCount());
		assertEquals("bobo", list.getValue(0));
	}
}
