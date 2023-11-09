package com.eric.fileshare;

import com.eric.fileshare.config.SpringConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(SpringConfig.class)
public class TestFile {


    @Test
    public void testGetFileExtension() {
        String file = "full.txt";
        int index = file.lastIndexOf('.');
        String name = file.substring(0, index);
        String extension = file.substring(index+1);

        System.out.println("name = " + name);
        System.out.println("extension = " + extension);
    }


}
