package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.StringUtils;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by yangming on 16/10/18.
 */
public class AllotSaleChooseAdapter extends RecyclerView.Adapter<AllotSaleChooseAdapter.ASCViewHolder> implements View.OnClickListener {
    public static final int TYPE_ADAPTER_CHOOSE = 0;
    public static final int TYPE_ADAPTER_DELETE = 1;

    List<StudentBean> datas;

    private OnRecycleItemClickListener listener;
    private int type = 0;

    public AllotSaleChooseAdapter(List<StudentBean> d) {
        this.datas = d;
    }

    public AllotSaleChooseAdapter(List<StudentBean> d, int type) {
        this.datas = d;
        this.type = type;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public List<StudentBean> getDatas() {
        return this.datas;
    }

    public void setDatas(List<StudentBean> d) {
        this.datas = d;
        notifyDataSetChanged();
    }

    @Override public ASCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ASCViewHolder holder =
            new ASCViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allotsale_choose, parent, false));

        return holder;
    }

    @Override public void onBindViewHolder(ASCViewHolder holder, int position) {
        if (datas.size() == 0) return;

        switch (type) {
            case TYPE_ADAPTER_CHOOSE:
                holder.itemCheckbox.setVisibility(View.VISIBLE);
                holder.itemDelete.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(this);
                holder.itemView.setTag(position);
                break;
            case TYPE_ADAPTER_DELETE:
                holder.itemCheckbox.setVisibility(View.GONE);
                holder.itemDelete.setVisibility(View.VISIBLE);
                holder.itemDelete.setOnClickListener(this);
                holder.itemDelete.setTag(position);
                break;
        }

        StudentBean studentBean = datas.get(position);
        holder.itemCheckbox.setChecked(studentBean.isChosen());
        //if (studentBean.isChosen) {
        //    if (studentBean.isOrigin) {
        //        holder.itemCheckbox.setButtonDrawable(
        //            holder.itemView.getContext().getResources().getDrawable(R.drawable.radio_selector_allotsale_grey));
        //    } else {
        //        holder.itemCheckbox.setButtonDrawable(
        //            holder.itemView.getContext().getResources().getDrawable(R.drawable.radio_selector_allotsale));
        //    }
        //    holder.itemCheckbox.setChecked(true);
        //} else {
        //    holder.itemCheckbox.setButtonDrawable(
        //        holder.itemView.getContext().getResources().getDrawable(R.drawable.radio_selector_allotsale));
        //    holder.itemCheckbox.setChecked(false);
        //}

        // 销售names
        if (studentBean.sellers == null || studentBean.sellers.size() == 0) {
            holder.itemPersonDesc.setText(holder.itemView.getContext().getResources().getString(R.string.qc_allotsale_sale, " "));
        } else {
            holder.itemPersonDesc.setText(holder.itemView.getContext()
                .getResources()
                .getString(R.string.qc_allotsale_sale, StringUtils.sellersNames(studentBean.sellers)));
        }

        holder.itemPersonName.setText(studentBean.getUsername());
        holder.itemPersonPhonenum.setText(studentBean.getPhone());

        //        binding.personContent.personal.itemPersonHeaderLoop.setBackgroundDrawable(new LoopView(TextUtils.isEmpty(studentBean.color) ? "#00000000" : studentBean.color));
        if (studentBean.gender) {//男
            holder.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            holder.itemPersonGender.setImageResource(R.drawable.ic_gender_signal_female);
        }
        if (studentBean.isTag) {
            holder.itemStudentModifyAlpha.setVisibility(View.VISIBLE);
            holder.itemStudentModifyAlpha.setText(getItem(position).head);
        } else {
            holder.itemStudentModifyAlpha.setVisibility(View.GONE);
        }
        StringUtils.studentStatus(holder.status, studentBean.status);
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(studentBean.avatar))
            .asBitmap()
            .placeholder(R.drawable.ic_default_head_nogender)
            .error(R.drawable.ic_default_head_nogender)
            .into(new CircleImgWrapper(holder.itemPersonHeader, holder.itemView.getContext()));
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = datas.get(i).head;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public StudentBean getItem(int position) {
        return datas.get(position);
    }

    public class ASCViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_student_modify_alpha) TextView itemStudentModifyAlpha;
        @BindView(R.id.item_checkbox) CheckBox itemCheckbox;
        @BindView(R.id.item_delete) ImageView itemDelete;
        @BindView(R.id.item_person_header) ImageView itemPersonHeader;
        @BindView(R.id.item_person_name) TextView itemPersonName;
        @BindView(R.id.item_person_gender) ImageView itemPersonGender;
        @BindView(R.id.tv_referrer_count) TextView tvReferrerCount;
        @BindView(R.id.item_person_phonenum) TextView itemPersonPhonenum;
        @BindView(R.id.item_person_desc) TextView itemPersonDesc;
        @BindView(R.id.status) TextView status;

        public ASCViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
