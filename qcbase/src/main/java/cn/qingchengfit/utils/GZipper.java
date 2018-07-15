package cn.qingchengfit.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipper{

public static final String DEFAULT_CHARSET = "utf-8";
private static final int BYTE_BLOCK_LENGTH = 1024;

public static byte[] doZip(final String message){
    if(message == null || message.isEmpty()){
        throw new RuntimeException("Fail to zip - given message is null or empty");
    }
    byte[] gzippped = null;
    try {
        gzippped = doZip(message.getBytes(DEFAULT_CHARSET));
    } catch (Throwable e) {
        throw new RuntimeException(e.getMessage(), e);
    }
    return gzippped;
}

public static byte[] doZip(final byte[] unzippedMessageByte){
    validate(unzippedMessageByte, "Fail to zip - given bytes is null or empty");

    ByteArrayInputStream is = null;
    ByteArrayOutputStream bos = null;
    GZIPOutputStream gzip_os = null;
    byte[] compressedBytes = null;
    try{
        is = new ByteArrayInputStream(unzippedMessageByte);
        bos = new ByteArrayOutputStream();
        gzip_os = new GZIPOutputStream(bos);
        copy(is, gzip_os);
        gzip_os.finish();
        compressedBytes = bos.toByteArray();
    }catch(IOException e){
        throw new RuntimeException(e.getMessage(), e);
    }finally{
        try{
            if(is != null){is.close();}
            if(gzip_os != null){gzip_os.close();}
            if(bos != null){bos.close();}
        }catch(IOException e){
            //ignore
        }
    }
    return compressedBytes;
}

public static String doUnZipToString(final byte[] gzippedMessage){
    validate(gzippedMessage, "Fail to unzip - given bytes is null or empty");
    byte[] gzippped = null;
    String unzippedMessage = null;
    try {
        gzippped = doUnZip(gzippedMessage);
        unzippedMessage = new String(gzippped, DEFAULT_CHARSET);
    } catch (Throwable e) {
        throw new RuntimeException(e.getMessage(), e);
    }
    return unzippedMessage;
}

private static void validate(final byte[] bytes, String failedMessage) {
    if(bytes == null || bytes.length == 0){
        throw new RuntimeException(failedMessage);
    }
}

public static byte[] doUnZip(InputStream in) {
    if(!(in instanceof ByteArrayInputStream)){
        try {
            return doUnZip(IOUtils.toByteArray(in));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

     ByteArrayOutputStream bos = null;
     InputStream gzip_is = null;
     byte[] bytes = null;
    try{
        bos = new ByteArrayOutputStream();
        gzip_is = new GZIPInputStream(in);
        copy(gzip_is,bos);
        bytes = bos.toByteArray();
    }catch(IOException e){
        throw new RuntimeException(e.getMessage(), e);
    }finally{
        try{
            if(gzip_is != null) gzip_is.close();
            if(bos != null) bos.close();
        }catch(IOException e){
            //ignore
        }
    }
    return bytes;
}

public static byte[] doUnZip(final byte[] zippedMessage){
    validate(zippedMessage, "Fail to unzip - given bytes is null or empty");
    ByteArrayInputStream is = null;
    try{
        is = new ByteArrayInputStream(zippedMessage);
        return doUnZip(is);
    }finally{
        try{
            if(is != null) is.close();
        }catch(IOException e){
            //ignore
        }
    }
}

public static String doUnZip(File file){
    validate(file);

    GZIPInputStream gzipInputStream = null;
    StringWriter writer = null;
    String result = "";

    try{
        byte[] buffer = new byte[BYTE_BLOCK_LENGTH];
        gzipInputStream = new GZIPInputStream(new FileInputStream(file));
        writer = new StringWriter();
        while((gzipInputStream.read(buffer)) > 0){
            writer.write(new String(buffer));
            writer.flush();
        }
        result = writer.toString();
    }catch(IOException e){
        //do something to handle exception
    }
    finally{
        try{
            if(writer != null){writer.close();}
            if(gzipInputStream != null){gzipInputStream.close();}
        }catch(IOException e){
            //ignore
        }
    }
    return result;
}

private static void validate(File file) {
    if(file==null || !file.exists()){
        throw new RuntimeException("Fail to unzip - file is not exist");
    }
}

private static void copy(InputStream in, OutputStream out)throws IOException {
    byte[] buf = new byte[BYTE_BLOCK_LENGTH];
    int len = -1;
    while ((len = in.read(buf, 0, buf.length)) != -1) {
        out.write(buf, 0, len);
    }
}

public static boolean isGzipped(byte[] input){
    return isGzippped(new ByteArrayInputStream(input));
}

public static boolean isGzippped(InputStream in){
    boolean markSupported = in.markSupported();
    boolean result = false;
    try {
        if(markSupported){
            in.mark(0);
            result = (readUShort(in) == GZIPInputStream.GZIP_MAGIC);
            in.reset();
        }
    } catch (Exception e) {
        result = false;
    }
    return result;
}

private static int readUShort(InputStream in) throws IOException {
    int b = readUByte(in);
    return ((int)readUByte(in) << 8) | b;
}

/*
 * Reads unsigned byte.
 */
private static int readUByte(InputStream in) throws IOException {
    int b = in.read();
    if (b == -1) {
        throw new EOFException();
    }
    if (b < -1 || b > 255) {
       b = 0;
    }
    return b;
}
}