package com.koenrodenburg.techassignment.v1.diff.controller;

import com.koenrodenburg.techassignment.v1.diff.model.Side;
import com.koenrodenburg.techassignment.v1.diff.service.DiffInputService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/v1/diff")
public class DiffInputController {
    private static final String LOG = "Processed '%s' for ID %s in %dms";

    @Autowired
    private DiffInputService diffInputService;

    @PostMapping("/{id}/left")
    public void left(@PathVariable("id") String id, @RequestBody String body) {
        long startTime = System.currentTimeMillis();

        diffInputService.saveInput(Side.LEFT, id, body);

        log.info(String.format(LOG, "left", id, System.currentTimeMillis() - startTime));
    }

    @PostMapping("/{id}/right")
    public void right(@PathVariable("id") String id, @RequestBody String body) {
        long startTime = System.currentTimeMillis();

        diffInputService.saveInput(Side.RIGHT, id, body);

        log.info(String.format(LOG, "right", id, System.currentTimeMillis() - startTime));
    }
}
