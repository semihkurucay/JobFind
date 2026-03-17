package com.semihkurucay.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@UtilityClass
public class PageUtil {

    private boolean isNullOrEmpty(String name){
        return name == null || name.isEmpty();
    }

    public Pageable toPageable(RestPageableRequest request){
        if (isNullOrEmpty(request.getColumnName())){
            return PageRequest.of(request.getPage(), request.getSize());
        }

        Sort sort = Sort.by(request.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, request.getColumnName());
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    public <T> RestPageableEntity<T> pageableResponse(Page<?> page, List<T> contant){
        RestPageableEntity<T> restPageableEntity = new RestPageableEntity();
        restPageableEntity.setContent(contant);
        restPageableEntity.setPageNumber(page.getPageable().getPageNumber());
        restPageableEntity.setPageSize(page.getPageable().getPageSize());
        restPageableEntity.setTotalElements(page.getTotalElements());

        return restPageableEntity;
    }
}
