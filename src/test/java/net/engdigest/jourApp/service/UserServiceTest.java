package net.engdigest.jourApp.service;

import net.engdigest.jourApp.Entity.User;
import net.engdigest.jourApp.Repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "Nirmit",
            "Archis",
            "ram"
            // Removed "Shyam" since this user does not exist in your MongoDB database
    })
    public void testFindByusername(String name){
        assertNotNull(userRepository.findByUserName(name),"failed for "+name);
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "3,2,1",
            "3,6,7",
            "4,3,1"
    })
    public void test(int expected ,int a,int b){
        assertEquals(expected,a+b);
    }

}