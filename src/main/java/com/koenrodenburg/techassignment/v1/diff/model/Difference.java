package com.koenrodenburg.techassignment.v1.diff.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Difference {
    private int fromIndex;
    private int toIndex;
}
