package com.qingchengfit.fitcoach.server;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Utils.RevenUtils;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CalendarIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CAL_DAY = "com.qingchengfit.fitcoach.server.action.FOO";
    private static final String ACTION_CAL_WEEK = "com.qingchengfit.fitcoach.server.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.qingchengfit.fitcoach.server.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.qingchengfit.fitcoach.server.extra.PARAM2";

    public CalendarIntentService() {
        super("CalendarIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDay(Context context, long time, String param2) {
        try {
            if (context == null) {
                return;
            }
            boolean isSync = PreferenceUtils.getPrefBoolean(context, "cal_sync", true);
            if (!isSync)
                return;
            ;
            Intent intent = new Intent(context, CalendarIntentService.class);
            intent.setAction(ACTION_CAL_DAY);
            intent.putExtra(EXTRA_PARAM1, time);
            intent.putExtra(EXTRA_PARAM2, param2);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionWeek(Context context, long param1, String param2) {
        try {
            if (context == null) {
                return;
            }
            boolean isSync = PreferenceUtils.getPrefBoolean(context, "cal_sync", true);
            if (!isSync)
                return;
            ;
            Intent intent = new Intent(context, CalendarIntentService.class);
            intent.setAction(ACTION_CAL_WEEK);
            intent.putExtra(EXTRA_PARAM1, param1);
            intent.putExtra(EXTRA_PARAM2, param2);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CAL_DAY.equals(action)) {
                final long param1 = intent.getLongExtra(EXTRA_PARAM1, 0l);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDay(param1, param2);
            } else if (ACTION_CAL_WEEK.equals(action)) {
                final long param1 = intent.getLongExtra(EXTRA_PARAM1, 0l);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionWeek(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDay(long param1, String param2) {
        try {

            int alertTime = PreferenceUtils.getPrefInt(App.AppContex,"cal_sync_time",60);

            long calid = PreferenceUtils.getPrefLong(App.AppContex, "calendar_id", -1l);
            PhoneFuncUtils.delOndDayCal(this, calid, param1);

            QcSchedulesResponse qcSchedulesResponse = new Gson().fromJson(param2, QcSchedulesResponse.class);
            List<QcSchedulesResponse.System> systems = qcSchedulesResponse.data.systems;
            for (int i = 0; i < systems.size(); i++) {
                QcSchedulesResponse.System system = systems.get(i);

                String gymname = system.system.name;
                List<QcScheduleBean> schedules = system.schedules;
                if (system.system == null)
                    continue;
                for (QcScheduleBean bean : schedules) {
                    String title, users;
                    if (bean.orders != null && bean.orders.size() == 1) {
                        users = bean.orders.get(0).username;
                        title = bean.course.name + "(" + bean.count + "人:" + users + ") -[健身教练助手]";

                    } else {
                        if (bean.orders != null) {
                            StringBuffer sb = new StringBuffer();
                            for (QcScheduleBean.Order order : bean.orders) {
                                sb.append(order.username);
                                sb.append(";");
                            }
                            users = sb.toString();
                        } else {
                            users = "无";
                        }
                        title = bean.course.name + "(" + bean.count + "人已预约) -[健身教练助手]";

                    }
                    if (bean.count > 0)
                        PhoneFuncUtils.insertEvent(this, calid, title, users, gymname, DateUtils.formatDateFromServer(bean.start).getTime(), DateUtils.formatDateFromServer(bean.end).getTime(),alertTime);
                }

            }
        } catch (Exception e) {
            RevenUtils.sendException("add calendar err!", "", e);
        }


    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionWeek(long param1, String param2) {
        try {
            long calid = PreferenceUtils.getPrefLong(App.AppContex, "calendar_id", -1l);
            int alertTime = PreferenceUtils.getPrefInt(App.AppContex,"cal_sync_time",60);
            long start = DateUtils.getDayMidnight(new Date(param1));
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(start));
            c.add(Calendar.DAY_OF_MONTH, 7);
            PhoneFuncUtils.delTimeCal(this, calid, start, c.getTimeInMillis());

            QcSchedulesResponse qcSchedulesResponse = new Gson().fromJson(param2, QcSchedulesResponse.class);
            List<QcSchedulesResponse.System> systems = qcSchedulesResponse.data.systems;
            for (int i = 0; i < systems.size(); i++) {
                QcSchedulesResponse.System system = systems.get(i);

                String gymname = system.system.name;
                List<QcScheduleBean> schedules = system.schedules;
                if (system.system == null)
                    continue;
                for (QcScheduleBean bean : schedules) {
                    String title, users;
                    if (bean.orders != null && bean.orders.size() == 1) {
                        users = bean.orders.get(0).username;
                        title = bean.course.name + "(" + bean.count + "人:" + users + ") -[健身教练助手]";

                    } else {
                        if (bean.orders != null) {
                            StringBuffer sb = new StringBuffer();
                            for (QcScheduleBean.Order order : bean.orders) {
                                sb.append(order.username);
                                sb.append(";");
                            }
                            users = sb.toString();
                        } else {
                            users = "无";
                        }
                        title = bean.course.name + "(" + bean.count + "人已预约) -[健身教练助手]";

                    }
                    PhoneFuncUtils.insertEvent(this, calid, title, users, gymname, DateUtils.formatDateFromServer(bean.start).getTime(), DateUtils.formatDateFromServer(bean.end).getTime(),alertTime);
                }

            }
        } catch (Exception e) {
            RevenUtils.sendException("add calendar err!", "", e);
        }
    }
}
