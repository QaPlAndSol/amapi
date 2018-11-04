package ru.pls.am.stepdefs;

import cucumber.api.java.ru.Когда;
import ru.pls.am.TestProperties;

import java.io.File;
import java.io.FileWriter;

public class FileStepDefs {

    @Когда("^сохраняем в файл \"([^\"]*)\"$")
    public void saveToFile(String fileName, String fileBody) throws Throwable {
        TestProperties testProperties = new TestProperties();
        String temp_files_path = testProperties.get(TestProperties.TEMP_FILES_PATH);
        File fileDir = new File(temp_files_path);
        File file = new File(temp_files_path + fileName);
        fileDir.mkdirs();
        if (!file.exists()) {
            file.createNewFile();
        }
        if (file.exists()) {
            FileWriter writer = new FileWriter(file);
            writer.write(fileBody);
            writer.flush();
            writer.close();
        } else {
            throw new Exception();
        }
    }
}
