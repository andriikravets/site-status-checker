package com.andrewkravets.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConnectionHandlerTest {

    ConnectionHandler connectionHandler;

    @Before
    public void init(){
        connectionHandler = ConnectionHandler.getInstance();
    }

    @Test
    public void getClientTest(){
        assertNotNull("Check instantiating of the client", connectionHandler.getHttpClient());
    }



}
