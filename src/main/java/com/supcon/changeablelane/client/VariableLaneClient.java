package com.supcon.changeablelane.client;

import com.alibaba.fastjson.JSONObject;
import com.supcon.changeablelane.client.dto.TimingSchemesDTO;
import com.supcon.changeablelane.client.dto.VariableLaneStatesDTO;
import com.supcon.changeablelane.dto.OutputSchemesDTO;
import com.supcon.changeablelane.dto.VariableLaneControl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
public class VariableLaneClient {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 数据交换url
     */
    @Value("${url.dataExchangeUrl}")
    private String exchangeUrl;

    /**
     * 可变车道状态接口
     */
    private static final String VARIABLE_LANE_STATE = "/variableLane/{acsId}";

    /**
     * 可变车道控制下发
     */
    private static final String VARIABLE_LANE_CONTROL_DOWN = "/output/variableLane";

    /**
     * 可变车道定时方案
     */
    private static final String VARIABLE_TIMING_SCHEME = "/timingScheme/{acsId}";

    /**
     * 下发方案接口
     */
    private static String LASTEST_OUTPUT_URL = "/output/latest/schemes";


    /**
     * 获取指定信号机定时方案
     *
     * @param acsId
     * @return
     */
    public Optional<TimingSchemesDTO> getTimingSchemeByAcsId(Integer acsId) {
        final String url = exchangeUrl + VARIABLE_TIMING_SCHEME;
        try {
            TimingSchemesDTO timingSchemes =
                    restTemplate
                            .exchange(
                                    url,
                                    HttpMethod.GET,
                                    null,
                                    new ParameterizedTypeReference<TimingSchemesDTO>() {
                                    },
                                    acsId)
                            .getBody();
            return Optional.ofNullable(timingSchemes);
        } catch (Exception e) {
            log.error("可变车道 / {} | 获取可变车道定时方案失败,cause:{}", acsId, ExceptionUtils.getStackTrace(e));
            return Optional.empty();
        }
    }

    /**
     * 获取可变车道状态信息
     *
     * @param acsId
     * @return
     */
    public Optional<VariableLaneStatesDTO> getVariableLaneState(Integer acsId) {
        String url = exchangeUrl + VARIABLE_LANE_STATE;
        try {
            VariableLaneStatesDTO body =
                    restTemplate
                            .exchange(
                                    url,
                                    HttpMethod.GET,
                                    null,
                                    new ParameterizedTypeReference<VariableLaneStatesDTO>() {
                                    },
                                    acsId)
                            .getBody();
            return Optional.ofNullable(body);
        } catch (Exception e) {
            log.error("获取可变车道状态信息失败,cause:{}", ExceptionUtils.getStackTrace(e));
            return Optional.empty();
        }
    }


    /**
     * 下发可变车道控制信息
     *
     * @param variableLaneControl 控制信息
     * @return true: 下发成功；false: 下发失败
     */
    public boolean variableLaneControlDown(VariableLaneControl variableLaneControl) {
        String url = exchangeUrl + VARIABLE_LANE_CONTROL_DOWN;
        try {
            //暂时不下发方案
            restTemplate.postForLocation(url, variableLaneControl);
            //      if (log.isDebugEnabled()) {
            log.info(
                    "可变车道 / {} | 下发控制信息成功,控制信息为:{}",
                    variableLaneControl.getAcsId(),
                    JSONObject.toJSONString(variableLaneControl));
            //      }
            return true;
        } catch (Exception e) {
            log.error(
                    "可变车道 / {} | 下发控制信息失败,cause:{}",
                    variableLaneControl.getAcsId(),
                    ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 下发输出方案
     *
     * @param outputSchemesDTO
     */
    public Optional<Integer> downOutput(OutputSchemesDTO outputSchemesDTO) {
        String url = exchangeUrl + LASTEST_OUTPUT_URL;
        try {
            //todo 暂时不下发
            restTemplate.postForLocation(url, outputSchemesDTO);
            //      if (log.isDebugEnabled()) {
            log.info(
                    "可变车道 / {} | 下发信号机方案成功 | 方案：{}",
                    outputSchemesDTO.obtainAcsId(),
                    JSONObject.toJSONString(outputSchemesDTO));
            //      }
            return Optional.of(1);
        } catch (Exception e) {
            log.error(
                    "可变车道 / {} | 下发信号机方案失败 | cause:{}",
                    outputSchemesDTO.obtainAcsId(),
                    ExceptionUtils.getStackTrace(e));
            return Optional.empty();
        }
    }

}
