package org.thesis.woodindustryecommerce.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    public String uploadFile(MultipartFile file){
        try{
            if(file != null){
                File uploadedFile = convertMultipartToFile(file);
                Map uploadResult = Singleton.getCloudinary().uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                return uploadResult.get("url").toString();
            }
            return "";
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fout = new FileOutputStream(convFile);
        fout.write(file.getBytes());
        fout.close();
        return convFile;
    }
}
