package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/9/28.
 */

public class FilterListFragment extends BaseListFragment implements
    FlexibleAdapter.OnItemClickListener{

  public static FilterListFragment newInstance(@ArrayRes int strs) {
    Bundle args = new Bundle();
    args.putInt("a", strs);
    FilterListFragment fragment = new FilterListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    isChild = true;
    View v = super.onCreateView(inflater, container, savedInstanceState);
    ViewGroup.LayoutParams p =  rv.getLayoutParams();
    p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    p.width = ViewGroup.LayoutParams.MATCH_PARENT;
    commonFlexAdapter.setStatus(SelectableAdapter.MODE_SINGLE);
    rv.setLayoutParams(p);
    rv.setBackgroundResource(R.color.white);
    initListener(this);
    List<FilterCommonLinearItem> items  =new ArrayList<>();
    if (getArguments() != null && getArguments().getInt("a", 0) != 0){
      try {
        for (String a : getResources().getStringArray(getArguments().getInt("a"))) {
          items.add(new FilterCommonLinearItem(a));
        }
        setDatas(items,1);
      }catch (Exception e){
        CrashUtils.sendCrash(e);
      }
    }

    return v;
  }

  @Override public int getNoDataIconRes() {
    return 0;
  }

  @Override public String getNoDataStr() {
    return null;
  }

  @Override public boolean onItemClick(int position) {
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyDataSetChanged();
    //RxBus.getBus().post(new );// TODO: 2017/10/10 发送选中消息
    return true;
  }
}
