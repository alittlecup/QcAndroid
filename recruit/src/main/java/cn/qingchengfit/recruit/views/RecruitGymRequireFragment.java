package cn.qingchengfit.recruit.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.databinding.FragmentRecruitPositionRequireBinding;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TwoScrollPicker;
import java.util.ArrayList;
import java.util.Arrays;

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
 * Created by Paper on 2017/6/8.
 */
public class RecruitGymRequireFragment extends BaseFragment {

  //@BindView(R2.id.civ_work_exp) CommonInputView civWorkExp;
  //@BindView(R2.id.civ_gender) CommonInputView civGender;
  //@BindView(R2.id.civ_age) CommonInputView civAge;
  //@BindView(R2.id.civ_education) CommonInputView civEducation;
  //@BindView(R2.id.civ_height) CommonInputView civHeight;
  //@BindView(R2.id.civ_weight) CommonInputView civWeight;
  //@BindView(R2.id.toolbar) Toolbar toolbar;
  //@BindView(R2.id.toolbar_title) TextView toolbarTitile;

  FragmentRecruitPositionRequireBinding db;

  SimpleScrollPicker simpleScrollPicker;
  TwoScrollPicker twoScrollPicker;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    simpleScrollPicker = new SimpleScrollPicker(getContext());
    twoScrollPicker = new TwoScrollPicker(getContext());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    db = DataBindingUtil.inflate(inflater,R.layout.fragment_recruit_position_require, container, false);
    initToolbar(db.layoutToolbar.toolbar);
    db.civAge.setOnClickListener(view -> onCivAgeClicked());
    db.civEducation.setOnClickListener(view -> onCivEducationClicked());
    db.civGender.setOnClickListener(view -> onCivGenderClicked());
    db.civHeight.setOnClickListener(view -> onCivHeightClicked());
    db.civWeight.setOnClickListener(view -> onCivWeightClicked());
    db.civWorkExp.setOnClickListener(view -> onCivWorkExpClicked());
    return db.getRoot();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    ((TextView)db.layoutToolbar.toolbarTitle).setText("职位要求");
  }

  @Override public String getFragmentName() {
    return RecruitGymRequireFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 工作经验
   */
  public void onCivWorkExpClicked() {
    String[] workexpStr = getContext().getResources().getStringArray(R.array.work_exp);
    final ArrayList<String> d = new ArrayList<>(Arrays.asList(workexpStr));
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        db.civWorkExp.setContent(d.get(left) + "-" + d.get(right));
      }
    });
    twoScrollPicker.show(d, d, 0, 0);//// TODO: 2017/6/8 选择时当前的位置
  }

   public void onCivGenderClicked() {
    final ArrayList<String> d = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.gender)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        db.civGender.setContent(d.get(pos));
      }
    });
    simpleScrollPicker.show(d, 0);
  }

  public void onCivAgeClicked() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 100; i++) {
      d.add(i + "岁");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        db.civAge.setContent(d.get(left) + "-" + d.get(right));
      }
    });
    twoScrollPicker.show(d, d, 0, 0);
  }

  public void onCivEducationClicked() {
    final ArrayList<String> d = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.education_degree)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        db.civEducation.setContent(d.get(pos));
      }
    });
    simpleScrollPicker.show(d, 0);
  }

  public void onCivHeightClicked() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 250; i++) {
      d.add(i + "cm");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        db.civHeight.setContent(d.get(left) + "-" + d.get(right));
      }
    });
    twoScrollPicker.show(d, d, 0, 0);
  }

  public void onCivWeightClicked() {
    final ArrayList<String> d = new ArrayList<>();
    d.add("不限");
    for (int i = 0; i < 250; i++) {
      d.add(i + "Kg");
    }
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        db.civWeight.setContent(d.get(left) + "-" + d.get(right));
      }
    });
    twoScrollPicker.show(d, d, 0, 0);
  }
}
