package ru.pls.am.stepdefs.cms.showcases;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Тогда;
import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.ConvertHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CollectionStepDefs extends AbstractStepDefs {

    /**
     * Удаление коллекци по ее названию
     *
     * @param name название коллекции
     */
    @Тогда("^удаляем коллекцию \"([^\"]*)\"$")
    public void deleteCollectionByName(String name) {
        dbHelper.executeSqlScript("DELETE FROM public.showcases_collection WHERE title=?", name);
        System.out.println("Удалили коллекцию " + name);
    }

    /**
     * Удаление коллекци по ее слагу
     *
     * @param slug слаг коллекции
     */
    @Тогда("^удаляем коллекцию со слагом \"([^\"]*)\"$")
    public void deleteCollectionBySlug(String slug) {
        dbHelper.executeSqlScript("DELETE FROM public.showcases_collection WHERE slug=?", slug);
    }

    /**
     * Удаление коллекци по ее id
     *
     * @param id идентификатор коллекции
     */
    @Тогда("^удаляем коллекцию с id (\\d+)$")
    public void deleteCollectionById(int id) {
        dbHelper.executeSqlScript("DELETE FROM public.showcases_collection WHERE id=?", id);
    }

    /**
     * Создание коллекции
     *
     * @param userName пользователь, который создает коллекцию
     * @param title    название коллекции. Поле обязательно для заполнения
     * @param id       идентификатор. По умолчанию генерируется автоинкрементом
     * @param kind     тип коллекции. По умолчанию editors
     * @param slug     слаг коллекции. Поле обязательно для заполнения
     * @param priority приоритет. По умолчанию 0
     */
    @Дано("^пользователь \"([^\"]*)\" создает коллекцию \"([^\"]*)\" с" +
            "(?: id \"([^\"]*)\")*" +
            "(?: типом \"([^\"]*)\")*" +
            "(?: slug \"([^\"]*)\")*" +
            "(?: приоритетом \"([^\"]*)\")*$")
    public void createCollection(String userName, String title, String id, String kind, String slug, String priority) {
        int userId = dbHelper.getUserIdByName(userName);
        slug = slug == null ? convertHelper.convertTextToSlug(title) : slug;
        List<String> slugs;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT slug FROM public.showcases_collection WHERE slug like ?;";
            slugs = handle.createQuery(sqlSelect).bind(0, slug + "%").mapTo(String.class).list();
        }
        slug = convertHelper.getNextSlug(slug, slugs);

        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(kind == null ? "editors" : kind);                                                                    // kind
        values.add(title);                                                                                              // title
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(slug);                                                                                               // slug
        values.add(false);                                                                                              // can_publish
        values.add(null);                                                                                               // publish_date
        values.add(false);                                                                                              // published
        values.add(priority == null ? 0 : Integer.valueOf(priority));                                                   // priority
        String sql = "INSERT INTO public.showcases_collection(" +
                (id != null ? "id, " : "") + "created, modified, kind, title, created_by_id, modified_by_id, " +
                "slug, can_publish, publish_date, published, priority) " +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Создали коллекцию " + title);
        dbCLeaner.addEntity("CMS", "public.showcases_collection", "title", title);
    }

    /**
     * Публикация коллекции по id
     *
     * @param userName    пользователь, который публикует коллекцию
     * @param id          идентификатор
     * @param publishDate дата публикации в формате dd.MM.yyyy HH:mm:ss. По умолчанию текущее время
     * @throws ParseException ошибка парсинга даты
     */
    @Дано("^пользователь \"([^\"]*)\" публикует коллекцию c id \"([^\"]*)\"" +
            "(?: \"([0-9]{2}\\.[0-9]{2}\\.[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})\")*$")
    public void publicCollectionById(String userName, String id, String publishDate)
            throws ParseException {
        Timestamp timestamp;
        if (publishDate != null) {
            timestamp = convertHelper.convertStringToTimestamp(publishDate, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        } else {
            String publishDateFromDb;
            try (Handle handle = dbHelper.getDbCms().open()) {
                String sql = "SELECT publish_date FROM public.showcases_collection WHERE id=?;";
                publishDateFromDb = handle.createQuery(sql).bind(0, id).mapTo(String.class).findOnly();
            }
            timestamp = publishDateFromDb == null ?
                    convertHelper.getActualTimestamp() :
                    convertHelper.convertStringToTimestamp(publishDateFromDb, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        }
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(dbHelper.getUserIdByName(userName));                                                                 // modified_by_id
        values.add(true);                                                                                               // can_publish
        values.add(timestamp);                                                                                          // publish_date
        values.add(timestamp.getTime() >= new Date().getTime());                                                        // published
        values.add(Integer.valueOf(id));                                                                                // id
        String sql = "UPDATE public.showcases_collection " +
                "SET modified=?, modified_by_id=?, can_publish=?, publish_date=?, published=? WHERE id=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
    }

    /**
     * Публикация коллекции по названию
     *
     * @param userName    пользователь, который публикует коллекцию
     * @param title       название коллекции
     * @param publishDate дата публикации в формате dd.MM.yyyy HH:mm:ss. По умолчанию текущее время
     * @throws ParseException ошибка парсинга даты
     */
    @Дано("^пользователь \"([^\"]*)\" публикует коллекцию \"([^\"]*)\"" +
            "(?: \"([0-9]{2}\\.[0-9]{2}\\.[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})\")*$")
    public void publicCollection(String userName, String title, String publishDate)
            throws ParseException {
        Timestamp timestamp;
        if (publishDate != null) {
            timestamp = convertHelper.convertStringToTimestamp(publishDate, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        } else {
            String publishDateFromDb;
            try (Handle handle = dbHelper.getDbCms().open()) {
                String sql = "SELECT publish_date FROM public.showcases_collection WHERE title=?;";
                publishDateFromDb = handle.createQuery(sql).bind(0, title).mapTo(String.class).findOnly();
            }
            timestamp = publishDateFromDb == null ?
                    convertHelper.getActualTimestamp() :
                    convertHelper.convertStringToTimestamp(publishDateFromDb, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        }
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(dbHelper.getUserIdByName(userName));                                                                 // modified_by_id
        values.add(true);                                                                                               // can_publish
        values.add(timestamp);                                                                                          // publish_date
        values.add(timestamp.getTime() >= new Date().getTime());                                                        // published
        values.add(title);                                                                                              // title
        String sql = "UPDATE public.showcases_collection " +
                "SET modified=?, modified_by_id=?, can_publish=?, publish_date=?, published=? WHERE title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали коллекцию " + title);
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет в коллекцию \"([^\"]*)\" фильм \"([^\"]*)\"$")
    public void addMovieToCollection(String userName, String title, String movie) {
        int collectionId = dbHelper.getCollectionIdByName(title);
        int contentId = dbHelper.getContentIdByTechnicalName(movie);
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sql = "SELECT MAX(\"number\") FROM public.showcases_collectioncontent WHERE collection_id=?;";
            String result = handle.createQuery(sql).bind(0, collectionId).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        int userId = dbHelper.getUserIdByName(userName);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(++number);                                                                                           // "number"
        values.add(collectionId);                                                                                       // collection_id
        values.add(contentId);                                                                                          // content_id
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.showcases_collectioncontent" +
                "(created, modified, \"number\", collection_id, content_id, created_by_id, modified_by_id)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили фильм " + movie + " в коллекцию " + title);
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("collection_id", collectionId);
        pairs.put("content_id", contentId);
        dbCLeaner.addEntity("CMS", "public.showcases_collectioncontent", pairs);
    }

    @Тогда("^удаляем из коллекци \"([^\"]*)\" фильм \"([^\"]*)\"$")
    public void deleteContentFromCollection(String title, String movie) {
        String sql = "DELETE FROM public.showcases_collectioncontent WHERE collection_id=? AND content_id=?;";
        dbHelper.executeSqlScript(sql, dbHelper.getCollectionIdByName(title), dbHelper.getContentIdByTechnicalName(movie));
        System.out.println("Удалили фильм " + movie + " из коллекции " + title);
    }

    @Тогда("^удаляем из коллекци \"([^\"]*)\" весь контент$")
    public void deleteAllContentFromCollection(String title) {
        String sql = "DELETE FROM public.showcases_collectioncontent WHERE collection_id=?;";
        dbHelper.executeSqlScript(sql, dbHelper.getCollectionIdByName(title));
    }
}
