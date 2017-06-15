package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.SimpleScrollPicker;
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
 * Created by Paper on 2017/6/12.
 */
public class ResumeBaseInfoFragment extends BaseFragment {

    public final static int MIN_WEIGHT = 30;
    public final static int MAX_WEIGHT = 150;
    public final static int MIN_HEIGHT = 140;
    public final static int MAX_HEIGHT = 230;


    @BindView(R2.id.civ_header_pic) ImageView civHeaderPic;
    @BindView(R2.id.civ_header_layout) LinearLayout civHeaderLayout;
    @BindView(R2.id.civ_name) CommonInputView civName;
    @BindView(R2.id.civ_gender) CommonInputView civGender;
    @BindView(R2.id.civ_city) CommonInputView civCity;
    @BindView(R2.id.civ_birthday) CommonInputView civBirthday;
    @BindView(R2.id.civ_height) CommonInputView civHeight;
    @BindView(R2.id.civ_weight) CommonInputView civWeight;
    @BindView(R2.id.civ_workexp) CommonInputView civWorkexp;
    @BindView(R2.id.et_sign_in) EditText etSignIn;
    @BindView(R2.id.tv_word_count) TextView tvWordCount;

    SimpleScrollPicker simpleScrollPicker;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleScrollPicker = new SimpleScrollPicker(getContext());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume_base_info, container, false);
        return view;
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
    @OnClick(R2.id.civ_header_layout) public void onCivHeaderLayoutClicked() {

    }

    /**
     *
     */
    @OnClick(R2.id.civ_gender) public void onMofifyinfoGenderClicked() {
        final ArrayList<String> d = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.gender)));
        simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
            @Override public void onSelectItem(int pos) {
                civWorkexp.setContent(d.get(pos));
            }
        });
        simpleScrollPicker.show(d, 0);
    }

    /**
     *
     */
    @OnClick(R2.id.civ_city) public void onMofifyinfoCityClicked() {

    }

    /**
     *
     */
    @OnClick(R2.id.civ_birthday) public void onMofifyinfoBirthdayClicked() {

    }

    /**
     *
     */
    @OnClick(R2.id.civ_height) public void onMofifyinfoHeightClicked() {
        simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
            @Override public void onSelectItem(int pos) {
                civWeight.setContent(Integer.toString((MIN_HEIGHT+pos)));
            }
        });
        simpleScrollPicker.show(MIN_HEIGHT,MAX_HEIGHT,55-MIN_HEIGHT);
    }

    /**
     *
     */
    @OnClick(R2.id.civ_weight) public void onMofifyinfoWeightClicked() {
        simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
            @Override public void onSelectItem(int pos) {
                civWeight.setContent(Integer.toString((MIN_WEIGHT+pos)));
            }
        });
        simpleScrollPicker.show(MIN_WEIGHT,MAX_WEIGHT,55-MIN_WEIGHT);
    }

    /**
     * 工作经验
     */
    @OnClick(R2.id.civ_workexp) public void onMofifyinfoWorkexpClicked(){
        final ArrayList<String> d = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.gender)));
        simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
            @Override public void onSelectItem(int pos) {
                civWorkexp.setContent(d.get(pos));
            }
        });
        simpleScrollPicker.show(d, 0);
    }
}
