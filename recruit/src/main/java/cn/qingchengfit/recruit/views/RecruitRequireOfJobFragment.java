package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TwoScrollPicker;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Arrays;

import static cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragment.MAX_HEIGHT;
import static cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragment.MAX_WEIGHT;
import static cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragment.MIN_HEIGHT;
import static cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragment.MIN_WEIGHT;

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
 * Created by Paper on 2017/6/28.
 */

@FragmentWithArgs public class RecruitRequireOfJobFragment extends BaseFragment {

	Toolbar toolbar;
	TextView toolbarTitle;
	CommonInputView civWorkExp;
	CommonInputView civGender;
	CommonInputView civAge;
	CommonInputView civEducation;
	CommonInputView civHeight;
	CommonInputView civWeight;

  SimpleScrollPicker simpleScrollPicker;
  TwoScrollPicker twoScrollPicker;
  @Arg JobBody body;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitRequireOfJobFragmentBuilder.injectArguments(this);
    twoScrollPicker = new TwoScrollPicker(getContext());
    simpleScrollPicker = new SimpleScrollPicker(getContext());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_position_require, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    civWorkExp = (CommonInputView) view.findViewById(R.id.civ_work_exp);
    civGender = (CommonInputView) view.findViewById(R.id.civ_gender);
    civAge = (CommonInputView) view.findViewById(R.id.civ_age);
    civEducation = (CommonInputView) view.findViewById(R.id.civ_education);
    civHeight = (CommonInputView) view.findViewById(R.id.civ_height);
    civWeight = (CommonInputView) view.findViewById(R.id.civ_weight);
    view.findViewById(R.id.civ_work_exp).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivWorkExpClicked();
      }
    });
    view.findViewById(R.id.civ_gender).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivGenderClicked();
      }
    });
    view.findViewById(R.id.civ_age).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivAgeClicked();
      }
    });
    view.findViewById(R.id.civ_education).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivEducationClicked();
      }
    });
    view.findViewById(R.id.civ_height).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivHeightClicked();
      }
    });
    view.findViewById(R.id.civ_weight).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivWeightClicked();
      }
    });

    return view;
  }

  @Override public String getFragmentName() {
    return RecruitRequireOfJobFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 工作经验
   */
 public void onCivWorkExpClicked() {
    final ArrayList<String> leftArray = new ArrayList<>();
    final ArrayList<String> rightArray = new ArrayList<>();
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left == 0 && right == 0) {
          civWeight.setContent("不限");
          body.min_work_year = body.max_work_year = -1;
        } else {
          if (left - 1 >= right) {
            ToastUtils.show("请选择正确的区间");
            return;
          }
          civWeight.setContent(leftArray.get(left) + "-" + rightArray.get(right));
          body.min_work_year = left - 1;
          body.max_work_year = right;
        }
      }
    });
    leftArray.add("不限");
    for (int i = 0; i < 11; i++) {
      leftArray.add(i + "");
    }
    rightArray.add("不限");
    for (int i = 1; i < 11; i++) {
      leftArray.add(i + "年");
    }
    twoScrollPicker.show(leftArray, rightArray, 0, 0);
  }

  /**
   * 性别
   */
 public void onCivGenderClicked() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.gender_2)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civGender.setContent(d.get(pos));
        body.gender = pos;
      }
    });
    simpleScrollPicker.show(d, body.gender);
  }

  /**
   * 年龄
   */
 public void onCivAgeClicked() {
    final int min_year = 18, max_year = 100;

    final ArrayList<String> leftArray = new ArrayList<>();
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left == 0 && right == 0) {
          civWeight.setContent("不限");
          body.min_weight = body.max_weight = (float) -1;
        } else {
          civWeight.setContent(leftArray.get(left) + "-" + leftArray.get(right));
          body.min_work_year = left + min_year - 1;
          body.max_work_year = right + min_year - 1;
        }
      }
    });
    leftArray.add("不限");
    for (int i = min_year; i < max_year; i++) {
      leftArray.add(i + "岁");
    }
    twoScrollPicker.show(leftArray, leftArray, 0, 0);
  }

  /**
   * 学历
   */
 public void onCivEducationClicked() {
    final ArrayList<String> d = new ArrayList<>(Arrays.asList(
        getContext().getResources().getStringArray(R.array.add_resume_education_degree)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        body.education = pos + 1;
        civEducation.setContent(d.get(pos));
      }
    });
    simpleScrollPicker.show(d,
        (body.education != null && body.education > 0) ? body.education - 1 : 0);
  }

  /**
   * 身高
   */
 public void onCivHeightClicked() {
    final ArrayList<String> leftArray = new ArrayList<>();
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left == 0 && right == 0) {
          civWeight.setContent("不限");
        } else if (right == 0) {
          civWeight.setContent("不限-" + leftArray.get(right) + "kg");
        } else {
          civWeight.setContent(leftArray.get(left) + "kg-不限");
        }
      }
    });

    leftArray.add("不限");
    for (int i = MIN_HEIGHT; i < MAX_HEIGHT + 1; i++) {
      leftArray.add(i + "cm");
    }
    twoScrollPicker.show(leftArray, leftArray, 0, 0);
  }

  /**
   * 体重
   */
 public void onCivWeightClicked() {
    final ArrayList<String> leftArray = new ArrayList<>();
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left == 0 && right == 0) {
          civWeight.setContent("不限");
        } else {
          civWeight.setContent(leftArray.get(left) + "-" + leftArray.get(right));
        }
      }
    });

    leftArray.add("不限");
    for (int i = MIN_WEIGHT; i < MAX_WEIGHT + 1; i++) {
      leftArray.add(i + "kg");
    }
    twoScrollPicker.show(leftArray, leftArray, 0, 0);
  }
}
