package cn.qingchengfit.items;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RichTxtTxtItem extends AbstractFlexibleItem<RichTxtTxtItem.RichTxtTxtVH> {

  String content;
  boolean showKb;

  public RichTxtTxtItem(String content, boolean showKb) {
    this.content = content;
    this.showKb = showKb;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_rich_txt_txt;
  }

  @Override public RichTxtTxtVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new RichTxtTxtVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RichTxtTxtVH holder, int position,
      List payloads) {
    if (position == 0 && TextUtils.isEmpty(content)) {
      holder.tv.setHint(R.string.hint_rich_txt);
    } else {
      holder.tv.setText(content);
    }
    if (showKb) {
      holder.tv.requestFocus();
      showKb = false;
    }
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class RichTxtTxtVH extends FlexibleViewHolder {
    @BindView(R2.id.tv) EditText tv;

    public RichTxtTxtVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
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
              }
            }
          }
          return false;
        }
      });
    }
  }
}