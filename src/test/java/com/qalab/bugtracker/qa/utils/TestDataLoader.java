package com.qalab.bugtracker.qa.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qalab.bugtracker.qa.model.BugReportTestCase;

import java.io.InputStream;
import java.util.List;

public class TestDataLoader {
    public static List<BugReportTestCase> loadBugReportTestData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = TestDataLoader.class
                .getClassLoader()
                .getResourceAsStream("testdata/bug_reports_testdata.json");

        return mapper.readValue(is, new TypeReference<>() {
        });
    }
}
