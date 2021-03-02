package com.supcon.changeablelane.client;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.client.dto.ResponseDTO;
import com.supcon.changeablelane.client.dto.TimingSchemesDTO;
import com.supcon.changeablelane.domain.TrafficScreen;
import com.supcon.changeablelane.domain.TrafficScreenScheme;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author caowenbo
 * @create 2021/2/25 19:14
 */
@Component
@Slf4j
public class TrafficScreenClient {

    @Value("${url.trafficScreenUrl}")
    private String trafficScreenUrl;

    /**
     * 单点方案url
     */
    private static final String GET_LED = "/led/getLedMessage";

    /**
     * 单点方案url
     */
    private static final String SEND_MESSAGE = "/jtjc_web/ledMode/sendLedMessage";

    @Autowired
    private RestTemplate restTemplate;


    public List<TrafficScreen> getLedMessage(){
        final String url = trafficScreenUrl + GET_LED;
        try {

            ResponseDTO<List<TrafficScreen>> body = restTemplate
                    .exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<ResponseDTO<List<TrafficScreen>>>() {
                            })
                    .getBody();
            if(body.getCode()==0){
                return body.getMessage();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("获取诱导屏列表失败 / cause:{}", ExceptionUtils.getStackTrace(e));
            return new ArrayList<>();
        }
    };

    public boolean sendLedMessage(TrafficScreenScheme trafficScreenScheme){
        final String url = trafficScreenUrl + SEND_MESSAGE;
        try {
            restTemplate.postForLocation(url, trafficScreenScheme);
            log.info(
                    "诱导屏 / {} ,进口 {}| 下发控制信息成功,控制信息为:{}",
                    trafficScreenScheme.getAcsId(),
                    trafficScreenScheme.getEntranceId(),
                    JSONObject.toJSONString(trafficScreenScheme));
            //      }
            return true;
        } catch (Exception e) {
            log.error(
                    "诱导屏 / {} ,进口 {}| 下发控制信息失败,cause:{}",
                    trafficScreenScheme.getAcsId(),
                    trafficScreenScheme.getEntranceId(),
                    ExceptionUtils.getStackTrace(e));
            return false;
        }
    };


}
