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




import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;

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
	Toolbar toolbar;
	CommonInputView addgymName;
	CommonInputView addgymContact;
	CommonInputView addgymCity;
	EditText workexpeditDescripe;
	Button addgymAddbtn;
	CommonInputView addgymBrand;
  @Inject QcRestRepository restRepository;
  private ArrayList<String> options1Items = new ArrayList<String>();
  private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
  private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
  private SearchInterface searchListener;


  public AddOganasitionFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_gym, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    addgymName = (CommonInputView) view.findViewById(R.id.addgym_name);
    addgymContact = (CommonInputView) view.findViewById(R.id.addgym_contact);
    addgymCity = (CommonInputView) view.findViewById(R.id.addgym_city);
    workexpeditDescripe = (EditText) view.findViewById(R.id.workexpedit_descripe);
    addgymAddbtn = (Button) view.findViewById(R.id.addgym_addbtn);
    addgymBrand = (CommonInputView) view.findViewById(R.id.addgym_brand);
    view.findViewById(R.id.decript_layout).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDescripte();
      }
    });
    view.findViewById(R.id.addgym_addbtn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickAdd();
      }
    });

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

 public void onDescripte() {
    workexpeditDescripe.requestFocus();
  }

 public void onClickAdd() {
    if (addgymName.getContent().length() < 3) {
      Toast.makeText(getActivity(), "机构名称至少填写三个字", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(addgymContact.getContent()) || addgymContact.getContent().length() < 7) {
      Toast.makeText(getActivity(), "请填写正确的联系方式", Toast.LENGTH_SHORT).show();
      return;
    }
    RxRegiste(restRepository.createRxJava1Api(PostApi.class)
        .qcAddOrganization(
            new OrganizationBean(addgymName.getContent(), addgymContact.getContent(), workexpeditDescripe.getText().toString()))
        .onBackpressureBuffer()
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
