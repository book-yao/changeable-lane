package com.supcon.changeablelane.service;

import com.supcon.changeablelane.client.OsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @Author caowenbo
 * @create 2021/2/24 14:33
 */
@Service
@Slf4j
public class OsService {
    private String token;
    @Autowired
    private OsClient osClient;
    @PostConstruct
    public void osLogin() {
        token = osClient.osLogin();
        if (StringUtils.isEmpty(token)) {
            log.info("信号os登陆失败");
            return;
        }

    }

    public void refreshToken() {
        token = osClient.refreshToken();
    }
}
