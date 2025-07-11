package com.JSCode.gestion_de_inventario.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class ImageStorageService {

    @Value("${STORAGE_API_BASE}") 
    private String storageApiBase;

    @Value("${STORAGE_APIKEY}")   
    private String apikey;

    private final RestTemplate restTemplate;

    public ImageStorageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> uploadImagesToImgBB(List<MultipartFile> files) {
        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                byte[] bytes = file.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("image", base64);

                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

                String url = storageApiBase + "?key=" + apikey;

                ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("data");
                    if (responseData != null && responseData.containsKey("url")) {
                        String imageUrl = (String) responseData.get("url");
                        uploadedUrls.add(imageUrl);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return uploadedUrls;
    }
}

