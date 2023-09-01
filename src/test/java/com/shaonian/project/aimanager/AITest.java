package com.shaonian.project.aimanager;


import cn.hutool.core.util.IdUtil;
import com.shaonian.project.config.XFAIConfig;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import com.unfbx.sparkdesk.entity.Text;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest
public class AITest {

    @Resource
    private XFAIConfig xfaiConfig;

    @Test
    void test1(){
        String uuid = "1696505991793123330";
//        String content = "我想让你充当 Linux 终端。我将输入命令，您将回复终端应显示的内容。我希望您只在一个唯一的代码块内回复终端输出，而不是其他任何内容。不要写解释。除非我指示您这样做，否则不要键入命令。当我需要用英语告诉你一些事情时，我会把文字放在中括号内[就像这样]。";
        String content = "我想让你担任Java开发工程师面试官。我将成为候选人，您将向我询问Java开发工程师职位的面试问题。我希望你只作为面试官回答。不要一次写出所有的问题。我希望你只对我进行采访。问我问题，等待我的回答。不要写解释。像面试官一样一个一个问我，等我回答。我的第一句话是“面试官你好”\\n";
        List<Text> textList = new ArrayList<>();

    }



    @Test
    void test2(){
        //创建流式输出客户端
        OpenAiStreamClient client = OpenAiStreamClient.builder()
                .apiKey(Arrays.asList("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"))
                .build();
        //聊天
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Message message = Message.builder().role(Message.Role.USER).content("你好！").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        client.streamChatCompletion(chatCompletion, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    void test3(){
        long snowflakeNextId = IdUtil.getSnowflakeNextId();
        System.out.println(snowflakeNextId);
    }

}

