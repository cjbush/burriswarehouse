package com.jmex.bui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jmex.bui.event.MouseEvent;

public class BGroupContainerSingleSelectionTest {
	private BGroupContainer group;
	private BToggleButton a;
	private BToggleButton b;
	private BToggleButton c;

	@Before
	public void setUp() {
		group = new BGroupContainer();
		a = new BToggleButton("a");
		b = new BToggleButton("b");
		c = new BToggleButton("c");
	}
	
	@Test
	public void test_When_added() throws Exception {
		group.add(a);
		group.add(b);
		group.add(c);
		
		assertSingleSelectionBehavior();
	}
	
	@Test
	public void test_When_added_with_constraints() throws Exception {
		Object constraints = new Object();
		group.add(a, constraints);
		group.add(b, constraints);
		group.add(c, constraints);
		
		assertSingleSelectionBehavior();
	}
	
	@Test
	public void test_When_added_to_specific_index() throws Exception {
		group.add(0, a);
		group.add(1, b);
		group.add(2, c);
		
		assertSingleSelectionBehavior();
	}
	
	@Test
	public void test_When_added_to_specific_index_with_constraints() throws Exception {
		Object constraints = new Object();
		group.add(0, a, constraints);
		group.add(1, b, constraints);
		group.add(2, c, constraints);
		
		assertSingleSelectionBehavior();
	}

	private void assertSingleSelectionBehavior() {
		assertOnlyOneItemIsSelected();
		assertRemovedItemDoesNotRecieveUpdates();
		assertAddingSelectedItemBecomesTheSelectedItem();
		assertRemovingAnItemThatIsSelectedIsNotAffectedAfterRemoval();
		assertRemovalByIndex();
		assertMouseEventSelectionWorks();
	}

	private void assertOnlyOneItemIsSelected() {
		a.setSelected(true);
		b.setSelected(true);
		assertFalse(a.isSelected());
		assertTrue(b.isSelected());
		assertFalse(c.isSelected());
		
		c.setSelected(true);
		assertFalse(a.isSelected());
		assertFalse(b.isSelected());
		assertTrue(c.isSelected());
	}

	private void assertRemovedItemDoesNotRecieveUpdates() {
		group.remove(b);
		b.setSelected(true);
		assertFalse(a.isSelected());
		assertTrue(b.isSelected());
		assertTrue(c.isSelected());
		group.add(b);
	}

	private void assertAddingSelectedItemBecomesTheSelectedItem() {
		assertFalse(a.isSelected());
		assertTrue(b.isSelected());
		assertFalse(c.isSelected());
	}
	
	private void assertRemovingAnItemThatIsSelectedIsNotAffectedAfterRemoval() {
		b.setSelected(true);
		group.remove(b);
		c.setSelected(true);
		assertFalse(a.isSelected());
		assertTrue(b.isSelected());
		assertTrue(c.isSelected());
		group.add(b);
	}
	
	private void assertRemovalByIndex() {
		b.setSelected(true);
		BComponent component = group.getComponent(1);
		group.remove(1);
		c.setSelected(true);
		assertFalse(a.isSelected());
		assertTrue(b.isSelected());
		assertTrue(c.isSelected());
		group.add(component);
	}
	
	private void assertMouseEventSelectionWorks() {
		b.setSelected(true);
		a.dispatchEvent(new MouseEvent(a, System.currentTimeMillis(), 0, MouseEvent.MOUSE_PRESSED, MouseEvent.BUTTON1, 0, 0));
		a.dispatchEvent(new MouseEvent(a, System.currentTimeMillis(), 0, MouseEvent.MOUSE_RELEASED, MouseEvent.BUTTON1, 0, 0));
		assertTrue(a.isSelected());
		assertFalse(b.isSelected());
		assertFalse(c.isSelected());
	}
}
