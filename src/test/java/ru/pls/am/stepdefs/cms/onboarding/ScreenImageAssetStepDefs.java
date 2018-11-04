package ru.pls.am.stepdefs.cms.onboarding;

import cucumber.api.java.ru.Когда;
import ru.pls.am.helpers.cms.onboarding.ScreenImageAssetHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class ScreenImageAssetStepDefs extends AbstractStepDefs {

    private ScreenImageAssetHelper screenImageAssetHelper = new ScreenImageAssetHelper();

    @Когда("^пользователь \"([^\"]*)\" создает ассет \"([^\"]*)\" с типом \"([^\"]*)\" для слайда \"([^\"]*)\"$")
    public void createAsset(String userName, String asset, String assetType, String screen) throws Throwable {
        int userId = dbHelper.getUserIdByName(userName);
        screenImageAssetHelper.createOnBoardingScreenImageAsset(userId, asset, assetType, screen);
    }
}
