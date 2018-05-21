package cn.qingchengfit.staffkit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.DrawerChangeEvent;
import cn.qingchengfit.staffkit.views.custom.DrawerImgItem;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/1 2016.
 */
@SuppressWarnings("unused") public class DrawerFragment extends BaseFragment implements View.OnClickListener {

	ImageView headerIcon;
	FrameLayout headerIconOut;
	TextView drawerName;
	RelativeLayout drawerHeaderview;
	LinearLayout oemActs;
	LinearLayout drawerModules;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
      headerIcon = (ImageView) view.findViewById(R.id.header_icon);
      headerIconOut = (FrameLayout) view.findViewById(R.id.header_icon_out);
      drawerName = (TextView) view.findViewById(R.id.drawer_name);
      drawerHeaderview = (RelativeLayout) view.findViewById(R.id.drawer_headerview);
      oemActs = (LinearLayout) view.findViewById(R.id.oem_acts);
      drawerModules = (LinearLayout) view.findViewById(R.id.drawer_modules);

      oemActs.setVisibility(View.VISIBLE);
        oemActs.addView(new DrawerImgItem(getContext(), R.drawable.ic_camara, "text", this));
        oemActs.addView(new DrawerImgItem(getContext(), R.drawable.ic_camara, "text", this));
        oemActs.addView(new DrawerImgItem(getContext(), R.drawable.ic_camara, "text", this));
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onClick(View v) {
        RxBus.getBus().post(new DrawerChangeEvent(1));
    }

    @Override public String getFragmentName() {
        return DrawerFragment.class.getName();
    }
}
