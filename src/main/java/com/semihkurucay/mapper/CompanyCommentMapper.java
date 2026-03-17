package com.semihkurucay.mapper;

import com.semihkurucay.dto.DtoCompanyCommentIU;
import com.semihkurucay.dto.DtoPublicCompanyComment;
import com.semihkurucay.entity.CompanyComment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",  uses = {CompanyMapper.class})
public interface CompanyCommentMapper {

    public CompanyComment toCompanyComment(DtoCompanyCommentIU dtoCompanyCommentIU);
    public CompanyComment toUpdated(DtoCompanyCommentIU dtoCompanyCommentIU, @MappingTarget CompanyComment companyComment);
    public DtoPublicCompanyComment toDtoPublicCompanyComment(CompanyComment companyComment);
}
