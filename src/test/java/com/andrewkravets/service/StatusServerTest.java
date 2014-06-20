package com.andrewkravets.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class StatusServerTest {
    
    StatusServer statusServer;
    
    @Before
    public void init(){
        statusServer = new StatusServer();
        statusServer.setUrl("http://testUrl");
    }

    @Test
    public void startTest(){
        statusServer.start();
        
        assertNotNull("Start polling service test", statusServer.getTimer());
    }


    @Test
    public void pollingTest(){
        statusServer.start();

        assertNotNull("Is server polled", statusServer.getTimer().purge());
    }

    @Test
    public void stopTest(){
        statusServer.stop();

        assertEquals("Stop polling service test", true, statusServer.isStop());
    }


}
