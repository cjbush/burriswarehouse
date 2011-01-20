package com.jmex.bui;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses ( {
	BTextFieldTest.class,
	BListTest.class,
	BListLabelProviderTest.class,
	BGroupContainerSingleSelectionTest.class,
	BToggleButtonTest.class, 
	BComboBoxLabelProviderTest.class,
	BComboBoxTest.class,
	BListDragAndDropTest.class,
	BTextAreaTest.class
})
@RunWith(Suite.class)
public class AllAutomatedTests {
	
}
