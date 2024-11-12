package com.car.rental.demo.Cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dyawf0vyu",
            "api_key", "616421482787377",
            "api_secret", "-KrOSBUStjj0Aav1y4Nalhg3rGo"));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
            ObjectUtils.asMap(
                "public_id", file.getOriginalFilename(),
                "format", "jpg"
            ));
        return uploadResult.get("url").toString();
    }
}
