package com.andrewkravets.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.ConnectException;

public class ConnectionHandler {
    private static HttpClient httpClient = null;

    private static ConnectionHandler handler = null;

    private ConnectionHandler(){
        httpClient = new DefaultHttpClient();
    }

    public static ConnectionHandler getInstance() {
        if(handler==null){
            synchronized (ConnectionHandler.class){
                if(handler==null){
                    handler = new ConnectionHandler();
                }
            }
        }
        return handler;
    }


    public HttpClient getHttpClient() {
        return httpClient;
    }
}
