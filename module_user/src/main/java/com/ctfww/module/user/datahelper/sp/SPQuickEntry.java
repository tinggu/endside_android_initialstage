package com.ctfww.module.user.datahelper.sp;

import com.blankj.utilcode.util.SPStaticUtils;

public class SPQuickEntry {
    public static boolean isAdmin() {
        return Const.ROLE_ADMIN.equals(SPStaticUtils.getString(Const.ROLE));
    }
}
