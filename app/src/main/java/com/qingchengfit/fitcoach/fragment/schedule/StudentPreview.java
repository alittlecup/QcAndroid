package com.qingchengfit.fitcoach.fragment.schedule;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import cn.qingchengfit.utils.ToastUtils;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.WebFragment;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2016/11/29.
 */

public class StudentPreview extends WebFragment {
    private String mCopyUrl;

    public static StudentPreview newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        StudentPreview fragment = new StudentPreview();
        fragment.setArguments(args);
        return fragment;
    }



    @Override public void initToolbar() {


        mTitle.setText("我的主页");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        guideToWechatLayout.setVisibility(View.GONE);
        mToobarActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //ShareDialogFragment.newInstance()
            }
        });
    }


    @Override public void onLoadedView() {
        super.onLoadedView();
    }

    @Override public void onWebFinish() {
        super.onWebFinish();
    }

    @OnClick({R.id.close_guide, R.id.copy_link_to_wechat, R.id.go_to_how})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_guide:
                guideToWechatLayout.setVisibility(View.GONE);
                break;
            case R.id.copy_link_to_wechat:
                ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mCopyUrl);
                ToastUtils.showS("链接已复制");
                break;
            case R.id.go_to_how:
                guideToWechatLayout.setVisibility(View.GONE);
                mWebviewWebView.loadUrl(Configs.Server + Configs.WECHAT_GUIDE);
                break;
        }
    }

}
