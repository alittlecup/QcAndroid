package cn.qingchengfit.staffkit.views.gym;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.main.HomeUnLoginFragment;
import cn.qingchengfit.staffkit.views.main.SetGymInMainFragmentBuilder;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import cn.qingchengfit.widgets.CommonInputView;
import com.bumptech.glide.Glide;
import java.io.File;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/28 2016.
 */
public class AddBrandFragment extends BaseDialogFragment implements AddBrandView {

	public RelativeLayout toolbarLayout;
	public Toolbar toolbar;
	public TextView toolbarTitle;
	Button btn;
	CommonInputView content;
	ImageView brandPhoto;
    @Inject CreateBrandPresenter presenter;
    private TextChange textChange;
    private String uploadImg;

    public static void start(Fragment fragment, int requestCode, String content) {
        AddBrandFragment f = newInstance(content);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static AddBrandFragment newInstance(String content) {
        Bundle args = new Bundle();
        AddBrandFragment fragment = new AddBrandFragment();
        args.putString("brand", content);
        fragment.setArguments(args);
        return fragment;
    }

    public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarTitle.setText("创建您的品牌");
        toolbar.setNavigationIcon(cn.qingchengfit.widgets.R.drawable.vd_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup ) {
            ((ViewGroup) toolbar.getParent()).setPadding(0,
              MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
        }
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_add_brand, container, false);
      toolbarLayout = (RelativeLayout) view.findViewById(R.id.toolbar_layout);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
      btn = (Button) view.findViewById(R.id.btn);
      content = (CommonInputView) view.findViewById(R.id.content);
      brandPhoto = (ImageView) view.findViewById(R.id.brand_photo);
      view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onComfirm();
        }
      });
      view.findViewById(R.id.brand_photo).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          addPhoto();
        }
      });

      initDI();
        presenter.attachView(this);
        content.addTextWatcher(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btn.setEnabled(true);
                } else {
                    btn.setEnabled(false);
                }
            }
        });
        initToolbar(toolbar);
        //if (!TextUtils.isEmpty(getArguments().getString("brand")))
        //    content.setContent(getArguments().getString("brand"));
        return view;
    }

    private void initDI() {
    }

 public void onComfirm() {
        if (!TextUtils.isEmpty(content.getContent().trim())) {
            showLoading();
            presenter.createBrand(content.getContent().trim(), uploadImg);

        }
    }

 public void addPhoto() {
        ChoosePictureFragmentDialog choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
        choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
            @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                    Glide.with(getContext()).load(new File(filePath)).into(brandPhoto);
                    RxRegiste(UpYunClient.rxUpLoad("brand/", filePath).subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            uploadImg = s;
                            Glide.with(getContext()).load(cn.qingchengfit.utils.PhotoUtils.getSmall(s)).into(brandPhoto);
                        }
                    }));
                }
            }
        });
        choosePictureFragmentDialog.show(getFragmentManager(), "");
    }

    @Override public void onSucceed(CreatBrand creatBrand) {
        //getTargetFragment().onActivityResult(getTargetRequestCode(), 1, IntentUtils.instanceStringIntent(content.getContent().trim()));
        //this.dismiss();
        hideLoading();
        if (getParentFragment() instanceof HomeUnLoginFragment) {
            ((HomeUnLoginFragment) getParentFragment()).replace(new SetGymInMainFragmentBuilder(creatBrand.brand).build(), true);
        }
    }

    @Override public void onFailed(String s) {
        ToastUtils.show(s);
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    class TextChange implements TextWatcher {

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
        }
    }
}
