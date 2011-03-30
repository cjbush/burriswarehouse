package code.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHandler {
	
	private static String host = "joseph.cedarville.edu";
	private static String database = "vwburr";
	private static String userName = "warehouse";
	private static String password = "vwburr15";
	private static int port = 3306;
	private static String url = "jdbc:mysql://"+host+":"+port+"/"+database;	
	private static Connection con;
	
	static{
		try {
			con = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ResultSet execute(String query) throws SQLException{
		try{
			return con.createStatement().executeQuery(query);
		}
		catch(SQLException e){
			con.createStatement().executeUpdate(query);
			return null;
		}
	}
	
	public static void close(){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static ResultSet executeQuery(String query) throws SQLException{
		return con.createStatement().executeQuery(query);		
	}
	
	public static void executeUpdate(String query) throws SQLException{
		con.createStatement().executeUpdate(query);
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
		ResultSet rs = executeQuery("SELECT " + column + " FROM " + table + " WHERE outWorkID=" + id + ";");
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
		ResultSet rs = executeQuery("SELECT COUNT(id) FROM " + table + ";");
		rs.next();
		return rs.getInt(1);
	}
}
