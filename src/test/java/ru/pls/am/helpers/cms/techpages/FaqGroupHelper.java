package ru.pls.am.helpers.cms.techpages;

import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.AbstractHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FaqGroupHelper extends AbstractHelper {

    public void createTechPagesFaqGroup(int userId, int sectionId, String name, String id) {
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.tech_pages_faqgroup;";
            String result = handle.createQuery(sqlSelect).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(false);                                                                                              // published
        values.add(number);                                                                                             // "number"
        values.add(name);                                                                                               // title
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(sectionId);                                                                                          // section_id
        String sql = "INSERT INTO public.tech_pages_faqgroup(" +
                (id != null ? "id, " : "") +
                "created, modified, published, \"number\", title, created_by_id, modified_by_id, section_id)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("title", name);
        pairs.put("section_id", sectionId);
        dbCLeaner.addEntity("CMS", "public.tech_pages_faqgroup", pairs);
        System.out.println("Создали группу часто задаваемых вопросов " + name);
    }

    public void publishTechPagesFaqGroup(int userId, String name) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(true);                                                                                               // published
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // title
        String sql = "UPDATE public.tech_pages_faqgroup " +
                "SET modified=?, published=?, modified_by_id=? WHERE title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали группу часто задаваемых вопросов " + name);
    }
}