package ru.pls.am.stepdefs.cms.licenses;

import cucumber.api.java.ru.Дано;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.util.LinkedList;
import java.util.List;

public class LicensorStepDefs extends AbstractStepDefs {

    @Дано("^пользователь \"([^\"]*)\" создает лицензиара \"([^\"]*)\" для правообладателя \"([^\"]*)\"$")
    public void createLicensor(String userName, String title, String rightsHolder) {
        int userId = dbHelper.getUserIdByName(userName);
        int rightsHolderId = dbHelper.getRightsHolderIdByName(rightsHolder);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(title);                                                                                              // title
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(rightsHolderId);                                                                                     // rightsholder_id
        String sql = "INSERT INTO public.licenses_licensor" +
                "(created, modified, title, created_by_id, modified_by_id, rightsholder_id)" +
                "VALUES(?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Создали лицензиара " + title);
        dbCLeaner.addEntity("CMS", "public.licenses_licensor", "title", title);
    }
}
