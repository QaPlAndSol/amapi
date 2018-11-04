package ru.pls.am.stepdefs.cms.onboarding;

import cucumber.api.java.ru.Когда;
import ru.pls.am.helpers.cms.onboarding.OnBoardingHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class OnBoardingStepDefs extends AbstractStepDefs {

    private OnBoardingHelper onBoardingHelper = new OnBoardingHelper();

    @Когда("^пользователь \"([^\"]*)\" создает стартовый экран \"([^\"]*)\" с типом \"([^\"]*)\"$")
    public void createOnBoarding(String userName, String title, String onboardingType) {
        int userId = dbHelper.getUserIdByName(userName);
        onBoardingHelper.createOnBoardingOnBoarding(userId, title, onboardingType);
    }
}
