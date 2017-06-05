package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/15 2015.
 */
public class QcVersionResponse extends QcResponse {

    /**
     * data : {"download":{"android":"http://cloud.qingchengfit.cn/","ios":"http://cloud.qingchengfit.cn/"},"version":{"android":{"release":100,"version":"0.1.1"},"ios":{"release":100,"version":"0.1.1"}}}
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * download : {"android":"http://cloud.qingchengfit.cn/","ios":"http://cloud.qingchengfit.cn/"}
         * version : {"android":{"release":100,"version":"0.1.1"},"ios":{"release":100,"version":"0.1.1"}}
         */

        private DownloadEntity download;
        private VersionEntity version;

        public DownloadEntity getDownload() {
            return download;
        }

        public void setDownload(DownloadEntity download) {
            this.download = download;
        }

        public VersionEntity getVersion() {
            return version;
        }

        public void setVersion(VersionEntity version) {
            this.version = version;
        }

        public static class DownloadEntity {
            /**
             * android : http://cloud.qingchengfit.cn/
             * ios : http://cloud.qingchengfit.cn/
             */

            private String android;
            private String ios;

            public String getAndroid() {
                return android;
            }

            public void setAndroid(String android) {
                this.android = android;
            }

            public String getIos() {
                return ios;
            }

            public void setIos(String ios) {
                this.ios = ios;
            }
        }

        public static class VersionEntity {
            /**
             * android : {"release":100,"version":"0.1.1"}
             * ios : {"release":100,"version":"0.1.1"}
             */

            private AndroidEntity android;
            private IosEntity ios;

            public AndroidEntity getAndroid() {
                return android;
            }

            public void setAndroid(AndroidEntity android) {
                this.android = android;
            }

            public IosEntity getIos() {
                return ios;
            }

            public void setIos(IosEntity ios) {
                this.ios = ios;
            }

            public static class AndroidEntity {
                /**
                 * release : 100
                 * version : 0.1.1
                 */

                private int release;
                private String version;

                public int getRelease() {
                    return release;
                }

                public void setRelease(int release) {
                    this.release = release;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }

            public static class IosEntity {
                /**
                 * release : 100
                 * version : 0.1.1
                 */

                private int release;
                private String version;

                public int getRelease() {
                    return release;
                }

                public void setRelease(int release) {
                    this.release = release;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }
        }
    }
}
