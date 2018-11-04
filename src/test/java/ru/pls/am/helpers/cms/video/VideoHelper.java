package ru.pls.am.helpers.cms.video;

import ru.pls.am.helpers.AbstractHelper;

import java.util.LinkedList;
import java.util.List;

public class VideoHelper extends AbstractHelper {

    public void createVideoVideo(int userId, String name, String path, int priority) {
        List<Object> values = new LinkedList<>();
        values.add(convertHelper.getActualTimestamp());                                                                 // created
        values.add(convertHelper.getActualTimestamp());                                                                 // modified
        values.add(name);                                                                                               // title
        values.add(path);                                                                                               // source_url
        values.add(null);                                                                                               // result_url
        values.add(priority);                                                                                           // priority
        values.add("done");                                                                                             // status
        values.add(null);                                                                                               // story_start_offset
        values.add(null);                                                                                               // story_end_offset
        values.add(userId);                                                                                             // created_by_id
        values.add(userId);                                                                                             // modified_by_id
        values.add(null);                                                                                               // error
        values.add(null);                                                                                               // opening_credits_offset
        values.add(null);                                                                                               // encoding_end_offset
        values.add(null);                                                                                               // encoding_start_offset
        String sql = "INSERT INTO public.video_video" +
                "(created, modified, title, source_url, result_url, priority, status, story_start_offset, " +
                "story_end_offset, created_by_id, modified_by_id, error, opening_credits_offset, " +
                "encoding_end_offset, encoding_start_offset)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        dbHelper.executeSqlScript(sql, values.toArray());
        dbCLeaner.addEntity("CMS", "public.video_video", "title", name);
        System.out.println("Создали видео " + name);
    }
}
