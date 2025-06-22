package com.JSCode.gestion_de_inventario.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ImageStorageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ImageStorageService imageStorageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set campos @Value manualmente porque no se inyectan en test por defecto
        imageStorageService = new ImageStorageService(restTemplate);
        imageStorageService.getClass().getDeclaredFields(); // solo para evitar warning de IDE
        try {
            var storageApiBase = ImageStorageService.class.getDeclaredField("storageApiBase");
            storageApiBase.setAccessible(true);
            storageApiBase.set(imageStorageService, "https://api.imgbb.com/1/upload");

            var apiKey = ImageStorageService.class.getDeclaredField("apikey");
            apiKey.setAccessible(true);
            apiKey.set(imageStorageService, "test_api_key");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void uploadImagesToImgBB_successfulUpload_returnsImageUrls() {
        // Arrange
        byte[] imageBytes = "dummy image".getBytes();
        MultipartFile mockFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", imageBytes);
        List<MultipartFile> files = List.of(mockFile);

        Map<String, Object> data = new HashMap<>();
        data.put("url", "https://fake.imgbb.com/image.jpg");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", data);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // Act
        List<String> result = imageStorageService.uploadImagesToImgBB(files);

        // Assert
        assertEquals(1, result.size());
        assertEquals("https://fake.imgbb.com/image.jpg", result.get(0));
    }

    @Test
    void uploadImagesToImgBB_failureOrEmptyResponse_returnsEmptyList() {
        // Arrange
        byte[] imageBytes = "dummy image".getBytes();
        MultipartFile mockFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", imageBytes);
        List<MultipartFile> files = List.of(mockFile);

        // Simulamos un response sin 'data'
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(Map.of(), HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // Act
        List<String> result = imageStorageService.uploadImagesToImgBB(files);

        // Assert
        assertTrue(result.isEmpty());
    }
}
