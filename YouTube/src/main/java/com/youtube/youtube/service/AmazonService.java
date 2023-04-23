package com.youtube.youtube.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.youtube.youtube.model.DTOs.VideoInfoDTO;
import com.youtube.youtube.model.entities.Category;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

import com.youtube.youtube.AmazonConfig;

@Service
public class AmazonService extends AbstractService{
    @Autowired
    public AmazonS3 amazonS3;
    @Value("${aws.s3.bucketname}")
    private String awsBucketName;
    private static String[] allowedVideoFormats = {"video/mp4", "video/mpeg", "video/webm",
            "video/quicktime", "video/x-msvideo", "video/x-flv", "video/3gpp"};

    public VideoInfoDTO uploadFile(MultipartFile file, String name, String description,
                             int visibilityId, int categoryId, int userId) {
        try {
            validUploadData(file, name, description);
            Visibility visibility=findVisibility(visibilityId);
            Category category= findCategory(categoryId);
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "."+ext;
            File convertedFile = convertMultiPartFileToFile(file);
            String url =File.separator+awsBucketName+File.separator+".s3.amazonaws.com"
                        +File.separator+ fileName;
            User user = getUserById(userId);
            Video video=new Video();
            video.setName(name);
            video.setDescription(description);
            video.setVisibility(visibility);
            video.setCategory(category);
            video.setUser(user);
            video.setDateCreated(LocalDateTime.now());
            video.setVideoUrl(url);
            videoRepository.save(video);
            uploadToS3(fileName, convertedFile);
            convertedFile.delete();
            return mapper.map(video, VideoInfoDTO.class);
        } catch (IOException ex ) {
            throw new RuntimeException("There was a problem uploading the file");
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private void uploadToS3(String fileName, File file) {
        PutObjectRequest putRequest = new PutObjectRequest(awsBucketName, fileName, file);
        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putRequest);
    }

    private void validUploadData(MultipartFile file, String name, String description){
        if (file.isEmpty()) {
            throw new BadRequestException("The file is not attached. Please try again.");
        }
        if (!Arrays.asList(allowedVideoFormats).contains(file.getContentType())) {
            throw new BadRequestException("Invalid file format. Only video files are allowed.");
        }
        if (file.getSize() > MAX_VIDEO_SIZE) {
            throw new BadRequestException("File is too large. Maximum file size is 256 GB.");
        }
        if(name.length()> MAX_TITLE_LENGTH){
            throw new BadRequestException(TITLE_TOO_LONG);
        }
        if(description.length()> MAX_DESCRIPTION_LENGTH){
            throw new BadRequestException(DESCRIPTION_TOO_LONG);
        }
    }

    public S3ObjectInputStream download(String url) {
        String fileName = url.substring(url.lastIndexOf("/")+1);
        if (!amazonS3.doesObjectExist(awsBucketName, fileName)) {
            throw new NotFoundException(NO_SUCH_VIDEO);
        }
        S3Object s3Object = amazonS3.getObject(awsBucketName, fileName);
        return s3Object.getObjectContent();
    }

    public void deleteVideo(int userId, int videoId) {
        Video video=findVideoById(videoId);
        checkVideoOwner(video,userId);
        String videoName = getFileName(video.getVideoUrl());
        amazonS3.deleteObject(awsBucketName, videoName);
        videoRepository.delete(video);
    }


    private String getFileName(String url){
        String fileName = url.substring(url.lastIndexOf(File.separator)+1);
        System.out.println(fileName);
        if (!amazonS3.doesObjectExist(awsBucketName, fileName)) {
            throw new NotFoundException(NO_SUCH_VIDEO);
        }
        return fileName;
    }
}
