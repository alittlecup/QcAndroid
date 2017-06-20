package cn.qingchengfit.recruit.views.organization;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import java.util.ArrayList;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOganasitionFragment extends BaseFragment {
  public static final String TAG = AddOganasitionFragment.class.getName();
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.addgym_name) CommonInputView addgymName;
  @BindView(R2.id.addgym_contact) CommonInputView addgymContact;
  @BindView(R2.id.addgym_city) CommonInputView addgymCity;
  @BindView(R2.id.workexpedit_descripe) EditText workexpeditDescripe;
  @BindView(R2.id.addgym_addbtn) Button addgymAddbtn;
  @BindView(R2.id.addgym_brand) CommonInputView addgymBrand;
  @Inject QcRestRepository restRepository;
  private ArrayList<String> options1Items = new ArrayList<String>();
  private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
  private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
  private SearchInterface searchListener;
  private Unbinder unbinder;

  public AddOganasitionFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_gym, container, false);
    unbinder = ButterKnife.bind(this, view);
    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setTitle("添加主办机构");
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });
    addgymName.setLabel("机构名");
    addgymCity.setVisibility(View.GONE);
    addgymBrand.setVisibility(View.GONE);
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.decript_layout) public void onDescripte() {
    workexpeditDescripe.requestFocus();
  }

  @OnClick(R2.id.addgym_addbtn) public void onClickAdd() {
    if (addgymName.getContent().length() < 3) {
      Toast.makeText(getActivity(), "机构名称至少填写三个字", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(addgymContact.getContent()) || addgymContact.getContent().length() < 7) {
      Toast.makeText(getActivity(), "请填写正确的联系方式", Toast.LENGTH_SHORT).show();
      return;
    }
    RxRegiste(restRepository.createGetApi(PostApi.class)
        .qcAddOrganization(
            new OrganizationBean(addgymName.getContent(), addgymContact.getContent(), workexpeditDescripe.getText().toString()))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcAddOrganizationResponse>() {
          @Override public void call(QcAddOrganizationResponse qcResponse) {
            if (qcResponse.status == 200) {
              searchListener.onSearchResult(100, Integer.parseInt(qcResponse.data.gym.id), qcResponse.data.gym.name, "", "", false);
              Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(getActivity(), qcResponse.msg, Toast.LENGTH_SHORT).show();
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {

          }
        }));
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof SearchInterface) {
      searchListener = (SearchInterface) context;
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    searchListener = null;
  }
}
