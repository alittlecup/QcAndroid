package com.qingchengfit.fitcoach.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




import cn.qingchengfit.model.common.BriefInfo;
import cn.qingchengfit.utils.ChoosePicUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.HTMLUtils;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.component.ScaleWidthWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.ModifyDes;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyBrifeFragment extends BaseSettingFragment {

  public static int INSERT_TEXT = 1;
  public static int INSERT_PIC_CAMERA = 101;
  public static int INSERT_PIC_GALLEY = 102;
  public static int CHANGE_PIC_CAMERA = 103;
  public static int CHANGE_PIC_GALLEY = 104;
	RecyclerView recyclerview;
  private List<BriefInfo> mListData = new ArrayList<>();
  private ModifyBrifeAdapter adapter;
  private String mBrifeData;
  private TextInputDialog mTextInputDialog;

  private Subscription spUpImg;
  private ChoosePictureFragmentNewDialog DialogChoosepic;

  public ModifyBrifeFragment() {
  }

  public static ModifyBrifeFragment newInstance(String data) {

    Bundle args = new Bundle();
    args.putString("brifedata", data);
    ModifyBrifeFragment fragment = new ModifyBrifeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mBrifeData = getArguments().getString("brifedata");
      try {
        mListData.addAll(HTMLUtils.fromHTML(mBrifeData));
      } catch (Exception e) {
        LogUtil.e("error:" + e.getMessage() + "  :" + e.getCause());
      }
    }

    mTextInputDialog = new TextInputDialog(getContext());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_modify_brief, container, false);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

    view.findViewById(R.id.modifybrief_insertimg).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onInsertImg();
      }
    });
    view.findViewById(R.id.modifybrief_inserttext).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onInsertText();
      }
    });

    fragmentCallBack.onToolbarMenu(R.menu.menu_save, 0, "自我介绍");
    fragmentCallBack.onToolbarClickListener(item -> {
      onSave();
      return true;
    });

    recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapter = new ModifyBrifeAdapter(mListData);
    adapter.setListener(new OnRecycleItemClickListener() {
      @Override public void onItemClick(View v, int pos) {
        if (!TextUtils.isEmpty(mListData.get(pos).getImg())) {
          choosePic(pos);
        } else {
          mTextInputDialog.setOnOkListener(v1 -> {
            if (TextUtils.isEmpty(mTextInputDialog.getContent())) {
              mListData.remove(pos);
            } else {
              mListData.get(pos).setText(mTextInputDialog.getContent());
            }
            mTextInputDialog.dismiss();
            adapter.notifyDataSetChanged();
          });
          mTextInputDialog.show(mListData.get(pos).getText());
        }
      }
    });
    recyclerview.setAdapter(adapter);
    RxRegiste(QcCloudClient.getApi().getApi.qcGetCoach(App.coachid)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(qcCoachRespone -> {
        try {
          mListData.addAll(
            HTMLUtils.fromHTML(qcCoachRespone.getData().getCoach().getDescription()));
          adapter.notifyDataSetChanged();
        } catch (Exception e) {
          //e.printStackTrace();
        }
      }, throwable -> {
      }));

    return view;
  }

  public void onSave() {
    QcCloudClient.getApi().postApi.qcModifyDes(App.coachid,
      new ModifyDes(HTMLUtils.toHTML(mListData)))
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(qcResponse -> {
        getActivity().runOnUiThread(() -> {
          if (qcResponse.status == ResponseResult.SUCCESS) {
            getActivity().onBackPressed();
          } else {
            Toast.makeText(App.AppContex, "", Toast.LENGTH_SHORT).show();
          }
        });
      }, throwable -> {
      }, () -> {
      });
  }

  public void choosePic(int type) {
    if (DialogChoosepic == null) {
      DialogChoosepic = ChoosePictureFragmentNewDialog.newInstance();
      DialogChoosepic.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
        @Override public void onChoosefile(String s) {
          fragmentCallBack.ShowLoading("正在上传");
        }

        @Override public void onUploadComplete(String s, String s1) {
          fragmentCallBack.hideLoading();
          if (!TextUtils.isEmpty(s1)) {
            BriefInfo briefInfo = new BriefInfo(null, s1);
            mListData.add(briefInfo);
            adapter.notifyDataSetChanged();
          } else {
            Toast.makeText(getActivity(), "添加图片失败", Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
    DialogChoosepic.show(getChildFragmentManager(),"");
  }

 public void onInsertImg() {
    choosePic(-100);
  }

 public void onInsertText() {
    mTextInputDialog.setOnOkListener(v -> {
      if (TextUtils.isEmpty(mTextInputDialog.getContent())) {

      } else {
        mListData.add(new BriefInfo(mTextInputDialog.getContent(), null));
      }
      mTextInputDialog.dismiss();
      adapter.notifyDataSetChanged();
    });
    mTextInputDialog.show();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK) return;

    if (requestCode == ChoosePicUtils.CHOOSE_GALLERY
      || requestCode == ChoosePicUtils.CHOOSE_CAMERA) {
      //File f = ChoosePicUtils.choosePicFileCtl(getActivity(), requestCode, data, Configs.CameraPic);
      //fragmentCallBack.ShowLoading("正在上传");
      //if (spUpImg != null && spUpImg.isUnsubscribed()) {
      //  spUpImg.unsubscribe();
      //}
      //spUpImg = UpYunClient.rxUpLoad("brief/", f.getAbsolutePath())
      //  .observeOn(AndroidSchedulers.mainThread())
      //  .subscribe(s -> {
      //
      //  }, throwable -> {
      //  });
      //

    } else {
      //File f = ChoosePicUtils.choosePicFileCtl(getActivity(), requestCode, data, Configs.CameraPic);
      //fragmentCallBack.ShowLoading("正在上传");
      //if (spUpImg != null && spUpImg.isUnsubscribed()) {
      //  spUpImg.unsubscribe();
      //}
      //spUpImg = UpYunClient.rxUpLoad("brief/", f.getAbsolutePath())
      //  .observeOn(AndroidSchedulers.mainThread())
      //  .subscribe(s -> {
      //    fragmentCallBack.hideLoading();
      //    if (!TextUtils.isEmpty(s)) {
      //      mListData.get(requestCode % 100).setImg(s);
      //      adapter.notifyDataSetChanged();
      //    } else {
      //      Toast.makeText(getActivity(), "添加图片失败", Toast.LENGTH_SHORT).show();
      //    }
      //  }, throwable -> {
      //  });
    }
  }

  @Override public void onDestroyView() {

    super.onDestroyView();
    if (spUpImg != null && spUpImg.isUnsubscribed()) {
      spUpImg.unsubscribe();
    }
  }

  public static class ModifyBrifeVH extends RecyclerView.ViewHolder {
	TextView itemModifybriefText;
	ImageView itemModifybriefImg;
	Button itemModifybriefDel;
	Button itemModifybriefUp;
	Button itemModifybriefDown;

    public ModifyBrifeVH(View view) {
      super(view);
      itemModifybriefText = (TextView) view.findViewById(R.id.item_modifybrief_text);
      itemModifybriefImg = (ImageView) view.findViewById(R.id.item_modifybrief_img);
      itemModifybriefDel = (Button) view.findViewById(R.id.item_modifybrief_del);
      itemModifybriefUp = (Button) view.findViewById(R.id.item_modifybrief_up);
      itemModifybriefDown = (Button) view.findViewById(R.id.item_modifybrief_down);

    }

    public int OnDelclick(List data) {
      data.remove(getAdapterPosition());
      return getAdapterPosition();
    }

    public int OnUpClick(List data) {
      Collections.swap(data, getAdapterPosition(), getAdapterPosition() - 1);
      return getAdapterPosition();
    }

    public int OnDownClick(List data) {
      Collections.swap(data, getAdapterPosition(), getAdapterPosition() + 1);
      return getAdapterPosition();
    }
  }

  class ModifyBrifeAdapter extends RecyclerView.Adapter<ModifyBrifeVH>
    implements View.OnClickListener {

    private List<BriefInfo> datas;
    private OnRecycleItemClickListener listener;

    public ModifyBrifeAdapter(List datas) {
      this.datas = datas;
    }

    @Override public ModifyBrifeVH onCreateViewHolder(ViewGroup parent, int viewType) {
      ModifyBrifeVH brifeVH = new ModifyBrifeVH(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modifybrief, parent, false));
      brifeVH.itemModifybriefDel.setOnClickListener(v -> {
        this.notifyItemRemoved(brifeVH.OnDelclick(mListData));
        this.notifyDataSetChanged();
      });
      brifeVH.itemModifybriefUp.setOnClickListener(v -> {
        int pos = brifeVH.OnUpClick(mListData);
        this.notifyItemMoved(pos, pos - 1);
        this.notifyItemChanged(pos);
        this.notifyItemChanged(pos - 1);
      });
      brifeVH.itemModifybriefDown.setOnClickListener(v -> {
        int pos = brifeVH.OnDownClick(mListData);
        this.notifyItemMoved(pos, pos + 1);
        this.notifyItemChanged(pos);
        this.notifyItemChanged(pos + 1);
      });
      brifeVH.itemView.setOnClickListener(this);
      return brifeVH;
    }

    public void setListener(OnRecycleItemClickListener listener) {
      this.listener = listener;
    }

    @Override public void onBindViewHolder(ModifyBrifeVH holder, int position) {
      holder.itemView.setTag(position);
      BriefInfo briefInfo = datas.get(position);
      if (position == 0) {
        holder.itemModifybriefUp.setEnabled(false);
      } else {
        holder.itemModifybriefUp.setEnabled(true);
      }

      if (position == getItemCount() - 1) {
        holder.itemModifybriefDown.setEnabled(false);
      } else {
        holder.itemModifybriefDown.setEnabled(true);
      }

      if (briefInfo.getImg() != null) {
        Glide.with(App.AppContex)
          .load(PhotoUtils.getMiddle(briefInfo.getImg()))
          .asBitmap()
          .into(new ScaleWidthWrapper(holder.itemModifybriefImg));
        holder.itemModifybriefImg.setVisibility(View.VISIBLE);
        holder.itemModifybriefText.setVisibility(View.GONE);
      } else {
        holder.itemModifybriefText.setText(briefInfo.getText());
        holder.itemModifybriefText.setVisibility(View.VISIBLE);
        holder.itemModifybriefImg.setVisibility(View.GONE);
      }
    }

    @Override public int getItemCount() {
      return datas.size();
    }

    @Override public void onClick(View v) {
      listener.onItemClick(v, (int) v.getTag());
    }
  }
}
