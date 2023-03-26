package com.coderabbit214.bibliothecarius.dataset.document;

import com.coderabbit214.bibliothecarius.common.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DocumentQuery extends PageParam {
    private String documentName;
}
