package code.collisions;

import code.model.player.Player;
import code.model.player.PlayerHandler;

public class BoundingBoxes {
	PlayerHandler ph;
	private float leftX;
	private float rightX;
	private float lowerZ;
	private float upperZ;

	public BoundingBoxes() {

	}

	public void setLeftX(float x) {
		leftX = x;
	}

	public void setRightX(float x) {
		rightX = x;
	}

	public void setLowerZ(float z) {
		lowerZ = z;
	}

	public void setUpperZ(float z) {
		upperZ = z;
	}

	public void checkForCollisions(Player playerNode, float lastX, float lastZ) {
		float playerX = playerNode.getLocalTranslation().getX();
		float playerZ = playerNode.getLocalTranslation().getZ();
		ph = (PlayerHandler) playerNode.getInputHandler();

		if ((playerX > leftX) && (playerZ < lowerZ) && (playerX < rightX) && (playerZ > upperZ)) {

			float diffRightX = Math.abs(playerX-rightX);
			float diffLeftX = Math.abs(playerX-leftX);
			float top = Math.abs(playerZ-upperZ);
			float bottom = Math.abs(playerZ-lowerZ);
			
			// so if I'm closest to the right of the wall...
			if (diffRightX <= diffLeftX && diffRightX <= top && diffRightX<= bottom){
				playerNode.setLocalTranslation(playerX+diffRightX, .1f, playerZ);
			}
			
			else if (diffLeftX <= diffRightX && diffLeftX <= top && diffLeftX <= bottom){
				playerNode.setLocalTranslation(playerX-diffLeftX, .1f, playerZ);
			}
			
			else if (top <= bottom && top <= diffRightX && top <= diffLeftX){
				playerNode.setLocalTranslation(playerX, .1f, playerZ-top);
			}
			else if (bottom <= top && bottom <= diffRightX && bottom <= diffLeftX){
				playerNode.setLocalTranslation(playerX, .1f, playerZ+bottom);
			}
			

		}
	}

}
