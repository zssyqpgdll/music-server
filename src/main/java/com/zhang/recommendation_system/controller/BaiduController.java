package com.zhang.recommendation_system.controller;

import com.zhang.recommendation_system.service.FileService;
import com.zhang.recommendation_system.util.ServerResponse;
import com.zhang.recommendation_system.util.imagesearch.APPConfig;
import com.zhang.recommendation_system.util.imagesearch.AipImageSearch;
import com.zhang.recommendation_system.util.imagesearch.util.Base64Util;
import com.zhang.recommendation_system.util.imagesearch.util.Util;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@RequestMapping("/baidu")
@RestController
public class BaiduController {

    private static final Logger logger = LoggerFactory.getLogger(BaiduController.class);

    AipImageSearch aipImageSearch = new AipImageSearch(APPConfig.APP_ID, APPConfig.API_KEY, APPConfig.SECRET_KEY);

    @Autowired
    private FileService fileService;

    @PostMapping("/idRecognize")
    public ServerResponse idRecognize(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = fileService.storeFile(file);;
        String id_card_side = "front";
        HashMap<String, String> options = new HashMap<String, String>();
        String realFile = fileService.getFileStorePath() + "\\" + fileName;
        byte[] data = Util.readFileByBytes(realFile);
//        byte[] pic = getPic(realFile);
        String base64Content = Base64Util.encode(data);
        JSONObject result = aipImageSearch.idRecognize( base64Content, id_card_side, options);
//        Stringresult.get("");
        System.out.println(result);
        JSONObject words_result = result.getJSONObject("words_result");
        JSONObject idno = words_result.getJSONObject("公民身份号码");

        // 除了返回一个身份证号，还需要返回图片的地址
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/downloadFile/" )
                .path(fileName)
                .toUriString();
        HashMap<String, String> re = new HashMap<String, String>();
        re.put("fileDownloadUri",fileDownloadUri);
        re.put("words",idno.getString("words"));

        return ServerResponse.ofSuccess(re);
    }

    //获取图片
    public byte[] getPic(String picName) throws IOException {
        InputStream pngInStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(picName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        for (int readNum; (readNum = pngInStream.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();
        return bytes;
    }
}