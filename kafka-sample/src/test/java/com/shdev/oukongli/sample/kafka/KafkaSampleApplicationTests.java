package com.shdev.oukongli.sample.kafka;

import com.shdev.oukongli.sample.kafka.service.KafkaSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaSampleApplicationTests {

    @Autowired
    private KafkaSender kafkaSender;

    @Test
    public void contextLoads() {
    }

    @Test
    public void send() {
//        for (int i = 0; i < 3; i++) {
//            kafkaSender.send();
//        }

        TestThread testThread = new TestThread();
        testThread.start();
        try {
            testThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("3333333");
        }

        System.out.println("2222222");
    }


    private static class TestThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("111111111");
        }
    }

}
