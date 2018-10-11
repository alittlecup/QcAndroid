package cn.qingchengfit.router;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Build;
import android.text.TextUtils;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * app之间的组件调用
 * 兼容同步实现的异步调用 & 异步实现的同步调用
 * @author billy.qi
 * @since 17/6/29 11:45
 */
class RemoteQCInterceptor implements IQCInterceptor {
    /**
     * 组件app之间建立socket连接的最大等待时间
     */
    private static final int CONNECT_DELAY = 900;
    static final String MSG_CANCEL = "cancel";
    static final String MSG_TIMEOUT = "timeout";

    static final String KEY_CALL_ID = "cc_component_key_call_id";
    static final String KEY_COMPONENT_NAME = "cc_component_key_component_name";
    static final String KEY_SOCKET_NAME = "cc_component_key_local_socket_name";

    private CountDownLatch connectLatch = new CountDownLatch(1);
    /**
     * 发起组件调用的信息
     */
    private final QC qc;
    /**
     * 是否正在被其它app的组件进行处理
     */
    private volatile boolean ccProcessing = false;
    private final boolean callbackNecessary;
    /**
     * socket通信是否已停止
     */
    private boolean stopped;
    private ObjectOutputStream out;
    private String socketName;
    private static String receiverPermission;
    private static String receiverIntentFilterAction;

    RemoteQCInterceptor(QC qc) {
        this.qc = qc;
        //是否需要wait：异步调用且未设置回调，则不需要wait
        callbackNecessary = !qc.isAsync() || qc.getCallback() != null;
    }

    @Override
    public QCResult intercept(Chain chain) {
        ComponentManager.threadPool(new ConnectTask());
        if (!qc.isFinished() && callbackNecessary) {
            chain.proceed();
            if (qc.isCanceled()) {
                stopCaller(MSG_CANCEL);
            } else if (qc.isTimeout()) {
                stopCaller(MSG_TIMEOUT);
            }
        }
        return qc.getResult();
    }

    private void stopCaller(String msg) {
        if (QC.VERBOSE_LOG) {
            QC.verboseLog(qc.getCallId(), "RemoteQC stopped (%s).", msg);
        }
        if (!ccProcessing) {
            stopConnection();
        } else {
            sendToRemote(msg);
        }
    }

