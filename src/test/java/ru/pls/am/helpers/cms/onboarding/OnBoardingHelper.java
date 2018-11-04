package ru.pls.am.helpers.cms.onboarding;

import ru.pls.am.helpers.AbstractHelper;

import java.util.LinkedList;
import java.util.List;

public class OnBoardingHelper extends AbstractHelper {

    public void createOnBoardingOnBoarding(int userId, String title, String onboardingType) {
        switch (onboardingType.toLowerCase()) {
            case "первый запуск":
                onboardingType = "AFL";
                break;
            case "успешная авторизация":
                break;
            default:
                break;
        }
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(title);                                                                                              // title
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(false);                                                                                              // can_publish
        values.add(onboardingType);                                                                                     // onboarding_type
        values.add(true);                                                                                               // all_countries
        values.add(true);                                                                                               // all_device_models
        values.add(true);                                                                                               // all_device_types
        values.add(true);                                                                                               // all_device_vendors
        values.add(true);                                                                                               // all_platforms
        values.add(null);                                                                                               // publish_date
        values.add(false);                                                                                              // published
        values.add(0);                                                                                                  // priority
        String sql = "INSERT INTO public.onboarding_onboarding" +
                "(created, modified, title, created_by_id, modified_by_id, can_publish, onboarding_type, " +
                "all_countries, all_device_models, all_device_types, all_device_vendors, all_platforms, " +
                "publish_date, published, priority)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.onboarding_onboarding", "title", title);
        System.out.println("Создали стартовый экран " + title);
    }
}
