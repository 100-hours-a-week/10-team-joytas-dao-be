package com.example.daobe.upload.infrastructure.s3;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class S3PresignHandler {

    private static final long PRESIGNED_URL_EXPIRE_SECONDS = 60 * 5;

    private final S3Presigner s3Presigner;

    public String execute(PutObjectRequest request) {
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(PRESIGNED_URL_EXPIRE_SECONDS))
                .putObjectRequest(request)
                .build();
        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }
}
