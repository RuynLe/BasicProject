package com.example.ngan.basicproject.network;

import com.example.ngan.basicproject.network.services.ApiTask;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

public interface OnPostResponseListener {
    /**
     * Process Response on Child Classes
     *
     * @param task   ApiTask
     * @param status int
     * @return True if Task is Finished and Execute next Task
     */
    boolean onPostResponse(ApiTask task, int status);

    /**
     * Will Process on Child Classes
     *
     * @param task   ApiTask
     * @param status int
     * @return True if Response is Process on Child Classes
     */
    boolean willProcess(ApiTask task, int status);

}
