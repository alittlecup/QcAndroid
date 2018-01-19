package com.qingchengfit.fitcoach.fragment.settings;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import cn.qingchengfit.events.EventChooseImage;
import com.qingchengfit.fitcoach.fragment.BaseSettingFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.items.ImageItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/3/7.
 */
public class ImagesFragment extends BaseSettingFragment implements FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.btn_add) Button btnAdd;
    @BindView(R.id.del) TextView del;
    private CommonFlexAdapter commonFlexAdapter;
    private List<AbstractFlexibleItem> datas = new ArrayList<>();

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images_wall, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.menu_edit, R.drawable.ic_arrow_left, "照片墙");
        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                changeMode();
                return false;
            }
        });
        commonFlexAdapter = new CommonFlexAdapter(datas, this);
        SmoothScrollGridLayoutManager smoothScrollGridLayoutManager = new SmoothScrollGridLayoutManager(getContext(), 3);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(smoothScrollGridLayoutManager);
        smoothScrollGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                if (datas.size() > position && datas.get(position) instanceof CommonNoDataItem) {
                    return 3;
                }
                return 1;
            }
        });
        recyclerview.setAdapter(commonFlexAdapter);

        /**
         * 上传照片
         */
        RxBusAdd(EventChooseImage.class).subscribe(eventChooseImage -> {
            showLoading();
            UpYunClient.rxUpLoad("/", eventChooseImage.filePath).observeOn(Schedulers.io()).flatMap(s -> {

                HashMap<String, Object> p = new HashMap<String, Object>();
                p.put("photo", s);
                return QcCloudClient.getApi().postApi.qcUploadWallImage(p);
            })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcResponeSingleImageWall -> {
                hideLoading();
                if (ResponseConstant.checkSuccess(qcResponeSingleImageWall)) {
                    if (datas.size() == 1 && datas.get(0) instanceof CommonNoDataItem) {
                        datas.clear();
                    }
                    datas.add(new ImageItem(qcResponeSingleImageWall.data.photo.photo, qcResponeSingleImageWall.data.photo.id));
                    commonFlexAdapter.notifyDataSetChanged();
                    ToastUtils.show("上传成功");
                } else {
                    ToastUtils.show("上传失败");
                }
            }, throwable -> {
                hideLoading();
                ToastUtils.show("上传失败");
            });
        });
        freshData();
        return view;
    }

    private void freshData() {
        fragmentCallBack.ShowLoading("请稍后");
        RxRegiste(QcCloudClient.getApi().getApi.qcGetImageWalls()
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponeSingleImageWall -> {
                fragmentCallBack.hideLoading();
                if (qcResponeSingleImageWall.data.photos != null) {
                    datas.clear();
                    for (int i = 0; i < qcResponeSingleImageWall.data.photos.size(); i++) {
                        datas.add(new ImageItem(qcResponeSingleImageWall.data.photos.get(i).photo,
                            qcResponeSingleImageWall.data.photos.get(i).id));
                    }
                    if (datas.size() == 0) datas.add(new CommonNoDataItem(R.drawable.img_no_imgs, "暂无照片"));
                    commonFlexAdapter.notifyDataSetChanged();
                }
            }, throwable -> {
                fragmentCallBack.hideLoading();
            }));
    }

    private void changeMode() {
        if (commonFlexAdapter.getMode() == SelectableAdapter.Mode.IDLE) {
            commonFlexAdapter.setMode(SelectableAdapter.Mode.MULTI);
            btnAdd.setVisibility(View.GONE);
            del.setVisibility(View.VISIBLE);
        } else {
            commonFlexAdapter.clearSelection();
            commonFlexAdapter.setMode(SelectableAdapter.Mode.IDLE);
            del.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }
        commonFlexAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_add) void uploadImages() {
        if (datas.size() >= 5) {
            cn.qingchengfit.utils.ToastUtils.show("您最多只能上传五张图片");
            return;
        }

        ChoosePictureFragmentDialog.newInstance(true).show(getFragmentManager(), "");
    }

    @OnClick(R.id.del) public void deleteImages() {
        String ids = "";
        for (int i = 0; i < commonFlexAdapter.getSelectedPositions().size(); i++) {
            int pos = commonFlexAdapter.getSelectedPositions().get(i);
            AbstractFlexibleItem item = datas.get(pos);
            if (item instanceof ImageItem) {
                if (i < commonFlexAdapter.getSelectedPositions().size() - 1) {
                    ids = ids.concat(((ImageItem) item).getId()).concat(",");
                } else {
                    ids = ids.concat(((ImageItem) item).getId());
                }
            }
        }
        if (!TextUtils.isEmpty(ids)) {
            RxRegiste(QcCloudClient.getApi().postApi.qcDeleteWallImage(ids)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<QcResponse>() {
                    @Override public void call(QcResponse qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            cn.qingchengfit.utils.ToastUtils.show("删除成功");
                            commonFlexAdapter.setMode(SelectableAdapter.Mode.IDLE);
                            freshData();
                            btnAdd.setVisibility(View.VISIBLE);
                            del.setVisibility(View.GONE);
                        }
                    }
                }));
        } else {
            cn.qingchengfit.utils.ToastUtils.show("您没有选择删除的照片");
        }
    }

    @Override public String getFragmentName() {
        return ImagesFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        if (commonFlexAdapter.getMode() == SelectableAdapter.Mode.MULTI) {
            if (datas.get(position) instanceof ImageItem) {
                commonFlexAdapter.toggleSelection(position);
                commonFlexAdapter.notifyItemChanged(position);
            }
        }
        return false;
    }
}
