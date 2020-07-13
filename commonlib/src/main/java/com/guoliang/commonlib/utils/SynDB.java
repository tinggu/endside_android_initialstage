package com.guoliang.commonlib.utils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SynDB {
    public interface ISynHelper {
        void doThing();
    }

    public static void startSyn(final ISynHelper synHelper) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (synHelper != null) {
                    synHelper.doThing();
                }
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }
}
