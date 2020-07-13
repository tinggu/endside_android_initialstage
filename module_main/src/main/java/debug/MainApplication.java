package debug;

import com.guoliang.commonlib.base.BaseApplication;

/**
 *
 * 这个Application不能被业务组件中的代码引用，因为它的功能就是为了
 * 1. 使业务组件从BaseApplication中获取的全局Context生效
 * 2. 还有初始化数据之用
 *
 */

public class MainApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
