package ru.pls.am.stepdefs.cms.assets;

import cucumber.api.java.ru.Когда;
import ru.pls.am.helpers.cms.assets.AssetTypeHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class AssetTypeStepDefs extends AbstractStepDefs {

    private AssetTypeHelper assetTypeHelper = new AssetTypeHelper();

    @Когда("^пользователь \"([^\"]*)\" создает тип ассета \"([^\"]*)\" с" +
            " идентификатором \"([^\"]*)\"" +
            " форматом \"([^\"]*)\"" +
            " минимальной шириной \"([^\"]*)\"" +
            " минимальной высотой \"([^\"]*)\"" +
            " отклонением аспекта \"([^\"]*)\"" +
            " максимальным размером \"([^\"]*)\"$")
    public void createAssetType(String userName, String title, String slug, String format, String minWidth,
                                String minHeight, String aspectEpsilon, String maxSize) {
        int userId = dbHelper.getUserIdByName(userName);
        assetTypeHelper.createAssetsAssetType(userId, title, slug, format, minWidth, minHeight, aspectEpsilon, maxSize);
    }

    @Когда("^делаем ассет \"([^\"]*)\" допустимым для \"([^\"]*)\"$")
    public void makeAssetAllowedFor(String title, String contentTypes) {
        assetTypeHelper.createAssetsAssetTypeAllowedFor(title, contentTypes);
    }
}
