package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.event.EventCourse;
import cn.qingchengfit.saasbase.course.course.items.CourseItem;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.IFlexible;

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
 * Created by Paper on 2017/11/29.
 */
@Leaf(module = "course", path = "/choose/") public class CourseChooseFragment
    extends CourseListFragment {

  @Need public String src;
  @Need public String courseId;

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(mIsPrivate ? "选择私教课" : "选择团课种类");
    floatingActionButton.setVisibility(View.GONE);
    llBottomAdd.setVisibility(View.VISIBLE);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    TextView tvBottomContent = view.findViewById(R.id.tv_bottom_content);
    tvBottomContent.setText(mIsPrivate?"添加私教课种类":"添加团课种类");
  }

  @Override public boolean onItemClick(int position) {
    IFlexible flexible = commonFlexAdapter.getItem(position);
    if (flexible == null) return true;
    if (flexible instanceof CourseItem) {
      Course c = ((CourseItem) flexible).getCourse();
      RxBus.getBus().post(new EventCourse(src, c));
      popBack();
    }
    return true;
  }

  @Override public void onUpdateEmptyView(int size) {
    if (commonFlexAdapter != null) {
      for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
        IFlexible item = commonFlexAdapter.getItem(i);
        if (item instanceof CourseItem) {
          if (!TextUtils.isEmpty(courseId)
              && ((CourseItem) item).getCourse() != null
              && TextUtils.equals(courseId, ((CourseItem) item).getCourse().id)) {
            commonFlexAdapter.addSelection(i);
            commonFlexAdapter.notifyItemChanged(i);
            break;
          }
        }
      }
    }
  }
}
