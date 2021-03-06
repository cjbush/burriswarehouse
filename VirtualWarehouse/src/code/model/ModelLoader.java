package code.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;
import com.jme.util.CloneImportExport;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.export.xml.XMLImporter;
import com.jme.util.resource.ClasspathResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.game.state.GameStateManager;
import com.jmex.model.ModelFormatException;
import com.jmex.model.converters.ObjToJme;
import com.jmex.model.ogrexml.SceneLoader;

/**
 * Contains methods for loading models of various formats into JME as nodes.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * Many new optimizations take place, such as loading models as JME's native format (.jme) versus .obj, etc.
 */
public class ModelLoader {

	private static final Logger logger = Logger.getLogger(ModelLoader.class.getName());
	
	private static double totalTimeOBJ = 0.0;
	private static double totalTimeTRI = 0.0;
	
	private static String path;
	private static String mtlpath;
	
	private static boolean rebuildCache = false;
	
	public static void rebuildCache(){
		System.out.println("Model cache will be rebuilt.");
		rebuildCache = true;
	}
	
	public static void disableCacheRebuild(){
		System.out.println("Model cache rebuild cancelled.");
		rebuildCache = false;
	}

	/**
	 * Loads a model and returns it as a node, determining which load function to use
	 * based on the given format. SetBounds defaults to true if not specified.
	 */
	public static Node loadModel(String format, String filePath, String folderPath, boolean setBounds, Renderer r, String type) {

		Node model = null;
		
		path = filePath;
		mtlpath = folderPath;

		
		//if already loaded, get the loaded model instead of loading the file again
		model = SharedMeshManager.getNode(filePath);

		//load the model from file if not already loaded
		if (model == null)
		{

			Node newModel = null;

			if (format.equalsIgnoreCase("xml"))
			{
				newModel = loadXMLModel(filePath);
			}
			else if (format.equalsIgnoreCase("obj"))
			{
				newModel = loadObjModel(filePath, folderPath);
			}
			else if (format.equalsIgnoreCase("jme"))
			{
				newModel = loadJmeModel(filePath);
			}
			else if (format.equalsIgnoreCase("ogrexml"))
			{
				newModel = loadOgreXMLModel(filePath);
			}
			else
			{
				logger.info("Model format " + format + " cannot be loaded.");
			}
			
			if (newModel != null && r != null)
			{
				//Update, since there are some objects that we want to see the backsides of them, (like a garbage can) we added a quick check
				//To see what type of culling to add
				if (type.equals("object") || type.equals("rack")) //if it is a normal object or rack, we should not cull it, so we can see the backsides
				{
					CullState cs = r.createCullState();
					cs.setCullFace(CullState.Face.None);
					newModel.setRenderState(cs);
				}
				else //otherwise cull for optimization (really doesn't optimize much, but a little)
				{
					CullState cs = r.createCullState();
					cs.setCullFace(CullState.Face.Back);
					newModel.setRenderState(cs);
				}
			}

			//store the model in case it is used again
			if (newModel != null)
			{
				SharedMeshManager.cacheNode(filePath, newModel);
				System.out.println("Cached model "+filePath);

				//get the shared mesh version if possible
				model = SharedMeshManager.getNode(filePath);
				if (model == null)
				{
					model = newModel;
				}
			}

		}

		if (setBounds && model != null)
		{
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
		}
		
		

		return model;

	}

	public static Node loadModel(String format, String filePath, String folderPath) {
		return loadModel(format, filePath, folderPath, true, null, "ignore");		
	}

	private static Node loadJmeModel(String path) {
		//path = "data/models/world/warehouse/Warehouse.obj";
		URL url = ModelLoader.class.getClassLoader().getResource(path);
		//URL url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_MODEL, path);
		BinaryImporter bi = new BinaryImporter();

		try {
			Node jmeNode = (Node) bi.load(url);

			return jmeNode;
		} 

		catch (IOException e) {
			return null;
		}
		catch (NullPointerException e){
			url = ModelLoader.class.getClassLoader().getResource(path.substring(4));
			Node jmeNode = null;
			try {
				jmeNode = (Node) bi.load(url);
			} catch (IOException e1) {
			}
			catch(NullPointerException e2){
				e.printStackTrace();
				//return null;
				return loadObjModel(ModelLoader.path, ModelLoader.mtlpath, true);
			}
			return jmeNode;
		}
	}

	/**
	 * Loads an XML file exported with HottBJ
	 */
	private static Node loadXMLModel(String path) {

		XMLImporter xmlImporter = XMLImporter.getInstance();
		Node model = null;

		try {
			URL XMLFile = ModelLoader.class.getClassLoader().getResource(
					path);

			model = (Node) xmlImporter.load(XMLFile);

			BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestEnabled(true);
			as.setTestFunction(BlendState.TestFunction.GreaterThan);
			model.setRenderState(as);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}

		return model;
	}

