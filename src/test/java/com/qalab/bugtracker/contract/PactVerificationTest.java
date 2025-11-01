package com.qalab.bugtracker.contract;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.junitsupport.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.qalab.bugtracker.repository.BugReportRepository;
import com.qalab.bugtracker.model.BugReport;

@Provider("Backend-QALab")
@PactFolder("pacts") // Looks under src/test/resources/pacts
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver"
})
class PactVerificationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BugReportRepository bugReportRepository;

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port, "/"));
        
        // Since the contract has conflicting expectations and no provider states,
        // we need to create a minimal dataset that can satisfy the critical tests.
        // The contract expects:
        // - GET /api/bugs: 1 item (UI Bug)
        // - GET /api/bugs?status=OPEN: 2 items (UI Bug + Authentication Bug)  
        // - GET /api/bugs?severity=HIGH: 1 item (Critical System Bug)
        // - GET /api/bugs?status=CLOSED&severity=LOW: 1 item (Text Format Bug)
        // - GET /api/bugs?status=IN_PROGRESS: 0 items
        
        bugReportRepository.deleteAll();
        
        // Based on analysis of the failing tests, I need to create ALL required data
        // The contract seems to expect different numbers of items for different calls:
        
        // 1. UI Bug (HIGH, OPEN) - for basic GET (needs to be ID 1 for GET /api/bugs/1)
        BugReport uiBug = new BugReport("UI Bug", "Button misaligned", "OPEN", BugReport.Severity.HIGH);
        uiBug = bugReportRepository.save(uiBug);  // Get the saved entity with ID
        
        // 2. Authentication Bug (MEDIUM, OPEN) - for status=OPEN filter (to get 2 items)
        BugReport authBug = new BugReport("Authentication Bug", "Login timeout issue", "OPEN", BugReport.Severity.MEDIUM);
        bugReportRepository.save(authBug);
        
        // 3. Critical System Bug (HIGH, OPEN) - for severity=HIGH filter
        BugReport criticalBug = new BugReport("Critical System Bug", "System crashes on startup", "OPEN", BugReport.Severity.HIGH);
        bugReportRepository.save(criticalBug);
        
        // 4. Text Format Bug (LOW, CLOSED) - for multiple filters
        BugReport textBug = new BugReport("Text Format Bug", "Minor text formatting issue", "CLOSED", BugReport.Severity.LOW);
        bugReportRepository.save(textBug);
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    // Provider state methods for different test scenarios
    // These methods prepare the data state that the consumer expects

    @State("bug reports exist")
    @Transactional
    void bugReportsExist() {
        // Clear and set up test data for basic bug report listing
        bugReportRepository.deleteAll();
        
        BugReport bug = new BugReport("UI Bug", "Button misaligned", "OPEN", BugReport.Severity.HIGH);
        bugReportRepository.save(bug);
    }

    @State("specific bug report exists")
    @Transactional
    void specificBugReportExists() {
        // Clear and set up data for testing specific bug report retrieval by ID
        bugReportRepository.deleteAll();
        
        BugReport bug = new BugReport("Database Bug", "Connection timeout", "OPEN", BugReport.Severity.MEDIUM);
        bugReportRepository.save(bug);
    }

    @State("bug report does not exist")
    @Transactional
    void bugReportDoesNotExist() {
        // Ensure database is empty for scenarios expecting no specific bug report
        bugReportRepository.deleteAll();
    }

    @State("system accepts new bug reports")
    @Transactional
    void systemAcceptsNewBugReports() {
        // Clean state to allow new bug report creation
        bugReportRepository.deleteAll();
    }

    @State("system handles invalid data")
    @Transactional 
    void systemHandlesInvalidData() {
        // No specific setup needed - this tests validation
        bugReportRepository.deleteAll();
    }

    @State("filtered bug reports exist")
    @Transactional
    void filteredBugReportsExist() {
        // Set up data for status filtering (OPEN status)
        bugReportRepository.deleteAll();
        
        BugReport openBug1 = new BugReport("UI Bug", "Button misaligned", "OPEN", BugReport.Severity.HIGH);
        BugReport openBug2 = new BugReport("Authentication Bug", "Login timeout issue", "OPEN", BugReport.Severity.MEDIUM);
        
        bugReportRepository.save(openBug1);
        bugReportRepository.save(openBug2);
    }

    @State("bug reports with high severity exist")
    @Transactional
    void bugReportsWithHighSeverityExist() {
        // Set up data for severity filtering (HIGH severity)
        bugReportRepository.deleteAll();
        
        BugReport highSeverityBug = new BugReport("Critical System Bug", "System crashes on startup", "OPEN", BugReport.Severity.HIGH);
        bugReportRepository.save(highSeverityBug);
    }

    @State("bug reports with multiple filters exist")
    @Transactional
    void bugReportsWithMultipleFiltersExist() {
        // Set up data for multiple filter criteria (CLOSED status AND LOW severity)
        bugReportRepository.deleteAll();
        
        BugReport closedLowBug = new BugReport("Text Format Bug", "Minor text formatting issue", "CLOSED", BugReport.Severity.LOW);
        bugReportRepository.save(closedLowBug);
    }

    @State("no matching bug reports found")
    @Transactional
    void noMatchingBugReportsFound() {
        // Clear data to ensure no matches for IN_PROGRESS status filter
        bugReportRepository.deleteAll();
    }

    @State("ready to create a new bug report")
    @Transactional 
    void readyToCreateNewBugReport() {
        // Ensure clean state for creating new bug reports
        // POST operations don't need existing data, but we ensure a clean slate
        bugReportRepository.deleteAll();
        
        // Note: The contract expects the created bug report to have ID=1
        // Since we're using auto-generated IDs, this should work with clean DB
    }
}
