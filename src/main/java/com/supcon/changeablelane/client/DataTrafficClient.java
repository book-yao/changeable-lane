package com.supcon.changeablelane.client;


import com.supcon.changeablelane.client.dto.AcsConfigInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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
}
