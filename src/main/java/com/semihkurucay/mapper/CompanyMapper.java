package com.semihkurucay.mapper;

import com.semihkurucay.dto.*;
import com.semihkurucay.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, JobPostingMapper.class, CompanyCommentMapper.class})
public interface CompanyMapper {

    Company toEntity(DtoCompanyIU company);
    DtoAuthCompany toDtoAuthCompany(Company company);
    DtoPublicCompanyInfo toDtoPublicInfo(Company company);
    DtoCompany toDtoCompany(Company company);
    DtoCompanyJobPostingUser toDtoCompanyJobPostingUser(Company company);
    Company toUpdated(DtoCompanyIU dtoCompanyIU, @MappingTarget Company company);
}
