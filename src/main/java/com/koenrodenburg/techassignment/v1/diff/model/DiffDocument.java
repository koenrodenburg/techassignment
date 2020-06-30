package com.koenrodenburg.techassignment.v1.diff.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class DiffDocument {
    @Id
    @NonNull
    private String id;
    private String leftSide;
    private String rightSide;
}
