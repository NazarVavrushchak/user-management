package com.nazar.usermanagement.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

import static com.google.api.client.util.Preconditions.checkNotNull;

public class FileValidator {

    private static final String[] ALLOWED_FILE_TYPES = {"pdf" , "txt" , "docx"};

    private static final String[] ALLOWED_PHOTO_TYPES = {"jpg"};

    private static final long MAX_SIZE_IN_BYTES = 5 * 1024 * 1024;

    //check what type  of file
    public static String getFileExtension (String fullName){
        checkNotNull(fullName);
        String fileName = new File(fullName).getName();
        int doIndex = fileName.lastIndexOf('.');
        return (doIndex == -1) ? "" : fileName.substring(doIndex + 1);
    }

    public static boolean isValidFileType(MultipartFile file){
        String fileExtension  = getFileExtension(file.getOriginalFilename());
        return Arrays.asList(ALLOWED_FILE_TYPES).contains(fileExtension);
    }

    public static boolean isValidPhotoType(MultipartFile file){
        String fileExtension = getFileExtension(file.getOriginalFilename());
        return Arrays.asList(ALLOWED_PHOTO_TYPES).contains(fileExtension);
    }

    public static boolean isValidFileSize(MultipartFile file) {
        return file.getSize() <= MAX_SIZE_IN_BYTES;
    }
}