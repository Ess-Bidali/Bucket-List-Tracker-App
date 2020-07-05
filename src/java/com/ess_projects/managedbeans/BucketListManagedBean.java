
package com.ess_projects.managedbeans;

import com.ess_projects.model.BucketListActivity;
import com.ess_projects.model.Country;
import com.ess_projects.model.DATABASE_CONNECTION;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@ManagedBean
@RequestScoped
public class BucketListManagedBean {
    //Object variables
    public DATABASE_CONNECTION connectionSupplier  = new DATABASE_CONNECTION();
    private Country searchedCountry = new Country();
    private BucketListActivity newActivity = new BucketListActivity();

    //Generated instance variables
    private List<BucketListActivity> bucketList = new ArrayList<>();
    private int incomplete;
    private int complete;
    
    
    private final Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    private String queryString;
    
    //xhtml page strings
    private final String indexPage = "index.xhtml?faces-redirect=true";
    private final String createPage = "createNewActivity.xhtml?faces-redirect=true";
    private final String editPage = "editActivity.xhtml?faces-redirect=true";
    
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
    
    
    //Constructor
    public BucketListManagedBean() {
       
    }

    public List<BucketListActivity> getBucketList() {
        return bucketList;
    }   

    public void setBucketList(List<BucketListActivity> bucketList) {
        this.bucketList = bucketList;
    }  

    public Country getSearchedCountry() {
        return searchedCountry;
    }

    public void setSearchedCountry(Country searchedCountry) {
        this.searchedCountry = searchedCountry;
    }
        
    public BucketListActivity getNewActivity() {
        return newActivity;
    }

    public void setNewActivity(BucketListActivity newActivity) {
        this.newActivity = newActivity;
    }  

    public int getIncomplete() {
        return incomplete;
    }

    public int getComplete() {
        return complete;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
   
    //Get all activities and append to bucketlist
    public List<BucketListActivity> getAllBucketListActivities(){
        bucketList.clear();
        incomplete = 0;
        complete = 0;
        try (Connection conn = connectionSupplier.get_connection();
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
                
                if (activity.isCompleted()){
                    complete++;
                }else{
                    incomplete++;
                }
			}
            //Close resources
            preparedStatement.close();
            conn.close();
            
        } catch (SQLException e) {
                printSQLException(e);
        }
        
        return bucketList;
    }
    
    public String createBucketListActivity(BucketListActivity activity) {
        int result = 0;
        try (Connection conn = connectionSupplier.get_connection();
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
        if(result !=0){
            return indexPage;  
        }
            return createPage; 
    }
    
    public String fetchActivityToUpdate(int activityId){
       
        try (Connection conn = connectionSupplier.get_connection();
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ACTIVITY_BY_ID);) {
            preparedStatement.setInt(1, activityId);
            System.out.println(preparedStatement);
            //Create new object to hold the data
            try ( // Execute the query
                    ResultSet rs = preparedStatement.executeQuery()) {
                //Create new object to hold the data
                BucketListActivity activity = new BucketListActivity();
                // Process the ResultSet object.
                while (rs.next()) {
                    activity.setId(rs.getInt("id"));
                    activity.setTitle(rs.getString("title"));
                    activity.setLocation(rs.getString("location"));
                    activity.setEstimatedBudget(rs.getBigDecimal("budget"));
                    activity.setDetails(rs.getString("details"));
                    activity.setCompleted(rs.getBoolean("completed"));
                }
                //Set data to this session
                sessionMap.put("existingActivity", activity);
                //Close resources
            }
            preparedStatement.close();            
            conn.close();
            
        } catch (SQLException e) {
                printSQLException(e);
        }
        //Redirect to edit page
        return editPage;  
    }

    public String updateBucketListActivity(BucketListActivity activity) {
        
        try (Connection conn = connectionSupplier.get_connection();
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ACTIVITY_SQL);) {
            preparedStatement.setString(1, activity.getTitle());
            preparedStatement.setString(2, activity.getLocation());
            preparedStatement.setBigDecimal(3, activity.getEstimatedBudget());
            preparedStatement.setString(4, activity.getDetails());
            preparedStatement.setBoolean(5, activity.isCompleted());
            preparedStatement.setInt(6, activity.getId());
            preparedStatement.executeUpdate();
                        
            //Execute query
            preparedStatement.executeUpdate();
            
            //Close resources
            preparedStatement.close();  
            conn.close();
            
        } catch (SQLException e) {
            printSQLException(e);
        }
        
        //Redirect to index page
        return indexPage;
    }
   

    public void deleteBucketListActivity(BucketListActivity activity) {
        
        try (Connection conn = connectionSupplier.get_connection();
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
    
    public String setUpCreateActivityPage(){
        BucketListActivity activity = new BucketListActivity();
        //Set data to this session
        sessionMap.put("existingActivity", activity); 
        //Redirect to edit page
        return createPage;
    }
   
    public void getCountryData() throws MalformedURLException, IOException{
        String countryName = "Kenya";
        URL urlForGetRequest = new URL("https://restcountries.eu/rest/v2/name/"+ countryName);
        String readLine = "";
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
//        conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
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
                JSONArray jsonarr_1 = (JSONArray) parse.parse(readLine);
                
                
            } catch (ParseException ex) {
                Logger.getLogger(BucketListManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
           
    } else {
        System.out.println("GET NOT WORKED");
    }

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
