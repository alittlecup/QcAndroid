package cn.qingchengfit.testmodule;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

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

public class TestClass {
  public void group() {
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
    Observable.just(x.toArray(new String[x.size()])).groupBy(new Func1<String[], String>() {
      @Override public String call(String[] strings) {
        for (String string : strings) {
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
        return null;
      }
    }).subscribe(new Action1<GroupedObservable<String, String[]>>() {
      @Override
      public void call(final GroupedObservable<String, String[]> stringGroupedObservable) {
        if (stringGroupedObservable.getKey() != null) {
          //System.out.println(stringGroupedObservable.getKey() +":  ");
          stringGroupedObservable.subscribe(new Action1<String[]>() {
            @Override public void call(String[] strings) {
              System.out.println(stringGroupedObservable.getKey() + ":  " + strings.toString());
            }
          });
        }
      }
    })

    ;
  }
}
