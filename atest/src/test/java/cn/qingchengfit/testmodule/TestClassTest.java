package cn.qingchengfit.testmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/8/2.
 */
public class TestClassTest {
  @Test public void group() throws Exception {
    RxUnitTestTools.openRxTools();
    List<String> x = new ArrayList<>();
    x.add("abc");
    x.add("abc1");
    x.add("abc2");
    x.add("abc3");
    x.add("abc4");
    x.add("bbc1");
    x.add("bbc2");
    x.add("bbc3");
    x.add("bbc4");
    Observable.from(x.toArray(new String[x.size()]))
        .observeOn(AndroidSchedulers.mainThread())
        .groupBy(new Func1<String, String>() {
          @Override public String call(String string) {
            if (string.contains("1")) {
              return "1";
            } else if (string.contains("2")) {
              return "2";
            } else if (string.contains("3")) {
              return "3";
            } else {
              return "0";
            }
          }
        })
        .collect(new Func0<HashMap<String, List<String>>>() {
          @Override public HashMap<String, List<String>> call() {
            return new HashMap<String, List<String>>();
          }
        }, new Action2<HashMap<String, List<String>>, GroupedObservable<String, String>>() {
          @Override public void call(HashMap<String, List<String>> stringListHashMap,
              GroupedObservable<String, String> stringStringGroupedObservable) {
            List<String> strings = stringListHashMap.get(stringStringGroupedObservable.getKey());
            if (strings == null) {
              strings = new ArrayList<String>();
              stringListHashMap.put(stringStringGroupedObservable.getKey(), strings);
            }
            final List<String> x = strings;
            stringStringGroupedObservable.observeOn(Schedulers.immediate())
                .subscribe(new Action1<String>() {
                  @Override public void call(String s) {
                    x.add(s);
                  }
                });
          }
        })
        .subscribe(new Action1<HashMap<String, List<String>>>() {
          @Override public void call(HashMap<String, List<String>> stringListHashMap) {
            System.out.println(stringListHashMap.keySet().size() + "   size");
            for (String s : stringListHashMap.keySet()) {
              System.out.println(stringListHashMap.get(s).size() + " ::::" + s);
            }
          }
        });
    Assert.assertTrue(true);
  }
}