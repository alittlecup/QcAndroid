package cn.qingchengfit.widgets;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.MeasureUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2016/12/5.
 */

public class PicChooserFragment extends Fragment implements FlexibleAdapter.OnItemClickListener {

    protected RecyclerView mRecyclerView;
    protected TextView mCount;
    protected CommonFlexAdapter mFlexAdapter;
    protected List<AbstractFlexibleItem> mDatas = new ArrayList<>();
    Boolean mModeSingle = false;
    private LinearLayout mFoot;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_image, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        mCount = (TextView) view.findViewById(R.id.chosen_count);
        mFoot = (LinearLayout) view.findViewById(R.id.layout_foot);

        view.findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ArrayList<String> r = new ArrayList<String>();
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mFlexAdapter.isSelected(i)) {
                        r.add(((ImageChosenItem) mDatas.get(i)).getImgUrl());
                    }
                }
                Intent back = new Intent();
                back.putExtra("data", r);
                getActivity().setResult(Activity.RESULT_OK, back);
                getActivity().finish();
            }
        });
        mFlexAdapter = new CommonFlexAdapter(mDatas, this);
        mFlexAdapter.setMode(mModeSingle ? SelectableAdapter.MODE_SINGLE : SelectableAdapter.MODE_MULTI);
        mRecyclerView.setHasFixedSize(true);
        SmoothScrollGridLayoutManager manager = new SmoothScrollGridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.divider_translate_5dp, 5));
        mRecyclerView.setAdapter(mFlexAdapter);
        new LocalImagesTask().execute("");
        return view;
    }

    @Override public boolean onItemClick(int position) {
        if (mModeSingle) {

            ArrayList<String> r = new ArrayList<String>();
            r.add(((ImageChosenItem) mDatas.get(position)).getImgUrl());
            Intent back = new Intent();
            back.putExtra("data", r);
            getActivity().setResult(Activity.RESULT_OK, back);
            getActivity().finish();
        } else {
            mFlexAdapter.toggleSelection(position);
            int c = mFlexAdapter.getSelectedItemCount();
            mCount.setText(c + "");
            if (c > 0) {
                ViewCompat.animate(mFoot).translationY(0).setDuration(500).start();
            } else {
                ViewCompat.animate(mFoot)
                    .translationY(MeasureUtils.dpToPx(getResources().getDimension(R.dimen.qc_item_height), getResources()))
                    .setDuration(500)
                    .start();
            }
        }
        return true;
    }

    class LocalImagesTask extends AsyncTask<String, Integer, String> {

        @Override protected String doInBackground(String... params) {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = getContext().getContentResolver();

            //只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

            if (mCursor == null) {
                return null;
            }
            while (mCursor.moveToNext()) {
                //获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                mDatas.add(new ImageChosenItem(mModeSingle, path, getContext()));
                //获取该图片的父路径名
                //String parentName = new File(path).getParentFile().getName();
            }

            //通知Handler扫描图片完成

            mCursor.close();
            return "done";
        }

        @Override protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mFlexAdapter.notifyDataSetChanged();
        }
    }
}
