package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.Entity.KeyEventTraceDao;

import java.util.List;

public class KeyEventTraceDBHelper {
    private static final String TAG = "KeyEventTraceDBHelper";

    public static boolean add(KeyEventTraceDao dao, KeyEventTrace keyEventTrace) {
        try {
            dao.insert(keyEventTrace);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static boolean update(KeyEventTraceDao dao, KeyEventTrace keyEventTrace) {
        try {
            dao.update(keyEventTrace);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static KeyEventTrace get(KeyEventTraceDao dao, String eventId) {
        return dao.queryBuilder().where(KeyEventTraceDao.Properties.EventId.eq(eventId)).unique();
    }

    public static List<KeyEventTrace> getNoSynList(KeyEventTraceDao dao) {
        return dao.queryBuilder().where(KeyEventTraceDao.Properties.SynTag.eq("new")).list();
    }
}
