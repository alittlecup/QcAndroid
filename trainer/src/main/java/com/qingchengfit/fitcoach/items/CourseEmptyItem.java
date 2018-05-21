package com.qingchengfit.fitcoach.items;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
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
 * Created by Paper on 16/8/8.
 */
public class CourseEmptyItem extends AbstractFlexibleItem<CourseEmptyItem.CourseEmptyVH> {

    private View.OnClickListener clickListener;
    private String hint;
    private boolean isPrivate;

    public CourseEmptyItem(View.OnClickListener clickListener, String hint, boolean p) {
        this.clickListener = clickListener;
        this.isPrivate = p;
        this.hint = hint;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_no_course;
    }

    @Override public CourseEmptyVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new CourseEmptyVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CourseEmptyVH holder, int position, List payloads) {
        holder.addCourseBtn.setOnClickListener(clickListener);
        holder.nodataHint.setText(hint);
        holder.addCourseBtn.setText(isPrivate ? "+ 添加私教种类" : "+ 添加团课种类");
    }

    public static class CourseEmptyVH extends FlexibleViewHolder {

	TextView nodataHint;
	Button addCourseBtn;

        public CourseEmptyVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          nodataHint = (TextView) view.findViewById(R.id.nodata_hint);
          addCourseBtn = (Button) view.findViewById(R.id.add_course_btn);
        }
    }
}
