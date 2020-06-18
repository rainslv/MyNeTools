package com.lyhome.ETWeather.utility;

import java.io.InputStream;

/**
 * Created by lyhomeLY on 1/15/16.
 */
public interface HttpCallbackListener {
    void onFinish(InputStream in);

    void onError(Exception e);
}
