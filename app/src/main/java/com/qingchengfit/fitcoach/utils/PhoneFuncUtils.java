package com.qingchengfit.fitcoach.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;

import com.qingchengfit.fitcoach.Bean.Contact;

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
 * <p>
 * <p>
 * Created by Paper on 15/7/28 2015.
 */
public class PhoneFuncUtils {

    /**
     * 读取联系人
     * @param context
     * @return
     */
    public static List<Contact> initContactList(Context context) {
        List<Contact> contactList = new ArrayList<Contact>();

        // 查询联系人数据
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            // 获取联系人的Id
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的姓名
            String contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setContactName(contactName);

            // 有联系人姓名得到对应的拼音
//            String pinyin = PinyinUtils.getPinyin(contactName);
//            contact.setPinyin(pinyin);

            Cursor phoneCursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);

            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor
                        .getString(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contact.setPhoneNumber(phoneNumber);
            }

            if (null != phoneCursor && !phoneCursor.isClosed()) {
                phoneCursor.close();
            }

            contactList.add(contact);
            LogUtil.i("name:"+contact.getContactName()+"  "+contact.getPhoneNumber());
        }

        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }


        return contactList;
    }

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };
    public static void queryCalender(Context context){
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
//        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + Calendars.OWNER_ACCOUNT + " = ?))";
//        String[] selectionArgs = new String[] {"sampleuser@gmail.com", "com.google",
//                "sampleuser@gmail.com"};
// Submit the query and get a Cursor object back.
        Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        while (cur.moveToNext()){
            LogUtil.i(cur.getLong(0)+":id");
        }
        if (cur!= null && !cur.isClosed()){
            cur.close();
        }
    }


    public static void insertCalendar(Context context){
        LogUtil.i("time:"+System.currentTimeMillis());
//        ContentResolver cr = context.getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put(CalendarContract.Events.DTSTART, 1438061183509l+3600*24*1000);
//        values.put(CalendarContract.Events.DTEND, 1438061183509l+3600*24*1000*2);
//        values.put(CalendarContract.Events.TITLE, "Test");
//        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
//        values.put(CalendarContract.Events.CALENDAR_ID, 1);
//        values.put(CalendarContract.Events.EVENT_TIMEZONE, "China/Beijing");
//        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 6, 29, 3, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 6, 29, 8, 30);
        ContentValues values = new ContentValues();
//        values.put(
//                CalendarContract.Calendars.ACCOUNT_NAME,
//
//                "QingChengFit"); //Account name becomes equal to "nav_shift_manager" !!TEST!!
////                values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
////        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
//        values.put(
//                CalendarContract.Calendars.ACCOUNT_TYPE,
//                CalendarContract.ACCOUNT_TYPE_LOCAL);
//        values.put(
//                CalendarContract.Calendars.NAME,
//                "青橙科技");
//        values.put(
//                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
//                "QC");
//        values.put(
//                CalendarContract.Calendars.CALENDAR_COLOR,
//                0xff3366);
//        values.put(
//                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
//                CalendarContract.Calendars.CAL_ACCESS_OWNER);
//        values.put(
//                CalendarContract.Calendars.OWNER_ACCOUNT,
//                "user@qingchengfit.com");
//        values.put(
//                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
//                "Europe/London");
//        Uri.Builder builder =
//                CalendarContract.Calendars.CONTENT_URI.buildUpon();
//        builder.appendQueryParameter(
//                CalendarContract.Calendars.ACCOUNT_NAME,
//                "com.qingcheng");
//        builder.appendQueryParameter(
//                CalendarContract.Calendars.ACCOUNT_TYPE,
//                CalendarContract.ACCOUNT_TYPE_LOCAL);
//        builder.appendQueryParameter(
//                CalendarContract.CALLER_IS_SYNCADAPTER,
//                "true");
//        Uri uri = context.getContentResolver().insert(builder.build(), values);
////        ContentValues values = new ContentValues();
//
//        // Now get the CalendarID :
//         LogUtil.e("id:"+Long.parseLong(uri.getLastPathSegment()));

        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, "哈哈哈哈");
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values.put(CalendarContract.Events.CALENDAR_ID, 5);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/London");
//        values.put(CalendarContract.Events.ACCOUNT_NAME,"青橙科技");
//        values.put(CalendarContract.Events.SYNC_EVENTS,0);
        String[] des = TimeZone.getAvailableIDs();
        for (int i=0;i<des.length;i++){
//            LogUtil.e(des[i]);
        }
//        values.put(CalendarContract.Events.ACCOUNT_TYPE,CalendarContract.ACCOUNT_TYPE_LOCAL);
        Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        LogUtil.i(Long.parseLong(uri.getLastPathSegment())+"");


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
