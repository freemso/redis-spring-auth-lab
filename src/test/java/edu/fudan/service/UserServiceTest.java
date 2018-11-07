package edu.fudan.service;

import edu.fudan.domain.TokenEntry;
import edu.fudan.domain.User;
import edu.fudan.model.UserService;
import edu.fudan.repository.TokenRepository;
import edu.fudan.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Before
    public void setup() {
        User user = new User(1L, "user1", "1234", "stu@fudan.edu.cn");
        userRepository.save(user);
    }

    @After
    public void clean() {
        userRepository.deleteById(1L);
    }

    @Test
    public void testLogin() {
        //stu createToken
        //wrong email
        loginFailAssertion("stu@sjtu.edu.cn", "1234");
        //wrong password
        loginFailAssertion("stu@fudan.edu.cn", "123");
        //wrong email and password
        loginFailAssertion("stu@sjtu.edu.cn", "2");
        //succeed
        String auth = userService.createToken("stu@fudan.edu.cn", "1234");
//        assertNotNull(userService.createToken("stu@fudan.edu.cn", "1234"));
        // Every time we create a token, the old token related to that user is deleted
        assertNotNull(auth);

        // The token in AuthenticationResp is actually the combination of token and id
        // so we only need to pass it to getAuthentication() to get the tokenEntry
        TokenEntry tokenEntry = tokenRepository.getToken(auth);
        assertTrue(tokenRepository.checkToken(tokenEntry));

        // Test token override
        // create a token again
        assertNotNull(userService.createToken("stu@fudan.edu.cn", "1234"));
        TokenEntry oldTokenEntry = tokenRepository.getToken(auth);
        // old token will be invalid
        assertFalse(tokenRepository.checkToken(oldTokenEntry));
    }

    @Test
    public void testLogout() {
        //createToken
        String auth = userService.createToken("stu@fudan.edu.cn", "1234");
        assertNotNull(auth);

        TokenEntry tokenEntry = tokenRepository.getToken(auth);
        assertTrue(tokenRepository.checkToken(tokenEntry));

        //deleteToken
        userService.deleteToken(1);
        TokenEntry oldTokenEntry = tokenRepository.getToken(auth);
        assertFalse(tokenRepository.checkToken(oldTokenEntry));
    }

    @Test
    public void testRegister() {

        //createUser failure caused by duplicate email
        try {
            userService.createUser("stu@fudan.edu.cn", "x", "12");
            fail();
        } catch(RuntimeException ignore){
            // ignore it
        }

        //createUser successfully
        User resp = userService.createUser("stu2@fudan.edu.cn", "x", "12");
        assertNotNull(resp);
        User user = userRepository.findByEmail("stu2@fudan.edu.cn").orElse(null);
        assertNotNull(user);
        assertEquals(user.getPassword(), "12");
        assertEquals(user.getEmail(), "stu2@fudan.edu.cn");
        assertEquals(user.getName(), "x");

    }

    @Test
    public  void testIsExisted(){
        assertFalse(userRepository.findByEmail("").isPresent());
        assertFalse(userRepository.findByEmail("stu2@fudan.edu.cn").isPresent());
        assertTrue(userRepository.findByEmail("stu@fudan.edu.cn").isPresent());
    }

    private void loginFailAssertion(String email, String password) {
        try {
            userService.createToken(email, password);
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }



}
