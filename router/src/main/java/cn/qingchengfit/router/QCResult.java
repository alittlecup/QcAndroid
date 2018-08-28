package cn.qingchengfit.router;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 组件调用的结果
 * 根据success来判断是否成功
 * 根据code来判断失败类型
 * 根据data来获取返回的信息内容
 * @author billy.qi
 * @since 17/7/3 11:46
 */
public class QCResult {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_CODE = "code";
    private static final String KEY_ERROR_MESSAGE = "errorMessage";
    private static final String KEY_DATA = "data";
    /**
     * CC调用成功
     */
    public static final int CODE_SUCCESS = 0;
    /**
     * 组件调用成功，但业务逻辑判定为失败
     */
    public static final int CODE_ERROR_BUSINESS = 1;
    /**
     * 保留状态码：默认的请求错误code
     */
    public static final int CODE_ERROR_DEFAULT = -1;
    /**
     * 没有指定组件名称
     */
    public static final int CODE_ERROR_COMPONENT_NAME_EMPTY = -2;
    /**
     * result不该为null
     * 例如：组件回调时使用 QC.sendQCResult(callId, null) 或 interceptor返回null
     */
    public static final int CODE_ERROR_NULL_RESULT = -3;
    /**
     * 调用过程中出现exception
     */
    public static final int CODE_ERROR_EXCEPTION_RESULT = -4;
    /**
     * 没有找到组件能处理此次调用请求
     */
    public static final int CODE_ERROR_NO_COMPONENT_FOUND = -5;
    /**
     * context 为null，通过反射获取application失败
     */
    public static final int CODE_ERROR_CONTEXT_NULL = -6;
    /**
     * 跨app调用组件时，LocalSocket连接出错
     */
    public static final int CODE_ERROR_CONNECT_FAILED = -7;
    /**
     * 取消
     */
    public static final int CODE_ERROR_CANCELED = -8;
    /**
     * 超时
     */
    public static final int CODE_ERROR_TIMEOUT = -9;
    /**
     * 未调用CC.sendQCResult(callId, ccResult)方法
     */
    public static final int CODE_ERROR_CALLBACK_NOT_INVOKED = -10;
    /**
     * 跨app组件调用时对象传输出错，可能是自定义类型没有共用
     */
    public static final int CODE_ERROR_REMOTE_CC_DELIVERY_FAILED = -11;

    /**
     * CC调用是否成功
     */
    private boolean success;
    /**
     * error message
     */
    private String errorMessage;
    /**
     * 结果的code
     * 0:请求成功
     * <0:CC调用失败
     * >0:CC调用成功, 但业务逻辑失败（例如：登录页面打开成功但未执行登录或登录失败）
     */
    private int code;
    /**
     * 储存返回结果信息
     */
    private Map<String, Object> data;

    //下面提供一些便于构造的CCResult对象

    /**
     * 构建一个CC调用成功，但业务失败的CCResult
     * success=true, code=1 ({@link #CODE_ERROR_BUSINESS})
     * 可以通过CCResult.addData(key, value)来继续添加更多的返回信息
     * @param message 错误信息
     * @return 构造的CCResult对象
     */
    public static QCResult error(String message) {
        QCResult result = new QCResult();
        result.code = CODE_ERROR_BUSINESS;
        result.success = false;
        result.errorMessage = message;
        return result;
    }
    /**
     * 构建一个CC调用成功，但业务失败的CCResult，没有errorMessage
     * success=true, code=1 ({@link #CODE_ERROR_BUSINESS})
     * 可以通过CCResult.addData(key, value)来继续添加更多的返回信息
     * @param key 存放在data中的key
     * @param value 存放在data中的value
     * @return 构造的CCResult对象
     */
    public static QCResult error(String key, Object value) {
        QCResult result = new QCResult();
        result.code = CODE_ERROR_BUSINESS;
        result.success = false;
        result.data = new HashMap<>(4);
        result.data.put(key, value);
        return result;
    }

    static QCResult error(int code) {
        QCResult result = new QCResult();
        result.code = code;
        result.success = false;
        return result;
    }

