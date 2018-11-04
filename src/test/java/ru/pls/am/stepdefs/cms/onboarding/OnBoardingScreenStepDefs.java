package ru.pls.am.stepdefs.cms.onboarding;

import cucumber.api.java.ru.Когда;
import ru.pls.am.helpers.cms.onboarding.OnBoardingScreenHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class OnBoardingScreenStepDefs extends AbstractStepDefs {

    private OnBoardingScreenHelper onBoardingScreenHelper = new OnBoardingScreenHelper();

    @Когда("^пользователь \"([^\"]*)\" создает слайд \"([^\"]*)\" для стартого экрана \"([^\"]*)\" с" +
            " позицией \"([^\"]*)\"" +
            " типом слайда \"([^\"]*)\"" +
            " описанием$")
    public void createOnBoardingScreen(String userName, String title, String onBoarding, String number,
                                       String screenType, String description) {
        int userId = dbHelper.getUserIdByName(userName);
        onBoardingScreenHelper.createOnBoardingOnBoardingScreen(userId, title, onBoarding, number, screenType,
                description);
    }
}
