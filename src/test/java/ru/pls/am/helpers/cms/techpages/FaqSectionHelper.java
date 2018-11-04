package ru.pls.am.helpers.cms.techpages;

import ru.pls.am.WebDavClient;
import ru.pls.am.helpers.AbstractHelper;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class FaqSectionHelper extends AbstractHelper {

    public void createTechPagesFaqSection(int userId, String name, String id, int number) {
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(true);                                                                                               // all_countries
        values.add(true);                                                                                               // all_platforms
        values.add(true);                                                                                               // all_device_types
        values.add(true);                                                                                               // all_device_vendors
        values.add(true);                                                                                               // all_device_models
        values.add(false);                                                                                              // published
        values.add("");                                                                                                 // logo
        values.add(number);                                                                                             // "number"
        values.add(name);                                                                                               // title
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.tech_pages_faqsection(" +
                (id != null ? "id, " : "") +
                "created, modified, all_countries, all_platforms, all_device_types, all_device_vendors, " +
                "all_device_models, published, logo, \"number\", title, created_by_id, modified_by_id)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.tech_pages_faqsection", "title", name);
        System.out.println("Создали раздел часто задаваемых вопросов " + name);
    }

    public void addLogoToTechPagesFaqSection(int userId, String name, String logo) throws Exception {
        WebDavClient webDavClient = new WebDavClient();
        String webDavSubDirectory = "images/";
        String outputDirectory = "cms/faq_section_logo/qa/";
        String urlDirectory = webDavSubDirectory + outputDirectory;
        String assetDirectory = "images/cms/faqsectionlogo/";
        if (!webDavClient.putFile(urlDirectory + logo, assetDirectory + logo)) {
            throw new Exception();
        }
        logo = outputDirectory + logo;
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(logo);                                                                                               // logo
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // title
        String sql = "UPDATE public.tech_pages_faqsection " +
                "SET modified=?, logo=?, modified_by_id=? WHERE title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Добавили лого разделу часто задаваемых вопросов " + name);
    }

    public void addDeviceTypeToTechPagesFaqSection(int userId, String name, List<Integer> deviceTypeIds) {
    }

    public void addAllDeviceTypesToTechPagesFaqSection(int userId, String name) {
    }

    public void publishTechPagesFaqSection(int userId, String name) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(true);                                                                                               // published
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // title
        String sql = "UPDATE public.tech_pages_faqsection " +
                "SET modified=?, published=?, modified_by_id=? WHERE title=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали раздел часто задаваемых вопросов " + name);
    }

    public void createSeveralTechPagesFaqSection(int userId, int count, String name, int number) {
        List<Object> values = new LinkedList<>();
        StringBuilder sql = new StringBuilder();
        Timestamp actualTimestamp = convertHelper.getActualTimestamp();
        for (int i = 0; i < count; i++) {
            String nextName = name + "-" + i;
            values.add(actualTimestamp);                                                                                // created
            values.add(actualTimestamp);                                                                                // modified
            values.add(true);                                                                                           // all_countries
            values.add(true);                                                                                           // all_platforms
            values.add(true);                                                                                           // all_device_types
            values.add(true);                                                                                           // all_device_vendors
            values.add(true);                                                                                           // all_device_models
            values.add(true);                                                                                           // published
            values.add("");                                                                                             // logo
            values.add(number + i);                                                                                     // "number"
            values.add(nextName);                                                                                       // title
            values.add(userId);                                                                                         // created_by_id
            values.add(userId);                                                                                         // modified_by_id
            sql.append("INSERT INTO public.tech_pages_faqsection(" +
                    "created, modified, all_countries, all_platforms, all_device_types, all_device_vendors, " +
                    "all_device_models, published, logo, \"number\", title, created_by_id, modified_by_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        }
        dbHelper.executeSqlScript(sql.toString(), values.toArray());
        dbCLeaner.addLikeEntity("CMS", "public.tech_pages_faqsection", "title", name + "-%");
        System.out.println("Создали " + count + " разделов часто задаваемых вопросов " + name);
    }
}