package cn.qingchengfit.staffkit.views.custom;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;




import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseBottomSheetDialogFragment;
import cn.qingchengfit.utils.IntentUtils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/25 2016.
 */
@FragmentWithArgs public class BottomPayDialog extends BaseBottomSheetDialogFragment {

    @Arg boolean hasEditPermission;
	TextView payOffline;
	RelativeLayout cash;
	RelativeLayout card;
	RelativeLayout transfer;
	RelativeLayout others;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_bottom, container, false);
      payOffline = (TextView) view.findViewById(R.id.pay_offline);
      cash = (RelativeLayout) view.findViewById(R.id.cash);
      card = (RelativeLayout) view.findViewById(R.id.card);
      transfer = (RelativeLayout) view.findViewById(R.id.transfer);
      others = (RelativeLayout) view.findViewById(R.id.others);
      view.findViewById(R.id.wechat_code).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomPayDialog.this.onClick(v);
        }
      });
      view.findViewById(R.id.wechat_pay).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomPayDialog.this.onClick(v);
        }
      });
      view.findViewById(R.id.cash).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomPayDialog.this.onClick(v);
        }
      });
      view.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomPayDialog.this.onClick(v);
        }
      });
      view.findViewById(R.id.transfer).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomPayDialog.this.onClick(v);
        }
      });
      view.findViewById(R.id.others).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BottomPayDialog.this.onClick(v);
        }
      });

      payOffline.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        cash.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        card.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        transfer.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        others.setVisibility(hasEditPermission ? View.VISIBLE : View.GONE);
        return view;
    }

    @Override public void onDestroyView() {

        super.onDestroyView();
    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_code:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, IntentUtils.instanceStringIntent("0"));
                break;
            case R.id.wechat_pay:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, IntentUtils.instanceStringIntent("1"));
                break;
            case R.id.cash:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, IntentUtils.instanceStringIntent("2"));
                break;
            case R.id.card:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, IntentUtils.instanceStringIntent("3"));
                break;
            case R.id.transfer:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, IntentUtils.instanceStringIntent("4"));
                break;
            case R.id.others:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, IntentUtils.instanceStringIntent("5"));
                break;
        }
        dismiss();
    }
}
