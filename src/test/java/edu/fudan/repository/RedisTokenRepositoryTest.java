package edu.fudan.repository;


import edu.fudan.domain.TokenEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class RedisTokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    public void testCreateToken() {
        TokenEntry tokenEntry = tokenRepository.createToken(1);
        assertThat(tokenEntry).isNotNull();
    }

    @Test
    public void testCheckToken() {
        TokenEntry tokenEntry = tokenRepository.createToken(1);
        TokenEntry tokenEntry_fake1 = new TokenEntry(1, "somefaketoken");
        TokenEntry tokenEntry_fake2 = new TokenEntry(2, "anotherfaketoken");
        TokenEntry tokenEntry_fake3 = new TokenEntry(1, null);
        TokenEntry tokenEntry_fake4 = new TokenEntry(1, "");

        assertThat(tokenRepository.checkToken(tokenEntry)).isTrue();

        assertThat(tokenRepository.checkToken(tokenEntry_fake1)).isFalse();
        assertThat(tokenRepository.checkToken(tokenEntry_fake2)).isFalse();
        assertThat(tokenRepository.checkToken(tokenEntry_fake3)).isFalse();
        assertThat(tokenRepository.checkToken(tokenEntry_fake4)).isFalse();
        assertThat(tokenRepository.checkToken(null)).isFalse();
    }

    @Test
    public void testGetTokenAndGetAuthentication() {
        TokenEntry tokenEntry = tokenRepository.createToken(1);
        String auth = tokenRepository.getAuthentication(tokenEntry);
        TokenEntry tokenEntry_ = tokenRepository.getToken(auth);

        assertThat(tokenEntry.getId()).isEqualTo(tokenEntry_.getId());
        assertThat(tokenEntry.getToken()).isEqualTo(tokenEntry_.getToken());
        assertThat(tokenRepository.getAuthentication(tokenEntry_)).isEqualTo(auth);
    }

    @Test
    public void testDeleteToken() {
        TokenEntry tokenEntry1 = tokenRepository.createToken(1);
        TokenEntry tokenEntry2 = tokenRepository.createToken(2);

        assertThat(tokenRepository.checkToken(tokenEntry1)).isTrue();
        assertThat(tokenRepository.checkToken(tokenEntry2)).isTrue();

        tokenRepository.deleteToken(1);

        assertThat(tokenRepository.checkToken(tokenEntry1)).isFalse();
        assertThat(tokenRepository.checkToken(tokenEntry2)).isTrue();
    }

    @Test
    public void testCreateDup() {
        TokenEntry tokenEntry_old = tokenRepository.createToken(1);
        TokenEntry tokenEntry_new = tokenRepository.createToken(1);

        assertThat(tokenRepository.checkToken(tokenEntry_old)).isFalse();
        assertThat(tokenRepository.checkToken(tokenEntry_new)).isTrue();
    }


}
