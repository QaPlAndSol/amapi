package ru.pls.am.helpers.cms.content;

import ru.pls.am.helpers.AbstractHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EpisodeHelper extends AbstractHelper {

    public void createGenreInMetadataContentGenre(int userId, int genreId, int episodeId) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(episodeId);                                                                                          // content_id
        values.add(userId);                                                                                             // created_by_id
        values.add(genreId);                                                                                            // genre_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.metadata_contentgenre" +
                "(created, modified, content_id, created_by_id, genre_id, modified_by_id)" +
                "VALUES(?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("genre_id", genreId);
        pairs.put("content_id", episodeId);
        dbCLeaner.addEntity("CMS", "public.metadata_contentgenre", pairs);
        System.out.println("Создали связь эпизода с жанром");
    }

    public void createEpisodeInContentEpisode(int episodeId, String episodeTitle, int episodeNumber, int movieId) {
        List<Object> values = new LinkedList<>();
        values.add(episodeId);                                                                                          // content_ptr_id
        values.add(null);                                                                                               // video_publish_date
        values.add(false);                                                                                              // can_publish_video
        values.add(episodeNumber);                                                                                      // "number"
        values.add(movieId);                                                                                            // episode_parent_id
        values.add(null);                                                                                               // episode_season_id
        values.add(null);                                                                                               // video_id
        values.add(false);                                                                                              // video_published
        String sql = "INSERT INTO public.content_episode " +
                "(content_ptr_id, video_publish_date, can_publish_video, \"number\", episode_parent_id, " +
                "episode_season_id, video_id, video_published) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.content_episode", "content_ptr_id", episodeId);
        System.out.println("Создали эпизод " + episodeTitle);
    }

    public void createEpisodeInContentContent(int userId, String id, String episodeTitle, String originalTitle,
                                              String autocompleteTitle, String slug, String releaseYear, int genreId) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(episodeTitle);                                                                                       // title
        values.add(slug);                                                                                               // slug
        values.add(Integer.valueOf(releaseYear));                                                                       // release_year
        values.add(null);                                                                                               // end_year
        values.add("");                                                                                                 // short_description
        values.add("");                                                                                                 // long_description
        values.add(null);                                                                                               // tagline
        values.add(null);                                                                                               // tagline_color
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
        values.add(originalTitle);                                                                                      // original_title
        values.add(false);                                                                                              // published
        values.add(0);                                                                                                  // priority
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
        values.add(null);                                                                                               // quote
        String sql = "INSERT INTO public.content_content(" +
                (id != null ? "id, " : "") +
                "created, modified, title, slug, release_year, end_year, short_description, long_description, " +
                "tagline, tagline_color, publish_date, can_publish, is_preview, meta_title, meta_description, " +
                "redirect_url, is_indexed, badge_id, created_by_id, genre_id, modified_by_id, polymorphic_ctype_id, " +
                "original_title, published, priority, age_restrictions, rating_imdb, rating_kinopoisk, imdb_id, " +
                "kinopoisk_id, rating_internal, rating_override, audio, subtitles, autocomplete_title, quote)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, (SELECT id FROM public.django_content_type WHERE app_label='content' AND model='episode'), ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.content_content",
                "autocomplete_title", autocompleteTitle);
        System.out.println("Создали контент-эпизод " + episodeTitle);
    }

    public void addVideoToContentEpisode(int userId, int id, int videoId) {
        List<Object> values = new LinkedList<>();
        values.add(videoId);                                                                                            // video_id
        values.add(id);                                                                                                 // content_ptr_id
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(userId);                                                                                             // modified_by_id
        values.add(id);                                                                                                 // id
        String sql = "UPDATE public.content_episode SET video_id=? WHERE content_ptr_id=?;" +
                "UPDATE public.content_content SET modified=?, modified_by_id=? WHERE id=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили эпизоду видео");
    }
}
