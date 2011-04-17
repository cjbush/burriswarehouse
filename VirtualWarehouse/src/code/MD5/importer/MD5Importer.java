package code.MD5.importer;

import java.io.IOException;
import java.net.URL;

import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;

import code.model.player.RandomPerson;
import code.MD5.controller.MD5Controller;
import code.MD5.importer.resource.AnimImporter;
import code.MD5.importer.resource.MeshImporter;
import code.MD5.importer.resource.ResourceImporter;
import code.MD5.interfaces.IMD5Animation;
import code.MD5.interfaces.IMD5Controller;
import code.MD5.interfaces.IMD5Node;

/**
 * <code>MD5Importer</code> is a singleton utility class that provides
 * a mechanism to load models and animations of MD5 format.
 * <p>
 * <code>MD5Importer</code> allows separate <code>Mesh</code> and
 * <code>IMD5Animation</code> loading process. However, it also
 * provides convenient methods for loading both MD5 resources at once.
 * <p>
 * <code>MD5Importer</code> should be cleaned up after the loading
 * process is completed.
 * <P>
 * For details on MD5 format, please go to official MD5 wiki at
 * {@link}http://www.modwiki.net/wiki/MD5_(file_format).
 *
 * @author Yi Wang (Neakor)
 * @version Modified date: 02-19-2008 22:17 EST
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * Takes a RandomPerson object to determine which random textures to apply to MD5 model
 */
public class MD5Importer {
	/**
	 * The singleton <code>MD5Importer</code> instance.
	 */
	private static MD5Importer instance = new MD5Importer();
	/**
	 * The mesh <code>ResourceImporter</code> instance.
	 */
	private final ResourceImporter<IMD5Node> meshImporter;
	/**
	 * The animation <code>ResourceImporter</code> instance.
	 */
	private final ResourceImporter<IMD5Animation> animImporter;
	/**
	 * The <code>IMD5Node</code> instance.
	 */
	private IMD5Node node;
	/**
	 * The <code>IMD5Animation</code> instance.
	 */
	private IMD5Animation animation;

	/**
	 * Constructor of <code>MD5Importer</code>.
	 */
	public MD5Importer() {
		super();
		this.meshImporter = new MeshImporter();
		this.animImporter = new AnimImporter();
	}
	
	/**
	 * Retrieve the singleton importer instance.
	 * @return The <code>MD5Importer</code> instance.
	 */
	public static MD5Importer getInstance() {
		return MD5Importer.instance;
	}
	
	/**
	 * Load the given md5mesh and md5anim files and assign the loaded
	 * <code>JointAnimation</code> to the <code>Mesh</code>.
	 * @param md5mesh The <code>URL</code> of the md5mesh file.
	 * @param modelName The <code>String</code> name of the loaded model.
	 * @param md5anim The <code>URL</code> points to the md5anim file.
	 * @param animName The <code>String</code> name of the loaded animation.
	 * @param repeatType The <code>Integer</code> repeat type.
	 * @throws IOException Thrown when errors occurred during file reading.
	 */
	public void load(URL md5mesh, String modelName, URL md5anim, String animName, int repeatType) throws IOException {
		this.loadMesh(md5mesh, modelName);
		this.loadAnim(md5anim, animName);
		this.assignAnimation(repeatType);
	}
	
	/**
	 * Load the given md5mesh and md5anim files and assign the loaded
	 * <code>JointAnimation</code> to the <code>Mesh</code>.
	 * @param md5mesh The <code>URL</code> of the md5mesh file.
	 * @param modelName The <code>String</code> name of the loaded model.
	 * @param md5anim The <code>URL</code> points to the md5anim file.
	 * @param animName The <code>String</code> name of the loaded animation.
	 * @param repeatType The <code>Integer</code> repeat type.
	 * @throws IOException Thrown when errors occurred during file reading.
	 */
	public void load(URL md5mesh, String modelName, URL md5anim, String animName, int repeatType, RandomPerson rp) throws IOException {
		this.loadMesh(md5mesh, modelName, rp);
		this.loadAnim(md5anim, animName);
		this.assignAnimation(repeatType);
	}

