package com.koenrodenburg.techassignment.v1.diff.service;

import com.koenrodenburg.techassignment.v1.diff.comparator.StringComparator;
import com.koenrodenburg.techassignment.v1.diff.model.DiffDocument;
import com.koenrodenburg.techassignment.v1.diff.model.DiffResult;
import com.koenrodenburg.techassignment.v1.diff.model.Difference;
import com.koenrodenburg.techassignment.v1.diff.model.ResultType;
import com.koenrodenburg.techassignment.v1.diff.repository.DiffDocumentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class DiffResultService {

    @Autowired
    private DiffDocumentRepository diffDocumentRepository;

    @Autowired
    private StringComparator stringComparator;

    public DiffResult getDiffResultForId(String id) {
        return diffDocumentRepository.findById(id)
                .map(this::getDiffResultForDiffDocument)
                .orElse(getDiffResultForUnknownId(id));
    }

    private DiffResult getDiffResultForDiffDocument(DiffDocument diffDocument) {
        ResultType resultType;
        List<Difference> differences = List.of();

        if (diffDocument.getLeftSide() == null) {
            resultType = ResultType.LEFT_SIDE_MISSING;
        } else if (diffDocument.getRightSide() == null) {
            resultType = ResultType.RIGHT_SIDE_MISSING;
        } else if (diffDocument.getLeftSide().length() != diffDocument.getRightSide().length()) {
            resultType = ResultType.SIZE_NOT_EQUAL;
        } else if (diffDocument.getLeftSide().equals(diffDocument.getRightSide())) {
            resultType = ResultType.EQUAL;
        } else {
            resultType = ResultType.DIFFERENT;
            differences = stringComparator.compare(diffDocument.getLeftSide(), diffDocument.getRightSide());
        }

        return new DiffResult(diffDocument.getId(), resultType, diffDocument.getLeftSide(), diffDocument.getRightSide(), differences);
    }

    private DiffResult getDiffResultForUnknownId(String id) {
        return new DiffResult(id, ResultType.ID_UNKNOWN, null, null, List.of());
    }
}
