package com.getfrom.downloader.services;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class apiServices {


    public ResponseEntity<byte[]> SendFile(String file, String ext) throws IOException {

        Path filePath = Paths.get("src\\main\\resources\\downloads\\" + file + "." + ext);
        byte[] videoBytes = Files.readAllBytes(filePath);
        
        String contentType = switch (ext){
            case "mp4" -> "video/mp4";
            case "webm" -> "video/webm";
            case ".mkv" -> "video/x-matroska";
            default -> "application/octet-stream";
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\"" + file + "." + ext +"\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(videoBytes);
    }

    public void GetVideo(String url, String title, String ext) throws IOException, InterruptedException {

        String aext = switch (ext){
            case "mp4" -> "m4a";
            case "webm" -> "wemb";
            default -> "webm";
        };

        ProcessBuilder pb = new ProcessBuilder("yt-dlp",
                "--concurrent-fragments", " 10 ",
                "--fragment-retries", "10",
                "-f", "bestvideo[ext="+ext+"]+bestaudio[ext="+aext+"]/best[ext="+ext+"]",
                "--merge-output-format", ext,
                "-P", "C:\\Users\\pedro\\Documents\\code_jv\\spring_boot\\downloader\\src\\main\\resources\\downloads",
                "-o", title+".%(ext)s",
                url);


        //yt-dlp -P C:\\Users\\pedro\\Documents\\code_jv\\spring_boot\\downloader\\src\\main\\resources\\downloads --downloader aria2c --downloader-args aria2c:"--max-connection-per-server=10 --split=10"  https://youtu.be/UPveU2BQImg?si=axn529P4JPsfLt5s
        //10s - 18s
        // yt-dlp -P C:\\Users\\pedro\\Documents\\code_jv\\spring_boot\\downloader\\src\\main\\resources\\downloads --concurrent-fragments 10 --fragment-retries 10  https://youtu.be/UPveU2BQImg?si=axn529P4JPsfLt5s
        //10s - 18s
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null){
            System.out.println(line);
        }

        process.waitFor();

        System.out.println("concluido");

    }

    public String getTitle(String url) throws IOException, InterruptedException {
        String title;

        ProcessBuilder pb = new ProcessBuilder("yt-dlp", "--print", "\"%(channel)s - %(title)s\"", url);

        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        title = reader.readLine();
        System.out.println(title);

        process.waitFor();

        title = title.replaceAll("[?|]", "#");

        //return URLEncoder.encode(title, StandardCharsets.UTF_8);
        return title;
    }

}
