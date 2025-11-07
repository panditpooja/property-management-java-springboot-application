package com.mycompany.property_management.controller;

import com.mycompany.property_management.model.PropertiesDto;
import com.mycompany.property_management.model.PropertyDto;
import com.mycompany.property_management.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/addProperty")
    public ResponseEntity<PropertyDto> addProperty(@RequestBody PropertyDto property) {
        PropertyDto savedProperty = propertyService.createProperty(property);
        return new ResponseEntity<PropertyDto>(savedProperty, HttpStatus.CREATED);
    }

    @PostMapping("/addProperties")
    public ResponseEntity<List<PropertyDto>> addProperties(@RequestBody List<PropertyDto> properties) {
        List<PropertyDto> savedProperties = propertyService.createProperties(properties);
        return new ResponseEntity<List<PropertyDto>>(savedProperties, HttpStatus.CREATED);
    }

    @GetMapping("/getProperties")
    public ResponseEntity<List<PropertyDto>> getProperties() {
        List<PropertyDto> allPropertiesDto = propertyService.getAllProperties();
        return new ResponseEntity<List<PropertyDto>>(allPropertiesDto, HttpStatus.OK);
    }

    @GetMapping("/getProperty/{id}")
    public ResponseEntity<PropertyDto> getProperty(@PathVariable("id") Long id) {
        PropertyDto property = propertyService.getPropertyById(id);
        if (property!=null) {
            return new ResponseEntity<PropertyDto>(property, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateProperties/{id}")
    public ResponseEntity<PropertyDto> updateProperties (@RequestBody PropertyDto propertyDto, @PathVariable("id") Long id){
        PropertyDto updatedProperty = propertyService.updateProperty(propertyDto,id);
        return new ResponseEntity<PropertyDto>(updatedProperty, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProperty/{id}")
    public ResponseEntity<Void> deleteProperty (@PathVariable("id") Long id){
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/search", params = {"title"})
    public ResponseEntity<PropertiesDto> searchByTitle (@RequestParam("title") String title){
        PropertiesDto propertiesDto = propertyService.searchByTitle(title);
        return new ResponseEntity<>(propertiesDto, HttpStatus.OK);
    }

    @GetMapping(path = "/search", params = {"minPrice", "maxPrice"})
    public ResponseEntity<PropertiesDto> searchByPriceRange (@RequestParam("minPrice") Double minPrice, @RequestParam("maxPrice") Double maxPrice){
        PropertiesDto propertiesDto = propertyService.searchByPriceRange(minPrice,maxPrice);
        return new ResponseEntity<>(propertiesDto, HttpStatus.OK);
    }
}