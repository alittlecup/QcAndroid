package cn.qingchengfit.saasbase.course.batch.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.common.mvvm.ViewModelFactory;
import cn.qingchengfit.saasbase.course.batch.viewmodel.BatchCopyViewModel;
import cn.qingchengfit.saasbase.databinding.FragmentCopyBatchBinding;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;

/**
 * Created by fb on 2018/4/4.
 */

@Leaf(module = "course", path = "/batch/copy/")
public class BatchCopyFragment extends BaseFragment{

  @Inject
  ViewModelFactory viewModelFactory;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentCopyBatchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_copy_batch, container, false);
    BatchCopyViewModel viewModel = viewModelFactory.create(BatchCopyViewModel.class);
    binding.setCopyViewModel(viewModel);
    return binding.getRoot();
  }
}
