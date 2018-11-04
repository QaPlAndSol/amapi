package ru.pls.am.unit;

import org.junit.jupiter.api.Test;
import ru.pls.am.helpers.ConvertHelper;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConvertHelperTest {

    @Test
    void convertTextToSlug() {
        assertEquals("kollektsiya-10", new ConvertHelper().convertTextToSlug("Коллекция 10"));
        assertEquals("polnometrazhnyij-film-10", new ConvertHelper().convertTextToSlug("Полнометражный фильм 10"));
        assertEquals("zhanr-10", new ConvertHelper().convertTextToSlug("Жанр 10"));
    }

    @Test
    void getNextSlug() {
        String slug = "polnometrazhnyij-film-10";
        List<String> slugs = new LinkedList<>();
        slugs.add("polnometrazhnyij-film-10-1");
        slugs.add("polnometrazhnyij-film-10-abra-10");
        slugs.add("polnometrazhnyij-film-10-15-asd");
        slugs.add("polnometrazhnyij-film-10-15asd");
        assertEquals("polnometrazhnyij-film-10", new ConvertHelper().getNextSlug(slug, slugs));
        slugs = new LinkedList<>();
        slugs.add("polnometrazhnyij-film-10");
        slugs.add("polnometrazhnyij-film-10-abra-10");
        slugs.add("polnometrazhnyij-film-10-15-asd");
        slugs.add("polnometrazhnyij-film-10-15asd");
        assertEquals("polnometrazhnyij-film-10-1", new ConvertHelper().getNextSlug(slug, slugs));
        slugs = new LinkedList<>();
        slugs.add("polnometrazhnyij-film-10");
        slugs.add("polnometrazhnyij-film-10-12");
        slugs.add("polnometrazhnyij-film-10-256");
        slugs.add("polnometrazhnyij-film-10-1");
        slugs.add("polnometrazhnyij-film-10-3");
        assertEquals("polnometrazhnyij-film-10-257", new ConvertHelper().getNextSlug(slug, slugs));
        slugs = new LinkedList<>();
        slugs.add("polnometrazhnyij-film-10");
        slugs.add("polnometrazhnyij-film-10-12");
        slugs.add("polnometrazhnyij-film-10-256");
        slugs.add("polnometrazhnyij-film-10-1");
        slugs.add("polnometrazhnyij-film-10-3");
        slugs.add("polnometrazhnyij-film-10-abra-10");
        slugs.add("polnometrazhnyij-film-10-15-asd");
        slugs.add("polnometrazhnyij-film-10-15asd");
        assertEquals("polnometrazhnyij-film-10-257", new ConvertHelper().getNextSlug(slug, slugs));
        slugs = new LinkedList<>();
        slugs.add("polnometrazhnyij-film-10");
        slugs.add("polnometrazhnyij-film-10-999999999999999999");
        assertEquals("polnometrazhnyij-film-10-1000000000000000000", new ConvertHelper().getNextSlug(slug, slugs));
    }
}