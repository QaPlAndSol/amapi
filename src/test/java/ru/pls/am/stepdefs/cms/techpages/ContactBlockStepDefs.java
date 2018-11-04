package ru.pls.am.stepdefs.cms.techpages;

import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.techpages.ContactBlockHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class ContactBlockStepDefs extends AbstractStepDefs {

    private ContactBlockHelper contactBlockHelper = new ContactBlockHelper();

    @Дано("^пользователь \"([^\"]*)\" создает блок контактов с(?: id \"([^\"]*)\")* типом \"([^\"]*)\" и текстом$")
    public void createContactBlock(String user, String id, String type, String text) {
        int userId = dbHelper.getUserIdByName(user);
        contactBlockHelper.createTechPagesContactBlock(userId, id, type, text);
    }

    @Дано("^пользователь \"([^\"]*)\" создает \"([^\"]*)\" блоков контактов с типом \"([^\"]*)\" и текстом$")
    public void createSeveralContactBlock(String user, String count, String type, String text) {
        int userId = dbHelper.getUserIdByName(user);
        contactBlockHelper.createSeveralTechPagesContactBlock(userId, Integer.valueOf(count), type, text);
    }
}
