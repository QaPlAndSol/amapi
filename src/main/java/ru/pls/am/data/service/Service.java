package ru.pls.am.data.service;

public class Service {

    private String name;

    private String baseUrl;

    private DbInfo db;

    String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public DbInfo getDb() {
        return db;
    }
}
