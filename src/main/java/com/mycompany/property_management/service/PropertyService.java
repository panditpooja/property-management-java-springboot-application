package com.mycompany.property_management.service;

import com.mycompany.property_management.converter.PropertyConverter;
import com.mycompany.property_management.entity.PropertyEntity;
import com.mycompany.property_management.model.PropertiesDto;
import com.mycompany.property_management.model.PropertyDto;
import com.mycompany.property_management.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private PropertyConverter propertyConverter;

    public PropertyDto createProperty(PropertyDto property){

        PropertyEntity propertyEntity = propertyConverter.convertModelToEntity(property);
        propertyRepo.save(propertyEntity);
        property.setId(propertyEntity.getId());
        return property;

    }

    public List<PropertyDto> createProperties(List<PropertyDto> properties){
        List<PropertyEntity> entities = new ArrayList<>();
        PropertiesDto allPropertiesDto = new PropertiesDto();
        for (PropertyDto property: properties){
            PropertyEntity entity = propertyConverter.convertModelToEntity(property);
            entities.add(entity);
        }
        propertyRepo.saveAll(entities);
        entities = propertyRepo.findAll();
        for (PropertyEntity entity : entities){
            PropertyDto property = propertyConverter.convertEntityToModel(entity);
            allPropertiesDto.getProperties().add(property);
        }
        return allPropertiesDto.getProperties();
    }

    public List<PropertyDto> getAllProperties(){
        PropertiesDto allPropertiesDto = new PropertiesDto();
        List<PropertyEntity> entities = propertyRepo.findAll();
        for (PropertyEntity entity : entities){
            PropertyDto property = propertyConverter.convertEntityToModel(entity);
            allPropertiesDto.getProperties().add(property);
        }
        return allPropertiesDto.getProperties();
    }

    public PropertyDto getPropertyById(Long id){
        Optional<PropertyEntity> propertyEntity = null;
        try {
            propertyEntity = propertyRepo.findById(id);
            Thread.sleep((long)(Math.random()*1000));
            PropertyDto property = propertyConverter.convertEntityToModel(propertyEntity.get());
            return property;

        } catch (Exception e) {
            return null;
        }

    }

    public PropertyDto updateProperty(PropertyDto property, Long id){
        Optional<PropertyEntity> entity = null;
        try{
           entity = propertyRepo.findById(id);
           PropertyEntity newEntity = entity.get();
           newEntity.setTitle(property.getTitle());
           newEntity.setDescription(property.getDescription());
           newEntity.setPrice(property.getPrice());
           newEntity.setAddress(property.getAddress());
           propertyRepo.save(newEntity);
           PropertyDto newProperty = propertyConverter.convertEntityToModel(newEntity);
           return newProperty;

        }catch(Exception e){
            return null;
        }
    }

    public void deleteProperty(Long id){
        propertyRepo.deleteById(id);
    }

    public PropertiesDto searchByTitle(String title){
        PropertiesDto propertiesDto = new PropertiesDto();
        List<PropertyEntity> entityList =  propertyRepo.findByTitleContaining(title);
        for (PropertyEntity propertyEntity : entityList){
            PropertyDto property = propertyConverter.convertEntityToModel(propertyEntity);
            propertiesDto.getProperties().add(property);
        }
        return propertiesDto;
    }

    public PropertiesDto searchByPriceRange(Double minPrice, Double maxPrice){
        PropertiesDto propertiesDto = new PropertiesDto();
        List<PropertyEntity> entityList =  propertyRepo.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice);
        for (PropertyEntity propertyEntity : entityList){
            PropertyDto property = propertyConverter.convertEntityToModel(propertyEntity);
            propertiesDto.getProperties().add(property);
        }
        return propertiesDto;
    }

}
