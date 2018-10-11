package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/17.
 */

public class FormStepFragment extends BaseFragment {
  List<String> stepTexts = new ArrayList<>();
  LinearLayout llText, llImage;
  TextView tv1, tv2, tv3;
  TextView top1, top2, top3;
  Button btnNext;

  public static FormStepFragment newInstance(List<String> texts) {
    FormStepFragment fragment = new FormStepFragment();
    Bundle bundle = new Bundle();
    bundle.putStringArrayList("steps", new ArrayList<>(texts));
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View root = inflater.inflate(R.layout.form_step_fragment, container, false);
    llText = root.findViewById(R.id.ll_text_container);
    llImage = root.findViewById(R.id.ll_top_container);
    tv1 = root.findViewById(R.id.text1);
    tv2 = root.findViewById(R.id.text2);
    tv3 = root.findViewById(R.id.text3);
    top1 = root.findViewById(R.id.tv_first);
    top2 = root.findViewById(R.id.tv_second);
    top3 = root.findViewById(R.id.tv_third);
    if (getArguments() != null) {
      ArrayList<String> steps = getArguments().getStringArrayList("steps");
      tv1.setText(steps.get(0));
      tv2.setText(steps.get(1));
      tv3.setText(steps.get(2));

      top1.setText("1");
      top2.setText("2");
      top3.setText("3");
    }
    setCurPosition(0);
    top1.setOnClickListener(v -> setCurPosition(0));
    top2.setOnClickListener(v -> setCurPosition(1));
    top3.setOnClickListener(v -> setCurPosition(2));
    btnNext = root.findViewById(R.id.btn_next);
    btnNext.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (v instanceof Button) {
          if (((Button) v).getText().equals("下一步")) {
            index += 1;
            setCurPosition(index);
            if (index == 2) {
              ((Button) v).setText("完成");
            }
          } else {
            ToastUtils.show("click");
          }
        }
      }
    });
    return root;
  }

  public void setFragments(List<? extends Fragment> fragments) {
    this.fragments.clear();
    this.fragments.addAll(fragments);
  }

  private List<Fragment> fragments = new ArrayList<>();

  @Override public int getLayoutRes() {
    return R.id.frag_container;
  }

  @Override public String getFragmentName() {
    return FormStepFragment.class.getName();
  }

  private int index;

  private void setCurPosition(int position) {
    index = position;
    setTopPosition(position);
    setCurFragmentPosition(position);
    setButtonText(position);
  }

  private void setButtonText(int position) {
    if (btnNext == null) return;
    if (position == 2) {
      btnNext.setText("完成");
    } else {
      btnNext.setText("下一步");
    }
  }

  private void setCurFragmentPosition(int position) {
    if (!fragments.isEmpty()) {
      Fragment fragment = fragments.get(position);
      stuff(fragment);
    }
  }

  private void hideALL() {
    for (Fragment fragment : fragments) {
      if (fragment.isAdded()) {
        getChildFragmentManager().beginTransaction().hide(fragment).commit();
      }
    }
  }

  private void stuffFragment(Fragment fragment) {
    if (!fragment.isAdded()) {
      hideALL();
      getChildFragmentManager().beginTransaction()
          .setCustomAnimations(cn.qingchengfit.widgets.R.anim.slide_right_in,
              cn.qingchengfit.widgets.R.anim.slide_left_out,
              cn.qingchengfit.widgets.R.anim.slide_left_in,
              cn.qingchengfit.widgets.R.anim.slide_right_out)
          .show(fragment)
          .addToBackStack(null)
          .commit();
    } else {
      getChildFragmentManager().beginTransaction()
          .setCustomAnimations(cn.qingchengfit.widgets.R.anim.slide_right_in,
              cn.qingchengfit.widgets.R.anim.slide_left_out,
              cn.qingchengfit.widgets.R.anim.slide_left_in,
              cn.qingchengfit.widgets.R.anim.slide_right_out)
          .add(R.id.frag_container, fragment)
          .addToBackStack(null)
          .commit();
    }
  }

  private void setTopPosition(int position) {
    if (llText == null || llImage == null) return;
    for (int i = 0; i < llText.getChildCount(); i++) {
      if (i <= position * 2) {
        llText.getChildAt(i).setEnabled(true);
        llImage.getChildAt(i).setEnabled(true);
      } else {
        llText.getChildAt(i).setEnabled(false);
        llImage.getChildAt(i).setEnabled(false);
      }
    }
  }
}
