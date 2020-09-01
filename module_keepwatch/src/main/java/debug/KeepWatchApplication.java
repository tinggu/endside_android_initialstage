package debug;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.base.BaseApplication;
import com.ctfww.commonlib.im.ConnectionStateMonitor;
import com.ctfww.commonlib.network.CloudClient;
import com.ctfww.commonlib.storage.sp.SPUtil;
import com.ctfww.module.baidumap.utils.BaiduMapUtils;
import com.ctfww.module.user.datahelper.Utils;

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
        Utils.start(this);
        com.ctfww.module.keepwatch.Utils.start(this);
        com.ctfww.module.desk.datahelper.Utils.start(this);
        com.ctfww.module.keyevents.datahelper.Utils.init(this);

        // 初始化tms
        com.ctfww.module.tms.network.CloudClient.getInstance().init();
//        com.ctfww.module.tms.datahelper.NetworkHelper.getInstance().startTimedRefresh(); // 改写到主界面出现后再获取token，这样有助于数据的刷新后及时显示

        // 初始化百度地圖
        BaiduMapUtils.init(this);

        com.ctfww.module.keepwatch.Utils.synData();

        // 开启IM
        connectionStateMonitor = new ConnectionStateMonitor();
        connectionStateMonitor.enable(this);

        LogUtils.getConfig().setLog2FileSwitch(true);
    }


}
