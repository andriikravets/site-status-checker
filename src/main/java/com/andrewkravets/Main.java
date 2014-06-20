package com.andrewkravets;

import com.andrewkravets.service.StatusServer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static final Logger LOG = org.apache.log4j.Logger.getLogger(Main.class);

    public static void main(String[] args) {
        org.apache.camel.spring.Main main = new org.apache.camel.spring.Main();
        main.setApplicationContextUri("camel-context.xml");
        try {
            main.start();
        } catch (Exception e) {
            LOG.error(e);
        }

        final StatusServer statusServer = (StatusServer) main.getApplicationContext().getBean("statusServer");
        statusServer.setInterval(10);
        statusServer.setUrl(getUrl());
        statusServer.start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while(true) {
                    if (scanner.hasNext()) {
                        if (scanner.next().equals("stop")) {
                            statusServer.stop();
                            System.exit(1);
                        }
                    }
                }
            }
        }).start();

    }

    private static String getUrl(){
        String url;
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("application.properties");
        try {
            prop.load(stream);
            url = prop.getProperty("testUrl");
        } catch (IOException e) {
            LOG.error(e);
            url = "";
        }
        return url;
    }
}