	/**
	 * Loads an OgreXML scene file. Does not set bounds. All mesh files must be
	 * in the same folder as the scene file.
	 */
	private static Node loadOgreXMLModel(String path) {

		ResourceLocatorTool.addResourceLocator(
				ResourceLocatorTool.TYPE_TEXTURE,
				new ClasspathResourceLocator());

		SceneLoader ogreSceneLoader = null;
		String sceneString = "/" + path;
		try {
			URL sceneUrl = ResourceLocatorTool.locateResource(
					ResourceLocatorTool.TYPE_MODEL, sceneString);

			if (sceneUrl == null)
				throw new IllegalStateException(
						"Required runtime resource missing: "
						+ sceneString);
			ogreSceneLoader = new SceneLoader();
			ogreSceneLoader.load(sceneUrl);
			ogreSceneLoader.setModelsOnly(true);
			// modelsOnly means to ignore lights, cams, env. in scene file.
			logger.info("Successfully loaded");
			return ogreSceneLoader.getScene();
		} catch (ModelFormatException mfe) {
			logger.info("Model file is corrupted");
			// Not recoverable.
			throw new RuntimeException(mfe);
		} catch (IOException ioe) {
			logger.info("Unrecoverable I/O failure");
			// Not recoverable.
			throw new RuntimeException(ioe);
		} finally {
			ogreSceneLoader = null;  // encourage GC
		}

	}

	
	private static Node loadObjModel(String path, String mtlPath){
		return loadObjModel(path, mtlPath, false);
	}

	private static Node loadObjModel(String path, String mtlPath, boolean override) {
		ObjToJme converter = new ObjToJme();
		Node model = null;
		URL objFile = ModelLoader.class.getClassLoader().getResource(path);
		
		OutputStream fos;
		
		String jmePath = "src/";
		jmePath += path.substring(0, path.lastIndexOf('.'));
		jmePath += ".jme";
		
		File jmeFile = new File(jmePath);
		
		double start = System.currentTimeMillis();
		
		ByteArrayOutputStream BO = new ByteArrayOutputStream();
		
		try {
			if(jmeFile.exists() && !rebuildCache && !override){
				System.out.println("JME Model exists. Loading from "+jmePath);
				jmePath = path.substring(0, path.lastIndexOf('.'))+".jme";
				return loadJmeModel(jmePath);
			}
			//System.out.println("Loading model from: "+objFile.toString());
			System.out.println("Loading model from: "+path);
			if (null != mtlPath && mtlPath.length() > 0) {

				//set path for textures
				try {
					ResourceLocatorTool.addResourceLocator(
							ResourceLocatorTool.TYPE_TEXTURE, 
							new SimpleResourceLocator(ModelLoader.class.getResource("/" + mtlPath)));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}

				URL mtlFile = ModelLoader.class.getClassLoader().getResource(
						mtlPath);
				converter.setProperty("mtllib", mtlFile);
			} else {
				converter.setProperty("mtllib", objFile);
			}
			converter.setProperty("texdir",objFile);
			converter.convert(objFile.openStream(), BO);
			model = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			System.out.println("Exporting to: "+jmePath);
			try {
				jmeFile.createNewFile();
				fos = new FileOutputStream(jmeFile);
				BinaryExporter.getInstance().save(model, fos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (ClassCastException e) {
			//try to load as a TriMesh
			logger.info("obj could not be loaded - trying as TriMesh");
			double finish = System.currentTimeMillis();
			totalTimeOBJ += finish-start;
			System.out.println("Total time loading OBJ: "+totalTimeOBJ/1000);
			start = System.currentTimeMillis();
			model = new Node("TriMesh Holder Node");
			//model.attachChild(loadTriMeshModel(path, mtlPath));
			try {
				converter.convert(objFile.openStream(), BO);
				final TriMesh object = (TriMesh) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
				com.jme.util.geom.GeometryTool.minimizeVerts(object, 0);
				
				/*GameTaskQueueManager.getManager().render(new Callable<Object>(){
					public Object call() throws Exception{
						object.lockMeshes();
						return object;
					}
				});*/
				
				object.lockMeshes();
				
				SharedMeshManager.cacheTriMesh(path, object);
				
				model.attachChild(object);
			} catch (IOException e1) {
				return null;
			}
			finish = System.currentTimeMillis();
			totalTimeTRI += finish-start;
			System.out.println("Total time loading TRI: "+totalTimeTRI/1000);
		}

		return model;
	}

	private static TriMesh loadTriMeshModel(String path, String mtlPath) {

		ObjToJme converter = new ObjToJme();
		//TriMesh model = null;
		
		TriMesh model = SharedMeshManager.getTriMesh(path);
		
		if(model != null){
			System.out.println("Read TriMesh from cache: "+path);
			return model;
		}

		try {
			URL objFile = ModelLoader.class.getClassLoader().getResource(
					path);
			if (null != mtlPath && mtlPath.length() > 0) {
				URL mtlFile = ModelLoader.class.getClassLoader().getResource(
						mtlPath);
				converter.setProperty("mtllib", mtlFile);
			} else {
				converter.setProperty("mtllib", objFile);
			}
			converter.setProperty("texdir",objFile);
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			converter.convert(objFile.openStream(), BO);
			//load as a TriMesh if single object
			model = (TriMesh) BinaryImporter.getInstance().load(
					new ByteArrayInputStream(BO.toByteArray()));
			
			SharedMeshManager.cacheTriMesh(path, model);
			System.out.println("Cached TriMesh "+path);
			//load as a node if multiple objects
			//model=(Node)BinaryImporter.getInstance().load(
			//                new ByteArrayInputStream(BO.toByteArray()));

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}

		return model;
	}


}
