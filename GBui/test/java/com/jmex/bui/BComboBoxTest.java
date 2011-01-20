package com.jmex.bui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jmex.bui.BComboBox.Item;

public class BComboBoxTest {
	private DummyObject dummy;
	private BComboBox comboBox;

	@Before
	public void setUp() {
		dummy = new DummyObject("item 1");
		comboBox = new BComboBox();
	}
	
	@Test
	public void test_Normal_value_objects_can_be_passed_in() throws Exception {
		comboBox.addItem(dummy);
		comboBox.selectItem(0);
		assertEquals(dummy, comboBox.getSelectedItem());
		assertEquals(dummy, comboBox.getItem(0));
		assertEquals(dummy, comboBox.getSelectedValue());
		assertEquals(dummy, comboBox.getValue(0));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void test_Item_objects_can_be_passed_in() throws Exception {
		BComboBox.Item item = new BComboBox.Item(dummy, dummy.getLabel());
		comboBox.addItem(item);
		comboBox.selectValue(item.value);
		
		assertEquals(item, comboBox.getSelectedItem());
		assertEquals(item, comboBox.getItem(0));
		assertEquals(item.value, comboBox.getSelectedValue());
		assertEquals(item.value, comboBox.getValue(0));
	}
	
	@Test
	public void test_Invalid_value_given() throws Exception {
		comboBox.addItem(dummy);
		comboBox.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object object) {
				return null;
			}
		});
		comboBox.selectValue(dummy);
		assertEquals(dummy, comboBox.getSelectedValue());
		comboBox.selectValue(null);
	}
}
