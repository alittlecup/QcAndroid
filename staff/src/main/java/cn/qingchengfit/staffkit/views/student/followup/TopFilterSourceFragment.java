package cn.qingchengfit.staffkit.views.student.followup;

import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.views.student.filter.SourceFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import javax.inject.Inject;

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
 * // list
 * //Created by yangming on 16/12/2.
 */
public class TopFilterSourceFragment extends SourceFragment implements FlexibleAdapter.OnItemClickListener {

    @Inject public TopFilterSourceFragment() {
        chooseType = 3;
    }

    @Override public boolean onItemClick(int position) {
        super.onItemClick(position);
        FollowUpFilterEvent event = new FollowUpFilterEvent();
        event.eventType = FollowUpFilterEvent.EVENT_SOURCE_ITEM_CLICK;
        RxBus.getBus().post(event);
        return false;
    }
}
