package com.example.demo;

import com.example.demo.SpringbootAopApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootAopApplication.class)
@ActiveProfiles(profiles = "test")
public class BaseTest {

}
