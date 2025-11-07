package com.mycompany.property_management.converter;

import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.model.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserEntity convertModelToEntity(UserDto user){
        UserEntity userEntity = new UserEntity();
        userEntity.setOwnerName(user.getOwnerName());
        userEntity.setOwnerEmail(user.getOwnerEmail());
        userEntity.setPhone(user.getPhone());
        userEntity.setPassword(user.getPassword());

        return userEntity;

    }

    public UserDto convertEntityToModel(UserEntity userEntity){
        UserDto user = new UserDto();
        user.setId(userEntity.getId());
        user.setOwnerName(userEntity.getOwnerName());
        user.setOwnerEmail(userEntity.getOwnerEmail());
        user.setPhone(userEntity.getPhone());
//        user.setPassword(userEntity.getPassword());
        return user;

    }
}
