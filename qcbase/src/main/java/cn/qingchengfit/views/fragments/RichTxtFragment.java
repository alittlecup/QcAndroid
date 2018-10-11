package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.RichTxtImgItem;
import cn.qingchengfit.items.RichTxtTxtItem;
import cn.qingchengfit.model.common.BriefInfo;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
 * Created by Paper on 2017/6/14.
 */
public class RichTxtFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

  //EditText et;
  CommonFlexAdapter commonFlexAdapter;
  RecyclerView rv;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.cm_recylerview, container, false);
    rv = (RecyclerView) v.findViewById(R.id.rv);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.setAdapter(commonFlexAdapter);
    rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
      @Override public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView == null) {
          showKeyBoard();
          return true;
        }
        return false;
      }

      @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) {

      }

      @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

      }
    });
    return v;
  }

  public void setChangeListenter(Action1<String> contents) {
    RxRegiste(RxBus.getBus()
        .register(RichTxtFragment.class, String.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(content -> {
          contents.call(getContent());
        }));
  };

  @Override public void onResume() {
    super.onResume();
  }

  @Override public String getFragmentName() {
    return RichTxtFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void showKeyBoard() {
    if (commonFlexAdapter.getItemCount() > 0) {
      IFlexible item = commonFlexAdapter.getItem(commonFlexAdapter.getItemCount() - 1);
      if (item instanceof RichTxtTxtItem) {
        ((RichTxtTxtItem) item).requestFocus();
      }
    }
  }

  public void initContent(String s, String hint) {
    if (TextUtils.isEmpty(s)) {
      commonFlexAdapter.addItem(new RichTxtTxtItem("", true, hint));
      return;
    }
    if (s.contains("<br/>")) s = s.replace("<br/>", "\n");

    try {
      List<BriefInfo> a = CmStringUtils.fromHTML(s);
      int size = a.size(), i = 0;
      for (BriefInfo briefInfo : a) {
        if (TextUtils.isEmpty(briefInfo.getImg())) {
          commonFlexAdapter.addItem(new RichTxtTxtItem(briefInfo.getText(), i == size - 1));
        } else {
          commonFlexAdapter.addItem(new RichTxtImgItem(briefInfo.getImg()));
        }
        i++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    showKeyBoard();
  }

  public void insertImg(String url) {
    if (commonFlexAdapter.getItemCount() > 0) {
      IFlexible item = commonFlexAdapter.getItem(commonFlexAdapter.getItemCount() - 1);
      if (item instanceof RichTxtTxtItem) {
        if (TextUtils.isEmpty(((RichTxtTxtItem) item).getContent())) {
          commonFlexAdapter.removeItem(commonFlexAdapter.getItemCount() - 1);
        }
      }
    }
    commonFlexAdapter.addItem(new RichTxtImgItem(url));
    commonFlexAdapter.addItem(new RichTxtTxtItem("", true));
    RxBus.getBus().post(RichTxtFragment.class, getContent());

    showKeyBoard();
  }

  public String getContent() {
    StringBuffer ret = new StringBuffer();
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible item = commonFlexAdapter.getItem(i);
      if (item instanceof RichTxtTxtItem) {
        String content = ((RichTxtTxtItem) item).getContent();
        if (!TextUtils.isEmpty(content)) {
          ret.append("<p>").append(content).append("</p>");
        }
      } else if (item instanceof RichTxtImgItem) {
        ret.append("<img src=\"").append(((RichTxtImgItem) item).getUrl()).append("\" />");
      }
    }
    String srtRet = ret.toString();
    if (srtRet.contains("\n")) srtRet = srtRet.replace("\n", "<br/>");
    return srtRet;
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Override public boolean onItemClick(int i) {
    if (!(commonFlexAdapter.getItem(i) instanceof RichTxtTxtItem)) {
      showKeyBoard();
    }
    return true;
  }
}
