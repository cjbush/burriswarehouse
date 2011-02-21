package code.model.action.rack;

import java.util.List;

import code.model.action.pallet.Pallet;
import code.model.action.pick.Product;
import code.world.Room;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;
import com.jme.scene.Spatial;


public class DRack extends Node
{
	private enum PalletPosition {
		LEFT, RIGHT
	}
	
	public DRack()
	{
		/*
		if (binNumber1 != null && dbInfoRetriever.getIsPossiblePickJob(binNumber1) == true)
		{
			placePalletOnRack(object, binNumber1, r, rotationY, PalletPosition.LEFT);
		}
		else
		{
			randomPlacePalletOnRack(object, binNumber1, r, rotationY, PalletPosition.LEFT);
		}
		
		if (binNumber2 != null && dbInfoRetriever.getIsPossiblePickJob(binNumber2) == true)
		{
			placePalletOnRack(object, binNumber2, r, rotationY, PalletPosition.RIGHT);
		}
		else if (binNumber2 != null)
		{
			randomPlacePalletOnRack(object, binNumber2, r, rotationY, PalletPosition.RIGHT);
		}
		*/
	}
	
	/**
	 * Places pallets in their proper positions on the racks. Uses randomization if specified,
	 * so a pallet will only actually get placed a certain percentage of the time.
	 * @param object
	 * @param r
	 * @param rotationY
	 * @param p
	 */
	private void randomPlacePalletOnRack(Node object, String binNumber, Room r, float rotationY, PalletPosition p, boolean addRandomness) {
		//update rack object so that bounding measurements can be used
		object.updateGeometricState(0, true);
		
		float xOffset = (((BoundingBox) object.getWorldBound()).xExtent)/2;
		float zOffset = (((BoundingBox) object.getWorldBound()).zExtent)/2;
		
		float heightOffset = 0.82f; //space between each shelf
		
		/*
		for (int m=0; m<2; m++) //put pallets on the racks at m different heights
		{
			//add some randomness - possible empty spots on racks
			int randomNumber = (int)(Math.random()*10); 
			if (addRandomness == false || randomNumber < 8)
			{
				//place pallets
				
				//only allow pallets on the bottom (floor) level to be picked-up
				boolean canPickup = false;
				if (m == 0)
				{
					//canPickup = true;
					//disabled - not working well
					canPickup = false;
				}
				
				Pallet pallet = new Pallet(this, true, binNumber, canPickup, true);
				
				Node roomNode = (Node) palletRooms.getChild(r.getName());
				
				roomNode.attachChild(pallet);
				palletsList.add(pallet);
				
				List<Spatial> productsOnPallet = pallet.getProducts();
				if (productsOnPallet != null)
				{
					for (int n=0; n<productsOnPallet.size(); n++)
					{
						productsList.add((Product) productsOnPallet.get(n));
					}
				}
				
				if (p == PalletPosition.LEFT)
				{
					if (rotationY == 90  || rotationY == -270)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z+zOffset);
					}
					else if (rotationY == 270 || rotationY == -90)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z-zOffset);
					}
					else if (rotationY == 0)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x-xOffset, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z);
					}
					else if (rotationY == 180 || rotationY == -180)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x+xOffset, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z);
					}
				}
				else if (p == PalletPosition.RIGHT)
				{
					if (rotationY == 90 || rotationY == -270)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z-zOffset);
					}
					else if (rotationY == 270 || rotationY == -90)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z+zOffset);
					}
					else if (rotationY == 0)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x+xOffset, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z);
					}
					else if (rotationY == 180 || rotationY == -180)
					{
						pallet.setLocalTranslation(object.getWorldTranslation().x-xOffset, object.getWorldTranslation().y+heightOffset*m, object.getWorldTranslation().z);
					}
				}
				
				//pallet.setLocalRotation(q);
				pallet.updateWorldBound();
				pallet.lock();
			}
		}
		*/
	}

	/**
	 * Creates a pallet a certain percentage of the time.
	 * @param object
	 * @param binNumber
	 * @param r
	 * @param rotationY
	 * @param p
	 */
	private void randomPlacePalletOnRack(Node object, String binNumber, Room r, float rotationY, PalletPosition p) {
		randomPlacePalletOnRack(object, binNumber, r, rotationY, p, false);
	}
	
	/**
	 * Ensures that product will be placed on the pallet.
	 * @param object
	 * @param binNumber
	 * @param r
	 * @param rotationY
	 * @param p
	 */
	private void placePalletOnRack(Node object, String binNumber, Room r, float rotationY, PalletPosition p) {
		randomPlacePalletOnRack(object, binNumber, r, rotationY, p, false);
	}
}