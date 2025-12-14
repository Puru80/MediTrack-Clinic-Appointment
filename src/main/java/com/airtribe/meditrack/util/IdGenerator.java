package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static IdGenerator instance;
    
    private AtomicLong currentId;

    private IdGenerator() {
        this.currentId = new AtomicLong(1L);
    }

    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public Long generateId() {
        return currentId.getAndIncrement();
    }

    public void reset() {
        currentId.set(1L);
    }

    public Long getCurrentId() {
        return currentId.get();
    }
}

