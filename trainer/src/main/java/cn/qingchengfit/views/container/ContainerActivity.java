package cn.qingchengfit.views.container;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import cn.qingchengfit.article.ArticleCommentsListFragmentBuilder;
import cn.qingchengfit.article.ArticleReplyFragment;
import cn.qingchengfit.chat.RecruitMessageListFragmentBuilder;
import cn.qingchengfit.utils.IntentUtils;
import com.qingchengfit.fitcoach.R;

public class ContainerActivity extends AppCompatActivity {

    public static void router(String module, Context context) {
        Intent toStatement = new Intent(context, ContainerActivity.class);
        toStatement.putExtra("router", module);
        context.startActivity(toStatement);
    }

  public static void router(String module, Context context, Object... params) {
    Intent toStatement = new Intent(context, ContainerActivity.class);
    toStatement.putExtra("router", module);
    toStatement.putExtra("params", params);
    context.startActivity(toStatement);
  }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        String router = getIntent().getStringExtra("router");
        if (getIntent() != null && getIntent().getData() != null && getIntent().getData().getPath() != null) {
            router = getIntent().getData().getPath();
        }
        Fragment fragment = new Fragment();
        switch (router.toLowerCase()) {
            case "/replies/":
            case "/replies":
                fragment = new ArticleReplyFragment();
                break;
            case "/comments/":
            case "/comments":
                String articleid = IntentUtils.getIntentFromUri(getIntent(), "news_id");
                String replyId = IntentUtils.getIntentFromUri(getIntent(), "replyId");
                String replayname = IntentUtils.getIntentFromUri(getIntent(), "replyName");
                fragment = new ArticleCommentsListFragmentBuilder(articleid).replyId(replyId).replyName(replayname).build();
              break;
          case "/recruit/message_list":
            if (getIntent().getExtras().containsKey("params")) {
              fragment = new RecruitMessageListFragmentBuilder(
                  (String) ((Object[]) (getIntent().getExtras().get("params")))[0]).build();
            }
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag, fragment).commit();
    }

    @Override public void onBackPressed() {
        if (getIntent() != null && getIntent().getData() != null) {
            Intent ret = new Intent();
            String aciton = getIntent().getData().getHost() + getIntent().getData().getPath();
            ret.putExtra("web_action", aciton);
            setResult(Activity.RESULT_OK, ret);
        }
        super.onBackPressed();
    }
}
