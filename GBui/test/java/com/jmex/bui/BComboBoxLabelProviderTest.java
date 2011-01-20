package com.jmex.bui;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.jmex.bui.icon.BIcon;
import com.jmex.bui.icon.BlankIcon;

public class BComboBoxLabelProviderTest {
	
	private DummyObject item;
	private BComboBox comboBox;
	private BIcon icon;

	@Before
	public void setUp() {
		item = new DummyObject("item 1");
		comboBox = new BComboBox();
		icon = new BlankIcon(10, 10);
	}
	
	@Test
	public void test_Default_label_provider() throws Exception {
		comboBox.addItem(item);
		comboBox.selectItem(item);
		assertEquals(item.toString(), comboBox.getText());
	}
	
	@Test
	public void test_Given_label_provider() throws Exception {
		LabelProvider provider = EasyMock.createStrictMock(LabelProvider.class);
		EasyMock.expect(provider.getText(item)).andReturn(item.getLabel()).atLeastOnce();
		
		EasyMock.replay(provider);
		comboBox.setLabelProvider(provider);
		comboBox.addItem(item);
		comboBox.selectItem(item);
		assertEquals(item.getLabel(), comboBox.getText());
		EasyMock.verify(provider);
	}
	
	@Test
	public void test_Given_label_provider_with_null_item() throws Exception {
		LabelProvider provider = EasyMock.createStrictMock(LabelProvider.class);
		// although the label provider could give something else, it doesn't.
		EasyMock.expect(provider.getText(null)).andReturn(null).atLeastOnce();
		
		EasyMock.replay(provider);
		comboBox.setLabelProvider(provider);
		comboBox.addItem(null);
		comboBox.selectItem(null);
		assertNull(comboBox.getText());
		EasyMock.verify(provider);
	}
	
	@Test
	public void test_Set_label_provider_after_item_added() throws Exception {
		comboBox.addItem(item);
		comboBox.selectItem(item);
		LabelProvider provider = EasyMock.createStrictMock(LabelProvider.class);
		EasyMock.expect(provider.getText(item)).andReturn(item.getLabel()).atLeastOnce();
		
		EasyMock.replay(provider);
		comboBox.setLabelProvider(provider);
		assertEquals(item.getLabel(), comboBox.getText());
		EasyMock.verify(provider);
	}
	
	@Test
	public void test_Set_image_label_provider() throws Exception {
		ImageLabelProvider provider = EasyMock.createMock("imageProvider", ImageLabelProvider.class);
		EasyMock.expect(provider.getText(item)).andReturn(item.getLabel()).times(2);
		EasyMock.expect(provider.getImage(item)).andReturn(icon).times(2);
		
		EasyMock.replay(provider);
		comboBox.setLabelProvider(provider);
		comboBox.addItem(item);
		comboBox.selectItem(item);
		assertEquals(item.getLabel(), comboBox.getText());
		EasyMock.verify(provider);
	}
	
	@Test
	public void test_Set_image_label_provider_after_item_added() throws Exception {
		comboBox.addItem(item);
		comboBox.selectItem(item);
		ImageLabelProvider provider = EasyMock.createMock(ImageLabelProvider.class);
		EasyMock.expect(provider.getText(item)).andReturn(item.getLabel()).atLeastOnce();
		EasyMock.expect(provider.getImage(item)).andReturn(icon).atLeastOnce();
		
		EasyMock.replay(provider);
		comboBox.setLabelProvider(provider);
		EasyMock.verify(provider);
	}
	
	@Test
	public void test_Item_type_with_label_provider_ignores_label_provider() throws Exception {
		LabelProvider provider = EasyMock.createStrictMock(LabelProvider.class);
		BComboBox.Item object = new BComboBox.Item(item, "");
		EasyMock.expect(provider.getText(item)).andReturn(item.getLabel()).atLeastOnce();
		
		EasyMock.replay(provider);
		comboBox.setLabelProvider(provider);
		comboBox.addItem(object);
		comboBox.selectItem(object);
		assertEquals(item.getLabel(), comboBox.getText());
		EasyMock.verify(provider);
	}
	
	@Test
	public void test_Item_type_with_image_label_provider() throws Exception {
		BComboBox.Item object = new BComboBox.Item(item, "");
		ImageLabelProvider provider = EasyMock.createMock("imageProvider", ImageLabelProvider.class);
		EasyMock.expect(provider.getText(item)).andReturn(item.getLabel()).times(2);
		EasyMock.expect(provider.getImage(item)).andReturn(icon).times(2);
		
		EasyMock.replay(provider);
		comboBox.setLabelProvider(provider);
		comboBox.addItem(object);
		comboBox.selectItem(object);
		assertEquals(item.getLabel(), comboBox.getText());
		EasyMock.verify(provider);
	}
}
