package com.veggie.thefarm.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by Bohyun on 2021.04.16
 */
@SpringBootTest
class SecurityUtilsTest {

    @Test
    void encryptAES() {
        String str = "테스트 메시지";
        System.out.println(SecurityUtils.encryptAES(str));
    }

    @Test
    void decryptAES() {
        String str = "RfAsPbQ/jZy/ZoCUe2cc55Mv+z+OA1HCsaD9R+9jnrg=";
        System.out.println(SecurityUtils.decryptAES(str));
    }

    @Test
    void encryptBCryptPassword() {
        String password = "test1234*";
        System.out.println(SecurityUtils.encryptBCryptPassword(password));
    }

    @Test
    void matchesBCryptPassword() {
        String orgPassword = "$2a$10$Dz.AnufWLhaWcsnIJMBCteHIlI5yGX0O9hSvcUFRyfcRmxgyfwKKm";
        String comparePassword = "test1234*";
        System.out.println(SecurityUtils.matchesBCryptPassword(comparePassword, orgPassword));
    }

}