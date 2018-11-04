package ru.pls.am.stepdefs.cms.techpages;

import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.techpages.TechPagesHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class TechPagesStepDefs extends AbstractStepDefs {

    private TechPagesHelper techPagesHelper = new TechPagesHelper();

    @Дано("^пользователь \"([^\"]*)\" создает техническую страницу \"([^\"]*)\" с" +
            "(?: id \"([^\"]*)\")*" +
            " slug \"([^\"]*)\"" +
            "(?: заголовком \"([^\"]*)\")*$")
    public void createTechPage(String user, String name, String id, String slug, String title) {
        int userId = dbHelper.getUserIdByName(user);
        techPagesHelper.createTechPagesTechPage(userId, name, id, slug, title);
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет текст технической страницы \"([^\"]*)\"$")
    public void addTextToTechPage(String user, String name, String text) {
        int userId = dbHelper.getUserIdByName(user);
        techPagesHelper.addTextToTechPagesTechPage(userId, name, text);
    }

    @Дано("^пользователь \"([^\"]*)\" публикует техническую страницу \"([^\"]*)\"$")
    public void publishTechPage(String user, String name) {
        int userId = dbHelper.getUserIdByName(user);
        techPagesHelper.publishTechPagesTechPage(userId, name);
    }

    @Дано("^пользователь \"([^\"]*)\" создает \"([^\"]*)\" технических страниц с названием \"([^\"]*)\"" +
            "(?: заголовком \"([^\"]*)\")*$")
    public void createSeveralTechPages(String user, String count, String name, String title) {
        int userId = dbHelper.getUserIdByName(user);
        techPagesHelper.createSeveralTechPagesTechPage(userId, Integer.valueOf(count), name, title);
    }
}
