package com.andrewkravets.service;

import com.andrewkravets.model.State;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StatusManager {

    private static final Logger LOG = org.apache.log4j.Logger.getLogger(StatusManager.class);

    private Map<String, State> states = new HashMap<String, State>();
    private Producer notifyProducer;
    private Integer numberOfRetry;

    public boolean hasErrorMessages(String host) {
        State state = getState(host);
        return !state.getErrorMessage().isEmpty();
    }

    public String getErrorMessage(String host) {
        State state = getState(host);
        return state.getErrorMessage();
    }

    public void setErrorMessage(String host, String message) {
        State state = getState(host);
        state.setErrorMessage(message);
    }

    private void clearState(String host) {
        states.get(host).clear();
    }

    public void manageError(String host, String error) {
        State state = getState(host);
        state.increaseDownTime();
        if (state.getDownTimes().equals(numberOfRetry)) {
            sendGoingDownMessage(host, error);
        }
        state.setErrorMessage(error);
    }

    private State getState(String host) {
        State state = states.get(host);
        if (state == null) {
            states.put(host, new State());
        }
        return states.get(host);
    }

    public void sendNormalizeMessage(String host) {
        Exchange exchange = notifyProducer.createExchange();
        exchange.getIn().setBody("Your server(" + host + ") is working again. The reason of the problem was: " + getErrorMessage(host));
        try {
            notifyProducer.process(exchange);
        } catch (Exception e) {
            LOG.error(e);
        }
        clearState(host);
    }

    public void sendGoingDownMessage(String host, String reason) {
        setErrorMessage(host, reason);
        Exchange exchange = notifyProducer.createExchange();
        exchange.getIn().setBody("Your server(" + host + ") is down. The reason of the problem is: " + reason);
        try {
            notifyProducer.process(exchange);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    @Autowired
    public void setNotifyEndpoint(Endpoint notifyEndpoint) {
        try {
            notifyProducer = notifyEndpoint.createProducer();
        } catch (Exception exc) {
            //actually we should never be here as DirectEndpoint.createProducer never throws an exception
            LOG.error(exc);
        }
    }

    public Integer getNumberOfRetry() {
        return numberOfRetry;
    }

    public void init() {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("application.properties");
        try {
            prop.load(stream);
            numberOfRetry = Integer.valueOf(prop.getProperty("number_of_retry"));
        } catch (IOException e) {
            LOG.error(e);
            numberOfRetry = 0;
        }
    }
}
