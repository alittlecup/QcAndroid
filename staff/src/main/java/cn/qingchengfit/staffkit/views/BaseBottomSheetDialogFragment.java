package cn.qingchengfit.staffkit.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.util.Pair;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.activity.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/11/24.
 */

public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private List<Pair<String, Observable>> observables = new ArrayList<>();

    @Override public void onDestroyView() {
        for (int i = 0; i < observables.size(); i++) {
            RxBus.getBus().unregister(observables.get(i).first, observables.get(i).second);
        }
        super.onDestroyView();
    }

    /**
     * 跳转当前模块
     */
    public void routeTo(String model, String path, Bundle bd) {
        String uri = model + path;
        if (!uri.startsWith("/")) uri = "/" + uri;
        if (getActivity() instanceof BaseActivity) {
            routeTo(AppUtils.getRouterUri(getContext(), uri), bd);
        }
    }

    /**
     * 跳转到其他模块
     */
    protected void routeTo(String uri, Bundle bd) {
        routeTo(uri, bd, false);
    }

    protected void routeTo(String uri, Bundle bd, boolean b) {
        if (!uri.startsWith("/")) uri = "/" + uri;
        if (getActivity() instanceof BaseActivity) {
            routeTo(Uri.parse(AppUtils.getCurAppSchema(getContext())
              + "://"
              + ((BaseActivity) getActivity()).getModuleName()
              + uri), bd, b);
        }
    }
    /**
     * 根据uri 跳转
     */
    protected void routeTo(Uri uri, Bundle bd) {
        routeTo(uri, bd, false);
    }

    protected void routeTo(Uri uri, Bundle bd, boolean newActivity) {
        try {
            Intent to = new Intent(Intent.ACTION_VIEW, uri);
            if (getActivity() instanceof BaseActivity) {
                if (((BaseActivity) getActivity()).getModuleName().equalsIgnoreCase(uri.getHost())
                    //&& !uri.getPath().startsWith("/choose")
                  ) {
                    to.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                } else {
                    to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
            if (bd != null) {
                to.putExtras(bd);
            }
            startActivity(to);
        } catch (Exception e) {
            LogUtil.e("找不到模块去处理" + uri);
            CrashUtils.sendCrash(e);
        }
    }

    public <T> Observable<T> RxBusAdd(@NonNull Class<T> clazz) {
        Observable ob = RxBus.getBus().register(clazz);
        observables.add(new Pair<String, Observable>(clazz.getName(), ob));
        return ob;
    }

}
