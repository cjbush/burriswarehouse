package code.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseHandler {
	
	private String host;
	private String database;
	private String userName;
	private String password;
	private String table;
	private String url;
	private Connection con;
	private Statement stmt;
	private int port;
		
	public DatabaseHandler(String host, String database, String userName, String password) {
		this.host = host;
		this.database = database;
		this.userName = userName;
		this.password = password;
		this.port = 3306;
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

        //this way grabs the whole row and then returns the column...seems less efficient
//		ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE id=" + id + ";");
//    	rs.next();
//      return rs.getString(column);
	}
	
	public ResultSet executeQuery(String query) throws SQLException{
		ResultSet rs = con.createStatement().executeQuery(query);
		//rs.next();
		return rs;
	}
	
	/**
	 * Returns an ArrayList of results from a MySQL table - ONLY to be used where outWorkID is the value being
	 * searched on.
	 * @param table
	 * @param id
	 * @param column
	 * @return
	 * @throws SQLException
	 */
	//this could be easily modified to search on any value
	public ArrayList<String> getSet(String table, int id, String column) throws SQLException {
		ArrayList<String> returnArray = new ArrayList<String>();
		ResultSet rs = stmt.executeQuery("SELECT " + column + " FROM " + table + " WHERE outWorkID=" + id + ";");
		while (rs.next()) {
			returnArray.add(rs.getString(1));
		}
		return returnArray;
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
}
