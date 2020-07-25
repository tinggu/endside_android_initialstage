package debug;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.base.BaseApplication;
import com.ctfww.commonlib.im.ConnectionStateMonitor;
import com.ctfww.commonlib.network.CloudClient;
import com.ctfww.commonlib.storage.sp.SPUtil;
import com.ctfww.module.keepwatch.Utils;
import com.ctfww.module.user.datahelper.Util;

public class KeepWatchApplication extends BaseApplication {
    private final static String TAG = "KeepWatchApplication";

    private ConnectionStateMonitor connectionStateMonitor;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化SP，设置name
        SPUtil.setSPFileName("monitor");

        // 初始化cloud
        CloudClient.getInstance().init("http://www.littlepine.cn"); // http://39.98.147.77:8888

        // 初始化各个业务模块的网络模块和数据库模块
        Util.start(this);
        com.ctfww.module.keepwatch.Utils.start(this);

        // 初始化tms
        com.ctfww.module.tms.network.CloudClient.getInstance().init();
//        com.ctfww.module.tms.datahelper.NetworkHelper.getInstance().startTimedRefresh(); // 改写到主界面出现后再获取token，这样有助于数据的刷新后及时显示

        // 初始化百度地圖
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);

        Utils.synData();

        connectionStateMonitor = new ConnectionStateMonitor();
        connectionStateMonitor.enable(this);

        LogUtils.getConfig().setLog2FileSwitch(true);
    }


}
