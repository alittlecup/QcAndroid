package cn.qingchengfit.staffkit.views.student;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.LoopView;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
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
 * //Created by yangming on 16/12/20.
 */

public class StudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnClickListener {

  private static final int TYPE_STUDENT = 924;
  private static final int TYPE_FOOTER = 923;
  private static boolean isBindStudent = false;

  List<StudentBean> datas;
  boolean premiss = true;// TODO: 2017/8/14 权限传进来？
  private OnRecycleItemClickListener listener;
  private CoachService coachService;

  public StudentAdapter(List<StudentBean> d) {
    this.datas = d;
  }

  public void setListener(OnRecycleItemClickListener listener) {
    this.listener = listener;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder holder;
    if (viewType == TYPE_STUDENT) {
      holder = new StudentsHolder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
      holder.itemView.setOnClickListener(this);
      return holder;
    } else {
      holder = new ItemViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_total_count_footer, parent, false));
      holder.itemView.setOnClickListener(this);
      return holder;
    }
  }

  public void setDatas(List<StudentBean> datas) {
    this.datas = datas;
    notifyDataSetChanged();
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof StudentsHolder) {

      StudentsHolder studentsHolder = (StudentsHolder) holder;

      studentsHolder.itemView.setTag(position);
      if (datas.size() == 0) return;
      StudentBean studentBean = datas.get(position);

      studentsHolder.itemStudentGymname.setVisibility(View.GONE);
      //holder.itemStudentGymname.setText("所属场馆：" + studentBean.support_shop);
      studentsHolder.itemStudentName.setText(studentBean.username);
      studentsHolder.itemStudentPhonenum.setText(studentBean.phone);
      studentsHolder.itemHeaderLoop.setBackgroundDrawable(new LoopView("#00000000"));

      if (studentBean.gender) {//男
        studentsHolder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_male);
      } else {
        studentsHolder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_female);
      }
      if (studentBean.isTag) {
        if (TextUtils.equals(studentBean.head, "~")) {
          studentsHolder.itemStudentAlpha.setText("#");
        } else {
          studentsHolder.itemStudentAlpha.setText(studentBean.head);
        }
        studentsHolder.itemStudentAlpha.setVisibility(View.VISIBLE);
      } else {
        studentsHolder.itemStudentAlpha.setVisibility(View.GONE);
      }

      StringUtils.studentStatus(studentsHolder.tvStudentStatus, studentBean.status);

      if (isBindStudent) {
        studentsHolder.itemStudentAlpha.setVisibility(View.GONE);
        studentsHolder.tvStudentStatus.setVisibility(View.GONE);
      }

      Glide.with(holder.itemView.getContext())
          .load(PhotoUtils.getSmall(studentBean.avatar))
          .asBitmap()
          .placeholder(studentBean.gender ? R.drawable.default_student_male
              : R.drawable.default_student_female)
          .into(
              new CircleImgWrapper(studentsHolder.itemStudentHeader, holder.itemView.getContext()));
    } else {
      ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
      if (isBindStudent) {
        if (coachService != null) {
          //boolean premiss =
          //    serPermisAction.check(coachService.id(), coachService.model(), PermissionServerUtils.MANAGE_MEMBERS_IS_ALL);
          if (!premiss) {
            itemViewHolder.itemView.setVisibility(View.VISIBLE);
            itemViewHolder.tvTotalCount.setText("仅显示名下会员");
          } else {
            itemViewHolder.itemView.setVisibility(View.GONE);
          }
        }
      } else {
        itemViewHolder.tvTotalCount.setText(datas.size() + "名会员");
      }
    }
  }

  public void setCoachService(CoachService coachService) {
    this.coachService = coachService;
  }

  public void setIsBindStudent(boolean isBindStudent) {
    StudentAdapter.isBindStudent = isBindStudent;
  }

  public int getPositionForSection(char section) {
    for (int i = 0; i < getItemCount() - 1; i++) {
      String sortStr = datas.get(i).head;
      char firstChar = sortStr.toUpperCase().charAt(0);
      if (firstChar == section) {
        return i;
      }
    }
    return -1;
  }

  @Override public int getItemCount() {
    if (datas != null) {
      return datas.size() + 1;
    } else {
      return 0;
    }
  }

  @Override public int getItemViewType(int position) {
    if (position == datas.size()) return TYPE_FOOTER;
    return TYPE_STUDENT;
  }

  @Override public void onClick(View view) {
    if (listener != null && view.getTag() != null && (int) view.getTag() < datas.size()) {
      listener.onItemClick(view, (int) view.getTag());
    }
  }

  /**
   * recycle adapter
   */
  public static class StudentsHolder extends RecyclerView.ViewHolder {
    ImageView itemStudentHeader;
    TextView itemStudentName;
    TextView tvStudentStatus;
    TextView itemStudentPhonenum;
    TextView itemStudentGymname;
    ImageView itemStudentGender;
    TextView itemStudentAlpha;
    RelativeLayout itemHeaderLoop;

    public StudentsHolder(View itemView) {
      super(itemView);
      itemStudentHeader = (ImageView) itemView.findViewById(R.id.item_student_header);
      itemStudentName = (TextView) itemView.findViewById(R.id.item_student_name);
      tvStudentStatus = (TextView) itemView.findViewById(R.id.item_tv_student_status);
      itemStudentPhonenum = (TextView) itemView.findViewById(R.id.item_student_phonenum);
      itemStudentGymname = (TextView) itemView.findViewById(R.id.item_student_gymname);
      itemStudentGender = (ImageView) itemView.findViewById(R.id.item_student_gender);
      itemStudentAlpha = (TextView) itemView.findViewById(R.id.item_student_alpha);
      itemHeaderLoop = (RelativeLayout) itemView.findViewById(R.id.item_student_header_loop);
    }
  }

  public static class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTotalCount;

    public ItemViewHolder(View itemView) {
      super(itemView);
      tvTotalCount = (TextView) itemView.findViewById(R.id.tv_total_count);
    }
  }
}
