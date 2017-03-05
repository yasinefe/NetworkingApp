package com.networkingapp.util;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Clock {

    public Date now() {
        return new Date();
    }

}
