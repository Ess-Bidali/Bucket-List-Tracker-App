
package com.ess_projects.managedbeans;

import com.ess_projects.model.BucketListActivity;
import com.ess_projects.model.Country;
import com.ess_projects.model.DATABASE_CONNECTION;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;



@ManagedBean
@RequestScoped
public class BucketListManagedBean {
    //Object variables
    public DATABASE_CONNECTION dataSupplier  = new DATABASE_CONNECTION();
    private Country searchedCountry = new Country();
    private BucketListActivity newActivity = new BucketListActivity();

    //Generated instance variables
    private List<BucketListActivity> bucketList = new ArrayList<>();
    private int incomplete;
    private int complete;
    private String queryString;
    
    private final Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    
    //xhtml page strings
    private final String indexPage = "index.xhtml?faces-redirect=true";
    private final String createPage = "createNewActivity.xhtml?faces-redirect=true";
    private final String editPage = "editActivity.xhtml?faces-redirect=true";
    
    
    
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
        bucketList = dataSupplier.getAllBucketListActivities();
        incomplete = 0;
        complete = 0;
        for(BucketListActivity activity : bucketList){
            if(activity.isCompleted()){
                complete++;
            }else{
                incomplete++;
            }
        }
        
        return bucketList;
    }
    
    public String createBucketListActivity(BucketListActivity activity) {
        boolean result = dataSupplier.createBucketListActivity(activity);
        
        return indexPage;
    }
    
    public String fetchActivityToUpdate(int activityId){
        //Get data and Set data to this session
        BucketListActivity activity = dataSupplier.fetchActivityToUpdate(activityId);
        sessionMap.put("existingActivity", activity);
        
        //Redirect to edit page
        return editPage;  
    }

    public String updateBucketListActivity(BucketListActivity activity) {
        
        boolean result = dataSupplier.updateBucketListActivity(activity);
        
        return indexPage;
   
    }
   

    public void deleteBucketListActivity(BucketListActivity activity) {
        dataSupplier.deleteBucketListActivity(activity);
        
    }
    
    public String setUpCreateActivityPage(){
        BucketListActivity activity = new BucketListActivity();
        //Set data to this session
        sessionMap.put("existingActivity", activity); 
        //Redirect to edit page
        return createPage;
    }
   
    public void getCountryData() throws MalformedURLException, IOException{
        dataSupplier.getCountryData();
    }
    
}
