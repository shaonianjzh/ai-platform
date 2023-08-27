package com.shaonian.project.aimanager;


import com.shaonian.project.aimanager.xunfei.XFAI;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class AITest {

    @Resource
    private XFAI xfai;

    @Test
    void test1(){
        xfai.doChat();
    }

}

