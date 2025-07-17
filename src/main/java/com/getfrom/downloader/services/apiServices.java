package com.getfrom.downloader.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class apiServices {

    public HttpEntity<byte[]> download(String file, String ext) throws IOException {
        byte[] arquivo = Files.readAllBytes(Paths.get("src\\main\\resources\\downloads\\" + file + "." + ext));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Disposition", "attachment;filename=\"" + file + "." + ext +"\"");
        System.out.println(httpHeaders);

        return new HttpEntity<byte[]>(arquivo, httpHeaders);
    }

    public void GetVideo(String url, String title, String ext) throws IOException, InterruptedException {
        String aext;
        if (Objects.equals(ext, "mp4")) {
            aext = "m4a";
        }else {
            aext = "webm";
        }
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
