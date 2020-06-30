package com.koenrodenburg.techassignment.v1.diff.controller;

import com.koenrodenburg.techassignment.v1.diff.model.Side;
import com.koenrodenburg.techassignment.v1.diff.service.DiffInputService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class DiffInputControllerTest {

    @Mock
    private DiffInputService diffInputService;

    @InjectMocks
    private DiffInputController diffInputController;

    private final String id = "id";
    private final String body = "body";

    @Test
    void left() {
        diffInputController.left(id, body);

        verify(diffInputService).saveInput(Side.LEFT, id, body);
    }

    @Test
    void right() {
        diffInputController.right(id, body);

        verify(diffInputService).saveInput(Side.RIGHT, id, body);
    }
}