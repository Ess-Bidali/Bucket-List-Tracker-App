package com.ess_projects.model;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String flagUrl;
    private String name;
    private String capital;
    private String region;
    private List<String> languages;
    private List<String> currencies;
    private List<String> nameTranslations;
    private long population;
    private double totalArea;
    private String acronym;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public List<String> getNameTranslations() {
        return nameTranslations;
    }

    public void setNameTranslations(List<String> nameTranslations) {
        this.nameTranslations = nameTranslations;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(double totalArea) {
        this.totalArea = totalArea;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
    
    @Override
    public String toString(){
        return "Country: " + name
                + " Capital: "+ capital
                + " Region, " + region;
    }
    
    

}
