package com.andrewkravets.service;

import junit.framework.Assert;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static junit.framework.Assert.assertEquals;

public class StatusManagerTest {

    StatusManager statusManager;


    @Before
    public void init(){
        statusManager = new StatusManager();
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        statusManager.setNotifyEndpoint(endpoint);
    }

    @Test
    public void manageErrorTest(){
        //given
        String host = "http://testHost.com";
        String errorMessage = "Test error message";
        //when
        statusManager.manageError(host, errorMessage);
        //then
        assertEquals("Test storing of the error messages", errorMessage, statusManager.getErrorMessage(host));
    }

    @Test
    public void hasErrorsTest(){
        //given
        String host = "http://testHost.com";
        String errorMessage = "Test error message";
        //when
        statusManager.manageError(host, errorMessage);
        //then
        assertEquals("Test storing of the error messages", true, statusManager.hasErrorMessages(host));
    }

    @Test
    public void setErrorsTest(){
        //given
        String host = "http://testHost.com";
        String errorMessage = "Test error message";
        //when
        statusManager.setErrorMessage(host, errorMessage);
        //then
        assertEquals("Test storing of the error messages", true, statusManager.hasErrorMessages(host));
    }

    @Test
    public void initTest(){
        statusManager.init();

        assertEquals("Test system initialization", (Integer)3, statusManager.getNumberOfRetry());
    }

}
