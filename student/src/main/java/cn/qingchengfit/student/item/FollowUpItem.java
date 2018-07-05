package cn.qingchengfit.student.item;

import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.ItemStudentFollowUpStateBinding;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import java.util.ArrayList;
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
 * //Created by yangming on 16/12/6.
 */
public class FollowUpItem extends AbstractFlexibleItem<DataBindingViewHolder<ItemStudentFollowUpStateBinding>> implements IFilterable {

    public QcStudentBeanWithFollow data;
    public int status;

    public FollowUpItem(QcStudentBeanWithFollow data, Integer status) {
        this.data = data;
        this.status = status;
    }

    public QcStudentBeanWithFollow getData() {
        return data;
    }

    public void setData(QcStudentBeanWithFollow data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_follow_up_state;
    }

    @Override
    public DataBindingViewHolder<ItemStudentFollowUpStateBinding> createViewHolder(View view, FlexibleAdapter adapter) {
        return new DataBindingViewHolder<>(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, DataBindingViewHolder<ItemStudentFollowUpStateBinding> holder, int position, List payloads) {
        holder.itemView.setTag(data);
        ItemStudentFollowUpStateBinding binding = (ItemStudentFollowUpStateBinding) holder.getDataBinding();

        PhotoUtils.smallCircle(binding.itemPersonHeader,data.avatar);

        binding.itemPersonName.setText(data.username);
        if (0 == data.gender) { // 男
            binding.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            binding.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_female);
        }

        binding.tvStudentContactTa.setTag(data.phone);
        binding.itemPersonPhonenum.setText(new StringBuilder().append("手机：").append(data.phone).toString());
        List<String> sellerNames = new ArrayList<>();
        if (data.sellers != null && !data.sellers.isEmpty()) {
            for (Staff saler : data.sellers) {
                sellerNames.add(saler.username);
            }
        }

        binding.itemPersonDesc.setText(
            new StringBuilder().append("销售：").append(StringUtils.List2StrWithChineseSplit(sellerNames)).toString());

        StringUtils.studentStatusWithArrow(binding.tvStudentStatus, data.status);

        String desc = "";
        switch (status) {
            case 0:
                desc = new StringBuilder().append("来源：")
                    .append(TextUtils.isEmpty(data.origin) ? "" : data.origin)
                    .append("\n推荐人：")
                    .append(data.recommend_by == null || TextUtils.isEmpty(data.recommend_by.username) ? "" : data.recommend_by.username)
                    .append("\n注册时间：")
                    .append(TextUtils.isEmpty(data.joined_at) ? "" : data.joined_at.split("T")[0])
                    .toString();
                break;
            case 1:
                desc = new StringBuilder().append("最新跟进：").append(data.track_record).toString();
                break;
            case 2:
                if (!TextUtils.isEmpty(data.first_card_info)) {
                    desc = new StringBuilder().append("首次购卡：").append(data.first_card_info).toString();
                }
                break;
            case 4://会员转化
                // REFACTOR: 2017/11/13 会员转换的item中销售的字段重复
                StringBuilder sbStrans = new StringBuilder().append("销售：");
                if (data.sellers != null && data.sellers.size() > 0) {
                    for (Staff staff : data.sellers) {
                        sbStrans.append(staff.getUsername()).append(" ");
                    }
                }
                sbStrans.append("\n注册时间：").append(DateUtils.getYYYYMMDDfromServer(data.joined_at));
                desc = sbStrans.toString();
                break;
        }
        if (status == 2 && TextUtils.isEmpty(data.first_card_info)) {
            binding.tvStudentDesc.setVisibility(View.GONE);
        } else {
            binding.tvStudentDesc.setVisibility(View.VISIBLE);
        }
        binding.tvStudentDesc.setText(desc);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public boolean filter(String constraint) {
        if (data.username.contains(constraint) || data.phone.contains(constraint)) return true;
        return false;
    }
}