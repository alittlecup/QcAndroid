package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventPulishPosition;
import cn.qingchengfit.recruit.model.PublishPosition;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TwoScrollPicker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fb on 2017/7/3.
 */

public class RecruitPositionDemanFragment extends BaseFragment {

  @BindView(R2.id.civ_work_exp) CommonInputView civWorkExp;
  @BindView(R2.id.civ_work_gender) CommonInputView civWorkGender;
  @BindView(R2.id.civ_work_age) CommonInputView civWorkAge;
  @BindView(R2.id.civ_work_education) CommonInputView civWorkEducation;
  @BindView(R2.id.civ_work_height) CommonInputView civWorkHeight;
  @BindView(R2.id.civ_work_weight) CommonInputView civWorkWeight;
  private TwoScrollPicker twoScrollPicker;
  private SimpleScrollPicker simpleScrollPicker;

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  private HashMap<String, Object> map = new HashMap<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    simpleScrollPicker = new SimpleScrollPicker(getContext());
    twoScrollPicker = new TwoScrollPicker(getContext());
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_position_work_ex, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar();
    return view;
  }

  private void initToolbar() {
    super.initToolbar(toolbar);
    toolbarTitle.setText("工作要求");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        RxBus.getBus().post(EventPulishPosition.build(map));
        return false;
      }
    });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  //选择工作经验
  @OnClick(R2.id.civ_work_exp) public void onSelectWorkExp() {
    String[] workexpStr = getContext().getResources().getStringArray(R.array.work_exp);
    final ArrayList<String> d = new ArrayList<>(Arrays.asList(workexpStr));
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (right > left){
          ToastUtils.show("请选择正确的条件区间");
          return;
        }else{
          civWorkExp.setContent(d.get(left) + "-" + d.get(right));
          map.put("min_work_year", left - 1);
          map.put("max_work_year", right - 1);
        }
      }
    });
    twoScrollPicker.show(d, d, 0, 0);//// TODO: 2017/6/8 选择时当前的位置
  }

  //选择性别
  @OnClick(R2.id.civ_work_gender) public void onSelectWorkGender() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.filter_gender)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civWorkGender.setContent(d.get(pos));
        map.put("gender", pos - 1);
      }
    });
    simpleScrollPicker.show(d, 0);
  }

  //选择年龄
  @OnClick(R2.id.civ_work_age) public void onSelectWorkAge() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 100; i++) {
      d.add(i + "岁");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (right > left){
          ToastUtils.show("请选择正确的条件区间");
          return;
        }else{
          civWorkAge.setContent(d.get(left) + "-" + d.get(right));
          map.put("min_age", left - 1);
          map.put("max_age", right - 1);
        }
      }
    });
    twoScrollPicker.show(d, d, 0, 0);
  }

  //选择学历
  @OnClick(R2.id.civ_work_education) public void onSelectWorkEducation() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.education_degree)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civWorkEducation.setContent(d.get(pos));
        map.put("education", pos - 1);
      }
    });
    simpleScrollPicker.show(d, 0);
  }

  //选择身高
  @OnClick(R2.id.civ_work_height) public void onSelectWorkHeight() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 250; i++) {
      d.add(i + "cm");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (right > left){
          ToastUtils.show("请选择正确的条件区间");
          return;
        }else{
          civWorkHeight.setContent(d.get(left) + "-" + d.get(right));
          map.put("min_height", left - 1);
          map.put("max_height", right - 1);
        }
      }
    });
    twoScrollPicker.show(d, d, 0, 0);
  }

  //选择体重
  @OnClick(R2.id.civ_work_weight) public void onSelectWorkWeight() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 250; i++) {
      d.add(i + "Kg");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (right > left){
          ToastUtils.show("请选择正确的条件区间");
          return;
        }else{
          civWorkWeight.setContent(d.get(left) + "-" + d.get(right));
          map.put("min_weight", left - 1);
          map.put("max_weight", right - 1);
        }
      }
    });
    twoScrollPicker.show(d, d, 0, 0);
  }
}
