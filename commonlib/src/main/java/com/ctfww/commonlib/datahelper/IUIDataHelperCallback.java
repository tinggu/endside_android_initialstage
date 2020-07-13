package com.ctfww.commonlib.datahelper;

public interface IUIDataHelperCallback {
    /**
      * @param obj 返回的数据
     */
    void onSuccess(Object obj);

    /**
     * @param code 错误码
     */
    void onError(int code);
}
