package com.mycompany.property_management.entity;

import jakarta.persistence.*;

@Entity
@Table (name = "Address_Table")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "House_Number")
    private String HouseNo;

    @Column(name ="Street")
    private String street;

    @Column(name = "Postal_Code")
    private String postalCode;

    @Column(name = "Country")
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity userEntity;

    public Long getId() {
        return id;
    }

    public String getHouseNo() {
        return HouseNo;
    }

    public void setHouseNo(String houseNo) {
        HouseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
