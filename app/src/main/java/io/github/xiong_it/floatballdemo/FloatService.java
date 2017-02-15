package io.github.xiong_it.floatballdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FloatService extends Service {
    public FloatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FloatBallManager.getInstance(this).showFloatWindow();
        return super.onStartCommand(intent, flags, startId);
    }
}
