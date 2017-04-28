package cn.qingchengfit.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

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
public class IntentUtils {

    public static final String RESULT = "fragment_result";
    public static final String RESULT2 = "fragment_result2";
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_E = Activity.RESULT_CANCELED;

    public static Intent instanceStringIntent(String content) {
        Intent it = new Intent();
        it.putExtra(RESULT, content);
        return it;
    }

    public static Intent instanceListIntent(ArrayList<Integer> list) {
        Intent it = new Intent();
        it.putIntegerArrayListExtra("list", list);
        return it;
    }

    public static Intent instanceStringIntent(String content, String content2) {
        Intent it = new Intent();
        it.putExtra(RESULT, content);
        it.putExtra(RESULT2, content2);
        return it;
    }

    public static Intent instanceStringsIntent(String... strings) {
        Intent it = new Intent();
        for (int i = 0; i < strings.length; i++) {
            it.putExtra(RESULT + i, strings[i]);
        }
        return it;
    }

    public static Intent instancePacecle(Parcelable parcelable){
        Intent it = new Intent();
        it.putExtra(RESULT,parcelable);
        return it;
    }
    public static Parcelable getParcelable(Intent it){
        return it.getParcelableExtra(RESULT);
    }

    public static Intent instanceListParcelable(ArrayList<Parcelable> arrayList){
        Intent it = new Intent();
        it.putParcelableArrayListExtra(RESULT,arrayList);
        return it;
    }
    public static ArrayList<Parcelable> getListParcelable(Intent it){
        return it.getParcelableArrayListExtra(RESULT);
    }


    public static List<Integer> getIntegerList(Intent it) {
        return it.getIntegerArrayListExtra("list");
    }

    public static String getIntentString(Intent it) {
        return it.getStringExtra(RESULT);
    }

    public static String getIntentString(Intent it, int pos) {
        return it.getStringExtra(RESULT + pos);
    }

    public static String getIntentString2(Intent it) {
        return it.getStringExtra(RESULT2);
    }

    //public static Intent getChooseIntent(Context context,CoachService coachService,Brand brand,int to){
    //    Intent toChoose = new Intent(context, ChooseActivity.class);
    //    //toChoose.putExtra(Configs.EXTRA_BRAND,brand);
    //    //toChoose.putExtra(Configs.EXTRA_GYM_SERVICE,coachService);
    //    //toChoose.putExtra(Configs.EXTRA_GYM_STATUS,new GymStatus(false));
    //    toChoose.putExtra("to",to);
    //    return toChoose;
    //
    //}

    public static String getIntentFromUri(Intent i,String key){
        String ret = i.getStringExtra(key);
        if (i.getData() != null && i.getData().getHost() != null){
            ret = i.getData().getQueryParameter(key);
        }
        if (ret != null)
            return Uri.decode(ret);
        else return "";
    }
    
}
