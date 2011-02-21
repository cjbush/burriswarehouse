package code.MD5.importer.resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;

import code.model.player.RandomPerson;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * <code>ResourceImporter</code> defines the abstraction of an
 * importer that provides the common importing functionalities.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-18-2008 12:29 EST
 * @version Modified date: 02-19-2009 22:41 EST
 */
public abstract class ResourceImporter<T> {
	/**
	 * The base orientation value used for translating coordinate systems.
	 */
	protected static final Quaternion base = new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f);
	/**
	 * The current supported versions of MD5 format.
	 */
	protected static final int version = 10;
	/**
	 * The <code>StreamTokenizer</code> instance.
	 */
	protected StreamTokenizer reader;
	
	/**
	 * Constructor of <code>ResourceImporter</code>.
	 */
	protected ResourceImporter() {}
	
	/**
	 * Load the resource.
	 * @param url The <code>URL</code> to the file.
	 * @param name The <code>String</code> to set the result to.
	 * @return The <code>T</code> loaded data structure.
	 * @throws IOException If reading is interrupted.
	 */
	public T load(URL url, String name) throws IOException {
		this.setupReader(url);
		return this.load(name);
	}
	
	/*
	/**
	 * Load the resource.
	 * @param url The <code>URL</code> to the file.
	 * @param name The <code>String</code> to set the result to.
	 * @return The <code>T</code> loaded data structure.
	 * @throws IOException If reading is interrupted.
	 */
	public T load(URL url, String name, RandomPerson rp) throws IOException {
		this.setupReader(url);
		return this.load(name,rp);
	}
	
	/**
	 * Load the data with given name.
	 * @param name The <code>String</code> to set the result to.
	 * @return The <code>T</code> loaded data structure.
	 * @throws IOException If reading is interrupted.
	 */
	protected abstract T load(String name) throws IOException;
	
	/**
	 * Load the data with given name.
	 * @param name The <code>String</code> to set the result to.
	 * @return The <code>T</code> loaded data structure.
	 * @throws IOException If reading is interrupted.
	 */
	protected abstract T load(String name, RandomPerson rp) throws IOException;
	
	/**
	 * Setup the reader for reading.
	 * @param url The <code>URL</code> of the file.
	 * @throws IOException If reading is interrupted.
	 */
	private void setupReader(URL url) throws IOException {
		InputStreamReader streamReader = new InputStreamReader(url.openStream());
		this.reader = new StreamTokenizer(streamReader);
		this.reader.quoteChar('"');
		this.reader.ordinaryChar('{');
		this.reader.ordinaryChar('}');
		this.reader.ordinaryChar('(');
		this.reader.ordinaryChar(')');
		this.reader.parseNumbers();
		this.reader.slashSlashComments(true);
		this.reader.eolIsSignificant(true);
	}

	/**
	 * Read in a three-dimensional vector.
	 * @return The read in <code>Vector3f</code> instance.
	 * @throws IOException Thrown when errors occurred during file reading.
	 */
	protected Vector3f readVector() throws IOException {
		float[] values = new float[3];
		for(int i = 0; i < 3; i++) {
			while(this.reader.ttype != StreamTokenizer.TT_NUMBER) {
				this.reader.nextToken();
			}
			values[i] = (float)this.reader.nval;
			this.reader.nextToken();
		}
		return new Vector3f(values[0], values[1], values[2]);
	}
	
	/**
	 * Clean up the importer.
	 */
	public abstract void cleanup();
}
