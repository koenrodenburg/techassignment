package com.koenrodenburg.techassignment.v1.diff.controller;

import com.koenrodenburg.techassignment.v1.diff.model.DiffResult;
import com.koenrodenburg.techassignment.v1.diff.service.DiffResultService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/v1/diff")
public class DiffResultController {

    @Autowired
    private DiffResultService diffResultService;

    @GetMapping("/{id}")
    public DiffResult result(@PathVariable("id") String id) {
        long startTime = System.currentTimeMillis();

        DiffResult diffResult = diffResultService.getDiffResultForId(id);

        log.info(String.format("Calculated result for ID %s in %dms", id, System.currentTimeMillis() - startTime));

        return diffResult;
    }

}
