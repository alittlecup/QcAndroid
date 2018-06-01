package cn.qingchengfit.saasbase.cards.views.offday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.views.WriteDescFragment;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

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
 * Created by Paper on 16/3/18 2016.
 */

@Leaf(module = "card", path = "/add/offday") public class AddOffDayFragment extends SaasBaseFragment
    implements AddOffDayView {

	CommonInputView mark;
	CommonInputView offdayReason;
	CommonInputView startDay;
	CommonInputView endDay;
	CommonInputView spend;
	Button comfirm;
  @Need String cardId;
  @Inject AddOffDayPresenter presenter;
	Toolbar toolbar;
	TextView toolbarTitle;
  private TimeDialogWindow pwTime;
  private TextWatcher textchange = new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      comfirm.setEnabled(
          !TextUtils.isEmpty(offdayReason.getContent()) && !TextUtils.isEmpty(spend.getContent()));
    }
  };

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_add_offday, container, false);
    mark = (CommonInputView) view.findViewById(R.id.mark);
    offdayReason = (CommonInputView) view.findViewById(R.id.offday_reason);
    startDay = (CommonInputView) view.findViewById(R.id.start_day);
    endDay = (CommonInputView) view.findViewById(R.id.end_day);
    spend = (CommonInputView) view.findViewById(R.id.spend);
    comfirm = (Button) view.findViewById(R.id.comfirm);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.offday_reason).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        AddOffDayFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.start_day).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        AddOffDayFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.end_day).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        AddOffDayFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.mark).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        AddOffDayFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.comfirm).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        AddOffDayFragment.this.onClick(v);
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    toolbarTitle.setText("添加请假");
    spend.addTextWatcher(textchange);
    offdayReason.addTextWatcher(textchange);
    startDay.setContent(DateUtils.Date2YYYYMMDD(new Date()));
    endDay.setContent(DateUtils.Date2YYYYMMDD(new Date()));
    return view;
  }

  @Override public void onDestroyView() {
    presenter.unattachView();
    super.onDestroyView();
  }


  public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.offday_reason) {
      WriteDescFragment.start(this, 1, "请假理由", "请填写请假理由");
    } else if (i == R.id.start_day) {
      pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
          Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          startDay.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    } else if (i == R.id.end_day) {
      pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
          Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          endDay.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    } else if (i == R.id.mark) {
      WriteDescFragment.start(this, 2, "备注信息", "请填写备注信息");
    } else if (i == R.id.comfirm) {
      if (DateUtils.AlessOrEquelB(startDay.getContent(), endDay.getContent())) {
        showLoading();
        presenter.commitOffDay(cardId, startDay.getContent(), endDay.getContent(),
            offdayReason.getContent(), spend.getContent(), mark.getContent());
      } else {
        ToastUtils.show("开始时间不得小于结束时间");
        return;
      }
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        //请假理由
        offdayReason.setContent(IntentUtils.getIntentString(data));
      } else if (requestCode == 2) {
        mark.setContent(IntentUtils.getIntentString(data));
      }
    }
  }

  @Override public void onFailed(String s) {
    ToastUtils.show(s);
    hideLoading();
  }

  @Override public void onSucceess() {
    hideLoading();
    ToastUtils.showS("请假成功");
    getActivity().onBackPressed();
  }

  @Override public String getFragmentName() {
    return AddOffDayFragment.class.getName();
  }
}