    private void sendToRemote(String str) {
        if (QC.VERBOSE_LOG) {
            QC.verboseLog(qc.getCallId(), "send message to remote app by localSocket:\"" + str + "\"");
        }
        if (out != null) {
            try {
                out.writeObject(str);
                out.flush();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectTask implements Runnable {

        @Override
        public void run() {
            Context context = qc.getContext();
            if (context == null) {
                setResult(QCResult.error(QCResult.CODE_ERROR_CONTEXT_NULL));
                return;
            }
            //retrieve ComponentBroadcastReceiver permission
            if (TextUtils.isEmpty(receiverIntentFilterAction)) {
                try{
                    ComponentName receiver = new ComponentName(context.getPackageName(), ComponentBroadcastReceiver.class.getName());
                    ActivityInfo receiverInfo = context.getPackageManager().getReceiverInfo(receiver, PackageManager.GET_META_DATA);
                    receiverPermission = receiverInfo.permission;
                    receiverIntentFilterAction = "qc.action.com.billy.qc.libs.component.REMOTE_CC";
                } catch(Exception e) {
                    e.printStackTrace();
                    setResult(QCResult.error(QCResult.CODE_ERROR_CONNECT_FAILED));
                    return;
                }
            }
            Intent intent = new Intent(receiverIntentFilterAction);
            if (QC.DEBUG) {
                intent.addFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            }
            String callId = qc.getCallId();
            socketName = "lss:" + callId;
            RemoteQC remoteCC = new RemoteQC(qc);
            remoteCC.setCallId(callId);
            intent.putExtra(KEY_COMPONENT_NAME, qc.getComponentName());
            intent.putExtra(KEY_CALL_ID, callId);
            intent.putExtra(KEY_SOCKET_NAME, socketName);
            LocalServerSocket lss = null;
            //send broadcast for remote qc connection
            ObjectInputStream in = null;
            try {
                lss = new LocalServerSocket(socketName);
                if (QC.VERBOSE_LOG) {
                    QC.verboseLog(callId, "sendBroadcast to call component from other App."
                            + " permission:" + receiverPermission);
                }
                context.sendBroadcast(intent, receiverPermission);
                ComponentManager.threadPool(new CheckConnectTask());
                LocalSocket socket = lss.accept();
                ccProcessing = true;
                if (stopped) {
                    return;
                }
                connectLatch.countDown();

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                try {
                    out.writeObject(remoteCC);
                    out.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                    setResult(QCResult.error(QCResult.CODE_ERROR_REMOTE_CC_DELIVERY_FAILED));
                    return;
                }
                if (QC.VERBOSE_LOG) {
                    QC.verboseLog(callId, "localSocket connect success. " +
                            "start to wait for remote QCResult.");
                }
                //blocking for QCResult
                Object obj;
                try {
                    obj = in.readObject();
                } catch(Exception e) {
                    e.printStackTrace();
                    setResult(QCResult.error(QCResult.CODE_ERROR_REMOTE_CC_DELIVERY_FAILED));
                    return;
                }
                if (QC.VERBOSE_LOG) {
                    QC.verboseLog(callId, "localSocket received remote QCResult:" + obj);
                }
                RemoteQCResult result = (RemoteQCResult) obj;
                setResult(result.toCCResult());
            } catch(Exception e) {
                e.printStackTrace();
                setResult(QCResult.error(QCResult.CODE_ERROR_CONNECT_FAILED));
            } finally {
                if (connectLatch.getCount() > 0) {
                    connectLatch.countDown();
                }
                if (lss != null) {
                    try {
                        lss.close();
                    } catch (Exception ignored) {
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    void setResult(QCResult result) {
        if (callbackNecessary) {
            qc.setResult4Waiting(result);
        } else {
            qc.setResult(result);
        }
    }

    private class CheckConnectTask implements Runnable {
        @Override
        public void run() {
            try {
                connectLatch.await(CONNECT_DELAY, TimeUnit.MILLISECONDS);
            } catch(Exception e) {
                e.printStackTrace();
            }
            if (!ccProcessing && !qc.isFinished()) {
                setResult(QCResult.error(QCResult.CODE_ERROR_NO_COMPONENT_FOUND));
                stopConnection();
                QC.verboseLog(qc.getCallId(), "no component found");
                String cName = qc.getComponentName();
                //跨app组件调用需要组件所在app满足2个条件：
                // 1. 开启支持跨app调用 (QC.RESPONSE_FOR_REMOTE_CC = true;//默认为true，设置为false则关闭)
                // 2. 在系统设置页面中开启自启动权限（根据rom不同，一般在系统的权限设置页面，也可能在一个管家类app中）
                QC.log("call component:" + cName
                        + " failed. Could not found that component. "
                        + "\nPlease make sure the app which contains component(\"" + cName + "\") as below:"
                        + "\n1. " + cName + " set QC.enableRemoteCC(true) "
                        + "\n2. Turn on auto start permission in System permission settings page ");
            }
        }
    }

    private void stopConnection() {
        if (TextUtils.isEmpty(socketName)) {
            return;
        }
        if (QC.VERBOSE_LOG) {
            QC.verboseLog(qc.getCallId(), "stop localServerSocket.accept()");
        }
        stopped = true;
        //通过创建一个无用的client来让ServerSocket跳过accept阻塞，从而中止
        LocalSocket socket = null;
        try {
            socket = new LocalSocket();
            socket.connect(new LocalSocketAddress(socketName));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

}