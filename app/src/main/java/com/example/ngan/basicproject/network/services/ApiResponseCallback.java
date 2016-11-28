package com.example.ngan.basicproject.network.services;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

public interface ApiResponseCallback {
    /**
     * Process Response
     * @param task ApiTask
     * @return True if Task is finished and do next task
     */
    boolean onResponse(ApiTask task);
}
