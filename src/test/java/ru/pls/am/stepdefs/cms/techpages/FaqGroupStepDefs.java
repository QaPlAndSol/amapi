package ru.pls.am.stepdefs.cms.techpages;

import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.techpages.FaqGroupHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class FaqGroupStepDefs extends AbstractStepDefs {

    private FaqGroupHelper faqGroupHelper = new FaqGroupHelper();

    @Дано("^пользователь \"([^\"]*)\" создает в разделе \"([^\"]*)\" группу часто задаваемых вопросов \"([^\"]*)\"" +
            "(?: с id \"([^\"]*)\")*$")
    public void createFaqGroup(String user, String section, String name, String id) {
        int userId = dbHelper.getUserIdByName(user);
        int sectionId = dbHelper.getFaqSectionIdByName(section);
        faqGroupHelper.createTechPagesFaqGroup(userId, sectionId, name, id);
    }

    @Дано("^пользователь \"([^\"]*)\" публикует группу часто задаваемых вопросов \"([^\"]*)\"$")
    public void publishFaqGroup(String user, String name) {
        int userId = dbHelper.getUserIdByName(user);
        faqGroupHelper.publishTechPagesFaqGroup(userId, name);
    }
}