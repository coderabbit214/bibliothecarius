package com.coderabbit214.bibliothecarius.dataset.aliparser.entity;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pos",
    "yec",
    "xec",
    "ysc",
    "xsc",
    "cellUniqueId",
    "type",
    "alignment",
    "cellId",
    "layouts",
    "pageNum"
})
public class Cell {

    @JsonProperty("pos")
    private List<List<Integer>> pos;
    @JsonProperty("yec")
    private Integer yec;
    @JsonProperty("xec")
    private Integer xec;
    @JsonProperty("ysc")
    private Integer ysc;
    @JsonProperty("xsc")
    private Integer xsc;
    @JsonProperty("cellUniqueId")
    private Object cellUniqueId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("alignment")
    private String alignment;
    @JsonProperty("cellId")
    private Integer cellId;
    @JsonProperty("layouts")
    private List<Layout> layouts;
    @JsonProperty("pageNum")
    private List<Integer> pageNum;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("pos")
    public List<List<Integer>> getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(List<List<Integer>> pos) {
        this.pos = pos;
    }

    @JsonProperty("yec")
    public Integer getYec() {
        return yec;
    }

    @JsonProperty("yec")
    public void setYec(Integer yec) {
        this.yec = yec;
    }

    @JsonProperty("xec")
    public Integer getXec() {
        return xec;
    }

    @JsonProperty("xec")
    public void setXec(Integer xec) {
        this.xec = xec;
    }

    @JsonProperty("ysc")
    public Integer getYsc() {
        return ysc;
    }

    @JsonProperty("ysc")
    public void setYsc(Integer ysc) {
        this.ysc = ysc;
    }

    @JsonProperty("xsc")
    public Integer getXsc() {
        return xsc;
    }

    @JsonProperty("xsc")
    public void setXsc(Integer xsc) {
        this.xsc = xsc;
    }

    @JsonProperty("cellUniqueId")
    public Object getCellUniqueId() {
        return cellUniqueId;
    }

    @JsonProperty("cellUniqueId")
    public void setCellUniqueId(Object cellUniqueId) {
        this.cellUniqueId = cellUniqueId;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("alignment")
    public String getAlignment() {
        return alignment;
    }

    @JsonProperty("alignment")
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    @JsonProperty("cellId")
    public Integer getCellId() {
        return cellId;
    }

    @JsonProperty("cellId")
    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    @JsonProperty("layouts")
    public List<Layout> getLayouts() {
        return layouts;
    }

    @JsonProperty("layouts")
    public void setLayouts(List<Layout> layouts) {
        this.layouts = layouts;
    }

    @JsonProperty("pageNum")
    public List<Integer> getPageNum() {
        return pageNum;
    }

    @JsonProperty("pageNum")
    public void setPageNum(List<Integer> pageNum) {
        this.pageNum = pageNum;
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
