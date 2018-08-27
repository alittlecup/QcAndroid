#!/bin/bash
echo "------ init! ------"
BASE=$2/jiagu/jiagu.jar
NAME=app@qingcheng.it
PASSWORD=Qingcheng2014
KEY_PATH=$2/qingcheng.keystore #密钥路径
KEY_PASSWORD=Qingcheng2014 #密钥密码
ALIAS=Qingcheng #别名
ALIAS_PASSWORD=Qingcheng2014 #别名密码

APK=$1   #需要加固的apk路径
DEST=$2/output/  #输出加固包路径

echo "------ running! ------"

#java -jar ${BASE} -version
#java -jar ${BASE} -login ${NAME} ${PASSWORD}
#java -jar ${BASE} -showsign
#java -jar ${BASE}/jiagu.jar -importmulpkg ${BASE}/多渠道模板.txt #根据自身情况使用
#java -jar ${BASE} -showmulpkg
#java -jar ${BASE} -showconfig
#java -jar ${BASE} -jiagu ${APK} ${DEST} -autosign
java -jar jiagu/lib/apksigner.jar sign --ks ${KEY_PATH} --ks-key-alias ${ALIAS} --ks-pass pass:Qingcheng2014 --key-pass pass:Qingcheng2014 ${DEST}staff-product-release_1`date +%y%m%d`_jiagu.apk
echo "------ success! ------"
rm ${DEST}*temp.apk

echo "------ finished! ------"

#-login          <username>                    首次使用必须先登录 <360用户名>
#                <password>                    <登录密码>

#-importsign     <keystore_path>               导入签名信息 <密钥路径>
#                <keystore_password>           <密钥密码>
#                <alias>                       <别名>
#                <alias_password>              <别名密码>

#-importmulpkg   <mulpkg_filepath>             导入多渠道配置信息，txt格式
#-showsign                                     查看已配置的签名信息
#-showmulpkg                                   查看已配置的多渠道信息
#-help                                         显示帮助信息

#-config         [-update]                     配置加固可选项 【升级通知】
#                [-crashlog]                  【崩溃日志】
#                [-x86]                       【x86支持】

#-showconfig                                   显示已配置加固项
#-version                                      显示当前版本号
#-update                                       升级到最新版本

#-jiagu          <inputAPKpath>                加固命令 <APK路径>
#                <outputPath>                  <输出路径>
#                [-autosign]                  【自动签名】
#                [-automulpkg]                【自动多渠道】
#                [-pkgparam mulpkg_filepath]  【自定义文件生成多渠道】