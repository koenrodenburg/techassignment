package com.koenrodenburg.techassignment.v1.diff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
public class DiffResult {
    @NonNull
    private String id;
    @NonNull
    private ResultType resultType;
    private String leftSide;
    private String rightSide;
    private List<Difference> differences;
}
