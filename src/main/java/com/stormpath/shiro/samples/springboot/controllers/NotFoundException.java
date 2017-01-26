package com.stormpath.shiro.samples.springboot.controllers;

public class NotFoundException extends Exception {

    public NotFoundException(String id) {
        super(id);
    }
}
