package code.model.vehicle;

import java.util.ArrayList;

import code.model.player.Player;
import code.model.player.PlayerHandler;
import code.model.vehicle.actions.*;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.scene.Controller;

public class VehicleHandler extends InputHandler {
    //the vehicle we are going to control
    private Vehicle vehicle;
    //the default action
    private DriftAction drift;
    private Player player;
    
    public void update(float time) {
        if ( !isEnabled() ) return;

        super.update(time);
        if(KeyBindingManager.getKeyBindingManager().isValidCommand("autocomplete", false)){
        	player.getACH().activate();
        }
        
        if(player.getACH().isActive()) return;
        //we always want to allow friction to control the drift
        drift.performAction(event);
        vehicle.updateMovement(time);
    }
    
    /**
     * Supply the node to control and the api that will handle input creation.
     * @param vehicle the node we wish to move
     * @param api the library that will handle creation of the input.
     */
    public VehicleHandler(Vehicle vehicle, String api, Player player) {
        this.vehicle = vehicle;
        this.player = player;
        setKeyBindings(api);
        setActions(vehicle);
    }

    /**
     * creates the keyboard object, allowing us to obtain the values of a keyboard as keys are
     * pressed. It then sets the actions to be triggered based on if certain keys are pressed (WSAD).
     * @param api the library that will handle creation of the input.
     */
    private void setKeyBindings(String api) {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        keyboard.set("forward", KeyInput.KEY_W);
        keyboard.set("backward", KeyInput.KEY_S);
        keyboard.set("turnRight", KeyInput.KEY_D);
        keyboard.set("turnLeft", KeyInput.KEY_A);

        // the keyboard thing for being able to pick up the pallets.
        keyboard.set("pickupPallet", KeyInput.KEY_P);
    }

    /**
     * assigns action classes to triggers. These actions handle moving the node forward, backward and 
     * rotating it. It also creates an action for drifting that is not assigned to key trigger, this
     * action will occur each frame.
     * @param node the node to control.
     */
    private void setActions(Vehicle node) {
        ForwardAndBackwardAction forward = new ForwardAndBackwardAction(node, ForwardAndBackwardAction.FORWARD);
        addAction(forward, "forward", true);
        ForwardAndBackwardAction backward = new ForwardAndBackwardAction(node, ForwardAndBackwardAction.BACKWARD);
        addAction(backward, "backward", true);
        VehicleRotateAction rotateLeft = new VehicleRotateAction(node, VehicleRotateAction.LEFT);
        addAction(rotateLeft, "turnLeft", true);
        VehicleRotateAction rotateRight = new VehicleRotateAction(node, VehicleRotateAction.RIGHT);
        addAction(rotateRight, "turnRight", true);
        
        //not triggered by keyboard
        drift = new DriftAction(node);
    }
}
