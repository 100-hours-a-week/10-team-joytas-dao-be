package com.example.daobe.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws.s3")
public record S3Properties(
        int cacheTimeSeconds,
        String bucketName,
        String accessKey,
        String secretKey,
        String imageUrl,
        String region
) {
}
