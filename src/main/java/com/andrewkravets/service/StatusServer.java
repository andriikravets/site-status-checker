package com.andrewkravets.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class StatusServer {

    private static final Logger LOG = org.apache.log4j.Logger.getLogger(StatusServer.class);

    @Autowired
    private StatusManager manager;

    private Timer timer;
    private String url;
    private int interval;
    private boolean stop = false;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void start() {
        timer = new Timer();
        timer.schedule(new PollingTT(), interval);
    }

    public Timer getTimer() {
        return timer;
    }

    public boolean isStop() {
        return stop;
    }

    public void stop() {
        this.stop = true;
    }

    private class PollingTT extends TimerTask {

        @Override
        public void run() {
            while (!isStop()) {
                HttpClient client = ConnectionHandler.getInstance().getHttpClient();
                HttpGet request = new HttpGet(getUrl());
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() >= 400) {
                        manager.setErrorMessage(url, "Server return incorrect response: " + response.getStatusLine().getStatusCode());
                    } else {
                        if (manager.hasErrorMessages(url)) {
                            manager.sendNormalizeMessage(url);
                        }
                    }
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    manager.manageError(url, "Server unavailable");
                    LOG.error(e);
                }
            }
            this.cancel();
        }
    }
}
