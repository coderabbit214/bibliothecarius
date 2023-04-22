package com.coderabbit214.bibliothecarius.document.aliparser.entity;


import com.fasterxml.jackson.annotation.*;

import javax.annotation.processing.Generated;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "firstLinesChars",
        "pos",
        "blocks",
        "index",
        "subType",
        "lineHeight",
        "text",
        "alignment",
        "type",
        "pageNum",
        "uniqueId",
        "numCol",
        "cells",
        "numRow"
})
@Generated("jsonschema2pojo")
public class Layout {

    @JsonProperty("firstLinesChars")
    private Integer firstLinesChars;
    @JsonProperty("pos")
    private List<Pos> pos;
    @JsonProperty("blocks")
    private List<Block> blocks;
    @JsonProperty("index")
    private Integer index;
    @JsonProperty("subType")
    private String subType;
    @JsonProperty("lineHeight")
    private Integer lineHeight;
    @JsonProperty("text")
    private String text;
    @JsonProperty("alignment")
    private String alignment;
    @JsonProperty("type")
    private String type;
    @JsonProperty("pageNum")
    private List<Integer> pageNum;
    @JsonProperty("uniqueId")
    private String uniqueId;
    @JsonProperty("numCol")
    private Integer numCol;
    @JsonProperty("cells")
    private List<Cell> cells;
    @JsonProperty("numRow")
    private Integer numRow;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("firstLinesChars")
    public Integer getFirstLinesChars() {
        return firstLinesChars;
    }

    @JsonProperty("firstLinesChars")
    public void setFirstLinesChars(Integer firstLinesChars) {
        this.firstLinesChars = firstLinesChars;
    }

    @JsonProperty("pos")
    public List<Pos> getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(List<Pos> pos) {
        this.pos = pos;
    }

    @JsonProperty("blocks")
    public List<Block> getBlocks() {
        return blocks;
    }

    @JsonProperty("blocks")
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    @JsonProperty("index")
    public Integer getIndex() {
        return index;
    }

    @JsonProperty("index")
    public void setIndex(Integer index) {
        this.index = index;
    }

    @JsonProperty("subType")
    public String getSubType() {
        return subType;
    }

    @JsonProperty("subType")
    public void setSubType(String subType) {
        this.subType = subType;
    }

    @JsonProperty("lineHeight")
    public Integer getLineHeight() {
        return lineHeight;
    }

    @JsonProperty("lineHeight")
    public void setLineHeight(Integer lineHeight) {
        this.lineHeight = lineHeight;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("alignment")
    public String getAlignment() {
        return alignment;
    }

    @JsonProperty("alignment")
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("pageNum")
    public List<Integer> getPageNum() {
        return pageNum;
    }

    @JsonProperty("pageNum")
    public void setPageNum(List<Integer> pageNum) {
        this.pageNum = pageNum;
    }

    @JsonProperty("uniqueId")
    public String getUniqueId() {
        return uniqueId;
    }

    @JsonProperty("uniqueId")
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @JsonProperty("numCol")
    public Integer getNumCol() {
        return numCol;
    }

    @JsonProperty("numCol")
    public void setNumCol(Integer numCol) {
        this.numCol = numCol;
    }

    @JsonProperty("cells")
    public List<Cell> getCells() {
        return cells;
    }

    @JsonProperty("cells")
    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    @JsonProperty("numRow")
    public Integer getNumRow() {
        return numRow;
    }

    @JsonProperty("numRow")
    public void setNumRow(Integer numRow) {
        this.numRow = numRow;
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