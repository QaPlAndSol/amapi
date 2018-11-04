package ru.pls.am.stepdefs.cms.content;

import cucumber.api.java.ru.Дано;
import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.cms.content.EpisodeHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.util.List;

public class EpisodeStepDefs extends AbstractStepDefs {

    private EpisodeHelper episodeHelper = new EpisodeHelper();

    /**
     * Создание эпизода фильма.
     * INSERT public.content_content, public.content_episode и public.metadata_contentgenre.
     * Заполненные поля:
     * - id;
     * - Название;
     * - Оригинальное название;
     * - Техническое название;
     * - Приоритет для каталога;
     * - Слаг;
     * - Год премьеры;
     * - Цвет лейбла - черный;
     * - Основной жанр;
     * - Индексация поисковиками - true.
     * Эпизод при создании не публикуется.
     *
     * @param user              пользователь
     * @param episodeTitle      название
     * @param movieTitle        название фильма
     * @param number            номер эпизода
     * @param id                id
     * @param originalTitle     оригинальное название
     * @param autocompleteTitle техническое название
     * @param slug              слаг
     * @param releaseYear       год премьеры
     * @param genreName         основной жанр
     */
    @Дано("^пользователь \"([^\"]*)\" создает" +
            " эпизод(?: \"([^\"]*)\")*" +
            " к фильму \"([^\"]*)\" с" +
            " номером \"([^\"]*)\"" +
            "(?: id \"([^\"]*)\")*" +
            "(?: оригинальным названием \"([^\"]*)\")*" +
            " техническим названием \"([^\"]*)\"" +
            "(?: slug \"([^\"]*)\")*" +
            " годом премьеры \"([^\"]*)\"" +
            " жанром \"([^\"]*)\"$")
    public void createEpisode(String user, String episodeTitle, String movieTitle, String number, String id,
                              String originalTitle, String autocompleteTitle, String slug, String releaseYear,
                              String genreName) {
        int userId = dbHelper.getUserIdByName(user);
        int genreId = dbHelper.getGenreIdByName(genreName);
        int movieId = dbHelper.getContentIdByTechnicalName(movieTitle);
        episodeTitle = episodeTitle == null ? "Эпизод 1" : episodeTitle;
        originalTitle = originalTitle == null ? "Эпизод 1" : originalTitle;
        slug = slug == null ? convertHelper.convertTextToSlug(episodeTitle) : slug;
        List<String> slugs;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT slug FROM public.content_content WHERE slug like ?;";
            slugs = handle.createQuery(sqlSelect).bind(0, slug + "%").mapTo(String.class).list();
        }
        slug = convertHelper.getNextSlug(slug, slugs);
        episodeHelper.createEpisodeInContentContent(userId, id, episodeTitle, originalTitle, autocompleteTitle, slug,
                releaseYear, genreId);
        int episodeId = id == null ? dbHelper.getContentIdByTechnicalName(autocompleteTitle) : Integer.valueOf(id);
        Integer episodeNumber;
        if (number == null) {
            try (Handle handle = dbHelper.getDbCms().open()) {
                String sqlSelect = "SELECT MAX(\"number\") FROM public.content_episode WHERE episode_parent_id=?;";
                episodeNumber = handle.createQuery(sqlSelect).bind(0, movieId).mapTo(Integer.class).findOnly();
                episodeNumber = (episodeNumber == null ? 0 : episodeNumber) + 1;
            }
        } else {
            episodeNumber = Integer.valueOf(number);
        }
        episodeHelper.createEpisodeInContentEpisode(episodeId, episodeTitle, episodeNumber, movieId);
        episodeHelper.createGenreInMetadataContentGenre(userId, genreId, episodeId);
    }

    /**
     * Добавляет эпизоду видео.
     * UPDATE public.content_episode.
     *
     * @param user  пользователь
     * @param title техническое название эпизода
     * @param video нразвание видео
     */
    @Дано("^пользователь \"([^\"]*)\" добавляет эпизоду \"([^\"]*)\" видео \"([^\"]*)\"$")
    public void addVideoToEpisode(String user, String title, String video) {
        int userId = dbHelper.getUserIdByName(user);
        int videoId = dbHelper.getVideoIdByName(video);
        int episodeId = dbHelper.getContentIdByTechnicalName(title);
        episodeHelper.addVideoToContentEpisode(userId, episodeId, videoId);
    }
}
