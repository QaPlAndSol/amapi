package ru.pls.am.helpers.cms.content;

import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.AbstractHelper;
import ru.pls.am.helpers.ConvertHelper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

public class MovieHelper extends AbstractHelper {

    public void createGenreInMetadataContentGenre(int userId, int genreId, int movieId) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(movieId);                                                                                            // content_id
        values.add(userId);                                                                                             // created_by_id
        values.add(genreId);                                                                                            // genre_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.metadata_contentgenre" +
                "(created, modified, content_id, created_by_id, genre_id, modified_by_id)" +
                "VALUES(?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("genre_id", genreId);
        pairs.put("content_id", movieId);
        dbCLeaner.addEntity("CMS", "public.metadata_contentgenre", pairs);
        System.out.println("Создали связь фильма с жанром");
    }

    public void createMovieInContentMovie(String autocompleteTitle, int movieId) {
        String sql = "INSERT INTO public.content_movie (content_ptr_id) VALUES(?);";
        dbHelper.executeSqlScript(sql, movieId);
        dbCLeaner.addEntity("CMS", "public.content_movie", "content_ptr_id", movieId);
        System.out.println("Создали фильм " + autocompleteTitle);
    }

    public void createMovieInContentContent(String title, String id, String originalTitle, String autocompleteTitle,
                                            String slug, String releaseYear, String priority, int userId,
                                            int genreId) {
        slug = slug == null ? convertHelper.convertTextToSlug(title) : slug;
        List<String> slugs;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT slug FROM public.content_content WHERE slug like ?;";
            slugs = handle.createQuery(sqlSelect).bind(0, slug + "%").mapTo(String.class).list();
        }
        slug = convertHelper.getNextSlug(slug, slugs);
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(title);                                                                                              // title
        values.add(slug);                                                                                               // slug
        values.add(Integer.valueOf(releaseYear));                                                                       // release_year
        values.add(null);                                                                                               // end_year
        values.add("");                                                                                                 // short_description
        values.add("");                                                                                                 // long_description
        values.add(null);                                                                                               // tagline
        values.add("#000000");                                                                                          // tagline_color
        values.add(null);                                                                                               // publish_date
        values.add(false);                                                                                              // can_publish
        values.add(false);                                                                                              // is_preview
        values.add("");                                                                                                 // meta_title
        values.add("");                                                                                                 // meta_description
        values.add(null);                                                                                               // redirect_url
        values.add(true);                                                                                               // is_indexed
        values.add(null);                                                                                               // badge_id
        values.add(userId);                                                                                             // created_by_id
        values.add(genreId);                                                                                            // genre_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(originalTitle == null ? title : originalTitle);                                                      // original_title
        values.add(false);                                                                                              // published
        values.add(priority == null ? 0 : Integer.valueOf(priority));                                                   // priority
        values.add(null);                                                                                               // age_restrictions
        values.add(null);                                                                                               // rating_imdb
        values.add(null);                                                                                               // rating_kinopoisk
        values.add(null);                                                                                               // imdb_id
        values.add(null);                                                                                               // kinopoisk_id
        values.add(null);                                                                                               // rating_internal
        values.add(null);                                                                                               // rating_override
        values.add(null);                                                                                               // audio
        values.add(null);                                                                                               // subtitles
        values.add(autocompleteTitle);                                                                                  // autocomplete_title
        values.add("");                                                                                                 // quote
        String sql = "INSERT INTO public.content_content(" +
                (id != null ? "id, " : "") + "created, modified, title, slug, release_year, end_year, " +
                "short_description, long_description, tagline, tagline_color, publish_date, can_publish, is_preview, " +
                "meta_title, meta_description, redirect_url, is_indexed, badge_id, created_by_id, genre_id, " +
                "modified_by_id, polymorphic_ctype_id, original_title, published, priority, age_restrictions, " +
                "rating_imdb, rating_kinopoisk, imdb_id, kinopoisk_id, rating_internal, rating_override, audio, " +
                "subtitles, autocomplete_title, quote) " +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, (SELECT id FROM public.django_content_type WHERE app_label='content' AND model='movie'), ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.content_content", "autocomplete_title",
                autocompleteTitle);
        System.out.println("Создали контент-фильм " + autocompleteTitle);
    }

    public void addShortDescriptionToContentContent(int userId, String title, String shortDescription) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(shortDescription);                                                                                   // short_description
        values.add(title);                                                                                              // autocomplete_title
        String sql = "UPDATE public.content_content " +
                "SET modified=?, modified_by_id=?, short_description=? WHERE autocomplete_title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили короткое описание фильму " + title);
    }

    public void addLongDescriptionToContentContent(int userId, String title, String longDescription) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(longDescription);                                                                                    // long_description
        values.add(title);                                                                                              // title
        String sql = "UPDATE public.content_content " +
                "SET modified=?, modified_by_id=?, long_description=? WHERE autocomplete_title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили длинное описание фильму " + title);
    }

    public void addMetaTitleToContentContent(int userId, String title, String metaTitle) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(metaTitle);                                                                                          // meta_title
        values.add(title);                                                                                              // title
        String sql = "UPDATE public.content_content " +
                "SET modified=?, modified_by_id=?, meta_title=? WHERE autocomplete_title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили название для SEO фильму " + title);
    }

    public void addMetaTitleAndDescriptionToContentContent(int userId, String title, String metaTitle, String metaDescription) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        if (metaTitle != null) {
            values.add(metaTitle);                                                                                      // meta_title
        }
        values.add(metaDescription);                                                                                    // meta_description
        values.add(title);                                                                                              // title
        String sql = "UPDATE public.content_content " +
                "SET modified=?, modified_by_id=?, " +
                (metaTitle != null ? "meta_title=?, " : "") +
                "meta_description=? WHERE autocomplete_title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили название и описание для SEO фильму " + title);
    }

    public void checkChancePublishContentByName(String name) {
        try (Handle handle = dbHelper.getDbCms().open()) {
            String[] keys = {"short_description", "long_description", "meta_title", "meta_description"};
            StringBuilder sb = new StringBuilder("SELECT ");
            for (String key : keys) {
                sb.append(key).append(", ");
            }
            sb.reverse().delete(0, 2).reverse().append(" FROM public.content_content WHERE autocomplete_title=?;");
            Map<String, Object> props = handle.createQuery(sb.toString()).bind(0, name).mapToMap().findOnly();
            if (props.containsValue(null) || props.containsValue("")) {
                sb = new StringBuilder("Фильм не опубликован, не заполнены следующие поля: ");
                for (String key : props.keySet()) {
                    if (props.get(key) == null || String.valueOf(props.get(key)).isEmpty()) {
                        sb.append(key).append(", ");
                    }
                }
                sb.reverse().delete(0, 2).reverse().append("!");
                throw new RuntimeException(sb.toString());
            }
        }
    }

    public String getPublishDateContentByName(String name) {
        String publishDateFromDb;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sql = "SELECT publish_date FROM public.content_content WHERE autocomplete_title=?;";
            publishDateFromDb = handle.createQuery(sql).bind(0, name).mapTo(String.class).findOnly();
        }
        return publishDateFromDb;
    }

    public void publicContentContent(int userId, String name, String publishDate) throws ParseException {
        Timestamp timestamp = publishDate == null ? convertHelper.getActualTimestamp() :
                convertHelper.convertStringToTimestamp(publishDate, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(true);                                                                                               // can_publish
        values.add(timestamp);                                                                                          // publish_date
        values.add(timestamp.getTime() >= new Date().getTime());                                                        // published
        values.add(name);                                                                                               // name
        String sql = "UPDATE public.content_content " +
                "SET modified=?, modified_by_id=?, can_publish=?, publish_date=?, published=?" +
                " WHERE autocomplete_title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали фильм " + name);
    }

    public void publicEpisodeContentContentByParentNameAndNumber(int userId, String name, int number,
                                                                 String publishDate)
            throws ParseException {
        Timestamp timestamp = publishDate == null ? convertHelper.getActualTimestamp() :
                convertHelper.convertStringToTimestamp(publishDate, ConvertHelper.DD_MM_YYYY_HH_MM_SS);
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(true);                                                                                               // can_publish
        values.add(timestamp);                                                                                          // publish_date
        values.add(timestamp.getTime() >= new Date().getTime());                                                        // published
        values.add(name);                                                                                               // autocomplete_title
        values.add(number);                                                                                             // "number"
        String sql = "UPDATE public.content_content " +
                "SET modified=?, modified_by_id=?, can_publish=?, publish_date=?, published=?" +
                " WHERE id=(SELECT content_ptr_id FROM public.content_episode" +
                " WHERE episode_parent_id=(SELECT id FROM public.content_content" +
                " WHERE autocomplete_title=?) AND \"number\"=?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали эпизод " + number + " фильма " + name);
    }
}
