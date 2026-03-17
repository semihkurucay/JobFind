package com.semihkurucay.controller.impl;

import com.semihkurucay.controller.RestJobPostingController;
import com.semihkurucay.controller.RootEntity;
import com.semihkurucay.dto.DtoJobPosting;
import com.semihkurucay.dto.DtoJobPostingIU;
import com.semihkurucay.service.JobPostingService;
import com.semihkurucay.utils.RestPageableEntity;
import com.semihkurucay.utils.RestPageableRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class RestJobPostingControllerImpl extends RestBaseController implements RestJobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping("/me")
    @Override
    public RootEntity<RestPageableEntity<DtoJobPosting>> findAllPosting(Principal principal, @PageableDefault(page = 0, size = 10) RestPageableRequest request) {
        Page<DtoJobPosting> page = jobPostingService.findAllPosting(principal.getName(), toPageable(request));
        return ok(restPageableEntity(page, page.getContent()));
    }

    @PostMapping
    @Override
    public RootEntity<DtoJobPosting> createJobPosting(Principal principal, @Valid @RequestBody DtoJobPostingIU dtoJobPostingIU) {
        return ok(jobPostingService.createJobPosting(principal.getName(), dtoJobPostingIU));
    }

    @DeleteMapping("/{id}")
    @Override
    public RootEntity deleteJobPosting(Principal principal, @PathVariable(name = "id") Long id) {
        jobPostingService.deleteJobPosting(principal.getName(), id);
        return ok(null);
    }
}
