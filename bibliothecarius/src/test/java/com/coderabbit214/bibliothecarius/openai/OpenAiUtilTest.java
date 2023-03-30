package com.coderabbit214.bibliothecarius.openai;

import com.coderabbit214.bibliothecarius.common.utils.TokenUtil;
import org.junit.jupiter.api.Test;

class OpenAiUtilTest {

    @Test
    void getTokens() {
        Integer tokens = TokenUtil.getTokens("Your input contained one or more unicode characters that map to multiple tokens. The output visualization may display the bytes in each token in a non-standard way.");
        System.out.println(tokens);
    }
}