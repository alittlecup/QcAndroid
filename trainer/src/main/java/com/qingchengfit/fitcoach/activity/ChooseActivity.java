package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.chat.ConversationFriendsFragment;
import cn.qingchengfit.chat.model.ChatGym;
import cn.qingchengfit.recruit.ChooseStaffFragment;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.ChooseAddressFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.CmBean;
import com.qingchengfit.fitcoach.fragment.guide.AddCycleFragment;
import com.qingchengfit.fitcoach.fragment.manage.ChooseGymFragmentBuilder;
import com.qingchengfit.fitcoach.fragment.schedule.ChooseScheduleGymFragmentBuilder;

public class ChooseActivity extends BaseActivity {

    public static final int TO_CHOSSE_ADDRESS = 0;
    public static final int TO_CHOSSE_CIRCLE = 1;
    public static final int TO_CHOSSE_GYM = 2;
    public static final int TO_CHOSSE_GYM_SCHEDULE = 3;
  public static final int CONVERSATION_FRIEND = 61;
  public static final int CHOOSE_STAFF = 62; // 选择工作人员
    private String chosenId;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        Fragment fragment = new Fragment();
        int to = getIntent().getIntExtra("to", 0);
        chosenId = getIntent().getStringExtra("id");
        if (getIntent().getData() != null && getIntent().getData().getPath() != null) {
            String path = getIntent().getData().getPath();
            if (path.contains("chat_friend")) {
                to = CONVERSATION_FRIEND;
            }
        }

        switch (to) {
            case 1:
                fragment =
                    AddCycleFragment.newInstance((CmBean) getIntent().getParcelableExtra("cmbean"), getIntent().getLongExtra("len", 0));
                break;
            case TO_CHOSSE_GYM:
                fragment = new ChooseGymFragmentBuilder(getIntent().getParcelableExtra("service")).build();
                break;
            case TO_CHOSSE_GYM_SCHEDULE:
                fragment = new ChooseScheduleGymFragmentBuilder(getIntent().getParcelableExtra("service")).build();
                break;
            case CONVERSATION_FRIEND:
                fragment = new ConversationFriendsFragment();
              break;
          case CHOOSE_STAFF:
            ChatGym gym = getIntent().getParcelableExtra("chatgym");
            if (gym == null) {
              this.finish();
              return;
            }
            fragment = ChooseStaffFragment.newInstance(gym);
                break;
            default:
                fragment = new ChooseAddressFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_choose_address, fragment).commit();
    }
}
