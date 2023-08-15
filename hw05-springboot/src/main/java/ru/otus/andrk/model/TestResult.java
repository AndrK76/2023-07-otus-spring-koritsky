package ru.otus.andrk.model;

import lombok.Getter;

@Getter
public class TestResult {

    private final Student student;

    private int failCount = 0;

    private int successCount = 0;

    public TestResult(Student student) {
        this.student = student;
    }

    public void addResult(boolean result) {
        if (result) {
            successCount++;
        } else {
            failCount++;
        }
    }


}
