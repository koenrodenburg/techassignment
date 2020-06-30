package com.koenrodenburg.techassignment.v1.diff.comparator;

import com.koenrodenburg.techassignment.v1.diff.model.Difference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class StringComparatorTest {

    @InjectMocks
    private StringComparator stringComparator;

    @Test
    void compare_differentLength() {
        String left = "short string";
        String right = "somewhat longer string";

        assertThrows(IllegalArgumentException.class, () -> stringComparator.compare(left, right));
    }

    @Test
    void compare_noDifferences() {
        String left = "This is a string that's equal to the other one";
        String right = "This is a string that's equal to the other one";

        List<Difference> expectedDifferences = List.of();

        List<Difference> actualDifferences = stringComparator.compare(left, right);

        assertEquals(expectedDifferences, actualDifferences);
    }

    @Test
    void compare_oneDifference() {
        String left = "someString";
        String right = "sameString";

        List<Difference> expectedDifferences = List.of(new Difference(1, 2));

        List<Difference> actualDifferences = stringComparator.compare(left, right);

        assertEquals(expectedDifferences, actualDifferences);
    }

    @Test
    void compare_manyDifferences() {
        String left = "some srring witg qite a lot of typos";
        String right = "same string with absolutely no typos";

        List<Object> expectedDifferences = List.of(
                new Difference(1,2),
                new Difference(6,7),
                new Difference(15,16),
                new Difference(17,27),
                new Difference(28,30)
        );

        List<Difference> actualDifferences = stringComparator.compare(left, right);

        assertEquals(expectedDifferences, actualDifferences);
    }

    @Test
    void compare_edgeCases() {
        String left = "begin and end are diff";
        String right = "start and end are same";

        List<Object> expectedDifferences = List.of(
                new Difference(0,5),
                new Difference(18,22)
        );

        List<Difference> actualDifferences = stringComparator.compare(left, right);

        assertEquals(expectedDifferences, actualDifferences);
    }
}