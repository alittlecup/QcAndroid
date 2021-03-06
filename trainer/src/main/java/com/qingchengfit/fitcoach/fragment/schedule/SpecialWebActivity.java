package com.qingchengfit.fitcoach.fragment.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.main.WebForHomeFragment;
import com.qingchengfit.fitcoach.fragment.main.WebForResumeFragment;

public class SpecialWebActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_order_preview);
      findViewById(R.id.send_friend).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          SpecialWebActivity.this.onClick();
        }
      });

      int to = getIntent().getIntExtra("to", 0);
        Fragment fragment = new Fragment();
        switch (to) {
            case 1:
                fragment = WebForResumeFragment.newInstance(Configs.Server + String.format(Configs.HOST_RESUME, App.coachid));
                break;
            default:
                fragment = WebForHomeFragment.newInstance(Configs.Server + String.format(Configs.HOST_STUDENT_PREVIEW, App.coachid + ""));
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_student_order_preview, fragment).commit();
    }

 public void onClick() {
        ShareDialogFragment.newInstance(App.gUser.username + "教练的主页", getString(R.string.share_hint_open_desc, App.gUser.username),
            App.gUser.avatar, Configs.Server + String.format(Configs.HOST_STUDENT_PREVIEW, App.coachid + ""))
            .show(getSupportFragmentManager(), "");
    }
}
