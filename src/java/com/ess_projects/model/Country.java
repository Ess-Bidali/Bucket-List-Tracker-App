package com.ess_projects.model;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String flagUrl;
    private String name;
    private String capital;
    private List<String> languages;
    private List<String> currencies;
    private List<String> nameTranslations;
    private String population;
    private String totalArea;
    private String acronym;

   
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

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(String totalArea) {
        this.totalArea = totalArea;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
    
    

}
