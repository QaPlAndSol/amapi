package ru.pls.am.helpers.cms.assets;

import ru.pls.am.helpers.AbstractHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AssetTypeHelper extends AbstractHelper {

    public void createAssetsAssetType(int userId, String title, String slug, String format, String minWidth,
                                      String minHeight, String aspectEpsilon, String maxSize) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(slug);                                                                                               // slug
        values.add(title);                                                                                              // title
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(Float.valueOf(aspectEpsilon));                                                                       // aspect_epsilon
        values.add(format);                                                                                             // format
        values.add(null);                                                                                               // max_duration
        values.add(Long.valueOf(maxSize));                                                                              // max_size
        values.add(null);                                                                                               // min_duration
        values.add(Integer.valueOf(minHeight));                                                                         // min_height
        values.add(Integer.valueOf(minWidth));                                                                          // min_width
        String sql = "INSERT INTO public.assets_assettype" +
                "(created, modified, slug, title, created_by_id, modified_by_id, aspect_epsilon, format, " +
                "max_duration, max_size, min_duration, min_height, min_width)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.assets_assettype", "title", title);
        System.out.println("Создали тип ассета " + title);
    }

    public void createAssetsAssetTypeAllowedFor(String title, String contentTypes) {
        int assetTypeId = dbHelper.getAssetTypeIdByTitle(title);
        for (String contentType : contentTypes.split(",")) {
            contentType = contentType.trim();
            int contentTypeId = dbHelper.getContentTypeIdByContentType(contentType);
            List<Object> values = new LinkedList<>();
            values.add(assetTypeId);                                                                                    // assettype_id
            values.add(contentTypeId);                                                                                  // contenttype_id
            String sql = "INSERT INTO public.assets_assettype_allowed_for" +
                    "(assettype_id, contenttype_id)" +
                    "VALUES(?, ?);";
            dbHelper.executeSqlScript(sql, values.toArray());
            HashMap<String, Object> pairs = new HashMap<>();
            pairs.put("assettype_id", assetTypeId);
            pairs.put("contenttype_id", contentTypeId);
            dbCLeaner.addEntity("CMS", "public.assets_assettype_allowed_for", pairs);
            System.out.println("Сделали тип ассета " + title + " допустимым для " + contentType);
        }
    }
}
