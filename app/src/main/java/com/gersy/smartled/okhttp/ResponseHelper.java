package com.gersy.smartled.okhttp;


import com.piesat.outsideinvestigate.utils.JSONUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseHelper {

    public static String string(Response response) throws IOException {
        ResponseBody body = response.body();
        return body.string();
    }

    public static <T> T toBean(Response response, Class<T> clazz) throws IOException {
        String json = string(response);
        return JSONUtils.jsonToBean(json, clazz);
    }

    public static InputStream stream(Response response) {
        return response.body().byteStream();
    }

    /**
     * 下载文件
     */
    public static void downFile(Response response, File file, DownloadProgress downloadProgress) {
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        // 储存下载文件的目录
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            if (file.isDirectory()){
                file=new File(file,getHeaderFileName(response));
            }
            fos = new FileOutputStream(file);
            long sum = 0;
            int currentProgress = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                // 下载中
                if (progress > currentProgress) {
                    currentProgress = progress;
                    downloadProgress.onProgress(progress);
                }
            }
            fos.flush();
            // 下载完成
            downloadProgress.onSuccess(file);
        } catch (Exception e) {
            downloadProgress.onFailure(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取下载的文件名
     * @return
     */
    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response.header("Content-Disposition");
        if (!JSONUtils.isEmpty(dispositionHeader)) {
            dispositionHeader.replace("attachment;filename=", "");
            dispositionHeader.replace("filename*=utf-8", "");
            String[] strings = dispositionHeader.split("; ");
            if (strings.length > 1) {
                dispositionHeader = strings[1].replace("filename=", "");
                dispositionHeader = dispositionHeader.replace("\"", "");
                return dispositionHeader;
            }
            return "";
        }
        return "";
    }
}
