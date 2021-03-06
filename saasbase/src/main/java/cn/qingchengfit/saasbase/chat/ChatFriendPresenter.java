package cn.qingchengfit.saasbase.chat;

import android.util.Pair;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.apis.ChatApis;
import cn.qingchengfit.saasbase.chat.model.ChatGym;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatFriendPresenter extends BasePresenter {
    @Inject QcRestRepository restRepository;
    private MVPView view;

    @Inject public ChatFriendPresenter() {
    }

    public void queryChatFriend() {
        RxRegiste(restRepository.createRxJava1Api(ChatApis.class)
            .qcQueryChatFriends().onBackpressureBuffer().subscribeOn(Schedulers.computation())
            .flatMap(chatFriendsDataQcResponseData -> {
                List<ChatGym> chatGyms = chatFriendsDataQcResponseData.getData().gyms;
                List<Staff> staffs = new ArrayList<Staff>();
                if (chatGyms != null) {
                    for (int i = 0; i < chatGyms.size(); i++) {
                        ChatGym g = chatGyms.get(i);
                        if (g.staffs != null) {
                            for (int j = 0; j < g.staffs.size(); j++) {
                                if (!staffs.contains(g.staffs.get(j))) staffs.add(g.staffs.get(j));
                            }
                        }
                        if (g.coaches != null) {
                            for (int j = 0; j < g.coaches.size(); j++) {
                                if (!staffs.contains(g.coaches.get(j))) staffs.add(g.coaches.get(j));
                            }
                        }
                    }
                }
                return Observable.just(new Pair<>(chatGyms, staffs)).observeOn(AndroidSchedulers.mainThread());
            })
            .subscribe(listListPair -> {
                view.onGymList(listListPair.first);
                view.onUserList(listListPair.second);
            }, new NetWorkThrowable()));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onUserList(List<Staff> staffs);

        void onGymList(List<ChatGym> gyms);
    }
}
