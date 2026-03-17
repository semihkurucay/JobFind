package com.semihkurucay.controller.impl;

import com.semihkurucay.controller.RootEntity;
import com.semihkurucay.utils.PageUtil;
import com.semihkurucay.utils.RestPageableEntity;
import com.semihkurucay.utils.RestPageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class RestBaseController {

    public <T> RootEntity<T> ok(T data){
        return RootEntity.ok(data);
    }

    public Pageable toPageable(RestPageableRequest restPageableRequest){
        return PageUtil.toPageable(restPageableRequest);
    }

    public <T> RestPageableEntity<T> restPageableEntity(Page<?> page, List<T> contant){
        return PageUtil.pageableResponse(page, contant);
    }
}
