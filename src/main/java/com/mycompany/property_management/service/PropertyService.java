package com.mycompany.property_management.service;

import com.mycompany.property_management.converter.PropertyConverter;
import com.mycompany.property_management.entity.PropertyEntity;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.exception.BusinessException;
import com.mycompany.property_management.exception.ErrorModel;
import com.mycompany.property_management.model.PropertiesDto;
import com.mycompany.property_management.model.PropertyDto;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepo;

//    Helper methods
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // Returns the email (username)
        }
        return null;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    private boolean hasPropertyOwnerRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_PROPERTY_OWNER"));
        }
        return false;
    }

    public PropertyDto createProperty(PropertyDto property){
        String currentUserEmail = getCurrentUserEmail();
        Optional<UserEntity> userEntity;

        // Admin can create properties for any user, Property Owner can only create for themselves
        if (isAdmin()) {
            userEntity = userRepo.findById(property.getUserId());
        } else if (hasPropertyOwnerRole()) {
            // Property Owner must use their own user ID
            Optional<UserEntity> currentUser = userRepo.findByOwnerEmail(currentUserEmail);
            if (currentUser.isEmpty() || !currentUser.get().getId().equals(property.getUserId())) {
                List<ErrorModel> errors = new ArrayList<>();
                ErrorModel error = new ErrorModel();
                error.setCode("UNAUTHORIZED");
                error.setMessage("You can only add properties for yourself");
                errors.add(error);
                throw new BusinessException(errors);
            }
            userEntity = userRepo.findById(property.getUserId());
        } else {
            // User role cannot create properties
            List<ErrorModel> errors = new ArrayList<>();
            ErrorModel error = new ErrorModel();
            error.setCode("UNAUTHORIZED");
            error.setMessage("You need PROPERTY_OWNER or ADMIN role to add properties");
            errors.add(error);
            throw new BusinessException(errors);
        }

        if (userEntity.isPresent()){
            PropertyEntity propertyEntity = propertyConverter.convertModelToEntity(property);
            propertyEntity.setUserEntity(userEntity.get());
            propertyEntity = propertyRepo.save(propertyEntity);
            property.setId(propertyEntity.getId());
            return property;
        }
        List<ErrorModel> errors = new ArrayList<>();
        ErrorModel error = new ErrorModel();
        error.setCode("NON_EXISTING_USER_ID");
        error.setMessage("Entered User id is not present.");
        errors.add(error);
        throw new BusinessException(errors);
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

    public List<PropertyDto> getAllPropertiesUsers(String userId){
        String currentUserEmail = getCurrentUserEmail();
        Optional<UserEntity> currentUser = userRepo.findByOwnerEmail(currentUserEmail);

        // Admin can view any user's properties, Property Owner can only view their own
        if (!isAdmin()) {
            if (currentUser.isEmpty() || !currentUser.get().getId().equals(userId)) {
                List<ErrorModel> errors = new ArrayList<>();
                ErrorModel error = new ErrorModel();
                error.setCode("UNAUTHORIZED");
                error.setMessage("You can only view your own properties");
                errors.add(error);
                throw new BusinessException(errors);
            }
        }

        PropertiesDto allPropertiesDto = new PropertiesDto();
        List<PropertyEntity> entities = propertyRepo.findAllByUserEntityId(userId);
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
        Optional<PropertyEntity> propertyOpt = propertyRepo.findById(id);
        if (propertyOpt.isEmpty()) {
            List<ErrorModel> errors = new ArrayList<>();
            ErrorModel error = new ErrorModel();
            error.setCode("PROPERTY_NOT_FOUND");
            error.setMessage("Property not found");
            errors.add(error);
            throw new BusinessException(errors);
        }

        PropertyEntity property = propertyOpt.get();
        String currentUserEmail = getCurrentUserEmail();

        // SecurityConfig already ensures only ADMIN or PROPERTY_OWNER can access this endpoint
        // Here we just need to check: if property owner, they can only delete their own properties
        // Admins can delete any property (no ownership check needed)
        if (hasPropertyOwnerRole() && !property.getUserEntity().getOwnerEmail().equals(currentUserEmail)) {
            // Property Owner trying to delete someone else's property - not allowed
            List<ErrorModel> errors = new ArrayList<>();
            ErrorModel error = new ErrorModel();
            error.setCode("UNAUTHORIZED");
            error.setMessage("You can only delete your own properties.");
            errors.add(error);
            throw new BusinessException(errors);
        }

        // Admin or Property Owner deleting their own property - proceed
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
