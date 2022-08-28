package com.example.curse;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiskTest {
    Disk disk = new Disk();

    @Test
    void testPullValue(){
        ArrayList<Double> res = new ArrayList<>();
        String temp = "15605760 5259880";
        disk.pullValue(temp, res);

        assertEquals(14.8828125,disk.getTotal_rom());
        assertEquals(5.016212463378906,disk.getUsed_rom());
    }

    @Test
    void CmdRegexTest(){
        int points = 0;

        if(disk.getCmd().equals("df --output=size,used,avail /"))
            points++;

        if(disk.getRegex().equals("(.*)"+"(\\d+\\D+){2}"+"(\\d)"))
            points++;

        assertEquals(2, points);
    }

    @Test
    void testCleanAll(){
        String res = disk.cleanAll(100, 15, "test_name");
        assertEquals("delete from test_name where id > 0", res);

        res = disk.cleanAll(1,5,"test_name");
        assertEquals("",res);
    }

    @Test
    void testCleanOne(){
        String res = disk.cleanOne(8, "test_name");
        assertEquals("", res);

        res = disk.cleanOne(40, "test_name");
        assertEquals("delete from test_name where id < 31", res);
    }

}