package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.TextInputActivity;
import com.qingchengfit.fitcoach.bean.BriefInfo;
import com.qingchengfit.fitcoach.component.DrawableCenterTextView;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
import com.qingchengfit.fitcoach.http.UpYunClient;

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
public class ModifyBrifeFragment extends Fragment {

    public static int INSERT_TEXT = 1;
    public List<BriefInfo> listData = new ArrayList<>();
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.modifybrief_insertimg)
    DrawableCenterTextView modifybriefInsertimg;
    @Bind(R.id.modifybrief_inserttext)
    DrawableCenterTextView modifybriefInserttext;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ModifyBrifeAdapter adapter;

    public ModifyBrifeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_brief, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("自我介绍");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listData.add(new BriefInfo(getString(R.string.test_test_short), null));
        listData.add(new BriefInfo(getString(R.string.test_test_short), null));
        listData.add(new BriefInfo(null, "http://zoneke-img.b0.upaiyun.com/header/123123/IMG_20150812_182222716.jpg"));

        adapter = new ModifyBrifeAdapter(listData);
        recyclerview.setAdapter(adapter);
        return view;
    }

    @OnClick(R.id.modifybrief_insertimg)
    public void onInsertImg() {
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
                                   startActivityForResult(intent, ChoosePicUtils.CHOOSE_CAMERA);
                               }
                           },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/jpeg");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                        } else {
                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                        }
                    }
                }

        );
        dialog.show();
    }

    @OnClick(R.id.modifybrief_inserttext)
    public void onInsertText() {
        Intent toInput = new Intent(getActivity(), TextInputActivity.class);
        startActivityForResult(toInput, INSERT_TEXT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSERT_TEXT) {
            if (resultCode >= 0) {
                listData.add(new BriefInfo(data.getStringExtra(TextInputActivity.FIX_TEXT), null));
                adapter.notifyDataSetChanged();
            }


        } else if (requestCode == ChoosePicUtils.CHOOSE_GALLERY || requestCode == ChoosePicUtils.CHOOSE_GALLERY) {
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
                                        listData.add(briefInfo);
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
        SimpleDraweeView itemModifybriefImg;
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

    class ModifyBrifeAdapter extends RecyclerView.Adapter<ModifyBrifeVH> {


        private List<BriefInfo> datas;

        public ModifyBrifeAdapter(List datas) {
            this.datas = datas;
        }

        @Override
        public ModifyBrifeVH onCreateViewHolder(ViewGroup parent, int viewType) {
            ModifyBrifeVH brifeVH = new ModifyBrifeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modifybrief, parent, false));
            brifeVH.itemModifybriefDel.setOnClickListener(v -> {
                this.notifyItemRemoved(brifeVH.OnDelclick(listData));
                this.notifyDataSetChanged();
            });
            brifeVH.itemModifybriefUp.setOnClickListener(v -> {
                int pos = brifeVH.OnUpClick(listData);
                this.notifyItemMoved(pos, pos - 1);
                this.notifyItemChanged(pos);
                this.notifyItemChanged(pos - 1);
            });
            brifeVH.itemModifybriefDown.setOnClickListener(v -> {
                int pos = brifeVH.OnDownClick(listData);
                this.notifyItemMoved(pos, pos + 1);
                this.notifyItemChanged(pos);
                this.notifyItemChanged(pos + 1);
            });
            return brifeVH;
        }


        @Override
        public void onBindViewHolder(ModifyBrifeVH holder, int position) {
            BriefInfo briefInfo = datas.get(position);
            if (position == 0)
                holder.itemModifybriefUp.setEnabled(false);
            else holder.itemModifybriefUp.setEnabled(true);

            if (position == getItemCount() - 1)
                holder.itemModifybriefDown.setEnabled(false);
            else holder.itemModifybriefDown.setEnabled(true);

            if (briefInfo.getImg() != null) {
//                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(briefInfo.getImg()))
//                        .setPostprocessor(new BasePostprocessor() {
//                            @Override
//                            public void process(Bitmap bitmap) {
//                                super.process(bitmap);
//                                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                        bitmap.getHeight()*holder.itemModifybriefImg.getWidth()/bitmap.getWidth());
//                                holder.itemModifybriefImg.setLayoutParams(layoutParams);
//                            }
//                        })
//                        .build();
//                DraweeController controller = Fresco.newDraweeControllerBuilder()
//                        .setImageRequest(request)
//                        .setTapToRetryEnabled(true)
//                        .build();
                holder.itemModifybriefImg.setImageURI(Uri.parse(briefInfo.getImg()));
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
    }


}
