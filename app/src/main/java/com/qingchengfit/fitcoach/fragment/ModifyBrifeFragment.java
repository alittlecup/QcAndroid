package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.HTMLUtils;
import com.qingchengfit.fitcoach.bean.BriefInfo;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.http.bean.ModifyDes;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
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
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<BriefInfo> mListData = new ArrayList<>();
    private ModifyBrifeAdapter adapter;
    private String mBrifeData;
    private TextInputDialog mTextInputDialog;


    public ModifyBrifeFragment() {
    }

    public static ModifyBrifeFragment newInstance(String data) {

        Bundle args = new Bundle();
        args.putString("brifedata", data);
        ModifyBrifeFragment fragment = new ModifyBrifeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_brief, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.menu_save, 0, "自我介绍");
        fragmentCallBack.onToolbarClickListener(item -> {
            onSave();
            return true;
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ModifyBrifeAdapter(mListData);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (!TextUtils.isEmpty(mListData.get(pos).getImg())) {
                    choosePic(pos);
                } else {
                    mTextInputDialog.setOnOkListener(v1 -> {
                        mListData.get(pos).setText(mTextInputDialog.getContent());
                        mTextInputDialog.dismiss();
                        adapter.notifyDataSetChanged();
//                        QcCloudClient.getApi().postApi.qcModifyDes(App.coachid,new ModifyDes(HTMLUtils.toHTML(mListData)))
//                            .subscribeOn(Schedulers.newThread()).subscribe(qcResponse -> {
//                            if (qcResponse.status == ResponseResult.SUCCESS){
//
//                                getActivity().runOnUiThread(() -> {
//
//                                });
//
//                            }else {
//                                Toast.makeText(App.AppContex,"修改失败,请稍后重试",Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    });
                    mTextInputDialog.show();
                }
            }
        });
        recyclerview.setAdapter(adapter);
        return view;
    }


    public void onSave(){
        QcCloudClient.getApi().postApi.qcModifyDes(App.coachid, new ModifyDes(HTMLUtils.toHTML(mListData))).subscribeOn(Schedulers.newThread())
                .subscribe(qcResponse -> {
                    getActivity().runOnUiThread(() -> {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(App.AppContex, "", Toast.LENGTH_SHORT).show();
                        }
                    });

                });
    }

    public void choosePic(int type) {
        PicChooseDialog dialog = new PicChooseDialog(getActivity());
        dialog.setListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   dialog.dismiss();
                                   Intent intent = new Intent();
                                   // 指定开启系统相机的Action
                                   intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                   intent.addCategory(Intent.CATEGORY_DEFAULT);
                                   intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Configs.CameraPic)));
                                   if (type == -100)
                                       startActivityForResult(intent, INSERT_PIC_CAMERA);
                                   else startActivityForResult(intent, 200 + type);
                               }
                           },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/jpeg");
                        if (type == -100)
                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                        else startActivityForResult(intent, 300 + type);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
//                        } else {
//                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
//                        }
                    }
                }

        );
        dialog.show();
    }

    @OnClick(R.id.modifybrief_insertimg)
    public void onInsertImg() {
        choosePic(-100);
    }

    @OnClick(R.id.modifybrief_inserttext)
    public void onInsertText() {
        mTextInputDialog.setOnOkListener(v -> {
            mListData.add(new BriefInfo(mTextInputDialog.getContent(), null));
            mTextInputDialog.dismiss();
            adapter.notifyDataSetChanged();

        });
        mTextInputDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChoosePicUtils.CHOOSE_GALLERY || requestCode == ChoosePicUtils.CHOOSE_CAMERA) {
            File f = ChoosePicUtils.choosePicFileCtl(getActivity(), requestCode, data, Configs.CameraPic);
            Observable.just(f)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(s -> {
                        String filename = UUID.randomUUID().toString();
                        boolean reslut = UpYunClient.upLoadImg("/brief/", filename, s);
                        Observable.just(reslut)
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(aBoolean -> {
                                    if (aBoolean) {

                                        BriefInfo briefInfo = new BriefInfo(null, UpYunClient.UPYUNPATH + "/brief/" + filename + ".png");
                                        mListData.add(briefInfo);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getActivity(), "添加图片失败", Toast.LENGTH_SHORT).show();
                                    }

                                });
                    });

        } else {
            File f = ChoosePicUtils.choosePicFileCtl(getActivity(), requestCode, data, Configs.CameraPic);
            Observable.just(f)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(s -> {
                        String filename = UUID.randomUUID().toString();
                        boolean reslut = UpYunClient.upLoadImg("/brief/", filename, s);
                        Observable.just(reslut)
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(aBoolean -> {
                                    if (aBoolean) {

                                        mListData.get(requestCode % 100).setImg(UpYunClient.UPYUNPATH + "/brief/" + filename + ".png");
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getActivity(), "添加图片失败", Toast.LENGTH_SHORT).show();
                                    }

                                });
                    });
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static class ModifyBrifeVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_modifybrief_text)
        TextView itemModifybriefText;
        @Bind(R.id.item_modifybrief_img)
        ImageView itemModifybriefImg;
        @Bind(R.id.item_modifybrief_del)
        Button itemModifybriefDel;
        @Bind(R.id.item_modifybrief_up)
        Button itemModifybriefUp;
        @Bind(R.id.item_modifybrief_down)
        Button itemModifybriefDown;

        public ModifyBrifeVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

    class ModifyBrifeAdapter extends RecyclerView.Adapter<ModifyBrifeVH> implements View.OnClickListener {


        private List<BriefInfo> datas;
        private OnRecycleItemClickListener listener;
        public ModifyBrifeAdapter(List datas) {
            this.datas = datas;
        }

        @Override
        public ModifyBrifeVH onCreateViewHolder(ViewGroup parent, int viewType) {
            ModifyBrifeVH brifeVH = new ModifyBrifeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modifybrief, parent, false));
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

        @Override
        public void onBindViewHolder(ModifyBrifeVH holder, int position) {
            holder.itemView.setTag(position);
            BriefInfo briefInfo = datas.get(position);
            if (position == 0)
                holder.itemModifybriefUp.setEnabled(false);
            else holder.itemModifybriefUp.setEnabled(true);

            if (position == getItemCount() - 1)
                holder.itemModifybriefDown.setEnabled(false);
            else holder.itemModifybriefDown.setEnabled(true);

            if (briefInfo.getImg() != null) {
                Glide.with(App.AppContex).load(briefInfo.getImg()).into(holder.itemModifybriefImg);
                holder.itemModifybriefImg.setVisibility(View.VISIBLE);
                holder.itemModifybriefText.setVisibility(View.GONE);
            } else {
                holder.itemModifybriefText.setText(briefInfo.getText());
                holder.itemModifybriefText.setVisibility(View.VISIBLE);
                holder.itemModifybriefImg.setVisibility(View.GONE);
            }
        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }


}
