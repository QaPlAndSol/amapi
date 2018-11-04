package ru.pls.am.helpers.cms.techpages;

import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.AbstractHelper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ContactBlockHelper extends AbstractHelper {

    public void createTechPagesContactBlock(int userId, String id, String type, String text) {
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.tech_pages_contactblock;";
            String result = handle.createQuery(sqlSelect).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(++number);                                                                                           // "number"
        values.add(type);                                                                                               // title
        values.add(text);                                                                                               // "text"
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.tech_pages_contactblock(" +
                (id != null ? "id, " : "") +
                "created, modified, \"number\", title, \"text\", created_by_id, modified_by_id)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("title", type);
        pairs.put("\"text\"", text);
        dbCLeaner.addEntity("CMS", "public.tech_pages_contactblock", pairs);
        System.out.println("Создали блок контактов " + type);
    }

    public void createSeveralTechPagesContactBlock(int userId, int count, String type, String text) {
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.tech_pages_contactblock;";
            String result = handle.createQuery(sqlSelect).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        List<Object> values = new LinkedList<>();
        StringBuilder sql = new StringBuilder();
        Timestamp actualTimestamp = convertHelper.getActualTimestamp();
        for (int i = 0; i < count; i++) {
            values.add(actualTimestamp);                                                                                // created
            values.add(actualTimestamp);                                                                                // modified
            values.add(++number);                                                                                       // "number"
            values.add(type);                                                                                           // title
            values.add(text);                                                                                           // "text"
            values.add(userId);                                                                                         // created_by_id
            values.add(userId);                                                                                         // modified_by_id
            sql.append("INSERT INTO public.tech_pages_contactblock(" +
                    "created, modified, \"number\", title, \"text\", created_by_id, modified_by_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?);");
        }
        dbHelper.executeSqlScript(sql.toString(), values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("title", type);
        pairs.put("\"text\"", text);
        dbCLeaner.addEntity("CMS", "public.tech_pages_contactblock", pairs);
        System.out.println("Создали " + count + " блоков контактов " + type);

    }
}
