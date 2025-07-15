package com.getfrom.downloader.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class apiServices {

    public HttpEntity<byte[]> download(String file) throws IOException {
        byte[] arquivo = Files.readAllBytes(Paths.get("src/main/resources/static/" + file));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Disposition", "attachment;filename=\"" + file +"\"");
        System.out.println(httpHeaders);

        return new HttpEntity<byte[]>(arquivo, httpHeaders);
    }

}
