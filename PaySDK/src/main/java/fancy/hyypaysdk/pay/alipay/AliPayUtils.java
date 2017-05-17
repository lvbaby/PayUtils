package fancy.hyypaysdk.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;


import com.alipay.sdk.app.PayTask;

import java.util.Map;

import fancy.hyypaysdk.pay.PayCallBack;

/**
 * Created by Hyy on 2016/10/9.
 */
public class AliPayUtils {
    private static final int SDK_PAY_FLAG = 1;

    private Activity activity;

    private PayCallBack callBack;

    private String orderInfo;

    private static AliPayUtils instance;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

//                    LogUtil.MyLog("resultStatus", "=======resultStatus=====" + resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        callBack.paySuccess();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        callBack.payCancle();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        callBack.payFaild(resultStatus);
                    }
                    break;
                }
                default:
                    break;
            }

            dismissValue();
        }
    };

    /**
     * 支付宝支付业务
     */
    public void aliPay(Activity activity, String orderStr, PayCallBack callBack) {
        if (callBack == null) {
            Toast.makeText(activity, "回调函数不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        this.callBack = callBack;
        this.activity = activity;
        this.orderInfo = orderStr;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AliPayUtils.this.activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private AliPayUtils() {

    }

    public static AliPayUtils getInstance() {
        if (instance == null) {
            instance = new AliPayUtils();
        }
        return instance;
    }

    private void dismissValue() {
        callBack = null;
        activity = null;
    }
}
