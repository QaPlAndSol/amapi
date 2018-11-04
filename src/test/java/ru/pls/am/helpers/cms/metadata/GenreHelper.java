package ru.pls.am.helpers.cms.metadata;

import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.AbstractHelper;
import ru.pls.am.helpers.ConvertHelper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GenreHelper extends AbstractHelper {

    public void createMetadataGenre(String id, String name, String slug, int userId) {
        slug = slug == null ? convertHelper.convertTextToSlug(name) : slug;
        List<String> slugs;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT slug FROM public.metadata_genre WHERE slug like ?;";
            slugs = handle.createQuery(sqlSelect).bind(0, slug + "%").mapTo(String.class).list();
        }
        slug = convertHelper.getNextSlug(slug, slugs);
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(name);                                                                                               // name
        values.add(slug);                                                                                               // slug
        values.add("");                                                                                                 // description
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(false);                                                                                              // can_publish
        values.add(null);                                                                                               // publish_date
        values.add(false);                                                                                              // published
        String sql = "INSERT INTO public.metadata_genre(" +
                (id != null ? "id, " : "") + "created, modified, \"name\", slug, description, created_by_id, " +
                "modified_by_id, can_publish, publish_date, published) " +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.metadata_genre", "\"name\"", name);
        System.out.println("Создали жанр " + name);
    }

    public void createCatalogueCatalogueGenre(int userId, int genreId) {
        String catalogType = "movies";
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.catalogue_cataloguegenre " +
                    "WHERE catalogue_type='" + catalogType + "';";
            String result = handle.createQuery(sqlSelect).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // created_by_id
        values.add(genreId);                                                                                            // genre_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.catalogue_cataloguegenre " +
                "(created, modified, catalogue_type, \"number\", created_by_id, genre_id, modified_by_id) " +
                "VALUES(?, ?, '" + catalogType + "', " + (++number) + ", ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.catalogue_cataloguegenre", "genre_id", genreId);
        catalogType = "series";
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.catalogue_cataloguegenre " +
                    "WHERE catalogue_type='" + catalogType + "';";
            String result = handle.createQuery(sqlSelect).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        sql = "INSERT INTO public.catalogue_cataloguegenre " +
                "(created, modified, catalogue_type, \"number\", created_by_id, genre_id, modified_by_id) " +
                "VALUES(?, ?, '" + catalogType + "', " + (++number) + ", ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Создали жанр фильма и сериала");
    }

    public void addDescriptionToMetadataGenre(String name, String description, int userId) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(description == null ? "" : description);                                                             // description
        values.add(name);                                                                                               // name
        String sql = "UPDATE public.metadata_genre " +
                "SET modified=?, modified_by_id=?, description=? WHERE \"name\"=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили описание жанру " + name);
    }

    public void publishMetadataGenre(String name, String publishDate, int userId) throws ParseException {
        Timestamp timestamp;
        if (publishDate != null) {
            timestamp = convertHelper.convertStringToTimestamp(publishDate, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        } else {
            String publishDateFromDb;
            try (Handle handle = dbHelper.getDbCms().open()) {
                String sql = "SELECT publish_date FROM public.metadata_genre WHERE \"name\"=?;";
                publishDateFromDb = handle.createQuery(sql).bind(0, name).mapTo(String.class).findOnly();
            }
            timestamp = publishDateFromDb == null ?
                    convertHelper.getActualTimestamp() :
                    convertHelper.convertStringToTimestamp(publishDateFromDb, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        }
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(true);                                                                                               // can_publish
        values.add(timestamp);                                                                                          // publish_date
        values.add(timestamp.getTime() <= new Date().getTime());                                                        // published
        values.add(name);                                                                                               // name
        String sql = "UPDATE public.metadata_genre " +
                "SET modified=?, modified_by_id=?, can_publish=?, publish_date=?, published=? WHERE \"name\"=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали жанр " + name);
    }

    public void deleteMetadataGenre(String name) {
        String sql = "DELETE FROM public.metadata_genre WHERE \"name\"=?";
        dbHelper.executeSqlScript(sql, name);
        System.out.println("Удалили жанр " + name);
    }

    public void deleteCatalogueCatalogueGenre(String name) {
        String sql = "DELETE FROM public.catalogue_cataloguegenre " +
                "WHERE genre_id=(SELECT id FROM public.metadata_genre WHERE \"name\"=?);";
        dbHelper.executeSqlScript(sql, name);
        System.out.println("Удалили жанр фильма и сериала");
    }
}
