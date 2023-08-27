package com.shaonian.project.service;

import com.shaonian.project.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author 少年
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        boolean result = userService.updateById(user);
        Assertions.assertTrue(result);
    }

    @Test
    void testDeleteUser() {
        boolean result = userService.removeById(1L);
        Assertions.assertTrue(result);
    }

    @Test
    void testGetUser() {
        User user = userService.getById(1L);
        Assertions.assertNotNull(user);
    }

    @Test
    void userRegister() {
        String userAccount = "shaonian";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String email = "iamshaonian@gmail.com";
        String code = "Doxlxm";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword,email,code);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }

    @Test
    void sendCode(){
        userService.sendCode("iamshaonian@gmail.com");
    }
}