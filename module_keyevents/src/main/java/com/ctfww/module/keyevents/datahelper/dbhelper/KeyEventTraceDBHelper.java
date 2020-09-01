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

    public static List<KeyEventTrace> getCanSnatchList(KeyEventTraceDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventTraceDao.Properties.GroupId.eq(groupId), dao.queryBuilder().or(KeyEventTraceDao.Properties.Status.eq("create"), KeyEventTraceDao.Properties.Status.eq("free")))).list();
    }

    public static List<KeyEventTrace> getDoingList(KeyEventTraceDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventTraceDao.Properties.GroupId.eq(groupId), KeyEventTraceDao.Properties.UserId.eq(userId), dao.queryBuilder().or(KeyEventTraceDao.Properties.Status.eq("accepted"), KeyEventTraceDao.Properties.Status.eq("snatch")))).list();
    }
}
