package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.widgets.R;
import java.util.HashMap;

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
 * Created by Paper on 2017/9/27.
 */

public abstract class BaseFilterFragment extends BaseFragment {
  private HashMap<String, Object> filters = new HashMap<>();
  int showIndex;
  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_base_filter, container, false);
    view.findViewById(R.id.bg_filter).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    return view;
  }

  public void showPage(int index){
    if (index < getTags().length) {
      showIndex = index;
      showPage(getTags()[index]);
    }
  }

  public int getCurIndex(){
    return showIndex;
  }

  /**
   * 根据Tag展示Fragment
   */
  protected void showPage(String tag) {
    Fragment f = getChildFragmentManager().findFragmentByTag(tag);
    FragmentTransaction tr = getChildFragmentManager().beginTransaction();
    //先隐藏所有其他fragment
    for (String s : getTags()) {
      if (!s.equalsIgnoreCase(tag)) {
        if (getChildFragmentManager().findFragmentByTag(s) != null) {
          tr.hide(getChildFragmentManager().findFragmentByTag(s));
        }
      }
    }
    //如果没有先生成
    if (f == null) {
      f = getFragmentByTag(tag);
      tr.setCustomAnimations(R.anim.slide_hold, R.anim.slide_hold);
      tr.add(R.id.frag_base_filter,f,tag);
    }
    //展示
    tr.show(f);
    tr.commit();
  }

  public void dismiss(){
    getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
  }

  protected abstract String[] getTags();

  protected abstract Fragment getFragmentByTag(String tag);
}
