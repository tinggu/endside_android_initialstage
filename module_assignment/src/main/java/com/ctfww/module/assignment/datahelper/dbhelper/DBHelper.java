package com.ctfww.module.assignment.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.AssignmentInfoDao;
import com.ctfww.module.assignment.entity.DaoMaster;
import com.ctfww.module.assignment.entity.DaoSession;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.assignment.entity.TodayAssignmentDao;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private AssignmentInfoDao assignmentInfoDao;
    private TodayAssignmentDao todayAssignmentDao;

    private static class Inner {
        private static final DBHelper INSTANCE = new DBHelper();
    }

    public static DBHelper getInstance() {
        return Inner.INSTANCE;
    }

    public void init(Context ctx) {
        if (ctx == null) {
            return;
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "assignment");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        assignmentInfoDao = daoSession.getAssignmentInfoDao();
        todayAssignmentDao = daoSession.getTodayAssignmentDao();
    }

    // 1. 与任务有关

    // 用于app增加任务
    public boolean addAssignment(AssignmentInfo info) {
        return AssignmentInfoDBHelper.add(assignmentInfoDao, info);
    }

    // 用于app对任务信息的修改（包括删除：将status置为“delete”状态）
    public void updateAssignment(AssignmentInfo info) {
        AssignmentInfoDBHelper.update(assignmentInfoDao, info);
    }

    // 获取需要同步上云的任务
    public List<AssignmentInfo> getNoSynAssignmentList() {
        return AssignmentInfoDBHelper.getNoSynList(assignmentInfoDao);
    }

    // 用于app查看某个任务详细信息
    // 用于app确实是否存在该任务
    public AssignmentInfo getAssignment(String groupId, int objectId, String userId, String type) {
        return AssignmentInfoDBHelper.get(assignmentInfoDao, groupId, objectId, userId, type);
    }

    public AssignmentInfo getAssignment(String groupId, int objectId, String userId, String weekDay, String type) {
        return AssignmentInfoDBHelper.get(assignmentInfoDao, groupId, objectId, userId, weekDay, type);
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public List<AssignmentInfo> getAssignmentList(String groupId) {
        return AssignmentInfoDBHelper.getList(assignmentInfoDao, groupId);
    }

    public List<AssignmentInfo> getAssignmentList(String groupId, String userId) {
        return AssignmentInfoDBHelper.getList(assignmentInfoDao, groupId, userId);
    }

    public List<AssignmentInfo> getWeekDayAssignmentList(String weekDay) {
        return AssignmentInfoDBHelper.getWeekDayList(assignmentInfoDao, weekDay);
    }

    public List<AssignmentInfo> getWeekDayAssignmentList(String groupId, String weekDay) {
        return AssignmentInfoDBHelper.getWeekDayList(assignmentInfoDao, groupId, weekDay);
    }

    public List<AssignmentInfo> getWeekDayAssignmentList(String groupId, String userId, String weekDay) {
        return AssignmentInfoDBHelper.getWeekDayList(assignmentInfoDao, groupId, userId, weekDay);
    }

    // 3. 与任务有关

    public boolean addTodayAssignment(TodayAssignment info) {
        return TodayAssignmentDBHelper.add(todayAssignmentDao, info);
    }

    public void updateTodayAssignment(TodayAssignment info) {
        TodayAssignmentDBHelper.update(todayAssignmentDao, info);
    }

    public void updateTodayAssignment(AssignmentInfo info) {
        TodayAssignment todayAssignment = getTodayAssignment(info.getGroupId(), info.getObjectId(), info.getUserId(), MyDateTimeUtils.getTodayStartTime(), info.getType());
        String weekDay = GlobeFun.getTodayWeekDayStr();
        if (todayAssignment == null) { // 今日本没有此任务
            if (-1 == info.getCircleType().indexOf(weekDay)) { // 更新的计划任务不涉及今天
                return;
            }

            todayAssignment = new TodayAssignment();
            todayAssignment.setAssignmentId(info.createTodayAssignmentId(info.getGroupId(), info.getObjectId(), info.getUserId(), info.getType()));
            todayAssignment.setObjectId(info.getObjectId());
            todayAssignment.setGroupId(info.getGroupId());
            todayAssignment.setUserId(info.getUserId());
            todayAssignment.setDayTimeStamp(MyDateTimeUtils.getTodayStartTime());
            todayAssignment.setStartTime(info.getStartTime());
            todayAssignment.setEndTime(info.getEndTime());
            todayAssignment.setFrequency(info.getFrequency());
            todayAssignment.setScore(info.getScore());
            todayAssignment.setType(info.getType());
            todayAssignment.setTimeStamp(info.getTimeStamp());
            todayAssignment.setStatus(info.getStatus());

            addTodayAssignment(todayAssignment);
        }
        else {
            if ("delete".equals(info.getStatus()) || -1 == info.getCircleType().indexOf(weekDay)) {
                todayAssignment.setStatus("delete");
                todayAssignment.setTimeStamp(info.getTimeStamp());
                updateTodayAssignment(todayAssignment);
            }
            else {
                todayAssignment.setStatus("reserve");
                todayAssignment.setStartTime(info.getStartTime());
                todayAssignment.setEndTime(info.getEndTime());
                todayAssignment.setFrequency(info.getFrequency());
                todayAssignment.setScore(info.getScore());
                todayAssignment.setTimeStamp(info.getTimeStamp());
                updateTodayAssignment(todayAssignment);
            }
        }
    }

    public TodayAssignment getTodayAssignment(String groupId, int deskId, String userId, long dayTimeStamp, String type) {
        return TodayAssignmentDBHelper.get(todayAssignmentDao, groupId, deskId, userId, dayTimeStamp, type);
    }

    public List<TodayAssignment> getTodayAssignmentList(String groupId, long dayTimeStamp) {
        return TodayAssignmentDBHelper.getList(todayAssignmentDao, groupId, dayTimeStamp);
    }

    public List<TodayAssignment> getTodayAssignmentList(String groupId, String userId, long dayTimeStamp) {
        return TodayAssignmentDBHelper.getList(todayAssignmentDao, groupId, userId, dayTimeStamp);
    }

    public List<TodayAssignment> getTodayAssignmentList(String groupId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getList(todayAssignmentDao, groupId, startTime, endTime);
    }

    public List<TodayAssignment> getTodayAssignmentList(String groupId, String userId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getList(todayAssignmentDao, groupId, userId, startTime, endTime);
    }

    public long getTodayAssignmentCount(String groupId, long dayTimeStamp) {
        return TodayAssignmentDBHelper.getCount(todayAssignmentDao, groupId, dayTimeStamp);
    }

    public long getTodayAssignmentCount(String groupId, String userId, long dayTimeStamp) {
        return TodayAssignmentDBHelper.getCount(todayAssignmentDao, groupId, userId, dayTimeStamp);
    }

    public long getTodayAssignmentCount(String groupId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getCount(todayAssignmentDao, groupId, startTime, endTime);
    }

    public long getTodayAssignmentCount(String groupId, String userId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getCount(todayAssignmentDao, groupId, userId, startTime, endTime);
    }

    public List<TodayAssignment> getFinishList(String groupId, long dayTimeStamp) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, dayTimeStamp);
        List<TodayAssignment> todayAssignmentListRet = new ArrayList<>();
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                todayAssignmentListRet.add(todayAssignment);
            }
        }

        return todayAssignmentListRet;
    }

    public List<TodayAssignment> getFinishList(String groupId, String userId, long dayTimeStamp) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, userId, dayTimeStamp);
        List<TodayAssignment> todayAssignmentListRet = new ArrayList<>();
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                todayAssignmentListRet.add(todayAssignment);
            }
        }

        return todayAssignmentListRet;
    }

    public List<TodayAssignment> getFinishList(String groupId, long startTime, long endTime) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, startTime, endTime);
        List<TodayAssignment> todayAssignmentListRet = new ArrayList<>();
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                todayAssignmentListRet.add(todayAssignment);
            }
        }

        return todayAssignmentListRet;
    }

    public List<TodayAssignment> getFinishList(String groupId, String userId, long startTime, long endTime) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, userId, startTime, endTime);
        List<TodayAssignment> todayAssignmentListRet = new ArrayList<>();
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                todayAssignmentListRet.add(todayAssignment);
            }
        }

        return todayAssignmentListRet;
    }

    public long getFinishCount(String groupId, long dayTimeStamp) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, dayTimeStamp);
        long ret = 0;
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                ++ret;
            }
        }

        return ret;
    }

    public long getFinishCount(String groupId, String userId, long dayTimeStamp) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, userId, dayTimeStamp);
        long ret = 0;
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                ++ret;
            }
        }

        return ret;
    }

    public long getFinishCount(String groupId, long startTime, long endTime) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, startTime, endTime);
        long ret = 0;
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                ++ret;
            }
        }

        return ret;
    }

    public long getFinishCount(String groupId, String userId, long startTime, long endTime) {
        List<TodayAssignment> todayAssignmentList = getTodayAssignmentList(groupId, userId, startTime, endTime);
        long ret = 0;
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            if (todayAssignment.isFinished()) {
                ++ret;
            }
        }

        return ret;
    }

    public List<TodayAssignment> getLeakList(String groupId, long dayTimeStamp) {
        return TodayAssignmentDBHelper.getLeakList(todayAssignmentDao, groupId, dayTimeStamp);
    }

    public List<TodayAssignment> getLeakList(String groupId, String userId, long dayTimeStamp) {
        return TodayAssignmentDBHelper.getLeakList(todayAssignmentDao, groupId, userId, dayTimeStamp);
    }

    public List<TodayAssignment> getLeakList(String groupId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getLeakList(todayAssignmentDao, groupId, startTime, endTime);
    }

    public List<TodayAssignment> getLeakList(String groupId, String userId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getLeakList(todayAssignmentDao, groupId, userId, startTime, endTime);
    }

    public long getLeakCount(String groupId, long dayTimeStamp) {
        return TodayAssignmentDBHelper.getLeakCount(todayAssignmentDao, groupId, dayTimeStamp);
    }

    public long getLeakCount(String groupId, String userId, long dyaTimeStamp) {
        return TodayAssignmentDBHelper.getLeakCount(todayAssignmentDao, groupId, userId, dyaTimeStamp);
    }

    public long getLeakCount(String groupId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getLeakCount(todayAssignmentDao, groupId, startTime, endTime);
    }

    public long getLeakCount(String groupId, String userId, long startTime, long endTime) {
        return TodayAssignmentDBHelper.getLeakCount(todayAssignmentDao, groupId, userId, startTime, endTime);
    }
}
