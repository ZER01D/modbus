Modbus Library
--------------
Library written in java support Modbus_RTU and Modbus_TCP communication between modbus slave and modbus master.
Library can be used as Modbus master to request data from modbus slaves.

## Features
- Support for Serial RTU Modbus Master
- Support for TCP Modbus Master

## Sub-Modules
- [modbus-master](#modbus-master)
- [testSerialComm](#testSerialComm)

## modbus-master
Complete code for modbus library, which contains api's for rtu and tcp for modbus-master. Module also contain example for both rtu and tcp based master.

### Modbus Master Sub-Modules
- [final-module](#final-module)
- [modbus-codec](#modbus-codec)
- [modbus-core](#modbus-core)
- [modbus-example](#modbus-example)
- [modbus-master-rtu](#modbus-master-rtu)
- [modbus-master-tcp](#modbus-master-tcp)
- [modbus-rtu-example](#modbus-rtu-example)
- [modbus-slave-tcp](#modbus-slave-tcp)

#### final-module
On building the project final-module will contain the compiled Jar with all the supported modules and dependencies. To use modbus library in some project import the jar generated in this project.

#### modbus-codec
Module is having the Encoder, Decoder and Payload formats for RTU and TCP request and response.

#### modbus-core
Module is having classes for different modbus request and response. Its also have the interface for modbus PDU.

#### modbus-example
Example for modbus-master-tcp, which simulate TCP communication between modbus master and slave.

#### modbus-master-rtu
API's for modbus rtu master over serial communication.

#### modbus-rtu-example
Module is an example fot modbus rtu master, which utilize the api's provided by modbus-master-rtu.

#### modbus-slave-tcp
Modbus tcp slave, mainly for sample to work with modbus-example.

## testSerialComm
"testSerialComm" is sample code for listening on Serial Post, it will serve as a slave which will read on port and then sends back the hardcoded response by writing back on the serial port.

## Getting Started with Modbus RTU
### For Linux : 
Create a Virtual Serial Port.
1. install : socat
    Ubuntu Cmd : ```sudo apt-get install socat```
    
    Fedora/CentOS : ```sudo yum install socat```

2. Create Virtual Serial port

    CMD : ```socat -d -d pty,raw,echo=0 pty,raw,echo=0```

    Sample Cmd O/p :
    ```
    2017/07/13 16:26:50 socat[1265] N PTY is /dev/pts/28
    2017/07/13 16:26:50 socat[1265] N PTY is /dev/pts/45
    2017/07/13 16:26:50 socat[1265] N starting data transfer loop with FDs [5,5] and [7,7]
    ```
    Note : Remember the Port Name : /dev/pts/28 and /dev/pts/45

3. Test the Port : Open 2-more terminal -
    List the port to verify the port have been created :
    
        CMD : ls /dev/pts/
       
        Output : You will see the port with number : 28 and 45

    On Terminal 1 : Open Port in listening mode
    
                    CMD : cat < /dev/pts/28

    On Terminal 2 : Write on Port
    
                    CMD : echo "Hello, World" > /dev/pts/45

### Steps to Build Project and Execute RTU examples :
1. Import the two existing projects in your IDE in separate project and do "mvn clean install" on both the
   projects.

2. To run the project, we need to mention the Serial Ports in the code :

   - For modbus-master :
   
        Open java file ```RtuMasterExample.java``` under directory
        PATH : ```~/modbus-master/mosbus-rtu-example/src/main/org/hashmap/modbus/RtuExample/master```
        Search for ```ModbusRtuMasterConfig``` in ```start()``` function, provide the one of two serial port name you have created in above step.

   - For testSerialComm :
   
        Open java file ```MySerial.java``` under directory
        PATH : ```~/testSerialComm/src/main/java```
        Search for ```getPortIdentifier``` in ```initSerial()``` function, provide second serial port name.

3. First Run the ```MySerial.java``` in testSerialComm project from your IDE, you will see
   printing messages saying ```In Reading....```

    Now, move to modbus-master project and run the file ```MasterSlaveRtuThroughput.java```.

NOTE : Make sure that you are using in these project are not in listening mode on terminal.
       Otherwise the written messages will be directed to the terminal.

