package cn.qingchengfit.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.R;

public class VideoActivity extends BaseActivity {

  private String url;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play_video);
    Toolbar toolbar = findViewById(R.id.toolbar);
    TextView title = findViewById(R.id.toolbar_title);
    title.setText("播放视频");
    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(v -> finish());
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup) {
      ((ViewGroup) toolbar.getParent()).setPadding(0,
          MeasureUtils.getStatusBarHeight(this), 0, 0);
    }
    VideoView videoView = findViewById(R.id.video_play_view);
    if (getIntent().getExtras() != null)
      url = getIntent().getExtras().getString("url");
    if (url != null && !url.isEmpty()){
      videoView.setMediaController(new MediaController(this));
      videoView.setVideoURI(Uri.parse(url));
      videoView.start();
    }
  }

}
