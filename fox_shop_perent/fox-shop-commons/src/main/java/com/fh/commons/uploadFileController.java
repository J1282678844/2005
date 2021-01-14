package com.fh.commons;

import com.fh.commons.oss.OssUtil;
import com.fh.commons.result.CommonsReturn;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
/**
 * @ClassName uploadFileController
 * @Deacription TODO
 * @Author 阳
 * @Date 2020/12/3 11:56
 * @Version 1.0
 **/
@RestController
@RequestMapping("/uploadPhoto")
public class uploadFileController {

    @PostMapping
    public CommonsReturn uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = OssUtil.uploadFile("user", file);
        return CommonsReturn.success(url);
    }
}
