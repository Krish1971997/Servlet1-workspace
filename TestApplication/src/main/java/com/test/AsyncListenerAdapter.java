package com.test;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

class AsyncListenerAdapter implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent event) throws IOException {}
    
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {}
    
    @Override
    public void onError(AsyncEvent event) throws IOException {}
    
    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {}
}