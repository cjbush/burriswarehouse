package code.hud;

import code.app.VirtualWarehouse;
import code.component.Score;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;

/**
 * HUD element to show the player's final scores after the pick
 * jobs are complete.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class ScoreDisplay extends HUD {
	
	/**
	 * Creates a display to show the final score at the end of the game.
	 * @param f
	 * @param warehouseGame
	 */
	public ScoreDisplay(BitmapFont f, VirtualWarehouse warehouseGame) {
		
		float heightOffset = 0;
		
		Score score = warehouseGame.getScore();
		ScoringTimer scoringTimer = warehouseGame.getScoringTimer();
		
		//create the text
		
		//get the font that's passed in or load a new one if needed
		BitmapFont font = f;
		if (font == null)
		{
			font = BitmapFontLoader.loadDefaultFont();
		}
        
		//heading text
		BitmapText heading = new BitmapText(font, false);
		heading.setSize(32);
		heading.setDefaultColor(ColorRGBA.white.clone());
		heading.setText("Pick Jobs Complete");
		heading.setZOrder(1);
		heading.update();
		heading.setLocalTranslation(getDisplayWidth()/2-heading.getLineWidth()/2, getDisplayHeight()/2, 0);
        this.attachChild(heading);
		
        heightOffset += heading.getLineHeight();
        
		//total time text
		BitmapText timeText = new BitmapText(font, false);
		timeText.setSize(22);
		timeText.setDefaultColor(ColorRGBA.white.clone());
		timeText.setText("Total Time: " + scoringTimer.getFormattedTime());
		timeText.setZOrder(1);
		timeText.update();
		timeText.setLocalTranslation(getDisplayWidth()/2-timeText.getLineWidth()/2, getDisplayHeight()/2-heightOffset, 0);
        this.attachChild(timeText);
        
        heightOffset += timeText.getLineHeight();
        
        //distance traveled text
		BitmapText distanceTraveledText = new BitmapText(font, false);
		distanceTraveledText.setSize(22);
		distanceTraveledText.setDefaultColor(ColorRGBA.white.clone());
		distanceTraveledText.setText("Total Distance Traveled: " + score.getDistance());
		distanceTraveledText.setZOrder(1);
		distanceTraveledText.update();
		distanceTraveledText.setLocalTranslation(getDisplayWidth()/2-distanceTraveledText.getLineWidth()/2, getDisplayHeight()/2-heightOffset, 0);
        this.attachChild(distanceTraveledText);
        
        heightOffset += distanceTraveledText.getLineHeight();
        
        //picking errors
		BitmapText errorsText = new BitmapText(font, false);
		errorsText.setSize(22);
		errorsText.setDefaultColor(ColorRGBA.white.clone());
		errorsText.setText("Errors: " + score.getBoxesPickedWrong());
		errorsText.setZOrder(1);
		errorsText.update();
		errorsText.setLocalTranslation(getDisplayWidth()/2-errorsText.getLineWidth()/2, getDisplayHeight()/2-heightOffset, 0);
        this.attachChild(errorsText);
        
        heightOffset += errorsText.getLineHeight();
        
        //hit key to continue text
		BitmapText continueText = new BitmapText(font, false);
		continueText.setSize(22);
		continueText.setDefaultColor(ColorRGBA.white.clone());
		continueText.setText("(Press Escape To Continue)");
		continueText.setZOrder(1);
		continueText.update();
		continueText.setLocalTranslation(getDisplayWidth()/2-continueText.getLineWidth()/2, getDisplayHeight()/2-heightOffset, 0);
        this.attachChild(continueText);
        
        this.setLocalTranslation(0, heightOffset/2, 0);
        this.updateGeometricState(0, true);
        this.updateRenderState();
        
        KeyBindingManager.getKeyBindingManager().remove("pause");
        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
	}
	
}
