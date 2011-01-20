package code.gui;

import com.jmex.game.state.CameraGameState;

/**
 * A game state to be used for menus. All menu states that extend this
 * must implement a setAlpha method so that menus can be faded in
 * and out.
 *
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public abstract class MenuState extends CameraGameState {

	public MenuState(String name) {
		super(name);
	}

	public abstract void setAlpha(float alpha); 
}
