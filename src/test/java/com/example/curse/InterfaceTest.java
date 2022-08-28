package com.example.curse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest {
    Interface intfc = new Interface();
    String regexName = ".+" + "<UP,"+"(.+)+"+">"+"(.+)";

    @Test
    void testGetIntName() {
        String  line1 = "eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500",
                line2 = "eth0: flags=4163<DOWN,BROADCAST,RUNNING,MULTICAST>  mtu 1500";

        assertEquals("eth0",intfc.getIntName(line1));
        assertEquals("no name",intfc.getIntName(line2));
    }

    @Test
    void testGetRX_TX_FromLine() {
        String reg_rx = "(.+)"+"RX packets"+"(.+)",
                reg_tx = "(.+)"+"TX packets"+"(.+)";
        String line1 = "        RX packets 5932  bytes 1594986 (1.5 MB)", line2 = "        TX packets 5932  bytes 1594986 (1.5 MB)";

        assertEquals("1594986", intfc.getRX_TX_FromLine(line1,reg_rx));
        assertEquals("1594986", intfc.getRX_TX_FromLine(line2,reg_tx));
        assertEquals("no value", intfc.getRX_TX_FromLine(line2,reg_rx));
        assertEquals("no value", intfc.getRX_TX_FromLine(line1,reg_tx));
    }

    @Test
    void testCleanAll(){
        String res = intfc.cleanAll(50, 3, "test_name");
        assertEquals("delete from test_name where id > 0", res);

        res = intfc.cleanAll(1,1,"test_name");
        assertEquals("",res);
    }

    @Test
    void testCleanOne(){
        String res = intfc.cleanOne(8, "test_name");
        assertEquals("", res);

        res = intfc.cleanOne(40, "test_name");
        assertEquals("delete from test_name where group_num < 31", res);
    }
}