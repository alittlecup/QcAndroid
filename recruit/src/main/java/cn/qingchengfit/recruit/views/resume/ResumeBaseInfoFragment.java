package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.presenter.ResumePostPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.TypeConvertUtils;
import cn.qingchengfit.views.CitiesChooser;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.inject.Inject;

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
 * Created by Paper on 2017/6/12.
 */
@FragmentWithArgs public class ResumeBaseInfoFragment extends BaseFragment
    implements ResumePostPresenter.MVPView {

  public final static int MIN_WEIGHT = 0;
  public final static int MAX_WEIGHT = 260;
  public final static int MIN_HEIGHT = 0;
  public final static int MAX_HEIGHT = 200;

	ImageView civHeaderPic;
	LinearLayout civHeaderLayout;
	CommonInputView civName;
	CommonInputView civGender;
	CommonInputView civCity;
	CommonInputView civBirthday;
	CommonInputView civHeight;
	CommonInputView civWeight;
	CommonInputView civWorkexp;
	EditText etSignIn;
	TextView tvWordCount;
	Toolbar toolbar;
	TextView toolbarTitle;

  @Inject QcRestRepository qcRestRepository;
  @Inject ResumePostPresenter postPresenter;
  TimeDialogWindow timeDialogWindow;
  SimpleScrollPicker simpleScrollPicker;
  ChoosePictureFragmentNewDialog choosePictureFragmentNewDialog;
  CitiesChooser citiesChooser;

  @Arg ResumeBody resumeBody;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeBaseInfoFragmentBuilder.injectArguments(this);
    simpleScrollPicker = new SimpleScrollPicker(getContext());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_base_info, container, false);
    civHeaderPic = (ImageView) view.findViewById(R.id.civ_header_pic);
    civHeaderLayout = (LinearLayout) view.findViewById(R.id.civ_header_layout);
    civName = (CommonInputView) view.findViewById(R.id.civ_name);
    civGender = (CommonInputView) view.findViewById(R.id.civ_gender);
    civCity = (CommonInputView) view.findViewById(R.id.civ_city);
    civBirthday = (CommonInputView) view.findViewById(R.id.civ_birthday);
    civHeight = (CommonInputView) view.findViewById(R.id.civ_height);
    civWeight = (CommonInputView) view.findViewById(R.id.civ_weight);
    civWorkexp = (CommonInputView) view.findViewById(R.id.civ_workexp);
    etSignIn = (EditText) view.findViewById(R.id.et_sign_in);
    tvWordCount = (TextView) view.findViewById(R.id.tv_word_count);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.civ_header_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivHeaderLayoutClicked();
      }
    });
    view.findViewById(R.id.civ_gender).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMofifyinfoGenderClicked();
      }
    });
    view.findViewById(R.id.civ_city).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMofifyinfoCityClicked();
      }
    });
    view.findViewById(R.id.civ_birthday).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMofifyinfoBirthdayClicked();
      }
    });
    view.findViewById(R.id.civ_height).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMofifyinfoHeightClicked();
      }
    });
    view.findViewById(R.id.civ_weight).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMofifyinfoWeightClicked();
      }
    });
    view.findViewById(R.id.civ_workexp).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onMofifyinfoWorkexpClicked();
      }
    });

    delegatePresenter(postPresenter, this);
    initToolbar(toolbar);
    initView();
    return view;
  }

  private void initView() {
    int defaultImg = resumeBody.gender == 0 ? R.drawable.default_student_male
        : R.drawable.default_student_female;
    PhotoUtils.smallCircle(civHeaderPic, resumeBody.avatar, defaultImg, defaultImg);
    civName.setContent(resumeBody.username);
    civBirthday.setContent(
        DateUtils.date2YYMM(DateUtils.formatDateFromServer(resumeBody.birthday)));
    civCity.setContent(resumeBody.getCity());
    civHeight.setContent((resumeBody.height == null || resumeBody.height == 0) ? "170"
        : resumeBody.height.intValue() + "");
    civWeight.setContent((resumeBody.weight == null || resumeBody.weight == 0) ? "55"
        : resumeBody.weight.intValue() + "");
    civGender.setContent(resumeBody.gender == 0 ? "男" : "女");
    civWorkexp.setContent(resumeBody.work_year + "");
    resumeBody.city = null;
    etSignIn.setText(resumeBody.brief_description);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("编辑基本信息");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        showLoading();
        resumeBody.brief_description = etSignIn.getText().toString();
        resumeBody.height = Float.parseFloat(civHeight.getContent());
        resumeBody.weight = Float.parseFloat(civWeight.getContent());
        resumeBody.birthday = DateUtils.YYMMToServer(civBirthday.getContent());
        resumeBody.username = civName.getContent();
        resumeBody.self_description = null;
        postPresenter.editResume(resumeBody);
        return false;
      }
    });
  }

  @Override public String getFragmentName() {
    return ResumeBaseInfoFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 点击头像
   */
 public void onCivHeaderLayoutClicked() {
    if (choosePictureFragmentNewDialog == null) {
      choosePictureFragmentNewDialog = ChoosePictureFragmentNewDialog.newInstance(true);
    }
    choosePictureFragmentNewDialog.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
      @Override public void onChoosefile(String filePath) {
      }

      @Override public void onUploadComplete(String filePaht, String url) {
        PhotoUtils.smallCircle(civHeaderPic, url);
        resumeBody.avatar = url;
      }
    });
    choosePictureFragmentNewDialog.show(getChildFragmentManager(), "");
  }

  /**
   * 性别
   */
 public void onMofifyinfoGenderClicked() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.gender_2)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civGender.setContent(d.get(pos));
        resumeBody.gender = pos;
      }
    });
    simpleScrollPicker.show(d, resumeBody.gender);
  }

  /**
   * 城市选择
   */
 public void onMofifyinfoCityClicked() {
    if (citiesChooser == null) {
      citiesChooser = new CitiesChooser(getContext());
    }
    citiesChooser.setOnCityChoosenListener(new CitiesChooser.OnCityChoosenListener() {
      @Override public void onCityChoosen(String provice, String city, String district, int id) {
        civCity.setContent(city + district);
        resumeBody.gd_district_id = id + "";
      }
    });
    citiesChooser.show(getView());
  }

  /**
   * 生日选择
   */
 public void onMofifyinfoBirthdayClicked() {
    if (timeDialogWindow == null) {
      timeDialogWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH);
      int curYear = DateUtils.getYear(new Date());
      timeDialogWindow.setRange(curYear - 80, curYear - 15);
    }
    timeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        resumeBody.birthday = DateUtils.date2YYMM(date);
        civBirthday.setContent(resumeBody.birthday);
      }
    });

    timeDialogWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0,
        DateUtils.YYMM2date(civBirthday.getContent()));
  }

  /**
   * 身高选择
   */
 public void onMofifyinfoHeightClicked() {
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civHeight.setContent(Integer.toString((MIN_HEIGHT + pos)));
      }
    });

    int chooseHeight = 0;
    try {
      chooseHeight = Integer.parseInt(civHeight.getContent());
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
    simpleScrollPicker.show(MIN_HEIGHT, MAX_HEIGHT,
        chooseHeight == 0 ? 170 - MIN_HEIGHT : chooseHeight);
  }

  /**
   * 体重选择
   */
 public void onMofifyinfoWeightClicked() {
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civWeight.setContent(Integer.toString((MIN_WEIGHT + pos)));
      }
    });
    simpleScrollPicker.show(MIN_WEIGHT, MAX_WEIGHT,
        TypeConvertUtils.getInt(civWeight.getContent(), 55 - MIN_WEIGHT));
  }

  /**
   * 工作经验
   */
 public void onMofifyinfoWorkexpClicked() {
    final ArrayList<String> d = new ArrayList<>(
        Arrays.asList(getContext().getResources().getStringArray(R.array.resume_work_exp)));
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        civWorkexp.setContent(d.get(pos));
        resumeBody.work_year = pos;
      }
    });
    simpleScrollPicker.show(d, resumeBody.work_year);
  }

  @Override public void onPostOk() {
    hideLoading();
    RxBus.getBus().post(new EventResumeFresh());
    getActivity().onBackPressed();
  }
}
