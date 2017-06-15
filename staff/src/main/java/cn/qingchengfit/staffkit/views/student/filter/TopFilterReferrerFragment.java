package cn.qingchengfit.staffkit.views.student.filter;

import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpFilterEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;

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
 * // 推荐人 list
 * //Created by yangming on 16/12/2.
 */
public class TopFilterReferrerFragment extends ReferrerFragment implements FlexibleAdapter.OnItemClickListener {

    public TopFilterReferrerFragment() {
    }

    /**
     * list item click
     */
    private void itemClick(int position) {
        if (selectedPos >= 0) {
            ((ReferrerItem) items.get(selectedPos)).getData().isSelect = false;
        }
        selectedPos = position;
        ((ReferrerItem) items.get(selectedPos)).getData().isSelect = true;
        flexibleAdapter.notifyDataSetChanged();
    }

    @Override public boolean onItemClick(int position) {
        itemClick(position);
        FollowUpFilterEvent event = new FollowUpFilterEvent();
        event.eventType = FollowUpFilterEvent.EVENT_REFERRER_ITEM_CLICK;
        RxBus.getBus().post(event);
        return false;
    }

    @Override public void searchReferrer(String query) {
        super.searchReferrer(query);
    }
}
