package com.qalab.bugtracker.unit.controller;

import com.qalab.bugtracker.controller.BugReportController;
import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.service.BugReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;

@WebMvcTest(BugReportController.class)
class BugReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BugReportService bugReportService;

    @Test
    void shouldReturnCreatedWhenBugIsPosted() throws Exception {
        BugReport mockBug = new BugReport();
        mockBug.setTitle("Sample bug");
        mockBug.setDescription("Bug description");

        Mockito.when(bugReportService.saveBugReport(any(BugReport.class))).thenReturn(mockBug);

        String requestBody = """
            {
                "title": "Sample bug",
                "description": "Bug description"
            }
        """;

        mockMvc.perform(post("/api/bugs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnListOfBugs() throws Exception {
        BugReport bug1 = new BugReport();
        bug1.setTitle("Bug A");

        BugReport bug2 = new BugReport();
        bug2.setTitle("Bug B");

        Mockito.when(bugReportService.getAllBugReports()).thenReturn(Arrays.asList(bug1, bug2));

        mockMvc.perform(get("/api/bugs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
