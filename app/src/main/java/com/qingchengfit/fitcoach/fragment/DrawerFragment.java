package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CustomSetmentLayout;
import com.qingchengfit.fitcoach.component.SegmentLayout;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/10/10 2015.
 */
public class DrawerFragment extends Fragment {
    Gson gson;
    @Bind(R.id.header_icon)
    ImageView headerIcon;
    @Bind(R.id.drawer_name)
    TextView drawerName;
    @Bind(R.id.drawer_headerview)
    RelativeLayout drawerHeaderview;
    @Bind(R.id.drawer_radiogroup)
    CustomSetmentLayout drawerRadiogroup;
    @Bind(R.id.drawer_modules)
    LinearLayout drawerModules;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_drawer, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();

        String u = PreferenceUtils.getPrefString(getContext(), "user_info", "");
        if (!TextUtils.isEmpty(u)) {
            user = gson.fromJson(u, User.class);
            App.setgUser(user);
        } else {
            //TODO ERROR
        }
        drawerName.setText(user.username);
        int gender = R.drawable.img_default_female;
        if (user.gender == 0)
            gender = R.drawable.img_default_male;
        if (TextUtils.isEmpty(user.avatar)) {
            Glide.with(App.AppContex)
                    .load(gender)
                    .asBitmap()
                    .into(new CircleImgWrapper(headerIcon, App.AppContex));
        } else {
            Glide.with(App.AppContex)
                    .load(user.avatar)
                    .asBitmap()
                    .into(new CircleImgWrapper(headerIcon, App.AppContex));
        }


        String id = PreferenceUtils.getPrefString(getContext(), "coach", "");
        if (TextUtils.isEmpty(id)) {
            //TODO error
        }
        Coach coach = gson.fromJson(id, Coach.class);
        App.coachid = Integer.parseInt(coach.id);
        SegmentLayout button = new SegmentLayout(getContext());
        button.setText("日程安排");
        SegmentLayout button1 = new SegmentLayout(getContext());
        button.setText("数据报表");
        SegmentLayout button2 = new SegmentLayout(getContext());
        button.setText("会议培训");
//        button.setDrawables();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
