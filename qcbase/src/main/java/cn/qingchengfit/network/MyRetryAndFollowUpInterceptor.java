package cn.qingchengfit.network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.UnrepeatableRequestBody;
import okhttp3.internal.http2.ConnectionShutdownException;

public class MyRetryAndFollowUpInterceptor implements Interceptor {

  private static final int MAX_FOLLOW_UPS = 20;
  private final OkHttpClient client;
  private final boolean forWebSocket;
  private StreamAllocation streamAllocation;
  private Object callStackTrace;
  private volatile boolean canceled;

  public MyRetryAndFollowUpInterceptor(OkHttpClient client, boolean forWebSocket) {
    this.client = client;
    this.forWebSocket = forWebSocket;
  }

  public void cancel() {
    this.canceled = true;
    StreamAllocation streamAllocation = this.streamAllocation;
    if (streamAllocation != null) {
      streamAllocation.cancel();
    }

  }

  public boolean isCanceled() {
    return this.canceled;
  }

  public void setCallStackTrace(Object callStackTrace) {
    this.callStackTrace = callStackTrace;
  }

  public StreamAllocation streamAllocation() {
    return this.streamAllocation;
  }

  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(request.url()), this.callStackTrace);
    int followUpCount = 0;
    Response priorResponse = null;

    while(!this.canceled) {
      Response response = null;
      boolean releaseConnection = true;

      try {
        response = ((RealInterceptorChain)chain).proceed(request, this.streamAllocation, (HttpCodec)null, (RealConnection)null);
        releaseConnection = false;
      } catch (RouteException var13) {
        if (!this.recover(var13.getLastConnectException(), false, request)) {
          throw var13.getLastConnectException();
        }

        releaseConnection = false;
        continue;
      } catch (IOException var14) {
        boolean requestSendStarted = !(var14 instanceof ConnectionShutdownException);
        if (!this.recover(var14, requestSendStarted, request)) {
          throw var14;
        }

        releaseConnection = false;
        continue;
      } finally {
        if (releaseConnection) {
          this.streamAllocation.streamFailed((IOException)null);
          this.streamAllocation.release();
        }

      }

      if (priorResponse != null) {
        response = response.newBuilder().priorResponse(priorResponse.newBuilder().body((ResponseBody)null).build()).build();
      }

      Request followUp = this.followUpRequest(response);
      if (followUp == null) {
        if (!this.forWebSocket) {
          this.streamAllocation.release();
        }

        return response;
      }

      Util.closeQuietly(response.body());
      ++followUpCount;
      if (followUpCount > 20) {
        this.streamAllocation.release();
        throw new ProtocolException("Too many follow-up requests: " + followUpCount);
      }

      if (followUp.body() instanceof UnrepeatableRequestBody) {
        this.streamAllocation.release();
        throw new HttpRetryException("Cannot retry streamed HTTP body", response.code());
      }

      if (!this.sameConnection(response, followUp.url())) {
        this.streamAllocation.release();
        this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(followUp.url()), this.callStackTrace);
      } else if (this.streamAllocation.codec() != null) {
        throw new IllegalStateException("Closing the body of " + response + " didn't close its backing stream. Bad interceptor?");
      }

      request = followUp;
      priorResponse = response;
    }

    this.streamAllocation.release();
    throw new IOException("Canceled");
  }

  private Address createAddress(HttpUrl url) {
    SSLSocketFactory sslSocketFactory = null;
    HostnameVerifier hostnameVerifier = null;
    CertificatePinner certificatePinner = null;
    if (url.isHttps()) {
      sslSocketFactory = this.client.sslSocketFactory();
      hostnameVerifier = this.client.hostnameVerifier();
      certificatePinner = this.client.certificatePinner();
    }

    return new Address(url.host(), url.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
  }

  private boolean recover(IOException e, boolean requestSendStarted, Request userRequest) {
    this.streamAllocation.streamFailed(e);
    if (!this.client.retryOnConnectionFailure()) {
      return false;
    } else if (requestSendStarted && userRequest.body() instanceof UnrepeatableRequestBody) {
      return false;
    } else if (!this.isRecoverable(e, requestSendStarted)) {
      return false;
    } else {
      return this.streamAllocation.hasMoreRoutes();
    }
  }

  private boolean isRecoverable(IOException e, boolean requestSendStarted) {
    if (e instanceof ProtocolException) {
      return false;
    } else if (!(e instanceof InterruptedIOException)) {
      if (e instanceof SSLHandshakeException && e.getCause() instanceof CertificateException) {
        return false;
      } else {
        return !(e instanceof SSLPeerUnverifiedException);
      }
    } else {
      return e instanceof SocketTimeoutException && !requestSendStarted;
    }
  }

  private Request followUpRequest(Response userResponse) throws IOException {
    if (userResponse == null) {
      throw new IllegalStateException();
    } else {
      Connection connection = this.streamAllocation.connection();
      Route route = connection != null ? connection.route() : null;
      int responseCode = userResponse.code();
      String method = userResponse.request().method();
      switch(responseCode) {
        case 307:
        case 308:
        case 300:
        case 301:
        case 302:
        case 303:
            String location = userResponse.header("Location");
            if (location == null) {
              return null;
            } else {
              HttpUrl url = userResponse.request().url().resolve(location);
              if (url == null) {
                return null;
              } else {
                boolean sameScheme = url.scheme().equals(userResponse.request().url().scheme());
                if (!sameScheme && !this.client.followSslRedirects()) {
                  return null;
                }

                Request.Builder requestBuilder = userResponse.request().newBuilder();
                if (HttpMethod.permitsRequestBody(method)) {
                  boolean maintainBody = HttpMethod.redirectsWithBody(method);
                  if (method.equals("PROPFIND")) {
                    requestBuilder.method("GET", (RequestBody)null);
                  } else {
                    RequestBody requestBody = HttpMethod.requiresRequestBody(method) ? userResponse.request().body() : null;
                    requestBuilder.method(method, requestBody);
                  }

                  if (!maintainBody) {
                    requestBuilder.removeHeader("Transfer-Encoding");
                    requestBuilder.removeHeader("Content-Length");
                    requestBuilder.removeHeader("Content-Type");
                  }
                }

                if (!this.sameConnection(userResponse, url)) {
                  requestBuilder.removeHeader("Authorization");
                }

                return requestBuilder.url(url).build();
              }
            }

        case 401:
          return this.client.authenticator().authenticate(route, userResponse);
        case 407:
          Proxy selectedProxy = route != null ? route.proxy() : this.client.proxy();
          if (selectedProxy.type() != Proxy.Type.HTTP) {
            throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
          }

          return this.client.proxyAuthenticator().authenticate(route, userResponse);
        case 408:
          if (userResponse.request().body() instanceof UnrepeatableRequestBody) {
            return null;
          }

          return userResponse.request();
        default:
          return null;
      }
    }
  }

  private boolean sameConnection(Response response, HttpUrl followUp) {
    HttpUrl url = response.request().url();
    return url.host().equals(followUp.host()) && url.port() == followUp.port() && url.scheme().equals(followUp.scheme());
  }
}
