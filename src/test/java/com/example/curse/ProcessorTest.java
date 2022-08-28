package com.example.curse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {
    Processor proc = new Processor();

    @Test
    void testPullValue(){
        String test = "    1 root      20   0    1260    516    468 S   15.0  0.0   0:00.19 init";
        proc.pullValue(test);
        double sum = proc.getSum();

        assertEquals(15, sum);
    }

    @Test
    void Checker(){
        int points = 0;
        String test1 = "I", test2 = "S", test3 = "R", test4 = "123";

        if(proc.check_part(test1) == true)
            points++;

        if(proc.check_part(test2) == true)
            points++;

        if(proc.check_part(test3) == true)
            points++;

        if(proc.check_part(test4) == false)
            points++;

        assertEquals(4, points);
    }

    @Test
    void CmdRegexTest(){
        int points = 0;

        if(proc.getCmd().equals("top -bn 1"))
            points++;

        if(proc.getRegex().equals("(.*)"))
            points++;

        assertEquals(2, points);
    }

    @Test
    void testCleanAll(){
        String res = proc.cleanAll(10, 1, "test_name");
        assertEquals("delete from test_name where id > 0", res);
    }

    @Test
    void testCleanOne(){
        String res = proc.cleanOne(10, "test_name");
        assertEquals("", res);

        res = proc.cleanOne(100, "test_name");
        assertEquals("delete from test_name where id < 91", res);
    }
}