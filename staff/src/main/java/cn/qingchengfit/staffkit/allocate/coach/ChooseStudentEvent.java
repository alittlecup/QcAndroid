package cn.qingchengfit.staffkit.allocate.coach;

import android.widget.TextView;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by fb on 2017/5/3.
 */

public class ChooseStudentEvent {

    public boolean isSelectAll = false;
    public FlexibleAdapter flexibleAdapter;
    public TextView tvCount;
    public int position;

    public ChooseStudentEvent(boolean isSelectAll, FlexibleAdapter flexibleAdapter, TextView tvCount, int position) {
        this.isSelectAll = isSelectAll;
        this.flexibleAdapter = flexibleAdapter;
        this.tvCount = tvCount;
        this.position = position;
    }
}
