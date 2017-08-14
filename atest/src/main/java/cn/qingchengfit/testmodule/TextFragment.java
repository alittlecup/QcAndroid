package cn.qingchengfit.testmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.items.TextItem;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.ArrayList;

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
 * Created by Paper on 2017/7/25.
 */

public class TextFragment extends BaseFragment {
  CommonFlexAdapter commonFlexAdapter;
  @BindView(R.id.rv) RecyclerView rv;
  Unbinder unbinder;
  private LinearLayoutManager linearLayoutManager;
  private NestedFragmentFragment nestedFragmentFragment;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    nestedFragmentFragment = new NestedFragmentFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_test, container, false);
    unbinder = ButterKnife.bind(this, v);
    ButterKnife.findById(v, R.id.btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
            .replace(R.id.frag, new TextFragment())
            .addToBackStack(null)
            .commit();
      }
    });
    linearLayoutManager = new LinearLayoutManager(getContext());
    rv.setLayoutManager(linearLayoutManager);
    rv.setAdapter(commonFlexAdapter);
    if (savedInstanceState != null) {
      linearLayoutManager.scrollToPosition(savedInstanceState.getInt("pos", 0));
    }

    return v;
  }

  @Override public int getLayoutRes() {
    return R.id.frag_id;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(nestedFragmentFragment);
    for (int i = 0; i < 20; i++) {
      commonFlexAdapter.addItem(new TextItem("xx" + i, R.style.QcTextStyleLargeDark));
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putInt("pos", linearLayoutManager.findFirstCompletelyVisibleItemPosition());
    super.onSaveInstanceState(outState);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
