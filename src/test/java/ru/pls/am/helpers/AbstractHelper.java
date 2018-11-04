package ru.pls.am.helpers;

import ru.pls.am.DbCleaner;

import java.io.IOException;

public class AbstractHelper {

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
