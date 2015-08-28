package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.BriefInfo;
import com.qingchengfit.fitcoach.component.DrawableCenterTextView;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
import com.qingchengfit.fitcoach.http.UpYunClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyBrifeFragment extends Fragment {

    public static int INSERT_TEXT = 1;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.modifybrief_insertimg)
    DrawableCenterTextView modifybriefInsertimg;
    @Bind(R.id.modifybrief_inserttext)
    DrawableCenterTextView modifybriefInserttext;
    private ModifyBrifeAdapter adapter;
    private List<BriefInfo> listData = new ArrayList<>();

    public ModifyBrifeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_brief, container, false);
        ButterKnife.bind(this, view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ModifyBrifeAdapter(new ArrayList<>());
        recyclerview.setAdapter(adapter);
        return view;
    }

    @OnClick(R.id.modifybrief_insertimg)
    public void onInsertImg() {
        PicChooseDialog dialog = new PicChooseDialog(getActivity());
        dialog.show();
    }

    public void onInsertText() {
//        getActivity().startActivityForResult();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSERT_TEXT) {
            //TODO 添加一段文字
        } else if (requestCode == ChoosePicUtils.CHOOSE_GALLERY || requestCode == ChoosePicUtils.CHOOSE_GALLERY) {
            File f = ChoosePicUtils.choosePicFileCtl(getActivity(), requestCode, data, Configs.CameraPic);
            Observable.just(f)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(s -> {
                        String filename = UUID.randomUUID().toString();
                        boolean reslut = UpYunClient.upLoadImg("/brief/", filename, s);
                        if (reslut) {
                            LogUtil.d("success");
                            BriefInfo briefInfo = new BriefInfo();
                            briefInfo.setImg(UpYunClient.UPYUNPATH + "/brief/" + filename);
                            listData.add(briefInfo);
                            adapter.notifyDataSetChanged();
                        } else {                            //upload failed TODO

                        }
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

        public ModifyBrifeVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class ModifyBrifeAdapter extends RecyclerView.Adapter<ModifyBrifeVH> {


        private List<BriefInfo> datas;

        public ModifyBrifeAdapter(List datas) {
            this.datas = datas;
        }

        @Override
        public ModifyBrifeVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ModifyBrifeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modifybrief, null));
        }

        @Override
        public void onBindViewHolder(ModifyBrifeVH holder, int position) {
            BriefInfo briefInfo = datas.get(position);
            if (briefInfo.getImg() != null) {

            }
        }


        @Override
        public int getItemCount() {
            return 3;
        }
    }


}
