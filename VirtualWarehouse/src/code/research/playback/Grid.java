package code.research.playback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import code.app.VirtualWarehouse;
import code.model.ModelLoader;
import code.model.player.Player;
import code.vocollect.DBInfoRetriever;

import com.jme.bounding.BoundingBox;
import com.jme.input.KeyBindingManager;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.CloneImportExport;

public class Grid {
	private GridNode[][] grid;
	private double wLength = 48;
	private double wWidth = 54;
	private double resolutionX = 75;
	private double resolutionY = 75;
	private Spatial collisionModel;
	private VirtualWarehouse warehouseGame;
	private Node object;// =
						// ModelLoader.loadModel("obj","data/models/miscObj/gridnode/gridnode.obj",
						// "data/models/miscObj/gridnode/", null, true);
	private CloneImportExport ie;

	public Grid(Node root, VirtualWarehouse vw, boolean debug) {
		ie = new CloneImportExport();
		warehouseGame = vw;
		loadModel();
		grid = new GridNode[(int) resolutionX][(int) resolutionY];
		float y = -1.5f;
		for (int i = 0; i < resolutionX; i++) {
			float x = 1.5f;
			for (int j = 0; j < resolutionY; j++) {
				grid[i][j] = new GridNode(true, i, j, x, y, debug, root,
						(Node) ie.loadClone());
				grid[i][j].setModelBound(new BoundingBox());
				grid[i][j].updateModelBound();
				
				//UPDATE THIS
				/*
				//warehouseGame.getCollidables().attachChild(grid[i][j]);
				collisionModel = grid[i][j];
				if (!collisionModel.hasCollision(
						//warehouseGame.getCollidables(), false)) {
					grid[i][j].setUsable(true, root, (Node) ie.loadClone());
				}
				//warehouseGame.getCollidables().detachChild(grid[i][j]);
				*/
				x += wWidth / resolutionX;
			}
			y -= wLength / resolutionY;
		}
	}

	private void loadModel() {
		object = ModelLoader.loadModel("obj",
				"data/models/miscObj/gridnode/gridnode.obj",
				"data/models/miscObj/gridnode/", null, true, warehouseGame.getWarehouseWorld().getVirtualWarehouse().getDisplay().getRenderer(), "ignore");
		ie.saveClone(object);
	}
}
