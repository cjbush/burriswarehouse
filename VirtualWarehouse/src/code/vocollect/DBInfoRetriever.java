package code.vocollect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import code.util.ConfigurationManager;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * Added a couple functionality changes
 * 
 */

public class DBInfoRetriever {

	private static String host;
	private static String database;
	private static String userName;
	private static String password;
	private static int port;
	
	private String url;
	private Connection con;
	private Statement stmt;
	private HashMap<String, String> data;
	
	public static void setParameters(){
		host= ConfigurationManager.get("server");
		database = ConfigurationManager.get("vocollectdb");
		userName = ConfigurationManager.get("mysqluser");
		password = ConfigurationManager.get("mysqlpass");
		port = Integer.parseInt(ConfigurationManager.get("vocollectdbport"));
	}
	
	public DBInfoRetriever(){
		setParameters();
		this.data = new HashMap<String, String>();
		try {
			this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
		    Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		    this.con = DriverManager.getConnection (url, userName, password);
		    System.out.println ("Database connection established");
		    this.stmt = con.createStatement();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
				
		}
		try {
			loadHashMap();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
			
	public DBInfoRetriever(String host, String database, String userName, String password, Integer port) {
		DBInfoRetriever.host = host;
		DBInfoRetriever.database = database;
		DBInfoRetriever.userName = userName;
		DBInfoRetriever.password = password;
		DBInfoRetriever.port = port;
		this.data = new HashMap<String, String>();
		try {
			this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
		    Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		    this.con = DriverManager.getConnection (url, userName, password);
		    System.out.println ("Database connection established");
		    this.stmt = con.createStatement();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
				
		}
		try {
			loadHashMap();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a check digit from the database for a given bin number, or returns a random digit if
	 * bin does not exist in database.
	 * @param binNumber Bin number in format 1A23400, where 1A is the aisle and 234 is the bin number 
	 * @return
	 * @throws SQLException
	 */
	public String getCheckDigit (String binNumber) {

//		//strip last two off bin number
//		binNumber = binNumber.substring(0,5);
		
		//add to bin number
		binNumber += "00";
		
		//if we have this bin, return that check digit
	    if (data.containsKey(binNumber)) {
	    	return (String) data.get(binNumber);
	    } 
	    //if not, return a random check digit
	    else {
			Random generator = new Random();
			Integer randomCheckDigits = generator.nextInt(900) + 100; //random int between 100 and 999
			return randomCheckDigits.toString();
	    }  
	}
	
	/**
	 * Returns true if the specified bin has a pick job associated with it.
	 * @param binNumber
	 * @return
	 */
	public boolean getIsPossiblePickJob(String binNumber) {
		if (binNumber == null)
		{
			return false;
		}
		
		binNumber += "00";
		if (data.containsKey(binNumber)) {
	    	return true;
	    }
		else {
			return false;
		}
	}
		
	/**
	 * Returns a single result from a MySQL table
	 * @param table
	 * @param id
	 * @param column
	 * @return
	 * @throws SQLException
	 */
	public String getResult(String table, int id, String column) throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT " + column + " FROM " + table + " WHERE id=" + id + ";");
	   	rs.next();
	    return rs.getString(1);
	}

	/**
	 * Returns the number of rows in a given table
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public int getSize(String table) throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT COUNT(id) FROM " + table + ";");
		rs.next();
		return rs.getInt(1);
	}
	
	private void loadHashMap() throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT outLocID,outChkDigits FROM LUTGetPicks;");
		while (rs.next()) {
			data.put(rs.getString(1),rs.getString(2));
		}
	}

	
	public static void main(String[] args) throws SQLException {
		ConfigurationManager.read("server.cfg");
		DBInfoRetriever test = new DBInfoRetriever();
		System.out.println(test.getCheckDigit("1C021"));
	}
}
