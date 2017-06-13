package cn.qingchengfit.staffkit.views.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * Created by Paper on 16/7/27.
 */
public class AllCourseImagesAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    public AllCourseImagesAdapter(@NonNull List<AbstractFlexibleItem> items) {
        super(items);
    }

    public AllCourseImagesAdapter(@NonNull List<AbstractFlexibleItem> items, @Nullable Object listeners) {
        super(items, listeners);
    }

    //    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //        if (mInflater == null) {
    //			mInflater = LayoutInflater.from(parent.getContext());
    //		}
    //        switch (viewType){
    //            case TYPE_HEADER :
    //                return new AllCourseImageHeaderItem.CourseImageHeaderHolder(mInflater.inflate(R.layout.item_all_course_image_header,parent,false));
    //            case TYPE_VIEW:
    //                default:
    //                    return new AllCourseImageItem.AllCourseImageItemHolder(mInflater.inflate(R.layout.item_all_course_image_view,parent,false));
    //        }
    //    }
    //
    //    @Override
    //    public int getItemViewType(int position) {
    //        AbstractFlexibleItem item = getItem(position);
    //        if (item instanceof IHeader){
    //            return TYPE_HEADER;
    //        }else return TYPE_VIEW;
    //    }
    //
    //    @Override
    //    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    //        if (holder instanceof AllCourseImageHeaderItem.CourseImageHeaderHolder){
    //
    //        }else if (holder instanceof AllCourseImageItem.AllCourseImageItemHolder){
    //
    //        }
    //
    //
    //    }
}