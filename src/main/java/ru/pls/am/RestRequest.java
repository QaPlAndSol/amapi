package ru.pls.am;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestRequest {

    private StringBuilder url;
    private Map<String, Object> queries;
    private String body;
    private String method;

    public RestRequest(String method) {
        this.url = new StringBuilder();
        this.queries = new HashMap<>();
        this.method = method.toUpperCase();
    }

    public RestRequest(String method, String url) {
        this.url = new StringBuilder();
        this.queries = new HashMap<>();
        setUrl(url);
        this.method = method.toUpperCase();
    }

    public void setUrl(String endpoint) {
        this.url.append(endpoint.endsWith("/") ? endpoint : endpoint + "/");
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addQuery(String query, Object value) {
        this.queries.put(query, value);
    }

    public void addParam(String param, String type, String value) throws Exception {
        switch (param.toLowerCase()) {
            case "endpoint":
                for (String s : value.split("/")) {
                    if (!s.isEmpty()) {
                        setUrl(s);
                    }
                }
                break;
            case "query":
                addQuery(type, value);
                break;
            case "body":
                switch (type.toLowerCase()) {
                    case "file":
                        setBody(new Util().fileRead(new TestProperties().get(TestProperties.TEMP_FILES_PATH) + value));
                        break;
                    default:
                        setBody(value);
                }
                break;
            default:
                throw new Exception("Тип параметра не поддерживается");
        }
    }

    public void addParams(List<List<String>> params) throws Exception {
        for (List<String> row : params) {
            addParam(row.get(0), row.get(1), row.get(2));
        }
    }

    public Response send() {
        RequestSpecification request = RestAssured.given();
        request.queryParams(queries);
        if (body != null) {
            request.body(body);
        }
        return request.request(method, url.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(" ").append(url.toString());
        if (!queries.isEmpty()) {
            sb.append("?");
            for (String s : queries.keySet()) {
                sb.append(s).append("=").append(queries.get(s)).append("&");
            }
            sb.reverse().delete(0, 1).reverse();
        }
        if (body != null) {
            sb.append("\nBody:\n").append(body);
        }
        return sb.toString();
    }
}
