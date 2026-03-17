package com.semihkurucay.controller.impl;

import com.semihkurucay.controller.RestJobApplicationController;
import com.semihkurucay.controller.RootEntity;
import com.semihkurucay.dto.DtoJobApplicationCompany;
import com.semihkurucay.dto.DtoJobApplicationIU;
import com.semihkurucay.dto.DtoJobApplicationUser;
import com.semihkurucay.enums.ApplyType;
import com.semihkurucay.service.JobApplicationService;
import com.semihkurucay.utils.RestPageableEntity;
import com.semihkurucay.utils.RestPageableRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class RestJobApplicationControllerImpl extends RestBaseController implements RestJobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    @Override
    public RootEntity<DtoJobApplicationUser> joinJobApplication(Principal principal, @Valid @RequestBody DtoJobApplicationIU dtoJobApplicationIU) {
        return ok(jobApplicationService.joinJobApplication(principal.getName(), dtoJobApplicationIU));
    }

    @GetMapping("/me")
    @Override
    public RootEntity<RestPageableEntity<DtoJobApplicationUser>> getUserJobApplications(Principal principal, @PageableDefault(size = 10, page = 0) RestPageableRequest request) {
        Page<DtoJobApplicationUser> page = jobApplicationService.getUserJobApplications(principal.getName(), toPageable(request));
        return ok(restPageableEntity(page, page.getContent()));
    }

    @GetMapping("/job/{jobId}")
    @Override
    public RootEntity<RestPageableEntity<DtoJobApplicationCompany>> getCompanyJobApplications(Principal principal, @PathVariable(name = "jobId") Long jobId, @PageableDefault(size = 10, page = 0) RestPageableRequest request) {
        Page<DtoJobApplicationCompany> page = jobApplicationService.getCompanyJobApplications(principal.getName(), jobId, toPageable(request));
        return ok(restPageableEntity(page, page.getContent()));
    }

    @PatchMapping("/{id}/status")
    @Override
    public RootEntity<DtoJobApplicationCompany> setJobApplicationStatus(Principal principal, @PathVariable(name = "id") Long id, @RequestBody Map<String, ApplyType> statusBody) {
        return ok(jobApplicationService.setJobApplicationStatus(principal.getName(), id, statusBody.get("status")));
    }
}
