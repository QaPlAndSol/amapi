package ru.pls.am.stepdefs.cms.techpages;

import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.techpages.FaqAnswerHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class FaqAnswerStepDefs extends AbstractStepDefs {

    private FaqAnswerHelper faqAnswerHelper = new FaqAnswerHelper();

    @Дано("^пользователь \"([^\"]*)\" создает в группе \"([^\"]*)\" вопрос \"([^\"]*)\"" +
            "(?: с id \"([^\"]*)\")*$")
    public void createFaq(String user, String group, String name, String id) {
        int userId = dbHelper.getUserIdByName(user);
        int groupId = dbHelper.getFaqGroupIdByName(group);
        faqAnswerHelper.createTechPagesFaqAnswer(userId, groupId, name, id);
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет вопросу \"([^\"]*)\" ответ$")
    public void addAnswerForFaq(String user, String name, String answer) {
        int userId = dbHelper.getUserIdByName(user);
        faqAnswerHelper.addAnswerToTechPagesFaqAnswer(userId, name, answer);
    }

    @Дано("^пользователь \"([^\"]*)\" публикует вопрос и ответ \"([^\"]*)\"$")
    public void publishFaq(String user, String name) {
        int userId = dbHelper.getUserIdByName(user);
        faqAnswerHelper.publishTechPagesFaqAnswer(userId, name);
    }
}