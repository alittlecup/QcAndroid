package cn.qingchengfit.staffkit.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/1 2016.
 */
public class ClubCardView extends RelativeLayout {

    Switch switcher;
    LinearLayout content;
    TextView tvLable;
    int contentCount;
    boolean isExpand;
    int type;//1 储值卡,2 次卡 3 年卡
    String lable;
    List<String> costList = new ArrayList<>();
    private int mCourseType;
    private String cardid;
    private TextView showPayCard;
    private LinearLayout mDivier;
    private boolean mCannotChange = false;
    private View cannotChange;
    private MaterialDialog mAlert;

    public ClubCardView(Context context) {
        super(context);
        inflate(context, R.layout.layout_clubcard, this);
        onFinishInflate();
    }

    public ClubCardView(Context context, String name, boolean open, int typ, int count, int course_type, boolean cannotChange) {
        super(context);
        inflate(context, R.layout.layout_clubcard, this);
        isExpand = open;
        lable = name;
        type = typ;
        contentCount = count;
        this.mCannotChange = cannotChange;
        this.mCourseType = course_type;
        onFinishInflate();
    }

    /**
     * @param typ 会员卡种类
     */
    public ClubCardView(Context context, String name, boolean open, int typ, int count, int course_type, List<String> costs,
        boolean cannotChange) {
        super(context);
        inflate(context, R.layout.layout_clubcard, this);
        isExpand = open;
        lable = name;
        type = typ;
        contentCount = count;
        this.mCannotChange = cannotChange;
        this.costList = costs;
        this.mCourseType = course_type;
        onFinishInflate();
    }

    public ClubCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClubCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCost(HashMap<Integer, String> costs) {
        for (int i = 0; i < content.getChildCount(); i++) {
            ((CommonInputView) content.getChildAt(i)).setContent(costs.get(i + 1));
        }
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_clubcard, this, true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClubCardView);
        lable = ta.getString(R.styleable.ClubCardView_ccv_lable);
        type = ta.getInt(R.styleable.ClubCardView_ccv_type, 1);
        isExpand = ta.getBoolean(R.styleable.ClubCardView_ccv_expand, false);
        contentCount = ta.getInteger(R.styleable.ClubCardView_ccv_count, 1);

