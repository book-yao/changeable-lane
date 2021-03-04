package com.supcon.changeablelane.client.dto.os;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.List;

/**
 * @author jiangfei
 * @date 2020/2/21
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableLaneStateDTO {

    private List<VariableLaneState> states;

    @JsonGetter("states")
    public List<VariableLaneState> getStates() {
        return states;
    }

    @JsonSetter("vlc_state")
    public void setStates(List<VariableLaneState> states) {
        this.states = states;
    }
}
