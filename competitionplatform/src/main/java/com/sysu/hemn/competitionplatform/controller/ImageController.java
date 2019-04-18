package com.sysu.hemn.competitionplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.hemn.competitionplatform.utils.EncryptUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@EnableAutoConfiguration
@RequestMapping("/competitionplatform/api/image")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {

    private static String IMG_PATH = "D:\\images";

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public String upload(MultipartFile file, HttpServletRequest request){
        JSONObject result = new JSONObject();
        File dir = new File(IMG_PATH);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 后缀名
        String filePath = IMG_PATH + "\\";
        // 上传后的路径
        fileName = UUID.randomUUID() + suffixName;
        // 新文件名
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            result.put("code", -1);
            return result.toJSONString();
        }
        result.put("code", 1);
        result.put("url", "/images/"+fileName);
        return result.toJSONString();
    }
}
