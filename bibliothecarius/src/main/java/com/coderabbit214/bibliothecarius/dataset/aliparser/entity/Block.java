package com.coderabbit214.bibliothecarius.dataset.aliparser.entity;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pos",
    "styleId",
    "text"
})
public class Block {

    @JsonProperty("pos")
    private List<Pos> pos;
    @JsonProperty("styleId")
    private Integer styleId;
    @JsonProperty("text")
    private String text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("pos")
    public List<Pos> getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(List<Pos> pos) {
        this.pos = pos;
    }

    @JsonProperty("styleId")
    public Integer getStyleId() {
        return styleId;
    }

    @JsonProperty("styleId")
    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}