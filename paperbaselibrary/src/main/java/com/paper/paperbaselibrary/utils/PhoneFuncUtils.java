package com.paper.paperbaselibrary.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.RequiresPermission;

import com.paper.paperbaselibrary.bean.Contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * Created by Paper on 15/7/28 2015.
 */
public class PhoneFuncUtils {

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };
    public static final String[] EVENT_PROJECTION2 = new String[]{
            CalendarContract.Events._ID,                           // 0
            CalendarContract.Events.TITLE,                  // 1
    };


    public static boolean checkCalendarReadPremission(Context context) {
        return PermissionUtils.checkPermission(context, Manifest.permission.READ_CALENDAR);
    }

    public static boolean checkCalendarWritePremission(Context context) {
        return PermissionUtils.checkPermission(context, Manifest.permission.WRITE_CALENDAR);
    }


    /**
     * 读取联系人
     *
     * @param context context
     * @return 联系人实体
     */
    public static List<Contact> initContactList(Context context) {
        List<Contact> contactList = new ArrayList<>();

        // 查询联系人数据
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor == null)
            return null;
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            // 获取联系人的Id
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的姓名
            String contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setUsername(contactName);
//            String sortkey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.SORT_KEY_ALTERNATIVE));
            contact.setSortKey(ChineseCharToEn.getFirstLetter(contactName));
            // 有联系人姓名得到对应的拼音
//            String pinyin = PinyinUtils.getPinyin(contactName);
//            contact.setPinyin(pinyin);

            Cursor phoneCursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            if (phoneCursor == null)
                return null;
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor
                        .getString(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contact.setPhone(phoneNumber);
            }

            if (!phoneCursor.isClosed()) {
                phoneCursor.close();
            }

            contactList.add(contact);
            LogUtil.i("name:" + contact.getUsername() + "  " + contact.getPhone());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }


        return contactList;
    }

    /**
     * 查询日历账户
     *
     * @param context contex
     */
    @RequiresPermission(Manifest.permission.READ_CALENDAR)
    public static void queryCalender(Context context) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
//        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + Calendars.OWNER_ACCOUNT + " = ?))";
//        String[] selectionArgs = new String[] {"sampleuser@gmail.com", "com.google",
//                "sampleuser@gmail.com"};
// Submit the query and get a Cursor object back.
        Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        if (cur == null) {
            LogUtil.e("null");
            return;
        }
        while (cur.moveToNext()) {
            LogUtil.i(cur.getLong(0) + ":id" + "    other:" + cur.getString(1));
        }
        if (!cur.isClosed()) {
            cur.close();
        }
    }

    /**
     * 查询日历事件
     *
     * @param context context
     */

    public static void queryEvent(Context context, long starttime, long endtime) {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 6, 29, 0, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 6, 29, 12, 30);
//        ContentValues values = new ContentValues();

        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.calendar/events");
        String selection = "(((" + CalendarContract.Events.DTSTART + " >= ?) AND ("
                + CalendarContract.Events.DTSTART + " <= ?)) OR (("
                + CalendarContract.Events.DTEND + " >= ?) AND ("
                + CalendarContract.Events.DTEND + " <= ?)))";
        String[] selectionArgs = new String[]{
                Long.toString(beginTime.getTimeInMillis()), Long.toString(endTime.getTimeInMillis()),
                Long.toString(beginTime.getTimeInMillis()), Long.toString(endTime.getTimeInMillis())
        };
        Cursor cur = cr.query(uri, EVENT_PROJECTION2, selection, selectionArgs, null);
        if (cur == null)
            return;
        while (cur.moveToNext()) {
            LogUtil.i(cur.getString(1) + ":name");
        }
        if (!cur.isClosed()) {
            cur.close();
        }

    }

    /**
     * 插入日历事件
     *
     * @param context    context
     * @param calendarid 日历id
     * @param desc       事件描述
     * @param title      事件标题
     * @param endtime    结束时间
     * @param starttime  开始时间
     */
    @RequiresPermission(Manifest.permission.WRITE_CALENDAR)
    public static void insertEvent(Context context, int calendarid, String title, String desc, long starttime, long endtime) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 7, 29, 3, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 7, 29, 8, 30);
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, desc);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarid);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        values.put(CalendarContract.Events.HAS_ALARM, 1);
//        values.put(CalendarContract.Events.ACCOUNT_NAME,"青橙科技");
//        values.put(CalendarContract.Events.SYNC_EVENTS,0);
        String[] des = TimeZone.getAvailableIDs();
//        for (int i=0;i<des.length;i++){
////            LogUtil.e(des[i]);
//        }
//        values.put(CalendarContract.Events.ACCOUNT_TYPE,CalendarContract.ACCOUNT_TYPE_LOCAL);
        Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        assert uri != null;
        LogUtil.i(Long.parseLong(uri.getLastPathSegment()) + "");
        addEventNoti(context, Integer.parseInt(uri.getLastPathSegment()));
    }

    @RequiresPermission(Manifest.permission.WRITE_CALENDAR)
    public static void addEventNoti(Context context, int eventID) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.MINUTES, 15);
        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
    }

    /**
     * 插入日历账户
     *
     * @param context context
     */
    public static void insertCalendar(Context context) {
        LogUtil.i("time:" + System.currentTimeMillis());
        ContentValues values = new ContentValues();
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,

                "QingChengFit");
        //Account name becomes equal to "nav_shift_manager" !!TEST!!
//        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
//        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(
                CalendarContract.Calendars.NAME,
                "青橙科技");
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "QC");
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                0xff3366);
        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(
                CalendarContract.Calendars.OWNER_ACCOUNT,
                "user@qingchengfit.com");
        values.put(
                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
                "Europe/London");
        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "com.qingcheng");
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri = context.getContentResolver().insert(builder.build(), values);
//        ContentValues values = new ContentValues();

        // Now get the CalendarID :
        assert uri != null;
        LogUtil.d("id:" + Long.parseLong(uri.getLastPathSegment()));


//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2015, 7, 29, 7, 30);
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(2015, 7, 29, 8, 30);
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
//                .putExtra(CalendarContract.Events.TITLE, "Yoga")
//                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
//                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
//        context.startActivity(intent);
    }


}
