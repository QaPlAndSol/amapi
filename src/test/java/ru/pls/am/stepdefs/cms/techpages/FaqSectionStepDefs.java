package ru.pls.am.stepdefs.cms.techpages;

import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.techpages.FaqSectionHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.util.LinkedList;

public class FaqSectionStepDefs extends AbstractStepDefs {

    private FaqSectionHelper faqSectionHelper = new FaqSectionHelper();

    @Дано("^пользователь \"([^\"]*)\" создает раздел часто задаваемых вопросов \"([^\"]*)\" с" +
            "(?: id \"([^\"]*)\")*" +
            " порядковым номером \"([^\"]*)\"$")
    public void createFaqSection(String user, String name, String id, String number) {
        int userId = dbHelper.getUserIdByName(user);
        faqSectionHelper.createTechPagesFaqSection(userId, name, id, Integer.valueOf(number));
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет в раздел часто задаваемых вопросов \"([^\"]*)\" лого \"([^\"]*)\"$")
    public void addLogoToFaqSection(String user, String name, String logo) throws Exception {
        int userId = dbHelper.getUserIdByName(user);
        faqSectionHelper.addLogoToTechPagesFaqSection(userId, name, logo);
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет в раздел часто задаваемых вопросов \"([^\"]*)\" типы устройств \"([^\"]*)\"$")
    public void addDeviceTypesToFaqSection(String user, String name, String deviceTypes) {
        int userId = dbHelper.getUserIdByName(user);
        LinkedList<Integer> deviceTypeIds = new LinkedList<>();
        for (String deviseType : deviceTypes.split(",")) {
            deviceTypeIds.add(dbHelper.getDeviceTypeIdByName(deviseType.trim()));
        }
        faqSectionHelper.addDeviceTypeToTechPagesFaqSection(userId, name, deviceTypeIds);
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет в раздел часто задаваемых вопросов \"([^\"]*)\" все типы устройств$")
    public void addAllDeviceTypesToFaqSection(String user, String name) {
        int userId = dbHelper.getUserIdByName(user);
        faqSectionHelper.addAllDeviceTypesToTechPagesFaqSection(userId, name);
    }

    @Дано("^пользователь \"([^\"]*)\" публикует раздел часто задаваемых вопросов \"([^\"]*)\"$")
    public void publishFaqSection(String user, String name) {
        int userId = dbHelper.getUserIdByName(user);
        faqSectionHelper.publishTechPagesFaqSection(userId, name);
    }

    @Дано("^пользователь \"([^\"]*)\" создает \"([^\"]*)\" разделов часто задаваемых вопросов \"([^\"]*)\" начиная с порядкового номера \"([^\"]*)\"$")
    public void createSeveralFaqSections(String user, String count, String name, String number) {
        int userId = dbHelper.getUserIdByName(user);
        faqSectionHelper.createSeveralTechPagesFaqSection(userId, Integer.valueOf(count), name, Integer.valueOf(number));
    }
}