package ru.pls.am.stepdefs;

import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.junit.Assert;
import ru.pls.am.RestRequest;
import ru.pls.am.TestProperties;
import ru.pls.am.Util;

import java.io.File;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class RestApiStepDefs {

    private Response response;

    private Util util = new Util();

    @Когда("^отправляем ([^\"]*) запрос к сервису \"([^\"]*)\" с параметрами:$")
    public void getRequest(String method, String serviceName, DataTable table) throws Throwable {
        RestRequest restRequest = new RestRequest(method, util.loadServicesInfo().getService(serviceName).getBaseUrl());
        restRequest.addParams(table.asLists());

        System.out.println(restRequest);
        response = restRequest.send();
        System.out.println("Response body:");
        response.getBody().prettyPrint();
    }

    @Тогда("^json ответа сервера проходит валидацию по схеме \"([^\"]*)\"$")
    public void validateJsonSchema(String jsonSchema) throws Throwable {
        String filePath = new TestProperties().get(TestProperties.JSON_SCHEMA_PATH) + jsonSchema;
        response.then().assertThat().body(matchesJsonSchema(new File(filePath.replaceAll("\\\\", "/")).getAbsoluteFile()));
    }

    @Тогда("^статус-код ответа (\\d+)$")
    public void checkStatusCode(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Тогда("^json ответа сервера содержит поля:$")
    public void checkResponseBodyFields(DataTable params) throws Throwable {
        for (List<String> row : params.asLists()) {
            switch (row.get(1).toLowerCase()) {
                case "string":
                    Assert.assertEquals("Поле " + row.get(0),
                            row.get(2), response.jsonPath().getString(row.get(0)));
                    break;
                case "stringends":
                    Assert.assertTrue("Значение поля " + row.get(0) + " должно заканчиваться на " + row.get(2),
                            response.jsonPath().getString(row.get(0)).endsWith(row.get(2)));
                    break;
                case "integer":
                    Assert.assertEquals("Поле " + row.get(0),
                            Integer.valueOf(row.get(2)).intValue(), response.jsonPath().getInt(row.get(0)));
                    break;
                case "null":
                    Assert.assertNull("Поле " + row.get(0), response.jsonPath().get(row.get(0)));
                    break;
                case "count":
                    Assert.assertEquals("Количество объектов в массиве " + row.get(0),
                            Integer.valueOf(row.get(2)).intValue(), response.jsonPath().getList(row.get(0)).size());
                    break;
                default:
                    throw new Exception("Тип поля \"" + row.get(1) + "\" не определен!");
            }
        }
    }
}
