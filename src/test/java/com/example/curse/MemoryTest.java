package com.example.curse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryTest {
    Memory mem = new Memory();

    @Test
    void getVal() {
        double a = 12958128, b = 104148;
        String line = "KiB Mem : "+a+" total, 12299168 free,   "+b+" used,   554812 buff/cache";
        mem.getVal(line);

        assertEquals(a/1024, mem.getMb_total());
        assertEquals(b/1024, mem.getMb_used());
    }

    @Test
    void testCleanAll(){
        String res = mem.cleanAll(100, 15, "test_name");
        assertEquals("delete from test_name where id > 0", res);

        res = mem.cleanAll(1,1,"test_name");
        assertEquals("",res);
    }

    @Test
    void testCleanOne(){
        String res = mem.cleanOne(1, "test_name");
        assertEquals("", res);

        res = mem.cleanOne(72, "test_name");
        assertEquals("delete from test_name where id < 63", res);
    }
}