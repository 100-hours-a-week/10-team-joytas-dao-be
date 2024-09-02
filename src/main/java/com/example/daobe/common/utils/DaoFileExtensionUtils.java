package com.example.daobe.common.utils;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class DaoFileExtensionUtils {

    private static final List<String> allowedExtensions = Arrays.asList("jpeg", "jpg", "png", "webp");

    // 파일 확장자 추출
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    // 파일 확장자 검증
    public static boolean isValidFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName != null) {
            String fileExtension = getFileExtension(fileName);
            return allowedExtensions.contains(fileExtension.toLowerCase());
        }
        return false;
    }
}
