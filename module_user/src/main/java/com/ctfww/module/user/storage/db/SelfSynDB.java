package com.ctfww.module.user.storage.db;

import com.ctfww.commonlib.utils.SynDB;

public class SelfSynDB extends SynDB implements SynDB.ISynHelper {
    private final static String TAG = "SelfSynDB";

    private final static String SYN_TIME_KEY = "userSynTime";

    private static class Inner {
        private static final SelfSynDB INSTANCE = new SelfSynDB();
    }

    public static SelfSynDB getInstance() {
        return SelfSynDB.Inner.INSTANCE;
    }

    public void startSyn() {
        startSyn(this);
    }

    @Override
    public void doThing() {
        synUserInfo();
    }

    private void synUserInfo() {

    }


}
