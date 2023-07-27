package ru.otus.andrk.dao;

public class TestContentDaoCsvResourceImpl implements TestContentDao {
    private final String resourceName;

    public TestContentDaoCsvResourceImpl(String resourceName) {
        this.resourceName = resourceName;
    }


    @Override
    public String getInfo() {
        return null;
    }
}
