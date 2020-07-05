

package com.ess_projects.model;

import java.math.BigDecimal;

public class BucketListActivity {
    private int id;
    private String title;
    private String location;
    private BigDecimal estimatedBudget; //Up to 1 billion
    private String details;
    private boolean completed; 

    public BucketListActivity(int id, String title, String location, BigDecimal estimatedBudget, String details, boolean completed) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.estimatedBudget = estimatedBudget;
        this.details = details;
        this.completed = completed;
    }

    public BucketListActivity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(BigDecimal estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
