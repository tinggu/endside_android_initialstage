package com.ctfww.commonlib.storage.db;

import android.content.Context;

public class DBHelper {
    public static DBHelper getInstance() {
        return DBHelper.Inner.INSTANCE;
    }

    private static class Inner {
        private static final DBHelper INSTANCE = new DBHelper();
    }

    private DBHelper(){
    }

    public void init(Context ctx) {
        if (ctx == null) {
            return;
        }
    }
}
