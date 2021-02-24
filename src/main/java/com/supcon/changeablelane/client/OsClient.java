package com.supcon.changeablelane.client;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.dto.OSRequestDTO;
import com.supcon.changeablelane.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @Author caowenbo
 * @create 2021/2/24 14:22
 */
@Slf4j
@Service
public class OsClient {

    /** 信号os请求头(包含token) */
    private static HttpHeaders requestHeaders = new HttpHeaders();

    /** 刷新token的WEB URL端点 */
    private static final String URL_REFRESH_TOKEN = "/auth/refresh_token?user={user}";

    /** 信号os登陆账号 */
    @Value("${exchange-setting.os.user}")
    private String OS_USERNAME;
    /** 信号os登陆密码 */
    @Value("${exchange-setting.os.pass}")
    private String OS_PASSWORD;

    /** 登录方式 */
    @Value("${exchange-setting.loginType:body}")
    private String loginType;

    /** RESTful客户端对象 */
    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.IntellificOS.login}")
    private String URL_INTELLIFIC_USER_OS;

    /** 登陆信号OS的WEB URL端点 */
    private static final String URL_LOGIN = "/auth/login";

    /** 信号os登陆 */
    //  @HystrixCommand
    public String osLogin() {
        try {
            OSRequestDTO requestData = new OSRequestDTO(OS_USERNAME, OS_PASSWORD);
            HttpEntity httpEntity;
            if ("body".equals(loginType)) {
                httpEntity = new HttpEntity<>(requestData);
            } else if ("form-data".equals(loginType)) {
                // form-data方式登录 by JWF
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("user", requestData.getUser());
                map.add("pass", requestData.getPassword());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                httpEntity = new HttpEntity<>(map, headers);
            } else {
                throw new IllegalArgumentException(
                        "the argument of loginType[exchange-setting.loginType] is illegal");
            }

            String results =
                    restTemplate
                            .exchange(
                                    URL_INTELLIFIC_USER_OS + URL_LOGIN, HttpMethod.POST, httpEntity, String.class)
                            .getBody();
            Optional<JSONObject> resultsOption = JsonUtil.fromJson(results, JSONObject.class);
            if (resultsOption.isPresent()) {
                String token = resultsOption.get().get("token").toString();
                if (StringUtils.isEmpty(token)) {
                    log.info("login failed, cause: get token failed");
                } else {
                    requestHeaders.set("Authorization", "Bearer " + token);
                    return token;
                }
            }
        } catch (Exception e) {
            log.error("login failed, cause: {}", e.getLocalizedMessage());
        }
        return "";
    }

    /** 登陆后令牌有过期时间，需要刷新令牌，使其重新生效 */
    public String refreshToken() {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
            String results =
                    restTemplate
                            .exchange(
                                    URL_INTELLIFIC_USER_OS + URL_REFRESH_TOKEN,
                                    HttpMethod.GET,
                                    requestEntity,
                                    String.class,
                                    OS_USERNAME)
                            .getBody();
            Optional<JSONObject> resultsOption = JsonUtil.fromJson(results, JSONObject.class);
            if (resultsOption.isPresent()) {
                String token = resultsOption.get().get("token").toString();
                if (StringUtils.isEmpty(token)) {
                    log.info("login failed, cause: get token failed");
                    log.info("login result:{}", resultsOption.get());
                    return osLogin();
                } else {
                    log.debug("refreshToken success!");
                    requestHeaders.set("Authorization", "Bearer " + token);
                    return token;
                }
            }
        } catch (Exception e) {
            log.error("refreshToken failed, cause: {}", e.getLocalizedMessage());
        }
        return "";
    }
}
