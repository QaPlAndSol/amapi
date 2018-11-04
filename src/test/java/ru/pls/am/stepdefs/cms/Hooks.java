package ru.pls.am.stepdefs.cms;

import cucumber.api.java.After;
import ru.pls.am.DbCleaner;
import ru.pls.am.WebDavClient;

public class Hooks {

    private DbCleaner dbCleaner = new DbCleaner();
    private WebDavClient webDavClient = new WebDavClient();

    @After(value = "@all")
    public void after() {
        dbCleaner.cleanDb();
        webDavClient.clear();
    }
}
