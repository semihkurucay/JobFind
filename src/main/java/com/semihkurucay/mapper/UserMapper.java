package com.semihkurucay.mapper;


import com.semihkurucay.dto.DtoAuthUser;
import com.semihkurucay.dto.DtoUser;
import com.semihkurucay.dto.DtoUserIU;
import com.semihkurucay.dto.DtoUserJobApplication;
import com.semihkurucay.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserMapper {

    public User toUpdated(DtoUserIU dtoUserIU, @MappingTarget User user);
    public DtoAuthUser toDtoAuthUser(User user);
    @Mapping(source = "login.username", target = "username")
    public DtoUser  toDtoUser(User user);
    public DtoUserJobApplication toDtoUserJobApplication(User user);
}
