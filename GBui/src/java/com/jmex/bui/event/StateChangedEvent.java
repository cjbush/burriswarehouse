package com.jmex.bui.event;

public class StateChangedEvent extends BEvent {
	private static final long serialVersionUID = 1L;
	private final SelectionState state;
	public enum SelectionState {Selected, Unselected};
	
	public StateChangedEvent(Object source, long when, SelectionState state) {
		super(source, when);
		this.state = state;
	}

	public SelectionState getType() {
		return state;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(StateChangedEvent.class)) {
			StateChangedEvent event = (StateChangedEvent) obj;
			return event.state == state && event.getSource() ==  getSource();
		}
		return false;
	}

	public static StateChangedEvent create(Object source, boolean selected) {
		SelectionState state = selected?SelectionState.Selected: SelectionState.Unselected;
		return new StateChangedEvent(source, System.currentTimeMillis(), state);
	}
}
