package com.jmex.bui;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class BListLabelProviderTest {
	private BList list;
	private DummyObject value1;
	private DummyObject value2;
	private String value3;

	@Before
	public void setUp() {
		list = new BList();
		
		value1 = new DummyObject("item 1");
		value2 = new DummyObject("item 2");
		value3 = new String("the string");

		list.addValue(value1);
		list.addValue(value2);
		list.addValue(value3);
	}
	
	@Test
	public void test_Default_label_provider() throws Exception {
		assertEquals("DummyObject: item 1", getToggleButtonText(0));
		assertEquals("DummyObject: item 2", getToggleButtonText(1));
		assertEquals("the string", getToggleButtonText(2));
	}

	private String getToggleButtonText(int index) {
		return ((BToggleButton)list.getComponent(index)).getText();
	}
	
	@Test
	public void test_Label_provider_given() throws Exception {
		list.setLabelProvider(new NameLabelProvider());
		assertEquals("item 1", getToggleButtonText(0));
		assertEquals("item 2", getToggleButtonText(1));
		assertEquals("the string", getToggleButtonText(2));
	}
	
	@Test
	public void test_Give_label_provider_then_add_items() throws Exception {
		list.setLabelProvider(new NameLabelProvider());
		list.addValue(new DummyObject("item 4"));
		assertEquals("item 4", getToggleButtonText(3));
	}
	
	@Test
	public void test_Image_label_provider_given() throws Exception {
		ImageLabelProvider provider = EasyMock.createStrictMock(ImageLabelProvider.class);
		
		EasyMock.expect(provider.getText(value1)).andReturn("item 1");
		EasyMock.expect(provider.getImage(value1)).andReturn(null);
		EasyMock.expect(provider.getText(value2)).andReturn("item 2");
		EasyMock.expect(provider.getImage(value2)).andReturn(null);
		EasyMock.expect(provider.getText(value3)).andReturn("item 3");
		EasyMock.expect(provider.getImage(value3)).andReturn(null);
		
		EasyMock.replay(provider);
		list.setLabelProvider(provider);
		EasyMock.verify(provider);
	}
	
	@Test
	public void test_Image_label_provider_given_then_add_item() throws Exception {
		list.removeAll();
		ImageLabelProvider provider = EasyMock.createStrictMock(ImageLabelProvider.class);
		
		EasyMock.expect(provider.getText(value1)).andReturn("item 1");
		EasyMock.expect(provider.getImage(value1)).andReturn(null);
		
		EasyMock.replay(provider);
		list.setLabelProvider(provider);
		list.addValue(value1);
		EasyMock.verify(provider);
	}
	
	private final class NameLabelProvider implements LabelProvider {
		@Override
		public String getText(Object object) {
			if (object instanceof DummyObject)
				return ((DummyObject)object).getLabel();
			else 
				return null;
		}
	}
}
