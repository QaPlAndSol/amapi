package ru.pls.am.stepdefs.cms.content;

import cucumber.api.PendingException;
import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.content.MovieHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class MovieStepDefs extends AbstractStepDefs {

    private EpisodeStepDefs episodeStepDefs = new EpisodeStepDefs();
    private MovieHelper movieHelper = new MovieHelper();

    /**
     * Создание полнометражного фильма.
     * INSERT public.content_content, public.content_movie и public.metadata_contentgenre для фильма.
     * INSERT public.content_content, public.content_episode и public.metadata_contentgenre для эпизода,
     * если фильм полнометражный.
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
     * Фильм при создании не публикуется.
     *
     * @param userName          пользователь
     * @param type              тип фильма
     * @param title             название
     * @param id                id
     * @param originalTitle     оригинальное название
     * @param autocompleteTitle техническое название
     * @param slug              слаг
     * @param releaseYear       год премьеры
     * @param genreName         основной жанр
     * @param priority          приоритет для каталога
     */
    @Дано("^пользователь \"([^\"]*)\" создает (полнометражный|многосерийный) фильм \"([^\"]*)\" с" +
            "(?: id \"([^\"]*)\")*" +
            "(?: оригинальным названием \"([^\"]*)\")*" +
            "(?: техническим названием \"([^\"]*)\")*" +
            "(?: slug \"([^\"]*)\")*" +
            " годом премьеры \"([^\"]*)\"" +
            " жанром \"([^\"]*)\"" +
            "(?: приоритетом \"([^\"]*)\")*$")
    public void createMovie(String userName, String type, String title, String id, String originalTitle,
                            String autocompleteTitle, String slug, String releaseYear, String genreName,
                            String priority) {
        int userId = dbHelper.getUserIdByName(userName);
        int genreId = dbHelper.getGenreIdByName(genreName);
        autocompleteTitle = autocompleteTitle == null ? title : autocompleteTitle;
        movieHelper.createMovieInContentContent(title, id, originalTitle, autocompleteTitle, slug, releaseYear,
                priority, userId, genreId);
        int movieId = id == null ? dbHelper.getContentIdByTechnicalName(autocompleteTitle) : Integer.valueOf(id);
        movieHelper.createMovieInContentMovie(autocompleteTitle, movieId);
        movieHelper.createGenreInMetadataContentGenre(userId, genreId, movieId);
        if ("полнометражный".equals(type)) {
            episodeStepDefs.createEpisode(userName, null, autocompleteTitle, null, null,
                    null, autocompleteTitle + " Серия 1", null, releaseYear, genreName);
        }
    }

    /**
     * Добавляет фильму короткое описание по техническому названию.
     * UPDATE public.content_content для фильма.
     *
     * @param userName         пользователь
     * @param title            техническое название
     * @param shortDescription короткое описание
     */
    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" короткое описание:$")
    public void addShortDescriptionToMovieByName(String userName, String title, String shortDescription) {
        movieHelper.addShortDescriptionToContentContent(dbHelper.getUserIdByName(userName), title, shortDescription);
    }

    /**
     * Добавляет фильму длинное описание по техническому названию.
     * UPDATE public.content_content для фильма.
     *
     * @param userName        пользователь
     * @param title           техническое название
     * @param longDescription длинное описание
     */
    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" длинное описание:$")
    public void addLongDescriptionToMovieByName(String userName, String title, String longDescription) {
        movieHelper.addLongDescriptionToContentContent(dbHelper.getUserIdByName(userName), title, longDescription);
    }

    /**
     * Добавляет фильму название для SEO по техническому названию.
     * UPDATE public.content_content для фильма.
     *
     * @param userName  пользователь
     * @param title     техническое название
     * @param metaTitle название для SEO
     */
    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" название для SEO \"([^\"]*)\"$")
    public void addMetaTitleToMovieByName(String userName, String title, String metaTitle) {
        movieHelper.addMetaTitleToContentContent(dbHelper.getUserIdByName(userName), title, metaTitle);
    }

    /**
     * Добавляет фильму название и описание для SEO по техническому названию.
     * UPDATE public.content_content для фильма.
     *
     * @param userName        пользователь
     * @param title           техническое название
     * @param metaTitle       название для SEO
     * @param metaDescription описание для SEO
     */
    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\"(?: название для SEO \"([^\"]*)\")*" +
            " описание для SEO:")
    public void addMetaTitleAndDescriptionToMovieByName(String userName, String title, String metaTitle,
                                                        String metaDescription) {
        movieHelper.addMetaTitleAndDescriptionToContentContent(dbHelper.getUserIdByName(userName),
                title, metaTitle, metaDescription);
    }

    /**
     * Публикация фильма по техническому названию.
     * UPDATE public.content_content для фильма и эпизода.
     * Проверка заполненных полей:
     * - Короткое описание;
     * - Длинное описание;
     * - Название для SEO;
     * - Описание для SEO.
     * Технический флаг публикации ставится при условии, что дата публикации больше либо равна текущей
     *
     * @param userName    пользователь
     * @param name        техническое название
     * @param publishDate дата публикации в формате dd.MM.yyyy HH:mm:ss
     * @throws ParseException ошибка парсинга даты
     */
    @Дано("^пользователь \"([^\"]*)\" публикует (полнометражный|многосерийный) фильм \"([^\"]*)\"" +
            "(?: \"([0-9]{2}\\.[0-9]{2}\\.[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})\")*$")
    public void publicMovieByName(String userName, String type, String name, String publishDate) throws ParseException {
        movieHelper.checkChancePublishContentByName(name);
        int userId = dbHelper.getUserIdByName(userName);
        if (publishDate == null) {
            publishDate = movieHelper.getPublishDateContentByName(name);
        }
        movieHelper.publicContentContent(userId, name, publishDate);
        if ("полнометражный".equals(type)) {
            movieHelper.publicEpisodeContentContentByParentNameAndNumber(userId, name, 1, publishDate);
        }
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" возрастное ограничение \"([^\"]*)\"$")
    public void addAgeRestrictionToMovieByName(String userName, String title, String ageRestrictions) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(dbHelper.getUserIdByName(userName));                                                                 // modified_by_id
        values.add(ageRestrictions);                                                                                    // age_restrictions
        values.add(title);                                                                                              // title
        String sql = "UPDATE public.content_content " +
                "SET modified=?, modified_by_id=?, age_restrictions=? WHERE autocomplete_title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили возрастное ограничение фильму " + title);
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\"" +
            " рейтинг IMDB \"([^\"]*)\"" +
            " рейтинг Кинопоиска \"([^\"]*)\"" +
            " id IMDB \"([^\"]*)\"" +
            " id Кинопоиска \"([^\"]*)\"" +
            " внутренний рейтинг \"([^\"]*)\"" +
            " ручной рейтинг \"([^\"]*)\"$")
    public void addRatingToMovieByName(String userName, String title, String ratingImdb, String ratingKinopoisk,
                                       String imdbId, String kinopoiskId, String ratingInternal,
                                       String ratingOverride) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" бэйдж \"([^\"]*)\"$")
    public void addBadgeToMovieByName(String userName, String title, String badge) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" текст лэйбла \"([^\"]*)\" цвет лэйбла \"([^\"]*)\"$")
    public void addTagLineToMovieByName(String userName, String title, String tagline, String taglineColor) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" страницу редиректа после удаления \"([^\"]*)\"$")
    public void addRedirectUrlToMovieByName(String userName, String title, String redirectUrl) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" (разрешает|запрещает) фильму \"([^\"]*)\" индексироваться поисковиками$")
    public void changeIndexToMovieByName(String userName, String isIndexed, String title) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" языки видео \"([^\"]*)\"$")
    public void addVideoLanguageToMovieByName(String userName, String title, String video) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" языки субтитров \"([^\"]*)\"$")
    public void addSubtitlesLanguageToMovieByName(String userName, String title, String subtitle) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" страны производители \"([^\"]*)\"$")
    public void addCountriesToMovieByName(String userName, String title, String countries) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" теги \"([^\"]*)\"$")
    public void addTagsToMovieByName(String userName, String title, String tags) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" жанры \"([^\"]*)\"$")
    public void addGenresToMovieByName(String userName, String title, String genres) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Дано("^пользователь \"([^\"]*)\" добавляет фильму \"([^\"]*)\" студии \"([^\"]*)\"$")
    public void addStudiosToMovieByName(String userName, String title, String studio) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
