package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecordEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordEditFragment extends Fragment {
    public static final String TAG = RecordEditFragment.class.getName();
    private static final String TITLE = "param1";
    private static final String CONTENT = "param2";
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String mTitle;
    private String mContent;

    public RecordEditFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordEditFragment newInstance(String param1, String param2) {
        RecordEditFragment fragment = new RecordEditFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, param1);
        args.putString(CONTENT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
            mContent = getArguments().getString(CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_edit, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle(mTitle);
        toolbar.inflateMenu(R.menu.menu_delete);
        toolbar.setOnMenuItemClickListener(item -> false);//TODO 删除该条记录
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
