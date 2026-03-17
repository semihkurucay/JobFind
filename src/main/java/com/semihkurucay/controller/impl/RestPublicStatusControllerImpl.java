package com.semihkurucay.controller.impl;

import com.semihkurucay.controller.RestPublicStatusController;
import com.semihkurucay.controller.RootEntity;
import com.semihkurucay.dto.*;
import com.semihkurucay.service.PublicStatusService;
import com.semihkurucay.utils.RestPageableEntity;
import com.semihkurucay.utils.RestPageableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class RestPublicStatusControllerImpl extends RestBaseController implements RestPublicStatusController {

    private final PublicStatusService publicStatusService;

    @GetMapping("/stats")
    @Override
    public RootEntity<DtoPublicStatus> getStatus() {
        return ok(publicStatusService.getStatus());
    }

    @GetMapping("/companies")
    @Override
    public RootEntity<RestPageableEntity<DtoPublicCompany>> publicFindAllCompanyByName(@RequestParam(required = false) String search, @PageableDefault(page = 0, size = 10) RestPageableRequest request) {
        Page<DtoPublicCompany> page = publicStatusService.publicFindAllCompanyByName(search, toPageable(request));
        return ok(restPageableEntity(page, page.getContent()));
    }

    @GetMapping("/companies/{id}")
    @Override
    public RootEntity<DtoPublicCompanyInfo> publicFindCompanyById(@PathVariable(name = "id") Long id) {
        return ok(publicStatusService.publicFindCompanyById(id));
    }

    @GetMapping("/companies/{id}/comments")
    @Override
    public RootEntity<RestPageableEntity<DtoPublicCompanyComment>> publicFindAllCompanyCommentByCompany_Id(@PathVariable(name = "id") Long companyId, RestPageableRequest request) {
        Page<DtoPublicCompanyComment> page = publicStatusService.publicFindAllCompanyCommentByCompany_Id(companyId, toPageable(request));
        return ok(restPageableEntity(page, page.getContent()));
    }

    @GetMapping("/companies/{id}/jobs")
    @Override
    public RootEntity<RestPageableEntity<DtoPublicJobPosting>> publicFindAllJobPostingByCompany_Id(@PathVariable(name = "id") Long companyId, RestPageableRequest request) {
        Page<DtoPublicJobPosting> page = publicStatusService.publicFindAllJobPostingByCompany_Id(companyId, toPageable(request));
        return ok(restPageableEntity(page, page.getContent()));
    }

    @GetMapping("/jobs")
    @Override
    public RootEntity<RestPageableEntity<DtoPublicJobPosting>> publicFindAllPosting(@PageableDefault(page = 0, size = 10) RestPageableRequest request) {
        Page<DtoPublicJobPosting> page = publicStatusService.publicFindAllPosting(toPageable(request));
        return ok(restPageableEntity(page, page.getContent()));
    }
}
