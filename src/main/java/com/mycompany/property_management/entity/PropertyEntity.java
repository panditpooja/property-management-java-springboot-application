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

    @Column(name = "Price", length = 20, nullable = false)
    private Double price;

    @Column(name = "Address", length = 20, nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY) //It will not fetch the user data while fetching the properties
//    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity userEntity;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
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
}
