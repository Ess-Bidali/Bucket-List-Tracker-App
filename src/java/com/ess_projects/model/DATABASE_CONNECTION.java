
package com.ess_projects.model;

import com.ess_projects.managedbeans.BucketListManagedBean;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class DATABASE_CONNECTION {
    
    private final String jdbcURL = "jdbc:mysql://localhost:3306/bucketlist";
    private final String jdbcUsername = "admin";
    private final String jdbcPassword = "hELLOwORLD1";
    
    //Database strings
    private static final String INSERT_ACTIVITY_SQL = "INSERT INTO bucket_list"
                + "  (title, location, budget, details, completed) VALUES "
			+ " (?, ?, ?, ?, ?);";
    private static final String SELECT_ACTIVITY_BY_ID = "select * from bucket_list where id =?";
    private static final String SELECT_ALL_ACTIVITIES = "select * from bucket_list";
    private static final String DELETE_ACTIVITY_SQL = "delete from bucket_list where id = ?;";
    private static final String UPDATE_ACTIVITY_SQL = "update bucket_list set " 
            + "title = ?, location = ?, budget= ?, details =?, completed=?" 
            + " where id=?;";
    
    
    public Connection get_connection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
        }
        // TODO Auto-generated catch block
        return conn;
    }
    
    //Get all activities and append to bucketlist
    public List<BucketListActivity> getAllBucketListActivities(){
        List<BucketListActivity> bucketList = new ArrayList<>();
        try (Connection conn = get_connection();
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_ACTIVITIES);
            // Execute the query
            ResultSet rs = preparedStatement.executeQuery();
                ){
            
            // Process the ResultSet object.
            while (rs.next()) {
                BucketListActivity activity = new BucketListActivity();
                activity.setId(rs.getInt("id"));
                activity.setTitle(rs.getString("title"));
                activity.setLocation(rs.getString("location"));
                activity.setEstimatedBudget(rs.getBigDecimal("budget"));
                activity.setDetails(rs.getString("details"));
                activity.setCompleted( rs.getBoolean("completed"));
                bucketList.add(activity);
			}
            //Close resources
            preparedStatement.close();
            conn.close();
            
        } catch (SQLException e) {
                printSQLException(e);
        }
        
        return bucketList;
    }
    
    public boolean createBucketListActivity(BucketListActivity activity) {
        int result = 0;
        try (Connection conn = get_connection();
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ACTIVITY_SQL);) {
            preparedStatement.setString(1, activity.getTitle());
            preparedStatement.setString(2, activity.getLocation());
            preparedStatement.setBigDecimal(3, activity.getEstimatedBudget());
            preparedStatement.setString(4, activity.getDetails());
            preparedStatement.setBoolean(5, activity.isCompleted());
            System.out.println(preparedStatement);
            // Execute the query
            result = preparedStatement.executeUpdate();

            //Close resources
            preparedStatement.close();            
            conn.close();
            
        } catch (SQLException e) {
                printSQLException(e);
        }
        return result == 0;
    }
    
    public BucketListActivity fetchActivityToUpdate(int activityId){
        BucketListActivity activity = null;
        try (Connection conn = get_connection();
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ACTIVITY_BY_ID);) {
            preparedStatement.setInt(1, activityId);
            System.out.println(preparedStatement);
            //Create new object to hold the data
            try ( // Execute the query
                    ResultSet rs = preparedStatement.executeQuery()) {
                //Create new object to hold the data
                activity = new BucketListActivity();
                // Process the ResultSet object.
                while (rs.next()) {
                    activity.setId(rs.getInt("id"));
                    activity.setTitle(rs.getString("title"));
                    activity.setLocation(rs.getString("location"));
                    activity.setEstimatedBudget(rs.getBigDecimal("budget"));
                    activity.setDetails(rs.getString("details"));
                    activity.setCompleted(rs.getBoolean("completed"));
                }                
            }
            //Close resources
            preparedStatement.close();            
            conn.close();
            
        } catch (SQLException e) {
                printSQLException(e);
        }
        //Return the new activity
        return activity;  
    }

    public boolean updateBucketListActivity(BucketListActivity activity) {
        int result = 0;
        try (Connection conn = get_connection();
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ACTIVITY_SQL);) {
            preparedStatement.setString(1, activity.getTitle());
            preparedStatement.setString(2, activity.getLocation());
            preparedStatement.setBigDecimal(3, activity.getEstimatedBudget());
            preparedStatement.setString(4, activity.getDetails());
            preparedStatement.setBoolean(5, activity.isCompleted());
            preparedStatement.setInt(6, activity.getId());
            preparedStatement.executeUpdate();
                        
            //Execute query
            result = preparedStatement.executeUpdate();
            
            //Close resources
            preparedStatement.close();  
            conn.close();
            
        } catch (SQLException e) {
            printSQLException(e);
        }
        
        //Redirect to index page
        return result == 0;
    }
   

    public void deleteBucketListActivity(BucketListActivity activity) {
        try (Connection conn = get_connection();
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ACTIVITY_SQL);) {
            preparedStatement.setInt(1, activity.getId());

            //Execute query
            preparedStatement.executeUpdate();

            //Close resources
            preparedStatement.close();
            
            
        } catch (SQLException e) {
            printSQLException(e);
        }
        
    }
   
   
    public Country getCountryData(String countryName) throws MalformedURLException, IOException{
        Country country = new Country();
        
        URL urlForGetRequest = new URL("https://restcountries.eu/rest/v2/name/"+ countryName);
        String readLine = "";
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Scanner sc = new Scanner(urlForGetRequest.openStream());
            while(sc.hasNext())
            {
                readLine += sc.nextLine();
            }
            sc.close();
            JSONParser parse = new JSONParser();
            try {
                //Get array object from data
                JSONArray jsonarr_1 = (JSONArray) parse.parse(readLine);
                //Extract the json object
                JSONObject home = (JSONObject)jsonarr_1.get(0);
                //Get details
                String flagUrl = (String) home.get("flag");
                String name = (String) home.get("name");
                String capital = (String) home.get("capital");
                String region = (String) home.get("region");
                long population = (Long) home.get("population");
                double totalArea = (Double) home.get("area");
                
                country.setFlagUrl(flagUrl);
                country.setName(name);
                country.setCapital(capital);
                country.setRegion(region);
                country.setPopulation(population);
                country.setTotalArea(totalArea);
                
                
            } catch (ParseException ex) {
                Logger.getLogger(BucketListManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
           
    } else {
        System.out.println("GET NOT WORKED");
    }
        return country;
    }
    
    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
                if (e instanceof SQLException) {
                        e.printStackTrace(System.err);
                        System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                        System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                        System.err.println("Message: " + e.getMessage());
                        Throwable t = ex.getCause();
                        while (t != null) {
                                System.out.println("Cause: " + t);
                                t = t.getCause();
                        }
                }
        }
    }
    
    

}
