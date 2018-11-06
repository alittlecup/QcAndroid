package cn.qingchengfit.staffkit.views.gym;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.utils.DateUtils;

@Deprecated public class RenewalDialogFragment extends BaseDialogFragment {

	TextView close;
	TextView firstFirst;
	TextView firstSecond;
	TextView secondFirst;
	TextView secondSecond;
	TextView price;
	TextView comfirm;

    CoachService mCoachService;
    GymDetail.Recharge mRecharge;
	LinearLayout layoutFirstFirst;
	LinearLayout layoutFirstSecond;
	LinearLayout layoutSecondFirst;
	LinearLayout layoutSecondSecond;
	TextView hint;

    public static RenewalDialogFragment newInstance(GymDetail.Recharge recharge, CoachService coachService) {

        Bundle args = new Bundle();
        args.putParcelable("recharge", recharge);
        args.putParcelable("service", coachService);
        RenewalDialogFragment fragment = new RenewalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.LoadingDialog_Style);
        if (getArguments() != null) {
            mCoachService = getArguments().getParcelable("service");
            mRecharge = getArguments().getParcelable("recharge");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_renewal, container, false);
      close = (TextView) view.findViewById(R.id.close);
      firstFirst = (TextView) view.findViewById(R.id.first_first);
      firstSecond = (TextView) view.findViewById(R.id.first_second);
      secondFirst = (TextView) view.findViewById(R.id.second_first);
      secondSecond = (TextView) view.findViewById(R.id.second_second);
      price = (TextView) view.findViewById(R.id.price);
      comfirm = (TextView) view.findViewById(R.id.comfirm);
      layoutFirstFirst = (LinearLayout) view.findViewById(R.id.layout_first_first);
      layoutFirstSecond = (LinearLayout) view.findViewById(R.id.layout_first_second);
      layoutSecondFirst = (LinearLayout) view.findViewById(R.id.layout_second_first);
      layoutSecondSecond = (LinearLayout) view.findViewById(R.id.layout_second_second);
      hint = (TextView) view.findViewById(R.id.hint);
      view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          RenewalDialogFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.comfirm).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          RenewalDialogFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          RenewalDialogFragment.this.onClick();
        }
      });

      try {

            if (mRecharge.is_first_shop && !mRecharge.is_recharged) {//第一年第一家店
                layoutFirstFirst.setBackgroundResource(R.color.bg_orange);
            }
            if (!mRecharge.is_first_shop && mRecharge.is_recharged) {
                layoutSecondSecond.setBackgroundResource(R.color.bg_orange);
            }
            if (!mRecharge.is_first_shop && !mRecharge.is_recharged) {
                layoutSecondFirst.setBackgroundResource(R.color.bg_orange);
            }
            if (mRecharge.is_first_shop && mRecharge.is_recharged) {
                layoutFirstSecond.setBackgroundResource(R.color.bg_orange);
            }
            price.setText("¥" + mRecharge.recharge_price);
            if (mRecharge.trial_date <= 0) {
                close.setVisibility(View.GONE);
            }
            if (mRecharge.trial_date <= 7) {
                hint.setVisibility(View.VISIBLE);
                if (mRecharge.trial_date > 0) {
                    hint.setText(getString(R.string.hint_refund_less_7, DateUtils.getYYYYMMDDfromServer(mRecharge.system_end)));
                } else {
                    hint.setText(getString(R.string.hint_refund_less_0, DateUtils.getYYYYMMDDfromServer(mRecharge.system_end)));
                }
            }
            if (mRecharge.charge_rule != null) {
                for (int i = 0; i < mRecharge.charge_rule.size(); i++) {
                    if (mRecharge.charge_rule.get(i).key.equalsIgnoreCase("first_shop_first_year")) {
                        firstFirst.setText(mRecharge.charge_rule.get(i).value);
                    } else if (mRecharge.charge_rule.get(i).key.equalsIgnoreCase("first_shop_after_year")) {
                        firstSecond.setText(mRecharge.charge_rule.get(i).value);
                    } else if (mRecharge.charge_rule.get(i).key.equalsIgnoreCase("after_shop_first_year")) {
                        secondFirst.setText(mRecharge.charge_rule.get(i).value);
                    } else if (mRecharge.charge_rule.get(i).key.equalsIgnoreCase("after_shop_after_year")) {
                        secondSecond.setText(mRecharge.charge_rule.get(i).value);
                    }
                }
            }
        } catch (Exception e) {

        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    getActivity().onBackPressed();
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        return d;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.comfirm:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                break;
        }
    }

 public void onClick() {
        Intent toHistory = new Intent(getActivity(), GymActivity.class);
        toHistory.putExtra(GymActivity.GYM_TO, GymActivity.GYM_HISORY);
        toHistory.putExtra(Configs.EXTRA_BRAND, new Brand(mCoachService.getBrand_id()));
        toHistory.putExtra(Configs.EXTRA_GYM_SERVICE, mCoachService);
        toHistory.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus(false));
        startActivity(toHistory);
    }
}
