package code.hud;

import code.app.VirtualWarehouse;
import code.world.Room;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;

/**
 * 
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class InformationBar extends HUD {
	
	private static final int BAR_HEIGHT = 35;
	
	private BitmapText room;
	private BitmapText temperature;
	private BitmapText time;
	
	private int seconds;
	private int minutes;
	private Room currentRoom;
	
	private VirtualWarehouse warehouseGame;
	private ScoringTimer timer;
	
	public InformationBar(VirtualWarehouse vw, BitmapFont f) {
		
		warehouseGame = vw;
		timer = warehouseGame.getScoringTimer();
		
		float barWidth = getDisplayWidth()-30;
		
		//create a quad for showing the text on
		Quad infoBarQuad = new Quad("infoBarQuad", barWidth, BAR_HEIGHT);
		infoBarQuad.setZOrder(2);
		infoBarQuad.setDefaultColor(ColorRGBA.gray.clone());
		this.attachChild(infoBarQuad);
		
		//get the font that's passed in or load a new one if needed
		BitmapFont font = f;
		if (font == null)
		{
			font = BitmapFontLoader.loadDefaultFont();
		}
        
		//set some defaults for the room text
		room = new BitmapText(font, false);
		room.setBox(new Rectangle(0, 0, 150, 20));
		room.setSize(14);
		room.setDefaultColor(ColorRGBA.white.clone());
		room.setText("Room Name");
		room.setZOrder(1);
		room.update();
		room.setLocalTranslation(-barWidth/2+15, 10, 0);
        this.attachChild(room);
        
        //set some defaults for the temperature text
        temperature = new BitmapText(font, false);
        temperature.setBox(new Rectangle(0, 0, 100, 20));
        temperature.setSize(14);
        temperature.setDefaultColor(ColorRGBA.white.clone());
        temperature.setText("Temperature");
        temperature.setZOrder(1);
        temperature.update();
        temperature.setLocalTranslation(-15, 10, 0);
        this.attachChild(temperature);
        
        //set some defaults for the time text
        time = new BitmapText(font, false);
        time.setBox(new Rectangle(0, 0, 100, 20));
        time.setSize(14);
        time.setDefaultColor(ColorRGBA.white.clone());
        time.setText("Time");
        time.setZOrder(1);
        time.update();
        time.setLocalTranslation(barWidth/2-55, 10, 0);
        this.attachChild(time);
		
		this.setLocalTranslation(getDisplayWidth()/2, 30, 0);
		
		this.updateRenderState();
	}
	
	public void updateText() {
		
		//update the room info if it changed
		Room myRoom = warehouseGame.getWarehouseWorld().getRoomManager().getCurrentRoom();
		if (currentRoom != myRoom && myRoom != null)
		{
			currentRoom = myRoom;
			//if (currentRoom != null)
			//{
				room.setText(currentRoom.getName());
				temperature.setText(currentRoom.getTemperature());
			//}
			//else
			//{
			//	room.setText("No room info");
			//	temperature.setText("No temp info");
			//}
			
			room.update();
			temperature.update();
		}
		
		//update the time if a second has passed (don't need to update every frame)
		int s = timer.getSeconds();
		if (seconds != s)
		{
			seconds = s;
			setTimeText(timer.getFormattedTime());
		}
		
	}
	
	public void setScoringTimer(ScoringTimer timer) {
		this.timer = timer; 
	}
	
	public void setRoomText(String roomName) {
		room.setText(roomName);
		room.update();
	}
	
	public void setTemperatureText(String temperatureString) {
		temperature.setText(temperatureString);
		temperature.update();
	}
	
	public void setTimeText(String timeString) {
		time.setText(timeString);
		time.update();
	}
	
}
