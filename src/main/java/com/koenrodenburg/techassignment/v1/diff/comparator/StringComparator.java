package com.koenrodenburg.techassignment.v1.diff.comparator;

import com.koenrodenburg.techassignment.v1.diff.model.Difference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StringComparator {

    public List<Difference> compare(String leftString, String rightString) {
        if (leftString.length() != rightString.length()) {
            throw new IllegalArgumentException("leftString and rightString should be of the same length");
        }

        char[] left = leftString.toCharArray();
        char[] right = rightString.toCharArray();

        List<Difference> differences = new ArrayList<>();

        for (int i = 0; i < left.length; i++) {
            // Walk over both strings until a difference is found, that is the start index of a diff
            if (left[i] != right[i]) {
                int start = i;
                // Continue walking over both strings, until the characters in both strings are equal again, that is the end index of a diff
                do {
                    i++;
                } while (i < left.length && left[i] != right[i]);
                differences.add(new Difference(start, i));
            }
        }

        return differences;
    }
}
