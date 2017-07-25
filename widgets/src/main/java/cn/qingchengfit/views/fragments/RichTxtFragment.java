package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    //et = new EditText(getContext());
    //et.setGravity(Gravity.TOP);
    //et.setBackgroundResource(R.color.white);
    //int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    //et.setPadding(padding, padding, padding, padding);
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    //  et.setTextAppearance(R.style.QcTextStyleMediumDark);
    //} else {
    //  et.setTextAppearance(getContext(), R.style.QcTextStyleMediumDark);
    //}
    //et.setTextSize(15);
    //et.setHint("用人单位更关注求职者的专业能力、沟通能力和形象气质，建议从这方面来描述");
    //et.setMinWidth(MeasureUtils.getScreenWidth(getResources())-MeasureUtils.dpToPx(30f,getResources()));
    return v;
  }

  @Override public String getFragmentName() {
    return RichTxtFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void initContent(String s, String hint) {
    if (TextUtils.isEmpty(s)) {
      commonFlexAdapter.addItem(new RichTxtTxtItem("", true, hint));
      if (commonFlexAdapter.getItemCount() > 0) {
        IFlexible item = commonFlexAdapter.getItem(commonFlexAdapter.getItemCount() - 1);
        if (item instanceof RichTxtTxtItem) {
          ((RichTxtTxtItem) item).requestFocus();
        }
      }
      return;
    }
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
  }

  public String getContent() {
    StringBuffer ret = new StringBuffer();
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible item = commonFlexAdapter.getItem(i);
      if (item instanceof RichTxtTxtItem) {
        ret.append("<p>").append(((RichTxtTxtItem) item).getContent()).append("</p>");
      } else if (item instanceof RichTxtImgItem) {
        ret.append("<img src=\"").append(((RichTxtImgItem) item).getUrl()).append("\" />");
      }
    }
    return ret.toString();
  }

  //public void initContent(String s) {
  //  try {
  //    List<BriefInfo> a = CmStringUtils.fromHTML(s);
  //    et.requestFocus();
  //    et.setSelection(0);
  //    for (BriefInfo briefInfo : a) {
  //      if (TextUtils.isEmpty(briefInfo.getImg())) {
  //        if (!TextUtils.isEmpty(et.getText())) et.append("\n");
  //        et.append(briefInfo.getText());
  //      } else {
  //        insertImg(briefInfo.getImg());
  //      }
  //    }
  //  } catch (Exception e) {
  //    e.printStackTrace();
  //  }
  //}
  //
  //public void insertImg(String url) {
  //  final String tempUrl = "\n<img src=\"" + url + "\" />";
  //  Glide.with(getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
  //    @Override public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
  //      Bitmap bitmap = BitmapUtils.getWidthScale(resource, et.getWidth());
  //      SpannableString spannableString = new SpannableString(tempUrl);
  //      ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
  //      spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
  //      // 将选择的图片追加到EditText中光标所在位置
  //      int index = et.getSelectionStart();
  //      // 获取光标所在位置
  //      Editable edit_text = et.getEditableText();
  //      if (index < 0 || index >= edit_text.length()) {
  //        edit_text.append(spannableString);
  //      } else {
  //        edit_text.insert(index, spannableString);
  //      }
  //      edit_text.insert(index + spannableString.length(), "\n");
  //    }
  //  });
  //}
  //
  //public void insertImg(String url, String filePath) {
  //  String tempUrl = "<img src=\"" + url + "\" />";
  //  SpannableString spannableString = new SpannableString(tempUrl);
  //  Bitmap bitmap = BitmapFactory.decodeFile(filePath);
  //  //BitmapDrawable d = new BitmapDrawable(getResources(),bitmap);
  //  ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
  //
  //  spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
  //  // 将选择的图片追加到EditText中光标所在位置
  //  int index = et.getSelectionStart();
  //  // 获取光标所在位置
  //  Editable edit_text = et.getEditableText();
  //  if (index < 0 || index >= edit_text.length()) {
  //    edit_text.append(spannableString);
  //  } else {
  //    edit_text.insert(index, spannableString);
  //  }
  //  edit_text.insert(index + spannableString.length(), "\n");
  //}
  //
  //public String getContent() {
  //  String content = et.getText().toString();
  //  String ret = "";
  //  if (content.contains("\n")) {
  //    String[] x = content.split("<");
  //    for (String s : x) {
  //      if (TextUtils.isEmpty(s)) continue;
  //      if (s.startsWith("img")) {
  //        ret = TextUtils.concat(ret, "<", s).toString();
  //      } else {
  //        ret = TextUtils.concat(ret, "<p>", s, "</p>").toString();
  //      }
  //    }
  //  }
  //  return ret;
  //}

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof RichTxtTxtItem) {
      ((RichTxtTxtItem) commonFlexAdapter.getItem(i)).requestFocus();
    }
    return true;
  }
}
