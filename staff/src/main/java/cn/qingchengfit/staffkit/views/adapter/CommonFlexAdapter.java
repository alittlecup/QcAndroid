package cn.qingchengfit.staffkit.views.adapter;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.ShopCommentItem;
import cn.qingchengfit.staffkit.views.allotsales.choose.ChooseSalerItem;
import cn.qingchengfit.staffkit.views.gym.items.GymFuntionItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
 * Created by Paper on 16/8/1.
 */
public class CommonFlexAdapter extends FlexibleAdapter {
    private int status = 0;
    private ArrayMap<String, Object> Tags = new ArrayMap<>();

    private int positionOld = -1;

    public CommonFlexAdapter(@NonNull List items) {
        super(items);
    }

    public CommonFlexAdapter(@NonNull List items, @Nullable Object listeners) {
        super(items, listeners);
    }

    public void setTag(String key, Object value) {
        Tags.put(key, value);
    }

    public Object getTag(String key) {
        return Tags.get(key);
    }

    @Override public List<Animator> getAnimators(View itemView, int position, boolean isSelected) {
        List<Animator> animators = new ArrayList<Animator>();
        if (getItem(position) instanceof ShopCommentItem) {
            if (position > positionOld) //inverted to have items animated up-side-down
            {
                addSlideInFromBottomAnimator(animators, itemView);
            } else {
                addSlideInFromTopAnimator(animators, itemView);
            }
        } else if (getItem(position) instanceof ChooseSalerItem || getItem(position) instanceof GymFuntionItem) {
            //            if (position > positionOld)
            addScaleInAnimator(animators, itemView, 0.5f);
        }
        if (position != positionOld) positionOld = position;

        return animators;
    }

    @Override public boolean hasNewSearchText(String newText) {
        //return super.hasNewSearchText(newText);
        return true;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
