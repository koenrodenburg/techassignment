package com.koenrodenburg.techassignment.v1.diff.repository;

import com.koenrodenburg.techassignment.v1.diff.model.DiffDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiffDocumentRepository extends JpaRepository<DiffDocument, String> {
    Optional<DiffDocument> findById(String Id);
}
