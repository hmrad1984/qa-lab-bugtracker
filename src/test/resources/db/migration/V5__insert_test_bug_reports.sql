-- Test data for BugReportControllerIT

INSERT INTO bug_report (title, description, status, severity)
VALUES 
('Test-Login button not working', 'Clicking the login button doesn’t trigger any action.', 'OPEN', 'HIGH'),
('Test-Typo on homepage', 'There is a typo in the homepage header.', 'IN_PROGRESS', 'LOW'),
('Test-Profile page crash', 'Navigating to the profile page results in a 500 error.', 'RESOLVED', 'MEDIUM');
