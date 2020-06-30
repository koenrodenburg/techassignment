package com.koenrodenburg.techassignment.v1.diff.service;

import com.koenrodenburg.techassignment.v1.diff.comparator.StringComparator;
import com.koenrodenburg.techassignment.v1.diff.model.DiffDocument;
import com.koenrodenburg.techassignment.v1.diff.model.DiffResult;
import com.koenrodenburg.techassignment.v1.diff.model.Difference;
import com.koenrodenburg.techassignment.v1.diff.repository.DiffDocumentRepository;
import com.koenrodenburg.techassignment.v1.diff.model.ResultType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DiffResultServiceTest {

    @Mock
    private DiffDocumentRepository diffDocumentRepository;

    @Mock
    private StringComparator stringComparator;

    @InjectMocks
    private DiffResultService diffResultService;

    private final String id = "id";
    private final String leftSide = "leftSide";
    private final String rightSide = "rightSide";

    @Test
    void getResult_idUnknown() {
        when(diffDocumentRepository.findById(id)).thenReturn(Optional.empty());

        DiffResult expectedDiffResult = new DiffResult(id, ResultType.ID_UNKNOWN, null, null, List.of());

        DiffResult actualDiffResult = diffResultService.getDiffResultForId(id);

        assertEquals(expectedDiffResult, actualDiffResult);
    }

    @Test
    void getResult_leftSideMissing() {
        DiffDocument diffDocument = new DiffDocument(id);
        diffDocument.setRightSide(rightSide);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.of(diffDocument));

        DiffResult expectedDiffResult = new DiffResult(id, ResultType.LEFT_SIDE_MISSING, null, rightSide, List.of());

        DiffResult actualDiffResult = diffResultService.getDiffResultForId(id);

        assertEquals(expectedDiffResult, actualDiffResult);
    }

    @Test
    void getResult_rightSideMissing() {
        DiffDocument diffDocument = new DiffDocument(id);
        diffDocument.setLeftSide(leftSide);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.of(diffDocument));

        DiffResult expectedDiffResult = new DiffResult(id, ResultType.RIGHT_SIDE_MISSING, leftSide, null, List.of());

        DiffResult actualDiffResult = diffResultService.getDiffResultForId(id);

        assertEquals(expectedDiffResult, actualDiffResult);
    }

    @Test
    void getResult_sizeNotEqual() {
        DiffDocument diffDocument = new DiffDocument(id);
        diffDocument.setLeftSide(leftSide);
        diffDocument.setRightSide(rightSide);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.of(diffDocument));

        DiffResult expectedDiffResult = new DiffResult(id, ResultType.SIZE_NOT_EQUAL, leftSide, rightSide, List.of());

        DiffResult actualDiffResult = diffResultService.getDiffResultForId(id);

        assertEquals(expectedDiffResult, actualDiffResult);
    }

    @Test
    void getResult_equal() {
        String equal = "equal";

        DiffDocument diffDocument = new DiffDocument(id);
        diffDocument.setLeftSide(equal);
        diffDocument.setRightSide(equal);

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.of(diffDocument));

        DiffResult expectedDiffResult = new DiffResult(id, ResultType.EQUAL, equal, equal, List.of());

        DiffResult actualDiffResult = diffResultService.getDiffResultForId(id);

        assertEquals(expectedDiffResult, actualDiffResult);
    }

    @Test
    void getResult_different() {
        String left = "someString";
        String right = "sameString";

        DiffDocument diffDocument = new DiffDocument(id);
        diffDocument.setLeftSide(left);
        diffDocument.setRightSide(right);

        List<Difference> differences = List.of(new Difference(1, 2));

        when(diffDocumentRepository.findById(id)).thenReturn(Optional.of(diffDocument));
        when(stringComparator.compare(left, right)).thenReturn(differences);

        DiffResult expectedDiffResult = new DiffResult(id, ResultType.DIFFERENT, left, right, differences);

        DiffResult actualDiffResult = diffResultService.getDiffResultForId(id);

        assertEquals(expectedDiffResult, actualDiffResult);
    }
}