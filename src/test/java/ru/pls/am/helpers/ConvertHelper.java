package ru.pls.am.helpers;

import com.ibm.icu.text.Transliterator;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ConvertHelper {

    public static final String DD_MM_YYYY_HH_MM_SS_SSS = "dd.MM.yyyy HH:mm:ss.SSS";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd.MM.yyyy HH:mm:ss";
    public static final String DD_MM_YYYY = "dd.MM.yyyy";

    /**
     * Преобразует текст на русском языке в slug
     *
     * @param text текст на русском языке
     * @return slug
     */
    public String convertTextToSlug(String text) {
        Transliterator transliterator = Transliterator.getInstance("Russian-Latin/BGN");
        text = text.toLowerCase()
                .replaceAll("ъ", "")
                .replaceAll("ь", "")
                .replaceAll("й", "ij")
                .replaceAll(" ", "-");
        return transliterator.transliterate(text);
    }

    /**
     * Возвращает slug сгенерированный на основе уже имеющихся
     *
     * @param slug  предположительный slug
     * @param slugs список имеющихся slug
     * @return итоговый slug
     */
    public String getNextSlug(String slug, List<String> slugs) {
        if (slugs.contains(slug)) {
            LinkedList<String> slugStrings = new LinkedList<>();
            for (String s : slugs) {
                slugStrings.add(s.substring(slug.length()));
            }
            LinkedList<Long> slugInts = new LinkedList<>();
            for (String s : slugStrings) {
                if (s.matches("-\\d+")) {
                    slugInts.add(Long.valueOf(s.substring(1)));
                }
            }
            slug = slug + "-" + (slugInts.isEmpty() ? 1 : (Collections.max(slugInts) + 1));
        }
        return slug;
    }

    /**
     * Преобразует строковое значение времени в timestamp
     *
     * @param timestamp строковое значение времени
     * @param format    формат даты
     * @return время преобразованное в timestamp
     * @throws ParseException ошибка парсинга даты
     */
    public Timestamp convertStringToTimestamp(String timestamp, String format) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(timestamp);
        return new Timestamp(date.getTime());
    }

    /**
     * Возвращает timestamp текущего времени
     *
     * @return timestamp
     */
    public Timestamp getActualTimestamp() {
        return new Timestamp(new Date().getTime());
    }
}
