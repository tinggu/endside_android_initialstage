package com.ctfww.module.tms.datahelper;

import com.blankj.utilcode.util.SPStaticUtils;

public class DataHelper {
    private DataHelper() {

    }

    private static class Inner {
        private static final DataHelper INSTANCE = new DataHelper();
    }

    public static DataHelper getInstance() {
        return DataHelper.Inner.INSTANCE;
    }

    public String getToken() {
        return SPStaticUtils.getString(SPConstant.USER_ACCESS_TOKEN);
    }
}