	/**
	 * Load the given md5mesh file.
	 * @param md5mesh The <code>URL</code> points to the md5mesh file.
	 * @param name The <code>String</code> name of the loaded model.
	 * @throws IOException Thrown when errors occurred during file reading.
	 */
	public void loadMesh(URL md5mesh, String name) throws IOException {
		this.node = this.meshImporter.load(md5mesh, name);
	}
	
	/**
	 * Load the given md5mesh file.
	 * @param md5mesh The <code>URL</code> points to the md5mesh file.
	 * @param name The <code>String</code> name of the loaded model.
	 * @throws IOException Thrown when errors occurred during file reading.
	 */
	public void loadMesh(URL md5mesh, String name, RandomPerson rp) throws IOException {
		this.node = this.meshImporter.load(md5mesh, name, rp);
	}

	/**
	 * Load the given md5anim file.
	 * @param md5anim The <code>URL</code> points to the md5anim file.
	 * @param name The <code>String</code> name of the loaded animation.
	 * @throws IOException Thrown when errors occurred during file reading.
	 */
	public void loadAnim(URL md5anim, String name) throws IOException {
		this.animation = this.animImporter.load(md5anim, name);
	}

	/**
	 * Assign the loaded animation to the node.
	 * @param repeatType The <code>Integer</code> repeat type.
	 */
	private void assignAnimation(int repeatType) {
		IMD5Controller controller = new MD5Controller(this.node);
		controller.setRepeatType(repeatType);
		controller.addAnimation(this.animation);
		controller.setActive(true);
		this.node.addController(controller);
	}

	/**
	 * Set the minification (MM) <code>Texture</code> filter.
	 * @param filter The minification (MM) <code>Texture</code> filter.
	 */
	public void setMiniFilter(Texture.MinificationFilter filter) {
		((MeshImporter)this.meshImporter).setMiniFilter(filter);
	}

	/**
	 * Set the magnification (FM) <code>Texture</code> filter.
	 * @param filter The magnification (FM) <code>Texture</code> filter.
	 */
	public void setMagFilter(Texture.MagnificationFilter filter) {
		((MeshImporter)this.meshImporter).setMagFilter(filter);
	}

	/**
	 * Set the texture anisotropic level.
	 * @param value The <code>Integer</code> anisotropic level value.
	 */
	public void setAnisotropic(int aniso) {
		((MeshImporter)this.meshImporter).setAnisotropic(aniso);
	}

	/**
	 * Set if oriented bounding should be used for the meshes.
	 * @param orientedBounding True if oriented bounding should be used. False otherwise.
	 */
	public void setOrientedBounding(boolean orientedBounding) {
		((MeshImporter)this.meshImporter).setOrientedBounding(orientedBounding);
	}

	/**
	 * Retrieve the minification (MM) texture filter.
	 * @return The <code>MinificationFilter</code> enumeration.
	 */
	public MinificationFilter getMiniFilter() {
		return ((MeshImporter)this.meshImporter).getMiniFilter();
	}

	/**
	 * Retrieve the magnification (FM) texture filter.
	 * @return The <code>MagnificationFilter</code> enumeration.
	 */
	public MagnificationFilter getMagFilter() {
		return ((MeshImporter)this.meshImporter).getMagFilter();
	}

	/**
	 * Retrieve the anisotropic level.
	 * @return The <code>Integer</code> anisotropic level.
	 */
	public int getAnisotropic() {
		return ((MeshImporter)this.meshImporter).getAnisotropic();
	}

	/**
	 * Retrieve the MD5 node instance.
	 * @return The <code>IMD5Node</code> instance.
	 */
	public IMD5Node getMD5Node() {
		return this.node;
	}

	/**
	 * Retrieve the MD5 animation instance.
	 * @return The <code>IMD5Animation</code> instance.
	 */
	public IMD5Animation getAnimation() {
		return this.animation;
	}

	/**
	 * Check if oriented bounding should be used.
	 * @return True if oriented bounding should be used. False otherwise.
	 */
	public boolean isOriented() {
		return ((MeshImporter)this.meshImporter).isOriented();
	}

	/**
	 * Cleanup the importer.
	 */
	public void cleanup() {
		this.node = null;
		this.animation = null;
		this.meshImporter.cleanup();
		this.animImporter.cleanup();
	}
}
