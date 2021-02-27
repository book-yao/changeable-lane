package com.supcon.changeablelane.client;


import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.client.dto.AcsConfigInfoDTO;
import com.supcon.changeablelane.client.dto.RunningScheme;
import com.supcon.changeablelane.domain.scheme.AcsOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 交通数据客户端
 *
 * @author JiangWangfa
 * @date 2018/9/18
 */
@Component
@Slf4j
public class DataTrafficClient {

    @Value("${url.dataManagementUrl}")
    private String dataManageUrl;

    /**
     * 单点方案url
     */
    private static final String SINGLESCHEMEURL = "/acs/{acsId}/single-scheme";

    /**
     * 获取信号机当前信息接口
     */
    private static final String LAST_RUNNING_SCHEMES_URL =
            "/running-schemes/last?acsId={acsId}&startTime={startTime}&endTime={endTime}";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @description: 获取指定ACS的单点方案
     * @param: [acsId]
     * @return: java.util.Optional<com.supconit.suptap.algorithm.adaptive.dto.acsconfig.AcsConfigInfoDTO>
     * @author: JiangWangfa
     * @date: 2018/9/18 16:14
     */
    public Optional<AcsConfigInfoDTO> getAcsSingleScheme(Integer acsId) {
        try {
            String url = dataManageUrl + SINGLESCHEMEURL;
            AcsConfigInfoDTO acsConfig = restTemplate.getForObject(url, AcsConfigInfoDTO.class, acsId);
            return Optional.ofNullable(acsConfig);
        } catch (Exception e) {
            log.error("获取单点参数失败.param:acsId:{},error:{}", acsId, ExceptionUtils.getStackTrace(e));
            return Optional.empty();
        }
    }

    /**
     * 获取匝道状态数据
     *
     * @param acsId
     * @return
     */
    public AcsOutput getLastRunningScheme(Integer acsId) {
        String url = dataManageUrl + LAST_RUNNING_SCHEMES_URL;
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMinutes(30);
        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try {
            RunningScheme body = restTemplate
                    .exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<RunningScheme>() {
                            }, acsId, startTimeStr, endTimeStr).getBody();
            log.info(
                    "信号机 {} | 获取信号机最近一条运行记录 | 开始时间 {} 结束时间 {} url {} | 数据 {}",
                    acsId,
                    startTimeStr,
                    endTimeStr,
                    url,
                    JSONObject.toJSONString(body));
            return body.runningScheme2AcsOutput();
        } catch (Exception e) {
            log.error(
                    "信号机 {} 开始时间 {} 结束时间 {}| 获取信号机最近一条运行记录错误 | 错误信息:{}",
                    acsId,
                    startTimeStr,
                    endTimeStr,
                    ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
}
