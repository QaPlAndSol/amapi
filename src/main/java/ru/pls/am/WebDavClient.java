package ru.pls.am;

import io.restassured.RestAssured;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WebDavClient {

    private String webDavUrl;

    private static List<String> files = new LinkedList<>();

    {
        try {
            webDavUrl = new TestProperties().get(TestProperties.WEB_DAV);
            webDavUrl = webDavUrl.endsWith("/") ? webDavUrl : webDavUrl + "/";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String url) {
        return RestAssured.get(webDavUrl + url).getStatusCode() == 200;
    }

    public boolean createDirectory(String url) {
        url = url.endsWith("/") ? url : url + "/";
        return RestAssured.request("mkcol", webDavUrl + url).getStatusCode() == 201;
    }

    public boolean delete(String url) {
        return RestAssured.delete(webDavUrl + url).getStatusCode() == 204;
    }

    public boolean putFile(String url, String filePath) {
        try {
            String testDataDirectory = new TestProperties().get(TestProperties.TEST_DATA);
            String[] split = url.split("/");
            String thisDirectory = "";
            for (int i = 0; i < split.length - 1; i++) {
                thisDirectory = thisDirectory + split[i] + "/";
                if (!exists(thisDirectory)) {
                    if (!createDirectory(thisDirectory)) {
                        return false;
                    }
                }
            }
            boolean isCreated = RestAssured.given().body(new File(testDataDirectory + filePath))
                    .put(webDavUrl + url).getStatusCode() == 201;
            if (isCreated) {
                files.add(url);
            }
            return isCreated;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String list(String url) {
        return RestAssured.get(webDavUrl + url).getBody().asString();
    }

    public void clear() {
        Collections.reverse(files);
        for (String file : files) {
            delete(file);
        }
        files = new LinkedList<>();
    }
}
