package ru.pls.am.stepdefs;

import cucumber.api.PendingException;
import cucumber.api.java.ru.Когда;
import ru.pls.am.DbConnect;

public class DbStepDefs {

    @Когда("^в бд \"([^\"]*)\" выполняем SQL скрипт \"([^\"]*)\"$")
    public void executeSqlScriptFromFile(String serviceName, String fileName) {
        throw new PendingException();
    }

    @Когда("^в бд \"([^\"]*)\" выполняем SQL скрипт$")
    public void executeSqlScript(String serviceName, String script) throws Throwable {
        DbConnect dbConnect = new DbConnect(serviceName);
        dbConnect.getJdbi().useHandle(handle -> handle.execute(script));
    }
}
