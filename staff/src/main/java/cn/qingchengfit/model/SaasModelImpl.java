package cn.qingchengfit.model;

import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.cardtypes.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.cardtypes.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.repository.SaasModel;
import cn.qingchengfit.saasbase.student.bean.ClassRecords;
import cn.qingchengfit.saasbase.student.network.body.AddFollowRecordBody;
import cn.qingchengfit.saasbase.student.network.body.EditStudentBody;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Observable;

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

public class SaasModelImpl implements SaasModel {
  QcRestRepository qcRestRepository;

  public SaasModelImpl(QcRestRepository qcRestRepository) {
    this.qcRestRepository = qcRestRepository;
  }

  @Override
  public Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(@Path("id") String id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcCreateCardtpl(@Body CardtplBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardtpl(@Path("card_tpl_id") String card_tpl_id,
      @Body CardtplBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcDelCardtpl(@Path("card_tpl_id") String card_tpl_id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcResumeCardtpl(@Path("card_tpl_id") String card_tpl_id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcFixGyms(@Path("cardtpl_id") String card_tpl, String shops) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcDelCardStandard(@Path("option_id") String option_id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardStandard(@Path("option_id") String option_id,
      @Body OptionBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcCreateStandard(@Path("card_tpl_id") String card_tpl_id,
      @Body OptionBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents() {
    return null;
  }

  @Override public Observable<QcDataResponse> qcGetStudentDetail() {
    return null;
  }

  @Override public Observable<QcDataResponse<ClassRecords>> qcGetStudentRecords(String studentid,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcGetRealCardList() {
    return null;
  }

  @Override public Observable<QcDataResponse> qcGetFollowRecords() {
    return null;
  }

  @Override public Observable<QcDataResponse> qcCreateStudent(@Body EditStudentBody student) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcUpdateStudent(@Path("id") String id,
      @Body EditStudentBody student) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcDelStudent(@Path("id") String id) {
    return null;
  }

  @Override public Observable<QcResponse> qcAddFollowRecord(@Path("user_id") String user_id,
      @Body AddFollowRecordBody body) {
    return null;
  }
}
