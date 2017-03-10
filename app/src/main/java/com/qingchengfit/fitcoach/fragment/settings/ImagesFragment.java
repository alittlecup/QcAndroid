package com.qingchengfit.fitcoach.fragment.settings;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.bean.EventChooseImage;
import com.qingchengfit.fitcoach.fragment.BaseSettingFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.UpYunClient;
import com.qingchengfit.fitcoach.items.CommonNoDataItem;
import com.qingchengfit.fitcoach.items.ImageItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
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
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), R.color.divider_grey, 3));
        recyclerview.setLayoutManager(smoothScrollGridLayoutManager);
        smoothScrollGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                if (datas.size() > position && datas.get(position) instanceof CommonNoDataItem){
                    return 3;
                }
                return 1;
            }
        });
        recyclerview.setAdapter(commonFlexAdapter);


        /**
         * 上传照片
         */
        RxBusAdd(EventChooseImage.class).subscribe(eventChooseImage -> UpYunClient.rxUpLoad("/", eventChooseImage.filePath)
            .flatMap(s -> QcCloudClient.getApi().postApi.qcUploadWallImage(s))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponeSingleImageWall -> {
                datas.add(new ImageItem(qcResponeSingleImageWall.data.photo.photo));
                commonFlexAdapter.notifyDataSetChanged();
                ToastUtils.show("上传成功");
            }, throwable -> ToastUtils.show("上传失败")));

        RxRegiste(QcCloudClient.getApi().getApi.qcGetImageWalls()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponeSingleImageWall -> {
                if (qcResponeSingleImageWall.data.photos != null) {
                    datas.clear();
                    for (int i = 0; i < qcResponeSingleImageWall.data.photos.size(); i++) {
                        datas.add(new ImageItem(qcResponeSingleImageWall.data.photos.get(i).photo));
                    }
                    if (datas.size() == 0) datas.add(new CommonNoDataItem(R.drawable.img_no_imgs, "暂无照片"));
                    commonFlexAdapter.notifyDataSetChanged();
                }
            }, throwable -> {
            }));
        return view;
    }

    private void changeMode() {
        if (commonFlexAdapter.getMode() == SelectableAdapter.MODE_IDLE) {
            commonFlexAdapter.setMode(SelectableAdapter.MODE_MULTI);
            btnAdd.setVisibility(View.GONE);
            del.setVisibility(View.VISIBLE);
        } else {
            commonFlexAdapter.clearSelection();
            commonFlexAdapter.setMode(SelectableAdapter.MODE_IDLE);
            del.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }
        commonFlexAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_add) void uploadImages() {
        ChoosePictureFragmentDialog.newInstance(true).show(getFragmentManager(), "");
    }

    private void deleteImages() {
        //RxBusAdd(QcCloudClient.client.postApi)
    }

    @Override public String getFragmentName() {
        return ImagesFragment.class.getName();
    }

    @Override public boolean onItemClick(int position) {
        return false;
    }
}
