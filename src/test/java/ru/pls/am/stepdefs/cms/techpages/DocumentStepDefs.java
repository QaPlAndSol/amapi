package ru.pls.am.stepdefs.cms.techpages;

import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.techpages.DocumentHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class DocumentStepDefs extends AbstractStepDefs {

    private DocumentHelper documentHelper = new DocumentHelper();

    @Дано("^пользователь \"([^\"]*)\" создает документ \"([^\"]*)\" с" +
            "(?: id \"([^\"]*)\")*" +
            " slug \"([^\"]*)\" текстом$")
    public void createDocument(String user, String name, String id, String slug, String text) {
        int userId = dbHelper.getUserIdByName(user);
        documentHelper.createTechPagesDocument(userId, name, id, slug, text);
    }

    @Дано("^пользователь \"([^\"]*)\" публикует документ \"([^\"]*)\"$")
    public void publishDocument(String user, String name) {
        int userId = dbHelper.getUserIdByName(user);
        documentHelper.publishTechPagesDocument(userId, name);
    }

    @Дано("^пользователь \"([^\"]*)\" создает \"([^\"]*)\" документов с названием \"([^\"]*)\" текстом$")
    public void createSeveralDocument(String user, String count, String name, String text) {
        int userId = dbHelper.getUserIdByName(user);
        documentHelper.createSeveralTechPagesDocument(userId, Integer.valueOf(count), name, text);
    }
}
