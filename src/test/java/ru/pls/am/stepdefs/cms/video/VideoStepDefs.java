package ru.pls.am.stepdefs.cms.video;

import cucumber.api.PendingException;
import cucumber.api.java.ru.Дано;
import ru.pls.am.helpers.cms.video.VideoHelper;
import ru.pls.am.stepdefs.AbstractStepDefs;

public class VideoStepDefs extends AbstractStepDefs {

    private VideoHelper videoHelper = new VideoHelper();

    @Дано("^пользователь \"([^\"]*)\" создает видеофайл \"([^\"]*)\" с исходником на \"([^\"]*)\"" +
            " и приоритетом обработки \"(низкий|нормальный|высокий)\"$")
    public void createVideo(String user, String name, String path, String priority) {
        // TODO не готов, надо добавить заливку видео на WebDAV
        throw new PendingException();
//        switch (priority) {
//            default:
//                priority = "1";
//        }
//        videoHelper.createVideoVideo(dbHelper.getUserIdByName(user), name, path, Integer.valueOf(priority));
    }
}
