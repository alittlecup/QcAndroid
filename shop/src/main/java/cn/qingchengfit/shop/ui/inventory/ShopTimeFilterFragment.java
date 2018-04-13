package cn.qingchengfit.shop.ui.inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterCustomFragment;
import cn.qingchengfit.shop.R;

/**
 * Created by huangbaole on 2018/1/23.
 */

public class ShopTimeFilterFragment extends FilterCustomFragment {
  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    TextView textBack = view.findViewById(R.id.tv_absence_filter_reset);
    EditText editStart = view.findViewById(R.id.edit_absence_start);
    EditText editEnd = view.findViewById(R.id.edit_absence_end);
    textBack.setText(getString(R.string.reset));
    textBack.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        editStart.setText("开始日期");
        editEnd.setText("结束日期");
      }
    });
    return view;
  }

  public static ShopTimeFilterFragment newInstance(String title) {
    Bundle args = new Bundle();
    args.putString("title",title);
    ShopTimeFilterFragment fragment = new ShopTimeFilterFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
