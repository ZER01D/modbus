package org.hashmap.modbus.RtuExample;

import java.util.concurrent.ExecutionException;

import org.hashmap.modbus.RtuExample.master.RtuMasterExample;

public class MasterSlaveRtuThroughput {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        new RtuSlaveExample().start();
        new RtuMasterExample().start();
    }

}
