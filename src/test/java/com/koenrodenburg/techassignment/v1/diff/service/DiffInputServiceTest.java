package com.koenrodenburg.techassignment.v1.diff.service;

import com.koenrodenburg.techassignment.v1.diff.model.DiffDocument;
import com.koenrodenburg.techassignment.v1.diff.model.Side;
import com.koenrodenburg.techassignment.v1.diff.repository.DiffDocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DiffInputServiceTest {

    @Mock
    private DiffDocumentRepository diffDocumentRepository;

    @InjectMocks
    private DiffInputService diffInputService;

    private final String id = "id";
    private final String leftSide = "leftSide";
    private final String rightSide = "rightSide";

    @Test
    void input_newId_left() {
        DiffDocument expectedDiffDocument = new DiffDocument(id);
        expectedDiffDocument.setLeftSide(leftSide);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.empty());

        diffInputService.saveInput(Side.LEFT, id, leftSide);

        verify(diffDocumentRepository).save(expectedDiffDocument);
    }

    @Test
    void input_newId_right() {
        DiffDocument expectedDiffDocument = new DiffDocument(id);
        expectedDiffDocument.setRightSide(rightSide);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.empty());

        diffInputService.saveInput(Side.RIGHT, id, rightSide);

        verify(diffDocumentRepository).save(expectedDiffDocument);
    }

    @Test
    void input_existingId_left() {
        DiffDocument existingDiffDocument = new DiffDocument(id);
        existingDiffDocument.setRightSide(rightSide);

        DiffDocument expectedDiffDocument = new DiffDocument(id);
        expectedDiffDocument.setLeftSide(leftSide);
        expectedDiffDocument.setRightSide(rightSide);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.of(existingDiffDocument));

        diffInputService.saveInput(Side.LEFT, id, leftSide);

        verify(diffDocumentRepository).save(expectedDiffDocument);
    }

    @Test
    void input_existingId_right() {
        DiffDocument existingDiffDocument = new DiffDocument(id);
        existingDiffDocument.setLeftSide(leftSide);

        DiffDocument expectedDiffDocument = new DiffDocument(id);
        expectedDiffDocument.setLeftSide(leftSide);
        expectedDiffDocument.setRightSide(rightSide);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.of(existingDiffDocument));

        diffInputService.saveInput(Side.RIGHT, id, rightSide);

        verify(diffDocumentRepository).save(expectedDiffDocument);
    }
}