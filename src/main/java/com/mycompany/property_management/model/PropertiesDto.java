package com.mycompany.property_management.model;

import java.util.ArrayList;
import java.util.List;

public class PropertiesDto{
    private List<PropertyDto> allPropertiesList;

    public PropertiesDto(){
        this.allPropertiesList = new ArrayList<>();
    }

    public List<PropertyDto> getProperties() {
        return allPropertiesList;
    }

    public void setProperties(List<PropertyDto> allPropertiesList) {
        this.allPropertiesList = allPropertiesList;
    }
}
