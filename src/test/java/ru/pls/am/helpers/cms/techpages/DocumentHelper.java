package ru.pls.am.helpers.cms.techpages;

import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.AbstractHelper;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class DocumentHelper extends AbstractHelper {

    public void createTechPagesDocument(int userId, String name, String id, String slug, String text) {
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.tech_pages_document;";
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
        values.add(++number);                                                                                           // "number"
        values.add(name);                                                                                               // title
        values.add(text);                                                                                               // "text"
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(slug);                                                                                               // slug
        String sql = "INSERT INTO public.tech_pages_document(" + (id != null ? "id, " : "") +
                "created, modified, published, \"number\", title, \"text\", created_by_id, modified_by_id, slug)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.tech_pages_document", "slug", slug);
        System.out.println("Создали документ " + name);
    }

    public void publishTechPagesDocument(int userId, String name) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(true);                                                                                               // published
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // title
        String sql = "UPDATE public.tech_pages_document " +
                "SET modified=?, published=?, modified_by_id=? WHERE title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали документ " + name);
    }

    public void createSeveralTechPagesDocument(int userId, int count, String name, String text) {
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.tech_pages_document;";
            String result = handle.createQuery(sqlSelect).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        String slug = convertHelper.convertTextToSlug(name);
        List<String> slugs;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT slug FROM public.tech_pages_document WHERE slug like ?;";
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
            values.add(++number);                                                                                       // "number"
            values.add(nextName);                                                                                       // title
            values.add(text);                                                                                           // "text"
            values.add(userId);                                                                                         // created_by_id
            values.add(userId);                                                                                         // modified_by_id
            values.add(slug + "-" + i);                                                                                 // slug
            sql.append("INSERT INTO public.tech_pages_document(" +
                    "created, modified, published, \"number\", title, \"text\", created_by_id, modified_by_id, slug)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);");
        }
        dbHelper.executeSqlScript(sql.toString(), values.toArray());
        dbCLeaner.addLikeEntity("CMS", "public.tech_pages_document", "slug", slug + "-%");
        System.out.println("Создали " + count + " документов " + name);
    }
}
