package cn.qingchengfit.notisetting.item;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.notisetting.bean.NotiSettingMsgDetail;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class NotiSettingMsgDetailItem
    extends AbstractFlexibleItem<NotiSettingMsgDetailItem.NotiSettingMsgDetailVH> {
  NotiSettingMsgDetail msg;

  public NotiSettingMsgDetailItem(NotiSettingMsgDetail msg) {
    this.msg = msg;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_setting_msg_detail;
  }

  @Override
  public NotiSettingMsgDetailVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new NotiSettingMsgDetailVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, NotiSettingMsgDetailVH holder, int position,
      List payloads) {
    if (msg.user != null) {
      holder.tvTo.setText(
          (msg.user.username == null ? "" : msg.user.username) + "(" + (msg.user.phone == null ? (
              msg.phone == null ? "" : msg.phone) : msg.user.phone) + ")");
    }
    if (msg.is_successful) {
      holder.tvStatus.setText("发送成功");
      holder.tvStatus.setTextColor(
          ContextCompat.getColor(holder.tvStatus.getContext(), R.color.colorPrimary));
    } else {
      holder.tvStatus.setText("发送失败");
      holder.tvStatus.setTextColor(
          ContextCompat.getColor(holder.tvStatus.getContext(), R.color.red));
    }
    holder.tvContent.setText(msg.content);
    if (msg.type < 5 && msg.type > 0) {
      holder.tvChannel.setText((holder.tvChannel.getContext()
          .getResources()
          .getTextArray(R.array.noti_setting_filter_channel)[msg.type]).toString()
          + msg.number
          + "条");
    } else {
      holder.tvChannel.setText("");
    }
    holder.tvTimeSender.setText(getTimeSender(msg));
  }

  public String getTimeSender(NotiSettingMsgDetail m) {
    String ret = "";
    if (m.created_at != null) {
      ret = TextUtils.concat(ret,
          DateUtils.Date2MMDDHHmm(DateUtils.formatDateFromServer(m.created_at)), "，").toString();
    }
    if (m.created_by != null && m.created_by.username != null) {
      ret = TextUtils.concat(ret, m.created_by.username, "发送").toString();
    } else {
      ret = TextUtils.concat(ret, "系统发送").toString();
    }
    return ret;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class NotiSettingMsgDetailVH extends FlexibleViewHolder {
	TextView tvTo;
	TextView tvStatus;
	TextView tvContent;
	TextView tvChannel;
	TextView tvTimeSender;
    public NotiSettingMsgDetailVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTo = (TextView) view.findViewById(R.id.tv_to);
      tvStatus = (TextView) view.findViewById(R.id.tv_status);
      tvContent = (TextView) view.findViewById(R.id.tv_content);
      tvChannel = (TextView) view.findViewById(R.id.tv_channel);
      tvTimeSender = (TextView) view.findViewById(R.id.tv_time_sender);
    }
  }
}