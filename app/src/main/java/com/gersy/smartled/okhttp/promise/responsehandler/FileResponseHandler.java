package com.gersy.smartled.okhttp.promise.responsehandler;


import com.piesat.outsideinvestigate.okhttp.promise.handler.FileResultHandler;
import com.piesat.outsideinvestigate.okhttp.promise.handler.ResultHandler;
import com.piesat.outsideinvestigate.utils.JSONUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.HttpUrl;
import okhttp3.Response;

public class FileResponseHandler implements ResponseHandler<File> {

    private File file;
    private FileResultHandler fileResultHandler;

    public FileResponseHandler(File file) {
        this.file = file;
    }

    public FileResponseHandler(File file, FileResultHandler fileResultHandler) {
        this.file = file;
        this.fileResultHandler = fileResultHandler;
    }

    public void setFileResultHandler(ResultHandler fileResultHandler) {
        if (!(fileResultHandler instanceof FileResultHandler)){
            throw new RuntimeException("FileResponseHandler 必须使用FileResultHandler与其配合");
        }
        this.fileResultHandler = (FileResultHandler) fileResultHandler;
    }

    @Override
    public File handle(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        // 储存下载文件的目录
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            if (file.isDirectory()) {
                file = new File(file, getHeaderFileName(response));
            }
            fos = new FileOutputStream(file);
            long sum = 0;
            int currentProgress = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                if (fileResultHandler!=null) {
                    sum += len;
                    int progress = (int) (sum * 1.0f / total * 100);
                    // 下载中
                    if (progress > currentProgress) {
                        currentProgress = progress;
                        fileResultHandler.onProgress(progress);
                    }
                }
            }
            fos.flush();
//            // 下载完成
//            if (fileResultHandler!=null) {
//                fileResultHandler.resolve(file);
//            }
        } catch (Exception e) {
            if (fileResultHandler!=null) {
                fileResultHandler.reject(e);
            }
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
        return file;
    }

    /**
     * 获取下载的文件名
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
        }else {
            HttpUrl url = response.request().url();
            String s = url.toString();
            return s.substring(s.lastIndexOf("/")+1);
        }
    }
}
