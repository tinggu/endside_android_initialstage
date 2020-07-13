package com.guoliang.commonlib.storage.sp;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SPUtils;

public class SPUtil {

    public static void setSPFileName(String fileName) {
        SPStaticUtils.setDefaultSPUtils(SPUtils.getInstance(fileName));
    }

}
