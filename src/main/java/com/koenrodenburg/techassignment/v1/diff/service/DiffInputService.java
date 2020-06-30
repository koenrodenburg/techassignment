package com.koenrodenburg.techassignment.v1.diff.service;

import com.koenrodenburg.techassignment.v1.diff.model.DiffDocument;
import com.koenrodenburg.techassignment.v1.diff.model.Side;
import com.koenrodenburg.techassignment.v1.diff.repository.DiffDocumentRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiffInputService {

    @Autowired
    private DiffDocumentRepository diffDocumentRepository;

    public void saveInput(@NonNull Side side, @NonNull String id, String text) {
        DiffDocument diffDocument = diffDocumentRepository
                .findById(id)
                .orElse(new DiffDocument(id));

        switch (side) {
            case LEFT -> diffDocument.setLeftSide(text);
            case RIGHT -> diffDocument.setRightSide(text);
        }

        diffDocumentRepository.save(diffDocument);
    }

}
