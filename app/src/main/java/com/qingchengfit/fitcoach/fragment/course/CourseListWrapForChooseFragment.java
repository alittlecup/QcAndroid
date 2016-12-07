package com.qingchengfit.fitcoach.fragment.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.items.CourseItem;

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
 * Created by Paper on 16/11/22.
 */

public class CourseListWrapForChooseFragment extends CourseListFragment {
    public static CourseListWrapForChooseFragment newInstance(boolean isPrivate) {
        Bundle args = new Bundle();
        args.putBoolean("isPrivate", isPrivate);
        CourseListWrapForChooseFragment fragment = new CourseListWrapForChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public boolean onItemClick(int position) {
        //super.onItemClick(position);
        if (mAdatper.getItem(position) instanceof CourseItem) {
            Intent rst = new Intent();
            rst.putExtra("course", ((CourseItem) mAdatper.getItem(position)).courseDetail);
            getActivity().setResult(Activity.RESULT_OK, rst);
            getActivity().finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_course_btn){
            if (getActivity() instanceof CourseActivity){
                getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag, AddCourseFragment.newInstance(getArguments().getBoolean("isPrivate",false)))
                    .addToBackStack(getFragmentName())
                    .commit();
            }
        }
    }
}
