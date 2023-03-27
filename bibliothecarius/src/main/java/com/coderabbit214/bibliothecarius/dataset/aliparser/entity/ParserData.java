package com.coderabbit214.bibliothecarius.dataset.aliparser.entity;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "layouts"
})
public class ParserData {

    @JsonProperty("layouts")
    private List<Layout> layouts;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("layouts")
    public List<Layout> getLayouts() {
        return layouts;
    }

    @JsonProperty("layouts")
    public void setLayouts(List<Layout> layouts) {
        this.layouts = layouts;
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