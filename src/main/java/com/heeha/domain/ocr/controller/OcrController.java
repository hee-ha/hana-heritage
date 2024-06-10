package com.heeha.domain.ocr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeha.global.config.BaseException;
import com.heeha.global.config.BaseResponse;
import com.heeha.global.config.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ocr")
public class OcrController {
    @Value("${naver.ocr.secret}")
    private String ocrSecret;

    @Value("${naver.ocr.url}")
    private String ocrUrl;

    @Operation(summary = "OCR 요청", description = "이미지 파일을 OCR 서버로 전송하여 텍스트를 추출합니다.")
    @PostMapping
    public ResponseEntity<?> handleOcrRequest(@RequestParam("image") MultipartFile image) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-OCR-SECRET", ocrSecret);

            String base64Image = Base64.encodeBase64String(image.getBytes());

            Map<String, Object> request = new HashMap<>();
            request.put("version", "V1");
            request.put("requestId", "ocr-request");
            request.put("timestamp", System.currentTimeMillis());
            request.put("images", new Object[] {
                    new HashMap<String, Object>() {{
                        put("format", "jpg");
                        put("data", base64Image);
                        put("name", "image");
                    }}
            });

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(ocrUrl, requestEntity, String.class);

            // JSON 문자열을 객체로 변환
            Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);

            return new ResponseEntity<>(BaseResponse.success(result), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
