package com.xiejinxin.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.net.SocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.catalina.connector.ClientAbortException;

@RestController
@RequestMapping("/api/proxy")
public class VideoProxyController {
    
    private static final Logger logger = LoggerFactory.getLogger(VideoProxyController.class);
    private final OkHttpClient client;
    private static final String VIDEO_DIRECTORY = "D:/anime_videos/";
    private static final int CHUNK_SIZE = 1024 * 1024; // 1MB chunks
    
    public VideoProxyController() {
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build();
    }
    
    @GetMapping("/local-video/{fileName}")
    public ResponseEntity<Resource> serveVideo(
            @PathVariable String fileName,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            Path videoPath = Paths.get(VIDEO_DIRECTORY + fileName);
            File videoFile = videoPath.toFile();
            
            if (!videoFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            long fileLength = videoFile.length();
            long start = 0;
            long end = fileLength - 1;

            if (rangeHeader != null) {
                String[] ranges = rangeHeader.substring(6).split("-");
                start = Long.parseLong(ranges[0]);
                
                if (ranges.length > 1) {
                    end = Long.parseLong(ranges[1]);
                }
            }

            long contentLength = end - start + 1;
            
            InputStream inputStream = new BufferedInputStream(new FileInputStream(videoFile));
            inputStream.skip(start);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "video/mp4");
            headers.add("Accept-Ranges", "bytes");
            headers.add("Content-Length", String.valueOf(contentLength));
            headers.add("Content-Range", String.format("bytes %d-%d/%d", start, end, fileLength));

            return ResponseEntity.status(rangeHeader != null ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                    .headers(headers)
                    .body(new InputStreamResource(inputStream));
                    
        } catch (ClientAbortException e) {
            logger.warn("客户端中断连接: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            logger.error("视频文件访问失败: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/video")
    public ResponseEntity<byte[]> proxyVideo(@RequestParam String url) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Accept", "*/*")
                .addHeader("Range", "bytes=0-")
                .build();

            response = client.newCall(request).execute();
            
            if (!response.isSuccessful()) {
                return ResponseEntity.status(response.code()).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept-Ranges", "bytes");
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Range");
            
            MediaType mediaType = response.body().contentType();
            if (mediaType != null) {
                headers.setContentType(org.springframework.http.MediaType.parseMediaType(mediaType.toString()));
            }

            byte[] body = response.body().bytes();
            return ResponseEntity.status(response.code())
                .headers(headers)
                .body(body);
                
        } catch (Exception e) {
            logger.error("代理请求异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    @RequestMapping(value = "/video", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Range, Origin, X-Requested-With, Content-Type, Accept");
        headers.set("Access-Control-Max-Age", "3600");
        
        return ResponseEntity.ok()
            .headers(headers)
            .build();
    }
    
    // 获取视频文件列表
    @GetMapping("/local-videos")
    public ResponseEntity<List<String>> getVideoList() {
        File directory = new File(VIDEO_DIRECTORY);
        String[] videoFiles = directory.list((dir, name) -> name.toLowerCase().endsWith(".mp4"));
        
        if (videoFiles != null) {
            return ResponseEntity.ok(Arrays.asList(videoFiles));
        }
        
        return ResponseEntity.ok(new ArrayList<>());
    }
} 