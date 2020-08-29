package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.keepwatch.entity.DaoMaster;
import com.ctfww.module.keepwatch.entity.DaoSession;
import com.ctfww.module.keepwatch.entity.PersonTrends;
import com.ctfww.module.keepwatch.entity.Ranking;
import com.ctfww.module.keepwatch.entity.SigninInfo;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDeskDao;
import com.ctfww.module.keepwatch.entity.PersonTrendsDao;
import com.ctfww.module.keepwatch.entity.RankingDao;
import com.ctfww.module.keepwatch.entity.SigninInfoDao;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private SigninInfoDao signinInfoDao;
    private PersonTrendsDao personTrendsDao;
    private RankingDao rankingDao;
    private KeepWatchStatisticsByDeskDao keepWatchStatisticsByDeskDao;

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

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "keepwatch");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        signinInfoDao = daoSession.getSigninInfoDao();
        personTrendsDao = daoSession.getPersonTrendsDao();
        rankingDao = daoSession.getRankingDao();
        keepWatchStatisticsByDeskDao = daoSession.getKeepWatchStatisticsByDeskDao();
    }

    // 10. 与签到有关

    // 存储签到信息，用于签到
    public boolean addSignin(SigninInfo signinInfo) {
        return SigninDBHelper.add(signinInfoDao, signinInfo);
    }

    // 获取未同步上云的签到，用于签到信息的同步上云
    public List<SigninInfo> getNoSynSigninList() {
        return SigninDBHelper.getNoSynList(signinInfoDao);
    }

    // 用于同步上云后更改同步状态
    public void updateSignin(SigninInfo signin) {
        SigninDBHelper.update(signinInfoDao, signin);
    }

    // 30. 与每日统计相关

    // 用于app增加动态
    public boolean addPersonTrends(PersonTrends personTrends) {
        return PersonTrendsDBHelper.add(personTrendsDao, personTrends);
    }

    // 用于app对动态的修改
    public void updatePersonTrends(PersonTrends personTrends) {
        PersonTrendsDBHelper.update(personTrendsDao, personTrends);
    }

    // 获得动态列表
    public List<PersonTrends> getPersonTrends(String groupId) {
        return PersonTrendsDBHelper.getList(personTrendsDao, groupId);
    }

    // 删除所有动态
    public void deletePersonTrends() {
        PersonTrendsDBHelper.delete(personTrendsDao);
    }

    // 用于app增加排行
    public boolean addRanking(Ranking ranking) {
        return RankingDBHelper.add(rankingDao, ranking);
    }

    // 用于app对动态的修改
    public void updateRanking(Ranking ranking) {
        RankingDBHelper.update(rankingDao, ranking);
    }

    // 获得排行列表
    public List<Ranking> getRanking(String groupId) {
        return RankingDBHelper.getList(rankingDao, groupId);
    }

    // 删除所有排行
    public void deleteRanking() {
        RankingDBHelper.delete(rankingDao);
    }
}
