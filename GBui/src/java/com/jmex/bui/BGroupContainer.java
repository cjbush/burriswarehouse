package com.jmex.bui;

import com.jmex.bui.event.StateChangedEvent;
import com.jmex.bui.event.SelectionListener;
import com.jmex.bui.event.StateChangedEvent.SelectionState;

public class BGroupContainer extends BContainer {
	BToggleButton selected;
	private SingleSelectionMaintainer listener = new SingleSelectionMaintainer();

	@Override
	public void add(final BComponent child) {
		addListener(child);
		super.add(child);
	}

	@Override
	public void add(BComponent child, Object constraints) {
		addListener(child);
		super.add(child, constraints);
	}
	
	@Override
	public void add(int index, BComponent child) {
		addListener(child);
		super.add(index, child);
	}
	
	@Override
	public void add(int index, BComponent child, Object constraints) {
		addListener(child);
		super.add(index, child, constraints);
	}

	private void addListener(BComponent child) {
		if (child instanceof BToggleButton) {
			BToggleButton toggleButton = (BToggleButton) child;
			if (toggleButton.isSelected()) {
				listener.stateChanged(new StateChangedEvent(toggleButton, -1, SelectionState.Selected));
			}
			toggleButton.addSelectionListener(listener);
		}
	}
	
	@Override
	public void remove(BComponent child) {
		super.remove(child);
		removeListener(child);
	}
	
	@Override
	public void remove(int index) {
		removeListener(getComponent(index));
		super.remove(index);
	}

	private void removeListener(BComponent child) {
		if (child instanceof BToggleButton) {
			((BToggleButton)child).removeSelectionListener(listener);
			if (child == selected) {
				selected = null;
			}
		}
	}

	private final class SingleSelectionMaintainer implements SelectionListener {
		

		@Override
		public void stateChanged(StateChangedEvent event) {
			if (event.getType() == StateChangedEvent.SelectionState.Unselected) {
				return;
			}
			if (selected != null && selected != event.getSource()) {
				selected.setSelected(false);
			}
			BToggleButton button = (BToggleButton) event.getSource();
			if (button.isSelected()) {
				selected = (BToggleButton) event.getSource();
			}
		}
	}
}
