package com.mycompany.property_management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Property_Table")
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Title", length = 20, nullable = false)
    private String title;

    @Column(name = "Description", length = 40, nullable = true)
    private String description;

    @Column(name = "Owner_Name", length = 20, nullable = false)
    private String ownerName;

    @Column(name = "Owner_Email", length = 20, nullable = false)
    private String ownerEmail;

    @Column(name = "Price", length = 20, nullable = false)
    private Double price;

    @Column(name = "Address", length = 20, nullable = false)
    private String address;

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
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
}
