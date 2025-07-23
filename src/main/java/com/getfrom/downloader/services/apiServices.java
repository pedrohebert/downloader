package com.getfrom.downloader.services;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ContentDisposition;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;



@Service
public class apiServices {


    public String getTitle(String url) throws IOException, InterruptedException {
        String title;

        ProcessBuilder pb = new ProcessBuilder("yt-dlp", "--print", "\"%(channel)s - %(title)s\"", url);
        pb.environment().put("PYTHONIOENCODING", "utf-8");

        Process process = pb.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
        );

        title = reader.readLine();
        System.out.println(title);

        process.waitFor();

        return title.replaceAll("[\\\\/:*?\"<>|]", "");
    }

    public void SendMidia(String url, String ext, HttpServletResponse response) throws IOException {
        ProcessBuilder  processBuilder = CreateDownloadProcessBuilder(url, ext);

        processBuilder.redirectErrorStream(true);

        try {
            Process ytDlpProcess = processBuilder.start();

            String fileName = getTitle(url) + "." + ext;

            String contentType = "application/octet-stream";
            ContentDisposition contentDisposition = ContentDisposition.attachment()
                    .filename(fileName, StandardCharsets.UTF_8)
                    .build();

            response.setContentType(contentType);
            response.setHeader("", contentDisposition.toString());
            response.setCharacterEncoding("utf-8");

            try (InputStream inputStreamProcess = ytDlpProcess.getInputStream();
                 OutputStream outputStreamResponse = response.getOutputStream()){

                byte[] buffer = new byte[1024 * 8];
                int byteRead;

                while ((byteRead = inputStreamProcess.read(buffer)) != 0){
                    outputStreamResponse.write(buffer, 0, byteRead);
                    outputStreamResponse.flush();
                }
            }
            int exitCode = ytDlpProcess.waitFor();
            if (exitCode != 0){
                throw new RuntimeException("Processo falhou com código " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);     
            response.getWriter().write("Erro durante o processamento: " + e.getMessage());
        }
    }

    private ProcessBuilder CreateDownloadProcessBuilder(String url, String ext){
        return new ProcessBuilder("yt-dlp",
                "-t", ext, //"bestvideo[ext="+ext+"]+bestaudio[ext="+aext+"]/best[ext="+ext+"]",  // Pode ajustar conforme necessidade
                "-q",
                "--no-playlist",
                "-o", "-",  // Saída para stdout
                "--no-part",
                "--no-cache-dir",
                "--no-progress",
                "--no-call-home",
                url);
    }



}
