package cn.qingchengfit.staffkit.views.statement.custom;

import cn.qingchengfit.model.responese.QcResponseStatementDetail;
import cn.qingchengfit.staff.RxUnitTestTools;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import cn.qingchengfit.staffkit.views.statement.detail.StatementDetailPresenter;
import cn.qingchengfit.staffkit.views.statement.detail.StatementDetailView;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

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
 * Created by Paper on 16/7/7.
 */
public class CustomStatmentPresenterTest {

    StatementDetailPresenter presenter;
    StatementUsecase usecase;
    StatementDetailView view;

    @Before public void setUp() throws Exception {
        RxUnitTestTools.openRxTools();
        usecase = mock(StatementUsecase.class);
        view = mock(StatementDetailView.class);
        //presenter = new StatementDetailPresenter(usecase,new Brand("1"),null);

    }

    @Test public void testQueryData() {
        QcResponseStatementDetail qcResponseStatementDetail = new Gson().fromJson(
            "{\"status\": 200, \"msg\": \"ok\", \"info\": \"\", \"data\": {\"start\": \"2016-01-06\", \"stat\": {\"course_count\": 8, \"total_times\": 1.0, \"order_count\": 17, \"user_count\": 17, \"total_real_price\": 1384.5, \"total_account\": 2380.0}, \"user_id\": \"\", \"end\": \"2016-02-06\", \"schedules\": [{\"shop\": {\"address\": \"\\u5b89\\u5f92\\u751f\\u82b1\\u56ed2\", \"id\": 1, \"name\": \"Let`s Move Test\"}, \"order_count\": 1, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/0eb9b4b55f24a4708031cc0b1bf4c139.jpg\", \"id\": 50, \"name\": \"\\u4e09\\u516b\\u514d\\u8d39\\u56e2\\u8bfe\"}, \"orders\": [{\"status\": \"\\u5df2\\u5b8c\\u6210\", \"count\": 1, \"total_price\": 0.0, \"user\": {\"username\": \"\\u5411\\u9e3f\\u5112\", \"head\": \"x\", \"joined_at\": \"2014-10-22\", \"phone\": \"15313121715\", \"address\": \"\", \"remarks\": \"\", \"name_phone\": \"\\u5411\\u9e3f\\u5112 153****1715\", \"id\": 2, \"date_of_birth\": \"\", \"avatar\": \"http://zoneke-img.b0.upaiyun.com/6e6f4e4d8722fd84cca518461005dc7d.jpg!120x120\", \"email\": \"\"}, \"created_at\": \"2016-01-15T18:39:40\", \"remarks\": \"\", \"price\": 0.0, \"id\": 12574, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2014-08-01T11:00:45\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2014-08-01T11:00:45\", \"balance\": 1920.0, \"id\": 264, \"card_tpl_id\": 1, \"card_no\": \"\"}}], \"count\": 1, \"total_real_price\": 0.0, \"end\": \"2016-01-18T17:00:00\", \"total_times\": 0, \"schedule_remarks\": \"\\u5171\\uff1a1\\u4eba\", \"id\": 7203, \"start\": \"2016-01-18T16:00:00\", \"teacher\": {\"username\": \"Let's Move\", \"priority\": 0, \"phone\": \"18810267008\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/f0a2d3a5018da41c0443cd2de78e8b2d.jpg!120x120\", \"id\": 6}, \"total_account\": 0.0}, {\"shop\": {\"address\": \"\\u5b89\\u5f92\\u751f\\u82b1\\u56ed2\", \"id\": 1, \"name\": \"Let`s Move Test\"}, \"order_count\": 1, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/adc52f22704fe09e035a482a0ee5cb1f.jpg\", \"id\": 18, \"name\": \"\\u666e\\u534e\\u5e73\\u8861\"}, \"orders\": [{\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 1, \"total_price\": 200.0, \"user\": {\"username\": \"\\u9a70\\u8fdc\", \"head\": \"c\", \"joined_at\": \"2014-10-22\", \"phone\": \"18614043303\", \"address\": \"\\u9633\\u5149\\u4e0a\\u4e1c\", \"remarks\": \"\", \"name_phone\": \"\\u9a70\\u8fdc 186****3303\", \"id\": 1, \"date_of_birth\": \"1990-03-03\", \"avatar\": \"http://zoneke-img.b0.upaiyun.com/header/caa97151-a5fb-4885-9097-4ffd5a107eb4.png!120x120\", \"email\": \"chenchiyuan03@gmail.com\"}, \"created_at\": \"2016-01-21T13:10:34\", \"remarks\": \"\", \"price\": 200.0, \"id\": 12578, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2014-08-01T13:38:34\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2014-08-01T13:38:34\", \"balance\": 4582.0, \"id\": 267, \"card_tpl_id\": 1, \"card_no\": \"\"}}], \"count\": 1, \"total_real_price\": 137.3, \"end\": \"2016-01-25T06:45:00\", \"total_times\": 0, \"schedule_remarks\": \"\\u5171\\uff1a1\\u4eba\\uff0c\\u6536\\u5165\\uff1a200.0\\u5143\", \"id\": 7263, \"start\": \"2016-01-25T06:00:00\", \"teacher\": {\"username\": \"\\u738b\\u607a113\", \"priority\": 9, \"phone\": \"13581753477\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/65cd6696b089f0dee7ae8ee25bfeea74.jpg!120x120\", \"id\": 2}, \"total_account\": 200.0}, {\"shop\": {\"address\": \"\\u5b89\\u5f92\\u751f\\u82b1\\u56ed2\", \"id\": 1, \"name\": \"Let`s Move Test\"}, \"order_count\": 1, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/efc4dddc7163940e41852a581d92a9eb.jpg\", \"id\": 55, \"name\": \"total body\"}, \"orders\": [{\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 1, \"total_price\": 200.0, \"user\": {\"username\": \"\\u9a70\\u8fdc\", \"head\": \"c\", \"joined_at\": \"2014-10-22\", \"phone\": \"18614043303\", \"address\": \"\\u9633\\u5149\\u4e0a\\u4e1c\", \"remarks\": \"\", \"name_phone\": \"\\u9a70\\u8fdc 186****3303\", \"id\": 1, \"date_of_birth\": \"1990-03-03\", \"avatar\": \"http://zoneke-img.b0.upaiyun.com/header/caa97151-a5fb-4885-9097-4ffd5a107eb4.png!120x120\", \"email\": \"chenchiyuan03@gmail.com\"}, \"created_at\": \"2016-01-24T16:14:41\", \"remarks\": \"\", \"price\": 200.0, \"id\": 12589, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2014-08-01T13:38:34\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2014-08-01T13:38:34\", \"balance\": 4582.0, \"id\": 267, \"card_tpl_id\": 1, \"card_no\": \"\"}}], \"count\": 1, \"total_real_price\": 137.3, \"end\": \"2016-01-25T14:00:00\", \"total_times\": 0, \"schedule_remarks\": \"\\u5171\\uff1a1\\u4eba\\uff0c\\u6536\\u5165\\uff1a200.0\\u5143\", \"id\": 7302, \"start\": \"2016-01-25T13:00:00\", \"teacher\": {\"username\": \"myt\", \"priority\": 5, \"phone\": \"18668050921\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/b0a369a8c6336905fd63c851ac888ecb.jpg!120x120\", \"id\": 16}, \"total_account\": 200.0}, {\"shop\": {\"address\": \"\\u5b89\\u5f92\\u751f\\u82b1\\u56ed2\", \"id\": 1, \"name\": \"Let`s Move Test\"}, \"order_count\": 5, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/d84bf08a2455f3dae90ccc342feafa15.jpg\", \"id\": 63, \"name\": \"\\u559d\\u4e86\\u5496\\u5561\\u4f1a\\u4e0d\\u4f1a\\u80d6\"}, \"orders\": [{\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 4, \"total_price\": 800.0, \"user\": {\"username\": \"\\u5411\\u9e3f\\u5112\", \"head\": \"x\", \"joined_at\": \"2014-10-22\", \"phone\": \"15313121715\", \"address\": \"\", \"remarks\": \"\", \"name_phone\": \"\\u5411\\u9e3f\\u5112 153****1715\", \"id\": 2, \"date_of_birth\": \"\", \"avatar\": \"http://zoneke-img.b0.upaiyun.com/6e6f4e4d8722fd84cca518461005dc7d.jpg!120x120\", \"email\": \"\"}, \"created_at\": \"2016-01-21T17:44:53\", \"remarks\": \"\\u56e2\\u4f53\\u51fa\\u52a8\", \"price\": 200.0, \"id\": 12580, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2014-08-01T11:00:45\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2014-08-01T11:00:45\", \"balance\": 1920.0, \"id\": 264, \"card_tpl_id\": 1, \"card_no\": \"\"}}, {\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 1, \"total_price\": 200.0, \"user\": {\"username\": \"\\u5411\\u9e3f\\u5112\", \"head\": \"x\", \"joined_at\": \"2014-10-22\", \"phone\": \"15313121715\", \"address\": \"\", \"remarks\": \"\", \"name_phone\": \"\\u5411\\u9e3f\\u5112 153****1715\", \"id\": 2, \"date_of_birth\": \"\", \"avatar\": \"http://zoneke-img.b0.upaiyun.com/6e6f4e4d8722fd84cca518461005dc7d.jpg!120x120\", \"email\": \"\"}, \"created_at\": \"2016-01-21T18:20:49\", \"remarks\": \"\", \"price\": 200.0, \"id\": 12582, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2014-08-01T11:00:45\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2014-08-01T11:00:45\", \"balance\": 1920.0, \"id\": 264, \"card_tpl_id\": 1, \"card_no\": \"\"}}], \"count\": 5, \"total_real_price\": 819.7, \"end\": \"2016-01-27T15:40:00\", \"total_times\": 0, \"schedule_remarks\": \"\\u5171\\uff1a5\\u4eba\\uff0c\\u6536\\u5165\\uff1a1000.0\\u5143\", \"id\": 7280, \"start\": \"2016-01-27T15:00:00\", \"teacher\": {\"username\": \"\\u81ea\\u594b\", \"priority\": 7, \"phone\": \"18674054100\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/7f362320fb3c82270f6c9c623e39ba92.png!120x120\", \"id\": 18}, \"total_account\": 1000.0}, {\"shop\": {\"address\": \"\\u5b89\\u5f92\\u751f\\u82b1\\u56ed2\", \"id\": 1, \"name\": \"Let`s Move Test\"}, \"order_count\": 2, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/b0a369a8c6336905fd63c851ac888ecb.jpg\", \"id\": 61, \"name\": \"4202TEST\\u56e2\\u8bfe\"}, \"orders\": [{\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 1, \"total_price\": 100.0, \"user\": {\"username\": \"myt\", \"head\": \"m\", \"joined_at\": \"2015-07-13\", \"phone\": \"18668050921\", \"address\": \"\\u9633\\u5149\\u4e0a\\u4e1c31\\u53f7\\u697c209\", \"remarks\": \"\", \"name_phone\": \"myt 186****0921\", \"id\": 2169, \"date_of_birth\": \"2015-07-17\", \"avatar\": \"http://zoneke-img.b0.upaiyun.com/b0a369a8c6336905fd63c851ac888ecb.jpg!120x120\", \"email\": \"\"}, \"created_at\": \"2016-01-19T19:37:49\", \"remarks\": \"\", \"price\": 100.0, \"id\": 12576, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2015-03-19T11:52:53\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2015-03-19T11:52:53\", \"balance\": 6207.0, \"id\": 1945, \"card_tpl_id\": 1, \"card_no\": \"\"}}, {\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 1, \"total_price\": 100.0, \"user\": {\"username\": \"myt\", \"head\": \"m\", \"joined_at\": \"2015-07-13\", \"phone\": \"18668050921\", \"address\": \"\\u9633\\u5149\\u4e0a\\u4e1c31\\u53f7\\u697c209\", \"remarks\": \"\", \"name_phone\": \"myt 186****0921\", \"id\": 2169, \"date_of_birth\": \"2015-07-17\", \"avatar\": \"http://zoneke-img.b0.upaiyun.com/b0a369a8c6336905fd63c851ac888ecb.jpg!120x120\", \"email\": \"\"}, \"created_at\": \"2016-01-20T08:56:09\", \"remarks\": \"\", \"price\": 100.0, \"id\": 12577, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2015-03-19T11:52:53\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2015-03-19T11:52:53\", \"balance\": 6207.0, \"id\": 1945, \"card_tpl_id\": 1, \"card_no\": \"\"}}], \"count\": 2, \"total_real_price\": 166.7, \"end\": \"2016-01-29T08:45:00\", \"total_times\": 0, \"schedule_remarks\": \"\\u5171\\uff1a2\\u4eba\\uff0c\\u6536\\u5165\\uff1a200.0\\u5143\", \"id\": 7248, \"start\": \"2016-01-29T08:00:00\", \"teacher\": {\"username\": \"myt\", \"priority\": 5, \"phone\": \"18668050921\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/b0a369a8c6336905fd63c851ac888ecb.jpg!120x120\", \"id\": 16}, \"total_account\": 200.0}, {\"shop\": {\"address\": \"\\u5b89\\u5f92\\u751f\\u82b1\\u56ed2\", \"id\": 1, \"name\": \"Let`s Move Test\"}, \"order_count\": 3, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/cc60a3d7c53d3f0d7ff67c1ff6216805.png\", \"id\": 62, \"name\": \"\\u79c1\\u6559TEST_0921\"}, \"orders\": [{\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 1, \"total_price\": 250.0, \"user\": {\"username\": \"\\u6bdb\\u4e91\\u6d9b\", \"head\": \"m\", \"joined_at\": \"2015-12-11\", \"phone\": \"18618304202\", \"address\": \"\\u5317\\u4eac\\u5e02\\u671d\\u9633\\u533a\", \"remarks\": \"\", \"name_phone\": \"\\u6bdb\\u4e91\\u6d9b 186****4202\", \"id\": 3471, \"date_of_birth\": \"1989-12-27\", \"avatar\": \"\", \"email\": \"\"}, \"created_at\": \"2016-01-24T13:31:10\", \"remarks\": \"\", \"price\": 250.0, \"id\": 12587, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2015-11-20T17:13:44\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2015-11-20T17:13:44\", \"balance\": 130022.0, \"id\": 2162, \"card_tpl_id\": 1, \"card_no\": \"a0002\"}}, {\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 2, \"total_price\": 500.0, \"user\": {\"username\": \"\\u6bdb\\u4e91\\u6d9b\", \"head\": \"m\", \"joined_at\": \"2015-12-11\", \"phone\": \"18618304202\", \"address\": \"\\u5317\\u4eac\\u5e02\\u671d\\u9633\\u533a\", \"remarks\": \"\", \"name_phone\": \"\\u6bdb\\u4e91\\u6d9b 186****4202\", \"id\": 3471, \"date_of_birth\": \"1989-12-27\", \"avatar\": \"\", \"email\": \"\"}, \"created_at\": \"2016-01-24T13:31:43\", \"remarks\": \"\", \"price\": 250.0, \"id\": 12588, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2015-11-20T17:13:44\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2015-11-20T17:13:44\", \"balance\": 130022.0, \"id\": 2162, \"card_tpl_id\": 1, \"card_no\": \"a0002\"}}], \"count\": 3, \"total_real_price\": 123.5, \"end\": \"2016-01-31T11:30:00\", \"total_times\": 0, \"schedule_remarks\": \"\\u5171\\uff1a3\\u4eba\\uff0c\\u6536\\u5165\\uff1a750.0\\u5143\", \"id\": 7367, \"start\": \"2016-01-31T10:30:00\", \"teacher\": {\"username\": \"myt\", \"priority\": 5, \"phone\": \"18668050921\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/b0a369a8c6336905fd63c851ac888ecb.jpg!120x120\", \"id\": 16}, \"total_account\": 750.0}, {\"shop\": {\"address\": \"\\u5b89\\u5f92\\u751f\\u82b1\\u56ed2\", \"id\": 1, \"name\": \"Let`s Move Test\"}, \"order_count\": 3, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/b0a369a8c6336905fd63c851ac888ecb.jpg\", \"id\": 61, \"name\": \"4202TEST\\u56e2\\u8bfe\"}, \"orders\": [{\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 3, \"total_price\": 30.0, \"user\": {\"username\": \"\\u5f20\\u4e8c\", \"head\": \"z\", \"joined_at\": \"2014-10-22\", \"phone\": \"18601151370\", \"address\": \"\", \"remarks\": \"\", \"name_phone\": \"\\u5f20\\u4e8c 186****1370\", \"id\": 675, \"date_of_birth\": \"1987-03-05\", \"avatar\": \"\", \"email\": \"\"}, \"created_at\": \"2016-01-19T14:37:17\", \"remarks\": \"\", \"price\": 10.0, \"id\": 12575, \"card\": {\"card_type\": 1, \"card_name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"unit\": \"\\u5143\", \"end\": \"2014-09-19T15:07:59\", \"name\": \"\\u5f15\\u529b\\u50a8\\u503c\\u5361\", \"start\": \"2014-09-19T15:07:59\", \"balance\": 50.0, \"id\": 658, \"card_tpl_id\": 1, \"card_no\": \"\"}}], \"count\": 3, \"total_real_price\": 0.0, \"end\": \"2016-02-02T15:00:00\", \"total_times\": 0, \"schedule_remarks\": \"\\u5171\\uff1a3\\u4eba\\uff0c\\u6536\\u5165\\uff1a30.0\\u5143\", \"id\": 7223, \"start\": \"2016-02-02T14:00:00\", \"teacher\": {\"username\": \"\\u6bdbtest\", \"priority\": 0, \"phone\": \"15400000002\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/75656eb980b79e7748041f830332cc62.png!120x120\", \"id\": 21}, \"total_account\": 30.0}, {\"shop\": {\"address\": \"\\u5317\\u4eac\\u5e02\\u671d\\u9633\\u533a798\\u827a\\u672f\\u533a706\\u5317\\u4e09\\u88579\\u53f7\\u9752\\u6a59\\u79d1\\u6280\\u6d4b\\u8bd5\\u573a\\u9986\", \"id\": 2, \"name\": \"798\\u5e97\"}, \"order_count\": 1, \"course\": {\"photo\": \"http://zoneke-img.b0.upaiyun.com/cc60a3d7c53d3f0d7ff67c1ff6216805.png\", \"id\": 67, \"name\": \"\\u6d4b\\u8bd5\\u8bfe\\u7a0b\"}, \"orders\": [{\"status\": \"\\u5df2\\u9884\\u7ea6\", \"count\": 1, \"total_price\": 1.0, \"user\": {\"username\": \"\\u6bdb\\u4e91\\u6d9b\", \"head\": \"m\", \"joined_at\": \"2016-01-28\", \"phone\": \"15400010002\", \"address\": \"\", \"remarks\": \"\", \"name_phone\": \"\\u6bdb\\u4e91\\u6d9b 154****0002\", \"id\": 3477, \"date_of_birth\": \"2016-05-27\", \"avatar\": \"\", \"email\": \"\"}, \"created_at\": \"2016-01-28T16:10:29\", \"remarks\": \"\", \"price\": 1.0, \"id\": 12590, \"card\": {\"card_type\": 2, \"card_name\": \"\\u6d4b\\u8bd5\\u5361\", \"unit\": \"\\u6b21\", \"end\": \"2016-01-28T16:07:45\", \"name\": \"\\u6d4b\\u8bd5\\u5361\", \"start\": \"2016-01-28T16:07:45\", \"balance\": 500.0, \"id\": 2164, \"card_tpl_id\": 15, \"card_no\": \"\"}}], \"count\": 1, \"total_real_price\": 0.0, \"end\": \"2016-02-03T10:00:00\", \"total_times\": 1.0, \"schedule_remarks\": \"\\u5171\\uff1a1\\u4eba\\uff0c\\u6536\\u5165\\uff1a1.0\\u6b21\", \"id\": 7368, \"start\": \"2016-02-03T09:00:00\", \"teacher\": {\"username\": \"\\u738b\\u607a113\", \"priority\": 9, \"phone\": \"13581753477\", \"shop_id\": 1, \"avatar\": \"http://zoneke-img.b0.upaiyun.com/65cd6696b089f0dee7ae8ee25bfeea74.jpg!120x120\", \"id\": 2}, \"total_account\": 0}]}, \"level\": \"success\"}",
            QcResponseStatementDetail.class);
        //        when(usecase.queryStatementDetail(anyString(),anyString(),anyString(),anyString()))
        //                .thenReturn(Observable.just(qcResponseStatementDetail));

    }
}