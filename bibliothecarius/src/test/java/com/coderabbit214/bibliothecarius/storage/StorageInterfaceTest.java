package com.coderabbit214.bibliothecarius.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class StorageInterfaceTest {

    @Autowired
    private StorageFactory storageFactory;

    @Test
    void getExpiration() {
        StorageInterface ossService = storageFactory.getOssService();
        String expiration = ossService.getExpiration("动物庄园.txt");
        System.out.println(expiration);
    }
}