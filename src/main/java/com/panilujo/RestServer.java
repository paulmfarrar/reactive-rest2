package com.panilujo;

import io.vertx.core.Future;

import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.ext.web.api.validation.ValidationException;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class RestServer extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RestServer());
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);



        httpServer.requestHandler(router).listen(8080);

        HTTPRequestValidationHandler validationHandler = HTTPRequestValidationHandler.create()
                .addQueryParam("parameterName", ParameterType.INT, false)
//                .addFormParamWithPattern("formParameterName", "a{4}", true)
                .addPathParam("pathParam", ParameterType.FLOAT);

        router.route().handler(BodyHandler.create());

        router.get("/").handler(routingContext -> {

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");

            System.out.println("Request ended: " + routingContext.request().absoluteURI());
        });

        router.get("/awesome/:pathParam")
                // Mount validation handler
                .handler(validationHandler)
                //Mount your handler
                .handler((routingContext) -> {
                    System.out.println("Request received: " + routingContext.request().absoluteURI());
                    // Get Request parameters container
                    RequestParameters params = routingContext.get("parsedParameters");

                    // Get parameters
//                    Integer parameterName = params.queryParameter("parameterName") != null ? params.queryParameter("parameterName").getInteger() : null;
//                    String formParameterName = params.formParameter("formParameterName").getString();
                    Float pathParam = params.pathParameter("pathParam").getFloat();

                    // Write to the response and end it
//                    routingContext.response().write("parameterName: " + parameterName);
                    routingContext.response().end("pathParams: " + pathParam);
                })

                //Mount your failure handler
                .failureHandler((routingContext) -> {
                    Throwable failure = routingContext.failure();
                    System.out.println("Request failed: " + routingContext.request().absoluteURI());

                    if (failure instanceof ValidationException) {
                        // Something went wrong during validation!
                        String validationErrorMessage = failure.getMessage();
                        System.err.println(validationErrorMessage);
                    } else {
                        failure.printStackTrace();
                    }
                });
    }


}
