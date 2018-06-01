package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;


import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.views.activity.BaseActivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.manage.ChooseGymFragmentBuilder;

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
 * Created by Paper on 2017/3/10.
 */

public class PopFromBottomActivity extends BaseActivity {

	Toolbar toolbar;
	TextView toolbarTitile;
	FrameLayout frag;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_frag);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
      frag = (FrameLayout) findViewById(R.id.frag);

      toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Fragment f = new Fragment();
        int to = getIntent().getIntExtra("to", 0);
        switch (to) {
            default:
                f = new ChooseGymFragmentBuilder((CoachService) getIntent().getParcelableExtra("service")).build();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frag, f).commit();
    }

    @Override public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitile.setText(title);
    }

    @Override protected boolean isFitSystemBar() {
        return false;
    }

    @Override public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_hold, R.anim.slide_bottom_out);
    }
}
