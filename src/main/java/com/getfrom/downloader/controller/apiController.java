package com.getfrom.downloader.controller;


import com.getfrom.downloader.services.apiServices;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public HttpEntity<byte[]> GetVideo(@RequestParam("url") String url, @RequestParam("ext") String ext ) throws IOException, InterruptedException {
        System.out.println("chamado, url ="+url+" , ext = "+ext);
        String title = apiServices.getTitle(url);
        apiServices.GetVideo(url, title, ext);
        return apiServices.SendFile(title, ext);
    }

    @GetMapping("teste")
    public ResponseEntity<byte[]> seadVideo(@RequestParam("url") String url, @RequestParam("ext") String ext ) throws IOException, InterruptedException {
        System.out.println("chamado, url ="+url+" , ext = "+ext);
        String title = apiServices.getTitle(url);
        apiServices.GetVideo(url, title, ext);
        return apiServices.SendFile(title, ext);
    }
}
