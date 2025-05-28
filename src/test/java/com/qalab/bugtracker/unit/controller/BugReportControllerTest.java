package com.qalab.bugtracker.unit.controller;

import com.qalab.bugtracker.controller.BugReportController;
import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.model.BugReport.Severity;
import com.qalab.bugtracker.service.BugReportService;
import com.qalab.bugtracker.repository.BugReportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;
import java.lang.reflect.Field;

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

    @MockBean
    private BugReportRepository bugReportRepository;

    @Test
    void shouldReturnCreatedWhenBugIsPosted() throws Exception {
        BugReport mockBug = new BugReport();
        mockBug.setTitle("Sample bug");
        mockBug.setDescription("Bug description");
        mockBug.setStatus("open");
        mockBug.setSeverity(Severity.HIGH);

        Mockito.when(bugReportService.saveBugReport(any(BugReport.class))).thenReturn(mockBug);

        String requestBody = """
                    {
                        "title": "Sample bug",
                        "description": "Bug description",
                        "status": "open",
                        "severity": "HIGH"

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
        bug1.setDescription("Description for Bug A");
        bug1.setStatus("open");
        bug1.setSeverity(Severity.HIGH);

        BugReport bug2 = new BugReport();
        bug2.setTitle("Bug B");
        bug2.setDescription("Description for Bug B");
        bug2.setStatus("closed");
        bug2.setSeverity(Severity.MEDIUM);

        Mockito.when(bugReportService.getAllBugReports()).thenReturn(Arrays.asList(bug1, bug2));

        mockMvc.perform(get("/api/bugs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldReturnBugReportById() throws Exception {
        // Given
        Long bugId = 1L;
        BugReport bugReport = new BugReport("Sample Bug", "This is a sample bug description.", "open", Severity.HIGH);

        // Use reflection to set the id field since it's auto-generated and no setter is
        // available
        Field idField = BugReport.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(bugReport, bugId);

        Mockito.when(bugReportService.getBugReportById(bugId)).thenReturn(Optional.of(bugReport));

        // When & Then
        mockMvc.perform(get("/api/bugs/{id}", bugId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bugId))
                .andExpect(jsonPath("$.title").value("Sample Bug"))
                .andExpect(jsonPath("$.description").value("This is a sample bug description."))
                .andExpect(jsonPath("$.status").value("open"))
                .andExpect(jsonPath("$.severity").value("HIGH"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingBugReport() throws Exception {
        // Given
        Long bugId = 999L;

        Mockito.when(bugReportService.getBugReportById(bugId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/bugs/{id}", bugId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}