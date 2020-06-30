package com.koenrodenburg.techassignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koenrodenburg.techassignment.v1.diff.model.DiffResult;
import com.koenrodenburg.techassignment.v1.diff.model.Difference;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.koenrodenburg.techassignment.v1.diff.model.ResultType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTests {
    private static final String ENDPOINT_LEFT = "/v1/diff/{id}/left";
    private static final String ENDPOINT_RIGHT = "/v1/diff/{id}/right";
    private static final String ENDPOINT_RESULT = "/v1/diff/{id}";

    private static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String leftSide = "leftSide";
    private final String rightSide = "rightSide";

    @Test
    void idUnknown() throws Exception {
        String id = "0";

        DiffResult expectedDiffResult = new DiffResult(id, ID_UNKNOWN, null, null, List.of());
        String expectedJson = objectMapper.writeValueAsString(expectedDiffResult);

        mockMvc.perform(get(ENDPOINT_RESULT, id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void leftSideMissing() throws Exception {
        String id = "1";

        mockMvc.perform(post(ENDPOINT_RIGHT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(rightSide));

        DiffResult expectedDiffResult = new DiffResult(id, LEFT_SIDE_MISSING, null, rightSide, List.of());
        String expectedJson = objectMapper.writeValueAsString(expectedDiffResult);

        mockMvc.perform(get(ENDPOINT_RESULT, id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void rightSideMissing() throws Exception {
        String id = "2";

        mockMvc.perform(post(ENDPOINT_LEFT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(leftSide));

        DiffResult expectedDiffResult = new DiffResult(id, RIGHT_SIDE_MISSING, leftSide, null, List.of());
        String expectedJson = objectMapper.writeValueAsString(expectedDiffResult);

        mockMvc.perform(get(ENDPOINT_RESULT, id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void sizeNotEqual() throws Exception {
        String id = "3";

        mockMvc.perform(post(ENDPOINT_LEFT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(leftSide));

        mockMvc.perform(post(ENDPOINT_RIGHT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(rightSide));

        DiffResult expectedDiffResult = new DiffResult(id, SIZE_NOT_EQUAL, leftSide, rightSide, List.of());
        String expectedJson = objectMapper.writeValueAsString(expectedDiffResult);

        mockMvc.perform(get(ENDPOINT_RESULT, id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void equal() throws Exception {
        String id = "4";
        String equal = "equal";

        mockMvc.perform(post(ENDPOINT_LEFT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(equal));

        mockMvc.perform(post(ENDPOINT_RIGHT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(equal));

        DiffResult expectedDiffResult = new DiffResult(id, EQUAL, equal, equal, List.of());
        String expectedJson = objectMapper.writeValueAsString(expectedDiffResult);

        mockMvc.perform(get(ENDPOINT_RESULT, id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void different() throws Exception {
        String id = "5";
        String left = "someString";
        String right = "sameString";

        mockMvc.perform(post(ENDPOINT_LEFT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(left));

        mockMvc.perform(post(ENDPOINT_RIGHT, id)
                .contentType(CONTENT_TYPE_OCTET_STREAM)
                .content(right));

        DiffResult expectedDiffResult = new DiffResult(id, DIFFERENT, left, right, List.of(new Difference(1, 2)));
        String expectedJson = objectMapper.writeValueAsString(expectedDiffResult);

        mockMvc.perform(get(ENDPOINT_RESULT, id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}