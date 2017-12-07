package cn.qingchengfit.saasbase.course.batch.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.course.batch.bean.OnlineLimit;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.events.EventMutiChoose;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.Utils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.ExpandedLayout;
import java.util.ArrayList;
import rx.functions.Action1;

public class BatchPayOnlineFragment extends SaasBaseFragment {

    public Rule rule;
    public Integer maxPeople;


    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.lable) TextView lable;
    @BindView(R2.id.switcher_limit) SwitchCompat switcher;
    @BindView(R2.id.layout_limit_peop) RelativeLayout layoutLimitPeop;
    @BindView(R2.id.pay_online) ExpandedLayout payOnline;
    @BindView(R2.id.limit_who) CommonInputView limitWho;
    @BindView(R2.id.limit_num) CommonInputView limitNum;
    @BindView(R2.id.pay_online_money) CommonInputView payOnlineMoney;
    @BindView(R2.id.divider) View divider;

    /**
     * 用来记录在线支付的约课限制
     */
    private ArrayList<Integer> mLimit = new ArrayList<>();
    private DialogList stucount;



    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batch_pay_onlien, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        if (rule != null && rule.channel.equalsIgnoreCase(Configs.CHANNEL_ONLINE)) {
            payOnline.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override public void onGlobalLayout() {
                    CompatUtils.removeGlobalLayout(payOnline.getViewTreeObserver(), this);
                    payOnline.setExpanded(true);
                }
            });
            payOnlineMoney.setContent(rule.cost);
            if (rule.limits != null && rule.limits.user_status != null) {
                switcher.setChecked(true);
                limitNum.setVisibility(View.VISIBLE);
                limitWho.setVisibility(View.VISIBLE);
                limitNum.setContent(rule.limits.user_count == 0 ? maxPeople + "" : rule.limits.user_count + "");
                if (rule.limits.user_status != null) {
                    mLimit = new ArrayList<Integer>();
                    mLimit.addAll(rule.limits.user_status);
                    if (mLimit.size() == 0) {
                        mLimit.add(0);
                        mLimit.add(1);
                        mLimit.add(2);
                    }
                    String limStr = "";
                    if (mLimit.contains(0)) {
                        limStr = limStr.concat("新注册");
                    }
                    if (mLimit.contains(1)) {
                        if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                        limStr = limStr.concat("已接洽");
                    }
                    if (mLimit.contains(2)) {
                        if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                        limStr = limStr.concat("会员");
                    }
                    limitWho.setContent(limStr);
                }
            } else {
                switcher.setChecked(false);
                limitNum.setVisibility(View.GONE);
                limitWho.setVisibility(View.GONE);
            }
        } else {
            rule = new Rule();
            rule.channel = Configs.CHANNEL_ONLINE;
        }
        RxBusAdd(EventMutiChoose.class).subscribe(new Action1<EventMutiChoose>() {
            @Override public void call(EventMutiChoose eventMutiChoose) {
                mLimit = (ArrayList) eventMutiChoose.data;
                String limStr = "";
                if (mLimit.contains(0)) {
                    limStr = limStr.concat("新注册");
                }
                if (mLimit.contains(1)) {
                    if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                    limStr = limStr.concat("已接洽");
                }
                if (mLimit.contains(2)) {
                    if (!TextUtils.isEmpty(limStr)) limStr = limStr.concat(Configs.SEPARATOR);
                    limStr = limStr.concat("会员");
                }
                limitWho.setContent(limStr);
            }
        });
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    limitNum.setVisibility(View.VISIBLE);

                    limitWho.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(limitWho.getContent())) {
                        limitWho.setContent("新注册、已接洽、会员");
                        mLimit.clear();
                        mLimit.add(0);
                        mLimit.add(1);
                        mLimit.add(2);
                    }
                } else {
                    limitNum.setVisibility(View.GONE);
                    limitWho.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("设置在线支付结算");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (payOnline.isExpanded()) {
                    if (payOnlineMoney.isEmpty()) {
                        ToastUtils.show("在线支付未填写完整");
                        return true;
                    }
                    rule.cost = payOnlineMoney.getContent();
                    rule.from_number = 1;
                    rule.to_number = maxPeople;
                    rule.card_tpl_id = "0";
                    if (switcher.isChecked()) {
                        try {
                            rule.limits = new OnlineLimit(Integer.parseInt(limitWho.getContent()), mLimit);
                        } catch (Exception e) {
                            rule.limits = new OnlineLimit(maxPeople, mLimit);
                        }
                    } else {
                        rule.limits = null;
                    }
                    getActivity().setResult(Activity.RESULT_OK, IntentUtils.instancePacecle(rule));
                }
                getActivity().finish();
                return true;
            }
        });
    }

    @Override public String getFragmentName() {
        return BatchPayOnlineFragment.class.getName();
    }

    @OnClick(R2.id.limit_who) public void clickLimitWho() {
        OnlineLimitFragment.newInstance(Utils.toIntArray(mLimit)).show(getFragmentManager(), "");
    }

    @OnClick(R2.id.limit_num) public void clickLimitNum() {
        stucount = new DialogList(getContext()).list(StringUtils.getNums(1, maxPeople), new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                limitNum.setContent(Integer.toString(position + 1));
                stucount.dismiss();
            }
        }).title("选择人数");
        stucount.show();
    }
}
