package com.car.rental.demo.Cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        Dotenv dotenv = Dotenv.load();
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME"),
                "api_key", dotenv.get("CLOUDINARY_API_KEY"),
                "api_secret", dotenv.get("CLOUDINARY_API_SECRET")));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", file.getOriginalFilename(),
                        "format", "jpg"));
        return uploadResult.get("url").toString();
    }
}
