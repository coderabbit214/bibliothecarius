package com.coderabbit214.bibliothecarius.qdrant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: Mr_J
 * @Date: 2023/3/19 21:54
 */
@Component
public class QdrantConfig implements InitializingBean {

    @Value("${qdrant.host}")
    private String host;

    public static String HOST;

    @Override
    public void afterPropertiesSet() throws Exception {
        HOST = this.host;
    }
}
