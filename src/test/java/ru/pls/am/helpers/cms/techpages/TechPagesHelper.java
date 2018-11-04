package ru.pls.am.helpers.cms.techpages;

import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.AbstractHelper;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class TechPagesHelper extends AbstractHelper {

    public void createTechPagesTechPage(int userId, String name, String id, String slug, String title) {
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(false);                                                                                              // published
        values.add(name);                                                                                               // "name"
        values.add(slug);                                                                                               // slug
        values.add(title);                                                                                              // title
        values.add("");                                                                                                 // "text"
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.tech_pages_techpage(" +
                (id != null ? "id, " : "") +
                "created, modified, published, \"name\", slug, title, \"text\", created_by_id, modified_by_id)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.tech_pages_techpage", "slug", slug);
        System.out.println("Создали техническую страницу " + name);
    }

    public void addTextToTechPagesTechPage(int userId, String name, String text) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(text);                                                                                               // "text"
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // "name"
        String sql = "UPDATE public.tech_pages_techpage " +
                "SET modified=?, \"text\"=?, modified_by_id=? WHERE \"name\"=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили текст технической странице " + name);
    }

    public void publishTechPagesTechPage(int userId, String name) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(true);                                                                                               // published
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // "name"
        String sql = "UPDATE public.tech_pages_techpage " +
                "SET modified=?, published=?, modified_by_id=? WHERE \"name\"=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали техническую страницу " + name);
    }

    public void createSeveralTechPagesTechPage(int userId, int count, String name, String title) {
        String slug = convertHelper.convertTextToSlug(name);
        List<String> slugs;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT slug FROM public.tech_pages_techpage WHERE slug like ?;";
            slugs = handle.createQuery(sqlSelect).bind(0, slug + "%").mapTo(String.class).list();
        }
        slug = convertHelper.getNextSlug(slug, slugs);
        List<Object> values = new LinkedList<>();
        StringBuilder sql = new StringBuilder();
        Timestamp actualTimestamp = convertHelper.getActualTimestamp();
        for (int i = 0; i < count; i++) {
            String nextName = name + "-" + i;
            values.add(actualTimestamp);                                                                                // created
            values.add(actualTimestamp);                                                                                // modified
            values.add(true);                                                                                           // published
            values.add(nextName);                                                                                       // "name"
            values.add(slug + "-" + i);                                                                                 // slug
            values.add(title);                                                                                          // title
            values.add("");                                                                                             // "text"
            values.add(userId);                                                                                         // created_by_id
            values.add(userId);                                                                                         // modified_by_id
            sql.append("INSERT INTO public.tech_pages_techpage(" +
                    "created, modified, published, \"name\", slug, title, \"text\", created_by_id, modified_by_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);");
        }
        dbHelper.executeSqlScript(sql.toString(), values.toArray());
        dbCLeaner.addLikeEntity("CMS", "public.tech_pages_techpage", "\"name\"", name + "-%");
        System.out.println("Создали " + count + " технических страниц " + name);
    }
}
