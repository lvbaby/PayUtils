package fancy.hyypaysdk.pay;

/**
 * Created by Hyy on 2016/10/9.
 */
public interface PayCallBack {

    abstract void paySuccess();

    abstract void payFaild(String faildCode);

    abstract void payCancle();
}
