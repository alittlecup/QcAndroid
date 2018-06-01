package cn.qingchengfit.staffkit.views.student.score;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/22.
 */

public class StartItem extends AbstractFlexibleItem<StartItem.StartVH> {

    public View.OnClickListener listener;

    public StartItem(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_score_start;
    }

    @Override public StartVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new StartVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, StartVH holder, int position, List payloads) {
        //holder.img.setImageResource(imgRes);
        holder.hint.setOnClickListener(listener);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class StartVH extends FlexibleViewHolder {
	ImageView img;
	TextView hint;

        public StartVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          img = (ImageView) view.findViewById(R.id.img);
          hint = (TextView) view.findViewById(R.id.hint);
        }
    }
}
