package com.mycompany.property_management.controller;

import com.mycompany.property_management.model.PropertiesDto;
import com.mycompany.property_management.model.PropertyDto;
import com.mycompany.property_management.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Operation(
            summary = "Add a new property",
            description = "Creates a new property record in the database. The user must be authenticated to use this."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Property created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user is not logged in")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    @PostMapping("/addProperty")
    public ResponseEntity<PropertyDto> addProperty(@Valid @RequestBody PropertyDto property) {
        PropertyDto savedProperty = propertyService.createProperty(property);
        return new ResponseEntity<PropertyDto>(savedProperty, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    @PostMapping("/addProperties")
    public ResponseEntity<List<PropertyDto>> addProperties(@Valid @RequestBody List<PropertyDto> properties) {
        List<PropertyDto> savedProperties = propertyService.createProperties(properties);
        return new ResponseEntity<List<PropertyDto>>(savedProperties, HttpStatus.CREATED);
    }

    @GetMapping("/getProperties")
    public ResponseEntity<List<PropertyDto>> getProperties() {
        List<PropertyDto> allPropertiesDto = propertyService.getAllProperties();
        return new ResponseEntity<List<PropertyDto>>(allPropertiesDto, HttpStatus.OK);
    }

    @GetMapping("/getProperties/users/{userId}")
    public ResponseEntity<List<PropertyDto>> getPropertiesUsers(@PathVariable("userId") String userId) {
        List<PropertyDto> allPropertiesDto = propertyService.getAllPropertiesUsers(userId);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateProperties/{id}")
    public ResponseEntity<PropertyDto> updateProperties (@Valid @RequestBody PropertyDto propertyDto, @PathVariable("id") Long id){
        PropertyDto updatedProperty = propertyService.updateProperty(propertyDto,id);
        return new ResponseEntity<PropertyDto>(updatedProperty, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
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
    public ResponseEntity<PropertiesDto> searchByPriceRange (@Parameter(description = "The minimum price of the property", example = "30000")
                                                                 @RequestParam("minPrice") Double minPrice,
                                                             @Parameter(description = "The maximum price of the property", example = "100000")
                                                             @RequestParam("maxPrice") Double maxPrice){
        PropertiesDto propertiesDto = propertyService.searchByPriceRange(minPrice,maxPrice);
        return new ResponseEntity<>(propertiesDto, HttpStatus.OK);
    }
}