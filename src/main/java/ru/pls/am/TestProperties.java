package ru.pls.am;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    public static final String JSON_SCHEMA_PATH = "json_schema_path";

    public static final String TEMP_FILES_PATH = "temp_files_path";

    static final String TEST_DATA = "test_data";

    static final String SERVICES_INFO = "services_info";

    static final String WEB_DAV = "web_dav";

    private static final String TEST_PROPERTIES_FILE_PATH = "src/test/resources/ru/pls/am/test.properties";

    private Properties properties;

    public TestProperties() throws IOException {
        FileInputStream fis = null;
        properties = new Properties();

        try {
            fis = new FileInputStream(TEST_PROPERTIES_FILE_PATH);
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fis != null;
            fis.close();
        }
    }

    public String get(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
