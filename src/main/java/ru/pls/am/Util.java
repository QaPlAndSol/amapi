package ru.pls.am;

import com.google.gson.Gson;
import ru.pls.am.data.service.ServicesInfo;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class Util {

    private static String servicesInfoFilePath;

    public String fileRead(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fin = new FileInputStream(filePath)) {
            int i;
            while ((i = fin.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return sb.toString();
    }

    public ServicesInfo loadServicesInfo() throws IOException {
        if (servicesInfoFilePath == null) {
            servicesInfoFilePath = new TestProperties().get(TestProperties.SERVICES_INFO);
        }
        return new Gson().fromJson(new FileReader(servicesInfoFilePath),
                ServicesInfo.class);
    }
}
