package com.koenrodenburg.techassignment.v1.diff.controller;

import com.koenrodenburg.techassignment.v1.diff.model.DiffResult;
import com.koenrodenburg.techassignment.v1.diff.model.ResultType;
import com.koenrodenburg.techassignment.v1.diff.service.DiffResultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DiffResultControllerTest {

    @Mock
    private DiffResultService diffResultService;

    @InjectMocks
    private DiffResultController diffResultController;

    @Test
    void result() {
        String id = "id";

        DiffResult expectedDiffResult = new DiffResult(id, ResultType.EQUAL, null, null, List.of());

        when(diffResultService.getDiffResultForId(id)).thenReturn(expectedDiffResult);

        DiffResult actualDiffResult = diffResultController.result(id);

        assertEquals(expectedDiffResult, actualDiffResult);
    }
}