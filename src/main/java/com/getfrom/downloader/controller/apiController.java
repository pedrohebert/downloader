package com.getfrom.downloader.controller;


import com.getfrom.downloader.services.apiServices;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

@RestController
@RequestMapping("/")
public class apiController {

    private final apiServices apiServices;

    private apiController(apiServices apiServices){
        this.apiServices = apiServices;
    }

    @GetMapping("downloads")
    public HttpEntity<byte[]> download(@PathParam("file") String file) throws IOException {
        return apiServices.download(file);
    }
}
