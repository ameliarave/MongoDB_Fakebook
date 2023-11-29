import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;

import javax.naming.spi.DirStateFactory.Result;

//json.simple 1.1
// import org.json.simple.JSONObject;
// import org.json.simple.JSONArray;

// Alternate implementation of JSON modules.
import org.json.JSONObject;
import org.json.JSONArray;

public class GetData{
	
    static String prefix = "project3.";
	
    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;
	
    // You must refer to the following variables for the corresponding 
    // tables in your database

    String cityTableName = null;
    String userTableName = null;
    String friendsTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;
    String programTableName = null;
    String educationTableName = null;
    String eventTableName = null;
    String participantTableName = null;
    String albumTableName = null;
    String photoTableName = null;
    String coverPhotoTableName = null;
    String tagTableName = null;

    // This is the data structure to store all users' information
    // DO NOT change the name
    JSONArray users_info = new JSONArray();		// declare a new JSONArray

	
    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
	super();
	String dataType = u;
	oracleConnection = c;
	// You will use the following tables in your Java code
	cityTableName = prefix+dataType+"_CITIES";
	userTableName = prefix+dataType+"_USERS";
	friendsTableName = prefix+dataType+"_FRIENDS";
	currentCityTableName = prefix+dataType+"_USER_CURRENT_CITIES";
	hometownCityTableName = prefix+dataType+"_USER_HOMETOWN_CITIES";
	programTableName = prefix+dataType+"_PROGRAMS";
	educationTableName = prefix+dataType+"_EDUCATION";
	eventTableName = prefix+dataType+"_USER_EVENTS";
	albumTableName = prefix+dataType+"_ALBUMS";
	photoTableName = prefix+dataType+"_PHOTOS";
	tagTableName = prefix+dataType+"_TAGS";
    }
	
    
    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException{ 

    	JSONArray users_info = new JSONArray();
				
        int count = 0;
    	try(Statement user_stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){     // ResultSet variables set to be same as in FakebookOracleConstants.java
            ResultSet user_rst = user_stmt.executeQuery("SELECT * FROM " + userTableName);          // select all users
            while(user_rst.next()){
                count++;                                                                 // iterate each user in table
                JSONObject user_x = new JSONObject();
                user_x.put("user_id", user_rst.getInt(1));                  // add name, d.o.b., user_id, and gender to user_x's json object
                user_x.put("first_name", user_rst.getString(2));
                user_x.put("last_name", user_rst.getString(3));
                user_x.put("YOB", user_rst.getInt(4));
                user_x.put("MOB", user_rst.getInt(5));
                user_x.put("DOB", user_rst.getInt(6));
                user_x.put("gender", user_rst.getString(7));                
                // "user_x": {"user_id": _ , "first_name": _ , "last_name": _ , "YOB": _ , "MOB": _ , "DOB": _ , "gender": _ } so far..

                int home_city_id = 0; 
                try(Statement home_id_stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
                    ResultSet home_id_rst = home_id_stmt.executeQuery("SELECT * FROM " + hometownCityTableName + " WHERE USER_ID = " + user_x.getInt("user_id"));
                                                                                                    // select this user's hometown_city record
                    while(home_id_rst.next()){                                                      // iterate record (only one per user)
                        home_city_id = home_id_rst.getInt(2);                           // save user's home_city_id for index into cityTableName
                    }
                }
                
                try(Statement home_stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
                    ResultSet home_city_rst = home_stmt.executeQuery("SELECT * FROM " + cityTableName + " WHERE CITY_ID = " + home_city_id);
                    JSONObject home_city_info = new JSONObject();                                   // select this city's record (only one)
                    while(home_city_rst.next()){
                        home_city_info.put("city", home_city_rst.getString(2));         // add city_name to home_city json object
                        home_city_info.put("state", home_city_rst.getString(3));        // add state_name 
                        home_city_info.put("country", home_city_rst.getString(4));      // add country_name 
                        user_x.put("hometown", home_city_info);                                     // add hometown city object to user_x's json object
                    }
                }

                int curr_city_id = 0;
                try(Statement curr_city_id_stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
                    ResultSet curr_city_id_rst = curr_city_id_stmt.executeQuery("SELECT * FROM " + currentCityTableName + " WHERE USER_ID = " + user_x.getInt("user_id"));
                    while(curr_city_id_rst.next()){                                                 // select this user's current city record
                        curr_city_id = curr_city_id_rst.getInt(2);                      // save user's curr_city_id for index into cityTableName
                    }
                }

                try(Statement curr_city_stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
                    ResultSet curr_city_rst = curr_city_stmt.executeQuery("SELECT * FROM " + cityTableName + " WHERE CITY_ID = " + curr_city_id);
                    JSONObject curr_city_info = new JSONObject();                                   // select this city's record (only one)
                    while(curr_city_rst.next()){
                        curr_city_info.put("city", curr_city_rst.getString(2));         // add city_name to curr_city json object
                        curr_city_info.put("state", curr_city_rst.getString(3));        // add state_name
                        curr_city_info.put("country", curr_city_rst.getString(4));      // add country_name
                        user_x.put("current", curr_city_info);                                      // add current city object to user_x's json object
                    }
                }
                
                JSONArray friends_arr = new JSONArray();
                try(Statement friends_stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
                    ResultSet friends_rst = friends_stmt.executeQuery("SELECT * FROM " + friendsTableName + " WHERE USER1_ID = " + user_x.getInt("user_id"));
                    while(friends_rst.next()){                                                      // select set of friend records for which this user is user1_id
                        friends_arr.put(friends_rst.getInt(2));                         // add user2_id to json array of this user's friends
                    }
                    user_x.put("friends", friends_arr);                                             // add friends array to user_x's json object
                }

                //users_info.put(user_x);                       // DEBUGGING
                //System.out.println(user_x.toString(4));       // DEBUGGING
                ///System.out.println(count);                   // DEBUGGING
                users_info.put(user_x);                                                             // add user_x json object to array of users_info
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }

		
		return users_info;
    }

    // This outputs to a file "output.json"
    public void writeJSON(JSONArray users_info) {
	// DO NOT MODIFY this function
	try {
	    FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
	    file.write(users_info.toString());
	    file.flush();
	    file.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
		
    }
}