    /**
     * 快捷构建一个CC调用成功的CCResult
     * success=true, code=0 ({@link #CODE_SUCCESS})
     * 可以通过CCResult.addData(key, value)来继续添加更多的返回信息
     * @param key 存放在data中的key
     * @param value 存放在data中的value
     * @return 构造的CCResult对象
     */
    public static QCResult success(String key, Object value) {
        Map<String, Object> data = new HashMap<>(2);
        data.put(key, value);
        return success(data);
    }
    /**
     * 快捷构建一个CC调用成功的CCResult，只包含成功的状态，没有其它信息
     * success=true, code=0 ({@link #CODE_SUCCESS})
     * 可以通过CCResult.addData(key, value)来继续添加更多的返回信息
     * @return 构造的CCResult对象
     */
    public static QCResult success() {
        return success(null);
    }

    /**
     * 快捷构建一个CC调用成功的CCResult
     * success=true, code=0 ({@link #CODE_SUCCESS})
     * 可以通过CCResult.addData(key, value)来继续添加更多的返回信息
     * @return 构造的CCResult对象
     * @param data 返回的信息
     * @return 构造的CCResult对象
     */
    public static QCResult success(Map<String, Object> data) {
        QCResult result = new QCResult();
        result.code = CODE_SUCCESS;
        result.success = true;
        result.data = data;
        return result;
    }
    static QCResult defaultNullResult() {
        return error(CODE_ERROR_NULL_RESULT);
    }

    static QCResult defaultExceptionResult(Throwable e) {
        if (e != null) {
            e.printStackTrace();
        }
        return error(CODE_ERROR_EXCEPTION_RESULT);
    }

    /**
     * 尝试将json字符串转成CCResult对象
     * @param str json字符串
     * @return CCResult对象
     * @deprecated
     */
    @Deprecated
    public static QCResult fromString(String str) {
        if (!TextUtils.isEmpty(str)) {
            try{
                JSONObject json = new JSONObject(str);
                return fromJSONObject(json);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Deprecated
    private static QCResult fromJSONObject(JSONObject json) {
        QCResult result = null;
        if (json != null) {
            result = new QCResult();
            result.success = json.optBoolean(KEY_SUCCESS);
            result.code = json.optInt(KEY_CODE);
            result.errorMessage = json.optString(KEY_ERROR_MESSAGE);
            result.data = QCUtil.convertToMap(json.optJSONObject(KEY_DATA));
        }
        return result;
    }

    /**
     * 将CCResult对象转成Json字符串
     * @return json字符串
     */
    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        putValue(json, KEY_SUCCESS, success);
        putValue(json, KEY_CODE, code);
        putValue(json, KEY_ERROR_MESSAGE, errorMessage);
        putValue(json, KEY_DATA, QCUtil.convertToJson(data));
        try {
            return json.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private void putValue(JSONObject json, String key, Object value) {
        try {
            if (json != null && key != null) {
                json.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取CC调用的成功状态
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置CC调用的成功状态
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取CC调用的错误信息
     * @return 错误信息内容
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置CC调用的错误信息
     * @param errorMessage 错误信息内容
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 获取CC调用的状态码。
     * @return 状态码。 0:成功， 小于0: 组件调用失败, 大于0: 组件执行的业务逻辑失败
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置CC调用的状态码
     * @param code 状态码。 0:成功， 小于0: 组件调用失败, 大于0: 组件执行的业务逻辑失败
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取返回信息
     * @deprecated use {@link #getDataMap()}
     * @return 返回的信息
     */
    @Deprecated
    public JSONObject getData() {
        return QCUtil.convertToJson(data);
    }

    /**
     * 设置返回的信息
     * @deprecated use {@link #setDataMap(Map)}
     * @param data 返回给调用方的内容
     */
    @Deprecated
    public void setData(JSONObject data) {
        this.data = QCUtil.convertToMap(data);
    }

    /**
     * 获取CC调用的返回信息
     * @return 返回信息的内容
     */
    public Map<String, Object> getDataMap() {
        return data;
    }

    public <T> T getDataItem(String key, T defaultValue) {
        T item = getDataItem(key);
        if (item == null) {
            return defaultValue;
        }
        return item;
    }
    public <T> T getDataItem(String key) {
        if (data != null) {
            try {
                return (T) data.get(key);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置组件调用的返回信息内容
     * @param data 返回信息的内容
     */
    public void setDataMap(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * 添加组件调用的返回信息内容
     * @param key 返回信息内容的key
     * @param value 返回信息内容的value
     * @return CCResult对象，用于链式添加
     */
    public QCResult addData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>(16);
        }
        data.put(key, value);
        return this;
    }
}
