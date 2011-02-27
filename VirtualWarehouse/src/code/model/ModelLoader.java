package code.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;
import com.jme.util.CloneImportExport;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.export.xml.XMLImporter;
import com.jme.util.resource.ClasspathResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.ModelFormatException;
import com.jmex.model.converters.ObjToJme;
import com.jmex.model.ogrexml.SceneLoader;

/**
 * Contains methods for loading models of various formats into JME as nodes.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class ModelLoader {

	private static final Logger logger = Logger.getLogger(ModelLoader.class.getName());

	/**
	 * Loads a model and returns it as a node, determining which load function to use
	 * based on the given format. SetBounds defaults to true if not specified.
	 */
	public static Node loadModel(String format, String filePath, String folderPath, SharedMeshManager smm, boolean setBounds, Renderer r, String type) {

		Node model = null;

		//if a sharedNodeManager is specified
		if (smm != null)
		{
			//if already loaded, get the loaded model instead of loading the file again
			model = smm.getNode(filePath);
		}

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
				if (type.equals("object") || type.equals("rack"))
				{
					CullState cs = r.createCullState();
					cs.setCullFace(CullState.Face.None);
					newModel.setRenderState(cs);
				}
				else
				{
					CullState cs = r.createCullState();
					cs.setCullFace(CullState.Face.Back);
					newModel.setRenderState(cs);
				}
			}

			//store the model in case it is used again
			if (smm != null && newModel != null)
			{
				smm.cacheNode(filePath, newModel);

				//get the shared mesh version if possible
				model = smm.getNode(filePath);
				if (model == null)
				{
					model = newModel;
				}
			}
			else if (newModel != null)
			{
				//no sharedMeshManager being used
				model = newModel;
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
		return loadModel(format, filePath, folderPath, null, true, null, "ignore");		
	}

	public static Node loadJmeModel(String path) {
		URL url = ModelLoader.class.getClassLoader().getResource(path);
		BinaryImporter bi = new BinaryImporter();

		try {
			Node jmeNode = (Node) bi.load(url);

			return jmeNode;
		} 

		catch (IOException e) {
			return null;
		}
	}

	/**
	 * Loads an XML file exported with HottBJ
	 */
	public static Node loadXMLModel(String path) {

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
	public static Node loadOgreXMLModel(String path) {

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


	public static Node loadObjModel(String path, String mtlPath) {

		ObjToJme converter = new ObjToJme();
		Node model = null;

		try {
			URL objFile = ModelLoader.class.getClassLoader().getResource(
					path);
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
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			converter.convert(objFile.openStream(), BO);
			//load as a TriMesh if single object
			//model = (TriMesh) BinaryImporter.getInstance().load(
			//new ByteArrayInputStream(BO.toByteArray()));
			//load as a node if multiple objects
			model=(Node)BinaryImporter.getInstance().load(
					new ByteArrayInputStream(BO.toByteArray()));

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (ClassCastException e) {
			//try to load as a TriMesh
			logger.info("obj could not be loaded - trying as TriMesh");
			model = new Node("TriMesh Holder Node");
			model.attachChild(loadTriMeshModel(path, mtlPath));
			return model;
		}

		return model;
	}

	public static TriMesh loadTriMeshModel(String path, String mtlPath) {

		ObjToJme converter = new ObjToJme();
		TriMesh model = null;

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
