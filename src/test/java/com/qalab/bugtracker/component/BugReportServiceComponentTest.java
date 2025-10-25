package com.qalab.bugtracker.component;

import com.qalab.bugtracker.model.BugReport;
import com.qalab.bugtracker.repository.BugReportRepository;
import com.qalab.bugtracker.service.BugReportService;
import com.qalab.bugtracker.service.BugReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Component tests for BugReportService.
 * Tests service layer with minimal Spring context and mocked repository.
 */
@SpringJUnitConfig
class BugReportServiceComponentTest {

    @TestConfiguration
    static class ComponentTestConfig {
        @Bean
        public BugReportService bugReportService(BugReportRepository repository) {
            return new BugReportServiceImpl(repository);
        }
    }

    @Autowired
    private BugReportService bugReportService;

    @MockBean
    private BugReportRepository bugReportRepository;

    @Test
    void shouldReturnAllBugReports() {
        // Given
        BugReport bug1 = createBugReport("Bug 1", "Description 1", "OPEN", BugReport.Severity.HIGH);
        BugReport bug2 = createBugReport("Bug 2", "Description 2", "CLOSED", BugReport.Severity.LOW);
        
        when(bugReportRepository.findAll()).thenReturn(Arrays.asList(bug1, bug2));

        // When
        List<BugReport> result = bugReportService.getAllBugReports();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Bug 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Bug 2");
        verify(bugReportRepository).findAll();
    }

    @Test
    void shouldReturnBugReportById() {
        // Given
        Long bugId = 1L;
        BugReport expectedBug = createBugReport("Test Bug", "Test Description", "OPEN", BugReport.Severity.MEDIUM);
        
        when(bugReportRepository.findById(bugId)).thenReturn(Optional.of(expectedBug));

        // When
        Optional<BugReport> result = bugReportService.getBugReportById(bugId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Bug");
        assertThat(result.get().getSeverity()).isEqualTo(BugReport.Severity.MEDIUM);
        verify(bugReportRepository).findById(bugId);
    }

    @Test
    void shouldReturnEmptyWhenBugReportNotFound() {
        // Given
        Long nonExistentId = 999L;
        when(bugReportRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        Optional<BugReport> result = bugReportService.getBugReportById(nonExistentId);

        // Then
        assertThat(result).isEmpty();
        verify(bugReportRepository).findById(nonExistentId);
    }

    @Test
    void shouldSaveBugReport() {
        // Given
        BugReport inputBug = createBugReport("New Bug", "New Description", "OPEN", BugReport.Severity.HIGH);
        BugReport savedBug = createBugReport("New Bug", "New Description", "OPEN", BugReport.Severity.HIGH);
        
        // This line configures the mock to return 'savedBug' when save() is called
        when(bugReportRepository.save(any(BugReport.class))).thenReturn(savedBug);

        // When
        BugReport result = bugReportService.saveBugReport(inputBug);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Bug");
        assertThat(result.getDescription()).isEqualTo("New Description");
        verify(bugReportRepository).save(inputBug);
    }

    @Test
    void shouldFilterBugReportsByStatus() {
        // Given
        String status = "OPEN";
        BugReport openBug = createBugReport("Open Bug", "Description", "OPEN", BugReport.Severity.HIGH);
        
        when(bugReportRepository.findByStatus(status)).thenReturn(Arrays.asList(openBug));

        // When
        List<BugReport> result = bugReportService.getFilteredBugReports(status, null);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("OPEN");
        verify(bugReportRepository).findByStatus(status);
    }

    @Test
    void shouldFilterBugReportsBySeverity() {
        // Given
        BugReport.Severity severity = BugReport.Severity.HIGH;
        BugReport highSeverityBug = createBugReport("Critical Bug", "Description", "OPEN", BugReport.Severity.HIGH);
        
        when(bugReportRepository.findBySeverity(severity)).thenReturn(Arrays.asList(highSeverityBug));

        // When
        List<BugReport> result = bugReportService.getFilteredBugReports(null, severity);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSeverity()).isEqualTo(BugReport.Severity.HIGH);
        verify(bugReportRepository).findBySeverity(severity);
    }

    private BugReport createBugReport(String title, String description, String status, BugReport.Severity severity) {
        BugReport bug = new BugReport();
        bug.setTitle(title);
        bug.setDescription(description);
        bug.setStatus(status);
        bug.setSeverity(severity);
        return bug;
    }
}