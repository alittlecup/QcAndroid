package cn.qingchengfit.chat;

import android.util.Pair;
import cn.qingchengfit.chat.model.ChatGym;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.ChatFriendsData;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ChatFriendPresenter extends BasePresenter {
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public ChatFriendPresenter() {
    }

    public void queryChatFriend() {
        RxRegiste(restRepository.getGet_api()
            .qcQueryChatFriends()
            .subscribeOn(Schedulers.computation())
            .flatMap(new Func1<QcResponseData<ChatFriendsData>, Observable<Pair<List<ChatGym>, List<Staff>>>>() {
                @Override
                public Observable<Pair<List<ChatGym>, List<Staff>>> call(QcResponseData<ChatFriendsData> chatFriendsDataQcResponseData) {
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
                }
            })
            .subscribe(new Action1<Pair<List<ChatGym>, List<Staff>>>() {
                @Override public void call(Pair<List<ChatGym>, List<Staff>> listListPair) {
                    view.onGymList(listListPair.first);
                    view.onUserList(listListPair.second);
                }
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
