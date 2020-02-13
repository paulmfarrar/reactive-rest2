package com.panilujo;

import io.reactivex.Flowable;
import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.FlowableHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.file.AsyncFile;
import io.vertx.reactivex.core.file.FileSystem;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Vertx vertx = Vertx.vertx();
        FileSystem fs = vertx.fileSystem();
        fs.open("data.txt", new OpenOptions(), result -> {
            AsyncFile file = result.result();
            Flowable<Buffer> observable = file.toFlowable();
            observable.forEach(data -> System.out.println("Read data: " + data.toString("UTF-8")));
        });
    }
}
