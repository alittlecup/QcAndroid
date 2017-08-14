package cn.qingchengfit.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by Paper on 2017/3/30.
 */

public class ListUtils {
    public static void test() {

    }

    public static boolean isEmpty(List x) {
        return x == null || x.size() == 0;
    }

    /**
     * List 类型转换  好累
     */
    public static <T, G> List<T> transerList(List<T> to, List<G> list) {
        to = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                G g = list.get(i);
                to.add((T) g);
            }
        }
        return to;
    }

  public static HashMap<String, Object> mapRemoveNull(HashMap<String, Object> p) {
    List<String> re = new ArrayList<>();
    for (Map.Entry<String, Object> entry : p.entrySet()) {
      if (entry.getValue() == null) re.add(entry.getKey());
    }
    for (String s : re) {
      p.remove(s);
    }
    return p;
    }

  public static <T> ArrayList<T> list2array(List<T> s) {
    ArrayList<T> ret = new ArrayList<>();
    if (s == null) return null;
    for (T t : s) {
      ret.add(t);
    }
    return ret;
  }

  /**
   *  给List中所有元素 添加 Num大小
   * @param num
   * @return
   */
  public static ArrayList<Integer> listAddNum(List<Integer> s,int num){
    ArrayList<Integer> x = new ArrayList<>();
    for (Integer integer : s) {
      x.add(integer + num );
    }
    return x;
  }

}
