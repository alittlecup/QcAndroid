package cn.qingchengfit.staffkit.views.student.filter;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.MyBindingFelxibleViewHolder;
import cn.qingchengfit.utils.CompatUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * //Created by yangming on 16/12/2.
 */

public class ReferrerItem extends AbstractFlexibleItem<MyBindingFelxibleViewHolder> {

    private StudentReferrerBean data;

    public ReferrerItem(StudentReferrerBean referrer) {
        this.data = referrer;
    }

    public StudentReferrerBean getData() {
        return data;
    }

    public void setData(StudentReferrerBean data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_referrer;
    }

    @Override
    public MyBindingFelxibleViewHolder createViewHolder(final View view, FlexibleAdapter adapter) {
        cn.qingchengfit.staffkit.databinding.ItemReferrerBinding binding = DataBindingUtil.bind(view);
        MyBindingFelxibleViewHolder holder = new MyBindingFelxibleViewHolder(view, adapter);
        holder.setBinding(binding);
        return holder;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, MyBindingFelxibleViewHolder holder, int position, List payloads) {
        holder.itemView.setTag(position);
        cn.qingchengfit.staffkit.databinding.ItemReferrerBinding binding =
            (cn.qingchengfit.staffkit.databinding.ItemReferrerBinding) holder.getBinding();
        binding.personal.data.itemPersonGender.setVisibility(View.GONE);
        binding.personal.data.itemPersonDesc.setVisibility(View.GONE);
        binding.imgHook.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
        binding.personal.data.itemPersonName.setTextColor(CompatUtils.getColor(holder.itemView.getContext(),
            adapter.isSelected(position) ? R.color.qc_theme_green : R.color.qc_text_black));
        binding.personal.data.itemPersonPhonenum.setTextColor(CompatUtils.getColor(holder.itemView.getContext(),
            adapter.isSelected(position) ? R.color.qc_theme_green : R.color.qc_text_black));
        if (TextUtils.isEmpty(data.id)) {// 全部
            Glide.with(holder.itemView.getContext())
                .load(adapter.isSelected(position) ? R.drawable.ic_all_selected : R.drawable.ic_all_normal)
                .asBitmap()
                .placeholder(R.drawable.ic_default_head_nogender)
                .error(R.drawable.ic_default_head_nogender)
                .into(new CircleImgWrapper(binding.personal.header.itemPersonHeader, holder.itemView.getContext()));
            binding.personal.data.itemPersonName.setText(data.username);
            binding.personal.data.itemPersonPhonenum.setVisibility(View.GONE);
            binding.personal.data.tvReferrerCount.setVisibility(View.GONE);
        } else {
            Glide.with(holder.itemView.getContext())
                .load(data.avatar)
                .asBitmap()
                .placeholder(R.drawable.ic_default_head_nogender)
                .error(R.drawable.ic_default_head_nogender)
                .into(new CircleImgWrapper(binding.personal.header.itemPersonHeader, holder.itemView.getContext()));
            binding.personal.data.itemPersonName.setText(data.username);
            binding.personal.data.itemPersonPhonenum.setText(data.phone);
            binding.personal.data.tvReferrerCount.setVisibility(View.VISIBLE);
            binding.personal.data.tvReferrerCount.setText(holder.itemView.getContext()
                .getResources()
                .getString(R.string.qc_referrer_count, TextUtils.isEmpty(data.referrerCount) ? "0" : data.referrerCount));
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }
}
