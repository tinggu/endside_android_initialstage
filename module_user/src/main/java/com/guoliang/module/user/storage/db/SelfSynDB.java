package com.guoliang.module.user.storage.db;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.guoliang.commonlib.network.ICloudCallback;
import com.guoliang.commonlib.utils.SynDB;
import com.guoliang.module.user.datahelper.Util;
import com.guoliang.module.user.bean.UserInfoBean;
import com.guoliang.module.user.datahelper.DataHelper;
import com.guoliang.module.user.datahelper.CloudClient;
import com.guoliang.module.user.entity.UserInfo;

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
