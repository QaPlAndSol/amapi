package ru.pls.am.stepdefs.cms.metadata;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Тогда;
import ru.pls.am.helpers.cms.metadata.GenreHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

import java.text.ParseException;

public class GenreStepDefs extends AbstractStepDefs {

    private GenreHelper genreHelper = new GenreHelper();

    /**
     * Создание жанра.
     * INSERT public.metadata_genre и public.catalogue_cataloguegenre.
     * Заполненные поля:
     *      id;
     *      Название жанра;
     *      Слаг.
     * Жанр при создании не публикуется.
     *
     * @param userName пользователь, который создает жанр
     * @param name     название
     * @param id       идентификатор
     * @param slug     слаг
     */
    @Дано("^пользователь \"([^\"]*)\" создает жанр \"([^\"]*)\"(?: с)*" +
            "(?: id \"([^\"]*)\")*" +
            "(?: slug \"([^\"]*)\")*$")
    public void createGenre(String userName, String name, String id, String slug) {
        int userId = dbHelper.getUserIdByName(userName);
        genreHelper.createMetadataGenre(id, name, slug, userId);
        int genreId = id != null ? Integer.valueOf(id) : dbHelper.getGenreIdByName(name);
        genreHelper.createCatalogueCatalogueGenre(userId, genreId);
    }

    /**
     * Добавление описания жанра по названию. UPDATE public.metadata_genre
     *
     * @param userName    пользователь, который добавляет описание жанра
     * @param name        название жанра
     * @param description описание жанра
     */
    @Дано("^пользователь \"([^\"]*)\" добавляет описание жанра \"([^\"]*)\"$")
    public void addDescriptionToGenreByName(String userName, String name, String description) {
        int userId = dbHelper.getUserIdByName(userName);
        genreHelper.addDescriptionToMetadataGenre(name, description, userId);
    }

    /**
     * Публикация жанра по названию.
     * UPDATE public.metadata_genre.
     * Технический флаг публикации ставится при условии, что дата публикации больше либо равна текущей
     *
     * @param userName    пользователь, который публикует жанр
     * @param name        название жанра
     * @param publishDate дата публикации в формате dd.MM.yyyy HH:mm:ss
     * @throws ParseException ошибка парсинга даты
     */
    @Дано("^пользователь \"([^\"]*)\" публикует жанр \"([^\"]*)\"" +
            "(?: \"([0-9]{2}\\.[0-9]{2}\\.[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})\")*$")
    public void publicGenreByName(String userName, String name, String publishDate)
            throws ParseException {
        int userId = dbHelper.getUserIdByName(userName);
        genreHelper.publishMetadataGenre(name, publishDate, userId);
    }

    /**
     * Удаление жанра по его названию. DELETE public.metadata_genre и public.catalogue_cataloguegenre
     *
     * @param name название жанра
     */
    @Тогда("^удаляем жанр \"([^\"]*)\"$")
    public void deleteGenreByName(String name) {
        genreHelper.deleteCatalogueCatalogueGenre(name);
        genreHelper.deleteMetadataGenre(name);
    }
}
