package com.elin4it.spider.Util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件下载管理类
 */

public class FileDownloadUtil {


    /**
     * 每个线程下载的字节数
     */

//    private static final long unitSize = 100 * 1024;


    private static int task_num = 20;

    private static final ExecutorService taskExecutor = Executors.newFixedThreadPool(50);


    public static void doDownload(String remoteFileUrl, String localPath, String fileName) throws IOException {

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        String remoteFileName = new URL(remoteFileUrl).getFile();

        System.out.println("远程文件名称：" + remoteFileName);
        long fileSize = getRemoteFileSize(remoteFileUrl);

        long unitSize = fileSize < task_num ? fileSize : fileSize / task_num;


        createFile(localPath + File.separator + fileName, fileSize);

        Long threadCount = (fileSize / unitSize) + (fileSize % unitSize != 0 ? 1 : 0);
        long offset = 0;

        CountDownLatch end = new CountDownLatch(threadCount.intValue());

        if (fileSize <= unitSize) {// 如果远程文件尺寸小于等于unitSize

            DownloadThread downloadThread = new DownloadThread(remoteFileUrl,

                    localPath + File.separator + fileName, offset, fileSize, end, httpClient, 1);

            taskExecutor.execute(downloadThread);

        } else {// 如果远程文件尺寸大于unitSize

            for (int i = 1; i < threadCount; i++) {

                DownloadThread downloadThread = new DownloadThread(

                        remoteFileUrl, localPath + File.separator + fileName, offset, unitSize, end, httpClient, i);

                taskExecutor.submit(downloadThread);

                offset = offset + unitSize;

            }

            if (fileSize % unitSize != 0) {// 如果不能整除，则需要再创建一个线程下载剩余字节

                DownloadThread downloadThread = new DownloadThread(remoteFileUrl, localPath + fileName, offset, fileSize - unitSize * (threadCount - 1), end, httpClient, threadCount.intValue());
                taskExecutor.submit(downloadThread);
            }

        }
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        System.out.println("下载完成:" + localPath + File.separator + fileName);
    }

    /**
     * 获取远程文件尺寸
     */

    private static long getRemoteFileSize(String remoteFileUrl) throws IOException {

        long fileSize = 0;

        HttpURLConnection httpConnection = (HttpURLConnection) new URL(

                remoteFileUrl).openConnection();

        httpConnection.setRequestMethod("HEAD");

        int responseCode = httpConnection.getResponseCode();

        if (responseCode >= 400) {


            return 0;

        }

        String sHeader;

        for (int i = 1; ; i++) {

            sHeader = httpConnection.getHeaderFieldKey(i);

            if (sHeader != null && sHeader.equals("Content-Length")) {

                System.out.println("文件大小ContentLength:"
                        + httpConnection.getContentLength());

                fileSize = Long.parseLong(httpConnection
                        .getHeaderField(sHeader));

                break;

            }

        }

        return fileSize;

    }

    /**
     * 创建指定大小的文件
     */

    private static void createFile(String fileName, long fileSize) throws IOException {

        File newFile = new File(fileName);

        RandomAccessFile raf = new RandomAccessFile(newFile, "rw");

        raf.setLength(fileSize);

        raf.close();

    }

    static class DownloadThread extends Thread {


        /**
         * 待下载的文件
         */

        private String url = null;

        /**
         * 本地文件名
         */

        private String fileName = null;

        /**
         * 偏移量
         */

        private long offset = 0;

        /**
         * 分配给本线程的下载字节数
         */

        private long length = 0;

        private int no;

        private CountDownLatch end;

        private CloseableHttpClient httpClient;

        private HttpContext context;

        /**
         * @param url    下载文件地址
         * @param file   另存文件名
         * @param offset 本线程下载偏移量
         * @param length 本线程下载长度
         */

        public DownloadThread(String url, String file, long offset, long length,
                              CountDownLatch end, CloseableHttpClient httpClient, int no) {

            this.url = url;

            this.fileName = file;

            this.offset = offset;

            this.length = length;

            this.end = end;

            this.httpClient = httpClient;

            this.context = new BasicHttpContext();
            this.no = no;


        }

        public void run() {

            try {

                HttpGet httpGet = new HttpGet(this.url);
                httpGet.addHeader("Range", "bytes=" + this.offset + "-"
                        + (this.offset + this.length - 1));
                CloseableHttpResponse response = httpClient.execute(httpGet,
                        context);

                BufferedInputStream bis = new BufferedInputStream(response
                        .getEntity().getContent());

                byte[] buff = new byte[10240];

                int bytesRead;


                RandomAccessFile raf;

                File file = new File(fileName);

                double finish = 0;

                while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
                    raf = new RandomAccessFile(file, "rw");
                    raf.seek(this.offset);
                    raf.write(buff, 0, bytesRead);
                    this.offset = this.offset + bytesRead;
                    finish += bytesRead;
                    raf.close();

//                    System.out.println("分段" + no + "完成：" + (finish / length * 100));

                }


                bis.close();
            } catch (ClientProtocolException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                end.countDown();
                System.out.println(fileName + "第" + no + "部分下载完毕");
            }


//                HttpGet httpGet = new HttpGet(this.url);
//                httpGet.addHeader("Range", "bytes=" + this.offset + "-"
//                        + (this.offset + this.length - 1));
//                CloseableHttpResponse response = httpClient.execute(httpGet,
//                        context);
//                ;
//                BufferedInputStream bis = new BufferedInputStream(response
//                        .getEntity().getContent());
//
//                byte[] buff = new byte[1024];
//
//                int bytesRead;
//
//                File newFile = new File(fileName);
//
//                RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
//
//                while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
//                    System.out.println(end.getCount());
//                    raf.seek(this.offset);
//                    raf.write(buff, 0, bytesRead);
//                    this.offset = this.offset + bytesRead;
//                }
//                raf.close();
//                bis.close();
//            } catch (ClientProtocolException e) {
//                System.out.println(e);
//            } catch (IOException e) {
//                System.out.println(e);
//            } finally {
//                end.countDown();
//                System.out.println(fileName + "第" + end.getCount() + "部分下载完毕");
//            }
        }

    }
}

