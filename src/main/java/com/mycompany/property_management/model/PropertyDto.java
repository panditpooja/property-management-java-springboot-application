package com.mycompany.property_management.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PropertyDto {

    private Long id;
    
    @NotNull(message = "Property title is required")
    @NotEmpty(message = "Property title cannot be empty")
    @Size(max = 20, message = "Property title must be at most 20 characters")
    private String title;
    
    @Size(max = 40, message = "Property description must be at most 40 characters")
    private String description;
    
    @NotNull(message = "Property price is required")
    private Double price;
    
    @NotNull(message = "Property address is required")
    @NotEmpty(message = "Property address cannot be empty")
    private String address;
    
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
