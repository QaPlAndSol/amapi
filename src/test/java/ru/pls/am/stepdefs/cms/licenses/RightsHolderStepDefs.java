package ru.pls.am.stepdefs.cms.licenses;

import cucumber.api.java.ru.Дано;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.util.LinkedList;
import java.util.List;

public class RightsHolderStepDefs extends AbstractStepDefs {

    @Дано("^пользователь \"([^\"]*)\" создает правообладателя \"([^\"]*)\"$")
    public void createRightsHolder(String userName, String title) {
        int userId = dbHelper.getUserIdByName(userName);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(title);                                                                                              // title
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.licenses_rightsholder" +
                "(created, modified, title, created_by_id, modified_by_id)" +
                "VALUES(?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Создали правообладателя " + title);
        dbCLeaner.addEntity("CMS", "public.licenses_rightsholder", "title", title);
    }

    // TODO добавлять и удалять записи
    // Связь content-studio: Content_studios object (7)
}
