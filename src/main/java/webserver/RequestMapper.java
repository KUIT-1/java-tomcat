package webserver;

import controller.*;
import http.request.HttpRequest;
import http.request.RequestURL;
import http.response.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestMapper {
    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;

    private static final Map<String, Controller> controllers = new HashMap<>();
    private final Controller controller;

    public RequestMapper(HttpRequest httpRequest, HttpResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        controller = controllers.get(httpRequest.getUrl());
    }
    static {
        controllers.put(RequestURL.SIGNUP.getUrl(),new SignUpController());
        controllers.put(RequestURL.LOGIN_POST.getUrl(), new LoginController());
        controllers.put(RequestURL.USER_LIST.getUrl(), new ListController());
        controllers.put(RequestURL.INDEX.getUrl(), new HomeController());
        controllers.put("/",new HomeController());
    }
    //해당 url 이면 해당 controller 실행

    public void proceed() throws IOException {
        if (controller != null) {
            controller.execute(httpRequest, httpResponse);
            return;
        }
        httpResponse.forward(httpRequest.getUrl());
    }

}
