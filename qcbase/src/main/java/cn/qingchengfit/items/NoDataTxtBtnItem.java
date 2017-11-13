package cn.qingchengfit.items;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class NoDataTxtBtnItem extends AbstractFlexibleItem<NoDataTxtBtnItem.NoDataTxtBtnVH> {

    String title;
    String hint;
    String btnStr;

    public NoDataTxtBtnItem(String title, String hint, String btnStr) {
        this.title = title;
        this.hint = hint;
        this.btnStr = btnStr;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_no_data_text_btn;
    }

    @Override public NoDataTxtBtnVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new NoDataTxtBtnVH(view,adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, NoDataTxtBtnVH holder, int position, List payloads) {
        holder.tvTitle.setText(title);
        holder.hint.setText(hint);
        holder.btnFun.setText(btnStr);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class NoDataTxtBtnVH extends FlexibleViewHolder {
      @BindView(R2.id.tv_title) TextView tvTitle;
      @BindView(R2.id.hint) TextView hint;
      @BindView(R2.id.btn_fun) Button btnFun;

        public NoDataTxtBtnVH(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            btnFun.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    RxBus.getBus().post(new EventClickViewPosition.Builder().id(btnFun.getId()).position(getAdapterPosition()).build());
                }
            });
        }
    }
}