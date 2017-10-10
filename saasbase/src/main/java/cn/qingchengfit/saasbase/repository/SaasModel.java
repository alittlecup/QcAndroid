package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.student.bean.ClassRecords;
import cn.qingchengfit.saasbase.student.network.body.AddFollowRecordBody;
import cn.qingchengfit.saasbase.student.network.body.EditStudentBody;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.Path;

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
 * Created by Paper on 2017/8/16.
 */

public interface SaasModel {

  /**
   * 卡模板详情
   */
  rx.Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(@Path("id") String id);

  /**
   * 卡模板列表
   *
   * @param type 类型  = null时所有类型
   * @param isEnable 是否禁止使用
   */
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable);

  /**
   * 卡类型
   */
  //新建卡模板
  rx.Observable<QcDataResponse> qcCreateCardtpl(@Body CardtplBody body);

  /**
   * @param card_tpl_id 卡模板id
   * @param body 卡模板body
   */
  rx.Observable<QcDataResponse> qcUpdateCardtpl(@Path("card_tpl_id") String card_tpl_id,
      @Body CardtplBody body);

  /**
   * 停用会员卡种类
   */
  rx.Observable<QcDataResponse> qcDelCardtpl(@Path("card_tpl_id") String card_tpl_id);

  /**
   * 恢复会员卡种类
   */
  rx.Observable<QcDataResponse> qcResumeCardtpl(@Path("card_tpl_id") String card_tpl_id);

  /**
   * 修改适用场馆 { shops:12,12,123 }
   */
  rx.Observable<QcDataResponse> qcFixGyms(@Path("cardtpl_id") String card_tpl, String shops);

  /**
   * 卡规格操作
   */
  rx.Observable<QcDataResponse> qcDelCardStandard(@Path("option_id") String option_id);

  /**
   * 修改卡模板规格
   */
  rx.Observable<QcDataResponse> qcUpdateCardStandard(@Path("option_id") String option_id,
      @Body OptionBody body);

  /**
   * 新建卡模板规格
   */
  rx.Observable<QcDataResponse> qcCreateStandard(@Path("card_tpl_id") String card_tpl_id,
      @Body OptionBody body);



  /**
   * 获取所有会员
   */
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents();

  /**
   *  获取当前学员的详细信息
   */
  rx.Observable<QcDataResponse> qcGetStudentDetail();

  /**
   * 获取学员上课记录
   */
  rx.Observable<QcDataResponse<ClassRecords>> qcGetStudentRecords(String studentid,HashMap<String,Object> params);

  /**
   * 获取当前会员所有卡种类
   */
  rx.Observable<QcDataResponse> qcGetRealCardList();

  /**
   * 获取跟进记录
   */
  rx.Observable<QcDataResponse> qcGetFollowRecords();




  /**
   * 学员操作
   */
  rx.Observable<QcDataResponse> qcCreateStudent(@Body EditStudentBody student);

  /**
   * 修改会员信息
   */
  rx.Observable<QcDataResponse> qcUpdateStudent(
      @Path("id") String id, @Body EditStudentBody student);

  /**
   *  删除会员
   */
  rx.Observable<QcDataResponse> qcDelStudent(@Path("id") String id);

  /**
   *  新增跟进记录
   */
  rx.Observable<QcResponse> qcAddFollowRecord(
      @Path("user_id") String user_id, @Body AddFollowRecordBody body);


}
