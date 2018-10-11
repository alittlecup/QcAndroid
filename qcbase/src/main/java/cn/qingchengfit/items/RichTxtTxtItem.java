package cn.qingchengfit.items;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.RichTxtFragment;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RichTxtTxtItem extends AbstractFlexibleItem<RichTxtTxtItem.RichTxtTxtVH> {

  String content;
  boolean showKb;
  String strHint;
  private RichTxtTxtVH vh;

  public RichTxtTxtItem(String content, boolean showKb) {
    this.content = content;
    this.showKb = showKb;
  }

  public RichTxtTxtItem(String content, boolean showKb, String strHint) {
    this.content = content;
    this.showKb = showKb;
    this.strHint = strHint;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_rich_txt_txt;
  }

  @Override public RichTxtTxtVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new RichTxtTxtVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RichTxtTxtVH holder, int position,
      List payloads) {
    this.vh = holder;
    if (position == 0 && TextUtils.isEmpty(content)) {
      if (TextUtils.isEmpty(strHint)) {
        holder.tv.setHint(R.string.hint_rich_txt);
      } else {
        holder.tv.setHint(strHint);
      }
    } else {
      holder.tv.setText(content);
    }
    if (showKb) {
      holder.tv.requestFocus();
      AppUtils.showKeyboard(holder.tv.getContext(), holder.tv);
      showKb = false;
    }
  }

  public void requestFocus() {
    if (vh != null) {
      AppUtils.showKeyboard(vh.itemView.getContext(), vh.tv);
    }
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
    RxBus.getBus().post(RichTxtFragment.class, content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class RichTxtTxtVH extends FlexibleViewHolder {
	EditText tv;

    public RichTxtTxtVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      tv = (EditText) view.findViewById(R.id.tv);

      tv.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
          IFlexible item = adapter.getItem(getAdapterPosition());
          if (item instanceof RichTxtTxtItem) {
            ((RichTxtTxtItem) item).setContent(s.toString());
          }
        }
      });
      tv.setOnKeyListener(new View.OnKeyListener() {
        @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
          if (v instanceof EditText) {
            int x = ((EditText) v).getSelectionStart();
            if (x == 0 && getAdapterPosition() != 0) {
              if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                adapter.removeItem(getAdapterPosition() - 1);
                RxBus.getBus().post(RichTxtFragment.class, content);
              }
            }
          }
          return false;
        }
      });
    }
  }
}