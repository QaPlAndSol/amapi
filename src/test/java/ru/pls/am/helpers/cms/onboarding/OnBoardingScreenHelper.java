package ru.pls.am.helpers.cms.onboarding;

import ru.pls.am.helpers.AbstractHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class OnBoardingScreenHelper extends AbstractHelper {

    public void createOnBoardingOnBoardingScreen(int userId, String title, String onBoarding, String number,
                                                 String screenType, String description) {
        switch (screenType.toLowerCase()) {
            case "изображение":
                screenType = "image";
                break;
            case "видео":
                screenType = "video";
                break;
            default:
                screenType = null;
        }
        int onBoardingId = dbHelper.getOnBoardingIdByTitle(onBoarding);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(Integer.valueOf(number));                                                                            // "number"
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(onBoardingId);                                                                                       // onboarding_id
        values.add(title);                                                                                              // title
        values.add(description);                                                                                        // description
        values.add(screenType);                                                                                         // screen_type
        String sql = "INSERT INTO public.onboarding_onboardingscreen" +
                "(created, modified, \"number\", created_by_id, modified_by_id, onboarding_id, title, description, " +
                "screen_type)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("title", title);
        pairs.put("onboarding_id", onBoardingId);
        dbCLeaner.addEntity("CMS", "public.onboarding_onboardingscreen", pairs);
        System.out.println("Создали слайд " + title + " стартового экрана " + onBoarding);
    }
}
