package com.mycompany.property_management.converter;

import com.mycompany.property_management.entity.PropertyEntity;
import com.mycompany.property_management.model.PropertyDto;
import org.springframework.stereotype.Component;

@Component
public class PropertyConverter {

    public PropertyEntity convertModelToEntity(PropertyDto property){
        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setTitle(property.getTitle());
        propertyEntity.setDescription(property.getDescription());
        propertyEntity.setPrice(property.getPrice());
        propertyEntity.setAddress(property.getAddress());

        return propertyEntity;

    }

    public PropertyDto convertEntityToModel(PropertyEntity propertyEntity){
        PropertyDto propertyDto = new PropertyDto();
        propertyDto.setTitle(propertyEntity.getTitle());
        propertyDto.setDescription(propertyEntity.getDescription());
        propertyDto.setPrice(propertyEntity.getPrice());
        propertyDto.setAddress(propertyEntity.getAddress());
        propertyDto.setId(propertyEntity.getId());
        return propertyDto;

    }
}
