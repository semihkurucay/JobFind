package com.semihkurucay.mapper;

import com.semihkurucay.dto.DtoAddress;
import com.semihkurucay.dto.DtoAddressIU;
import com.semihkurucay.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    public DtoAddress toDtoAddress(Address address);
    public Address toUpdated(DtoAddressIU dtoAddressIU, @MappingTarget Address address);
}
