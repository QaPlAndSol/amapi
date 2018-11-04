package ru.pls.am.stepdefs;

import ru.pls.am.DbCleaner;
import ru.pls.am.helpers.ConvertHelper;
import ru.pls.am.helpers.DbHelper;

import java.io.IOException;

public abstract class AbstractStepDefs {

    protected DbHelper dbHelper;
    protected ConvertHelper convertHelper;
    protected DbCleaner dbCLeaner;

    {
        convertHelper = new ConvertHelper();
        dbCLeaner = new DbCleaner();
        try {
            dbHelper = new DbHelper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
