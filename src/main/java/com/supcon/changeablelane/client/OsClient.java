package com.supcon.changeablelane.client;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.supcon.changeablelane.client.dto.OSResponseDTO;
import com.supcon.changeablelane.client.dto.VariableLaneStatesDTO;
import com.supcon.changeablelane.client.dto.os.VariableLaneState;
import com.supcon.changeablelane.client.dto.os.VariableLaneStates;
import com.supcon.changeablelane.client.dto.os.VariableLaneStateDTO;
import com.supcon.changeablelane.dto.OSRequestDTO;
import com.supcon.changeablelane.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author caowenbo
 * @create 2021/3/4 13:34
 */
@Component
@Slf4j
public class OsClient {


    /** 信号os请求头(包含token) */
    private static HttpHeaders requestHeaders = new HttpHeaders();

    @Autowired
    private RestTemplate restTemplate;

    @Value("#{${param.acsId-intersectionId}}")
    private Map<Integer,Integer> acsIdIntersectionIdMap;

    /** 信号os的BASE URL地址 */
    @Value("${url.IntellificOS.changeable}")
    private String URL_INTELLIFIC_OS_CHANGEABLE;

    /** 可变车道状态 */
    private static final String URL_VARIABLELANE_STATE = "/intellivld/get_variable_lane_state";


    public VariableLaneStatesDTO variableLaneStates(Integer acsId) {
        VariableLaneStates variableLaneStates = new VariableLaneStates();
        try {
            OSRequestDTO<JSONObject> requestData = new OSRequestDTO<>();
            JSONObject data = new JSONObject();
            data.put("acs_id", acsId);
            data.put("inter_id", acsIdIntersectionIdMap.get(acsId));
            requestData.setData(data);
            String resultStr =
                    restTemplate
                            .exchange(
                                    URL_INTELLIFIC_OS_CHANGEABLE + URL_VARIABLELANE_STATE,
                                    HttpMethod.POST,
                                    new HttpEntity<>(requestData, requestHeaders),
                                    String.class)
                            .getBody();
            Optional<OSResponseDTO<VariableLaneStateDTO>> optional =
                    JsonUtil.fromJson(resultStr, new TypeReference<OSResponseDTO<VariableLaneStateDTO>>() {});
            if (optional.isPresent() && optional.get().getData() != null) {
                VariableLaneStateDTO states = optional.get().getData();
                List<VariableLaneState> stateList = states.getStates();
                if (!CollectionUtils.isEmpty(stateList)) {
                    stateList.forEach(
                            state -> {
                                state.setAcsId(acsId);
                            });
                    variableLaneStates.setLaneStates(stateList);
                }
            }
        } catch (Exception e) {
            variableLaneStates = null;
            log.error("get variableLaneStates failed, cause: {}", e.getLocalizedMessage());
        }
        if(Objects.nonNull(variableLaneStates)){
            List<com.supcon.changeablelane.client.dto.VariableLaneStateDTO> collect = variableLaneStates.getLaneStates().stream()
                    .map(item -> item.transform2VariableLaneStateDTO())
                    .collect(Collectors.toList());
            VariableLaneStatesDTO variableLaneStatesDTO = new VariableLaneStatesDTO();
            variableLaneStatesDTO.setLaneStates(collect);
            return variableLaneStatesDTO;
        }
        return null;
    }

//    public VariableLaneStatesDTO variableLaneStates(Integer acsId) {
//        VariableLaneStatesDTO variableLaneStates = new VariableLaneStatesDTO();
//        try {
//            OSRequestDTO<JSONObject> requestData = new OSRequestDTO<>();
//            JSONObject data = new JSONObject();
//            data.put("acs_id", acsId);
//            data.put("inter_id", acsIdIntersectionIdMap.get(acsId));
//            requestData.setData(data);
//            String resultStr =
//                    restTemplate
//                            .exchange(
//                                    URL_INTELLIFIC_OS_CHANGEABLE + URL_VARIABLELANE_STATE,
//                                    HttpMethod.POST,
//                                    new HttpEntity<>(requestData, requestHeaders),
//                                    String.class)
//                            .getBody();
//            Optional<OSResponseDTO<VariableLaneStatesDTO>> optional =
//                    JsonUtil.fromJson(resultStr, new TypeReference<OSResponseDTO<VariableLaneStatesDTO>>() {});
//            if (optional.isPresent() && optional.get().getData() != null) {
//                VariableLaneStatesDTO states = optional.get().getData();
//                List<VariableLaneStateDTO> stateList = states.getLaneStates();
//                if (!CollectionUtils.isEmpty(stateList)) {
//                    stateList.forEach(
//                            state -> {
//                                state.setAcsId(acsId);
//                            });
//                    variableLaneStates.setLaneStates(stateList);
//                }
//            }
//        } catch (Exception e) {
//            log.error("get variableLaneStates failed, cause: {}", e.getLocalizedMessage());
//            return null;
//        }
//        return variableLaneStates;
//    }
}
