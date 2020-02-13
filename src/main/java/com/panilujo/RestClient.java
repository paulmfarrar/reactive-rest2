package com.panilujo;

import io.reactivex.Flowable;
import io.vertx.core.http.RequestOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientRequest;
import io.vertx.reactivex.core.http.HttpClientResponse;

public class RestClient {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpClient client = vertx.createHttpClient();

        HttpClientRequest clientResponse = client.get(
                new RequestOptions()
                        .setHost("localhost")
                        .setPort(8080)
        );
        clientResponse.toFlowable()
                .doOnSubscribe(subscription -> clientResponse.end())
                .doOnError(thr -> System.err.println(thr.getMessage()))
                .forEach(data -> {
                    System.out.println("Read data: " + data.toString());
                });
//        flowable.forEach(data -> System.out.println("Read data: " + data.toString()));
    }

}
