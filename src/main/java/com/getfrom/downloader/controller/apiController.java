package com.getfrom.downloader.controller;

import com.getfrom.downloader.services.apiServices;
import jakarta.servlet.http.HttpServletResponse;
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
    public void StreamMedia(
            @RequestParam("url") String url,
            @RequestParam("ext") String ext,
            HttpServletResponse response
    ) throws IOException {
        apiServices.SendMidia(url, ext, response);
    }

}
