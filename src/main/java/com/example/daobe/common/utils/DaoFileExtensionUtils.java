package com.example.daobe.common.utils;

import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_IMAGE_EXTENSIONS;

import com.example.daobe.objet.exception.ObjetException;
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
    public static void validateFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName != null) {
            String fileExtension = getFileExtension(fileName);

            if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                throw new ObjetException(INVALID_OBJET_IMAGE_EXTENSIONS);
            }
        }
    }
}
