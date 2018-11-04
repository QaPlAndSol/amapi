package ru.pls.am.helpers.cms.techpages;

import org.jdbi.v3.core.Handle;
import ru.pls.am.helpers.AbstractHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FaqAnswerHelper extends AbstractHelper {

    public void createTechPagesFaqAnswer(int userId, int groupId, String name, String id) {
        int number;
        try (Handle handle = dbHelper.getDbCms().open()) {
            String sqlSelect = "SELECT MAX(\"number\") FROM public.tech_pages_faqanswer;";
            String result = handle.createQuery(sqlSelect).mapTo(String.class).findOnly();
            number = Integer.valueOf(result == null ? "0" : result);
        }
        List<Object> values = new LinkedList<>();
        if (id != null) {
            values.add(Integer.valueOf(id));                                                                            // id
        }
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(false);                                                                                              // published
        values.add(number);                                                                                             // "number"
        values.add(name);                                                                                               // question
        values.add("");                                                                                                 // answer
        values.add(userId);                                                                                             // created_by_id
        values.add(groupId);                                                                                            // group_id
        values.add(userId);                                                                                             // modified_by_id
        String sql = "INSERT INTO public.tech_pages_faqanswer(" +
                (id != null ? "id, " : "") +
                "created, modified, published, \"number\", question, answer, created_by_id, group_id, modified_by_id)" +
                "VALUES(" + (id != null ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        HashMap<String, Object> pairs = new HashMap<>();
        pairs.put("question", name);
        pairs.put("group_id", groupId);
        dbCLeaner.addEntity("CMS", "public.tech_pages_faqanswer", pairs);
        System.out.println("Создали вопрос " + name);
    }

    public void addAnswerToTechPagesFaqAnswer(int userId, String name, String answer) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(answer);                                                                                             // answer
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // question
        String sql = "UPDATE public.tech_pages_faqanswer " +
                "SET modified=?, answer=?, modified_by_id=? WHERE question=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали вопрос " + name);
    }

    public void publishTechPagesFaqAnswer(int userId, String name) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(true);                                                                                               // published
        values.add(userId);                                                                                             // modified_by_id
        values.add(name);                                                                                               // question
        String sql = "UPDATE public.tech_pages_faqanswer " +
                "SET modified=?, published=?, modified_by_id=? WHERE question=?;";
        dbHelper.executeSqlScript(sql, values.toArray());
        System.out.println("Опубликовали вопрос " + name);
    }
}