        ta.recycle();
    }

    public boolean isCannotChange() {
        return mCannotChange;
    }

    public void setCannotChange(boolean cannotChange) {
        mCannotChange = cannotChange;
        this.cannotChange.setVisibility(mCannotChange ? VISIBLE : GONE);
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public void setContentCount(int contentCount) {
        this.contentCount = contentCount;
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        switcher = ButterKnife.findById(this, R.id.switcher);
        content = ButterKnife.findById(this, R.id.content);
        showPayCard = ButterKnife.findById(this, R.id.show_card_pay);
        mDivier = ButterKnife.findById(this, R.id.divider);
        switcher.setChecked(isExpand);
        cannotChange = ButterKnife.findById(this, R.id.cannot_change);

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isExpand = isChecked;
                if (isExpand) {
                    expand();
                } else {
                    close();
                }
            }
        });
        tvLable = ButterKnife.findById(this, R.id.label);
        tvLable.setText(lable);
        cannotChange.setVisibility(mCannotChange ? VISIBLE : GONE);
        cannotChange.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                showAlert(null, getResources().getString(R.string.alert_batch_has_ordered));
            }
        });
        //        mDivier.setVisibility(type == 0 ? GONE:VISIBLE);
        //        if (type == 0) tvLable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pay_online,0,0,0);
        initfreshContent();
    }

    public void showAlert(String title, String content) {
        if (mAlert == null) ;
        mAlert = new MaterialDialog.Builder(getContext()).positiveText(R.string.common_i_konw)
            .autoDismiss(true)
            .canceledOnTouchOutside(true)
            .build();
        if (mAlert.isShowing()) mAlert.dismiss();
        if (StringUtils.isEmpty(title)) {
            mAlert.setTitle(title);
        } else {
            mAlert.setTitle("");
        }
        mAlert.setContent(content);
        mAlert.show();
    }

    public void showHint(boolean show) {
        showPayCard.setVisibility(show ? VISIBLE : GONE);
    }

    public int getCivCount() {
        return contentCount;
    }

    public View getCivAt(int index) {
        return content.getChildAt(index);
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (isExpand) {
            expand();
        } else {
            close();
        }
    }

    private void expand() {
        content.setVisibility(VISIBLE);
        freshContent();
        if (content.getChildCount() > 0) {
            AppUtils.showKeyboard(getContext(), ((CommonInputView) content.getChildAt(0)).getEditText());
        }
    }

    private void close() {
        content.setVisibility(GONE);
        AppUtils.hideKeyboard(((android.app.Activity) getContext()));
    }

    public boolean isComppleted() {
        boolean ret = true;
        for (int i = 0; i < content.getChildCount(); i++) {
            CommonInputView v = (CommonInputView) content.getChildAt(i);
            if (TextUtils.isEmpty(v.getContent())) ret = false;
        }
        return ret;
    }

    public List<Rule> getRules(String card_tpl_id) {
        List<Rule> rules = new ArrayList<>();
        if (mCourseType == Configs.TYPE_PRIVATE) {
            if (type == Configs.CATEGORY_VALUE || type == Configs.CATEGORY_TIMES) {
                for (int i = 0; i < content.getChildCount(); i++) {
                    Rule rule = new Rule();
                    rule.card_tpl_id = card_tpl_id;
                    rule.cost = ((CommonInputView) content.getChildAt(i)).getContent();
                    rule.from_number = i + 1;
                    rule.to_number = i + 2;
                    rule.channel = Configs.CHANNEL_CARD;
                    rules.add(rule);
                }
            } else if (type == Configs.CATEGORY_DATE) {
                Rule rule = new Rule();
                rule.card_tpl_id = card_tpl_id;
                rule.cost = "1";
                rule.from_number = 1;
                rule.to_number = contentCount + 1;
                rule.channel = Configs.CHANNEL_CARD;
                rules.add(rule);
            } else {                //微信支付
                Rule rule = new Rule();
                rule.card_tpl_id = "0";
                rule.cost = ((CommonInputView) content.getChildAt(0)).getContent();
                rule.from_number = 1;
                rule.to_number = contentCount + 1;
                rule.channel = Configs.CHANNEL_ONLINE;

                rules.add(rule);
            }
        } else {
            Rule rule = new Rule();

            if (type == Configs.CATEGORY_VALUE || type == Configs.CATEGORY_TIMES) {
                rule.card_tpl_id = card_tpl_id;
                rule.cost = ((CommonInputView) content.getChildAt(0)).getContent();
                rule.from_number = 1;
                rule.to_number = contentCount + 1;
                rule.channel = Configs.CHANNEL_CARD;
            } else if (type == Configs.CATEGORY_DATE) {
                rule.card_tpl_id = card_tpl_id;
                rule.cost = "1";
                rule.from_number = 1;
                rule.to_number = contentCount + 1;
                rule.channel = Configs.CHANNEL_CARD;
            } else {//微信支付
                rule.card_tpl_id = "0";
                rule.cost = ((CommonInputView) content.getChildAt(0)).getContent();
                rule.from_number = 1;
                rule.to_number = contentCount + 1;
                rule.channel = Configs.CHANNEL_ONLINE;
            }
            rules.add(rule);
        }
        return rules;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getCostList() {
        saveCost();
        return costList;
    }

    public void freshContent() {
        if (type < 3 && isExpand) {
            saveCost();
            content.removeAllViews();
            if (mCourseType == Configs.TYPE_PRIVATE) {
                if (type > 0) {
                    for (int i = 1; i <= contentCount; i++) {
                        setContent(type, i);
                    }
                } else {//在线支付
                    CommonInputView civ = new CommonInputView(getContext(), true, false, costList.size() > 0 ? costList.get(0) : null);
                    civ.setLabel("单价(元)");

                    content.addView(civ);
                }
            } else {
                String x = "";
                if (costList.size() > 0) {
                    x = costList.get(0);
                }
                CommonInputView civ = new CommonInputView(getContext(), true, false, x);
                civ.setLabel("单价(" + (type == 2 ? "次" : "元") + ")");

                content.addView(civ);
            }
        }
    }

    public void initfreshContent() {
        if (type < 3 && isExpand) {
            content.removeAllViews();
            if (mCourseType == Configs.TYPE_PRIVATE) {
                if (type > 0) {
                    for (int i = 1; i <= contentCount; i++) {
                        setContent(type, i);
                    }
                } else {//在线支付
                    CommonInputView civ = new CommonInputView(getContext(), true, false, costList.size() > 0 ? costList.get(0) : null);
                    civ.setLabel("单价(元)");

                    content.addView(civ);
                }
            } else {
                String x = "";
                if (costList.size() > 0) {
                    x = costList.get(0);
                }
                CommonInputView civ = new CommonInputView(getContext(), true, x);
                civ.setLabel("单价(" + (type == 2 ? "次" : "元") + ")");

                content.addView(civ);
            }
        }
    }

    public String getLable() {
        return tvLable.getText().toString();
    }

    public void setLable(String lable) {
        tvLable.setText(lable);
    }

    private void setContent(int type, int num) {
        String x = "";
        if (costList.size() > num - 1 && !TextUtils.isEmpty(costList.get(num - 1))) {
            x = costList.get(num - 1);
        }
        CommonInputView civ = new CommonInputView(getContext(), true, x);
        civ.setLabel(String.format(getContext().getString(R.string.card_content_lable), num) + (type == 2 ? getContext().getString(
            R.string.card_unit_times) : getContext().getString(R.string.card_unit_value)));

        content.addView(civ);
    }

    private void saveCost() {
        costList.clear();
        for (int i = 0; i < content.getChildCount(); i++) {
            costList.add(((CommonInputView) content.getChildAt(i)).getContent());
        }
    }
}
