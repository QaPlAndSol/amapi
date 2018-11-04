package ru.pls.am.helpers.cms.onboarding;

import ru.pls.am.WebDavClient;
import ru.pls.am.helpers.AbstractHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ScreenImageAssetHelper extends AbstractHelper {

    private final String assetDirectory = "images/cms/onboarding/screenimageasset/";
    private final String urlDirectory = "images/cms/onboarding/screenimageasset/qa/";
    private final String dbDirectory = "cms/onboarding/screenimageasset/qa/";

    public void createOnBoardingScreenImageAsset(int userId, String asset, String assetType, String screen) throws Throwable {
        WebDavClient webDavClient = new WebDavClient();
        if (!webDavClient.putFile(urlDirectory + asset, assetDirectory + asset)) {
            throw new Exception();
        }
        asset = dbDirectory + asset;
        int assetTypeId = dbHelper.getAssetTypeIdByTitle(assetType);
        int screenId = dbHelper.getOnBoardingScreenIdByTitle(screen);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(asset);                                                                                              // asset
        values.add(assetTypeId);                                                                                        // asset_type_id
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(screenId);                                                                                           // screen_id
        values.add(false);                                                                                              // is_active
        String sql = "INSERT INTO public.onboarding_screenimageasset" +
                "(created, modified, asset, asset_type_id, created_by_id, modified_by_id, screen_id, is_active)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?);\n";
        dbHelper.executeSqlScript(sql, values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("asset", asset);
        pairs.put("asset_type_id", assetTypeId);
        pairs.put("screen_id", screenId);
        dbCLeaner.addEntity("CMS", "public.onboarding_screenimageasset", pairs);
        System.out.println("Добавили изображение для слайда " + asset);
    }
}
