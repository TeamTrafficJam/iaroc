package org.jointheleague.iaroc;

import android.os.SystemClock;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import org.wintrisstech.irobot.ioio.IRobotCreateAdapter;
import org.wintrisstech.irobot.ioio.IRobotCreateInterface;
import org.jointheleague.iaroc.sensors.UltraSonicSensors;

public class Brain extends IRobotCreateAdapter {
    private final Dashboard dashboard;
    public UltraSonicSensors sonar;
    public int totalAngle = 0;
    public int timer = 0;


    public Brain(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard)
            throws ConnectionLostException {
        super(create);
        sonar = new UltraSonicSensors(ioio);
        this.dashboard = dashboard;
    }

    /* This method is executed when the robot first starts up. */
    public void initialize() throws ConnectionLostException {
        dashboard.log("Hello! I'm a Clever Robot!");
        dashboard.speak("Double O Three");
        //turnLeft(50);
        //what would you like me to do, Clever Human?


    }

    /* This method is called repeatedly. */
    public void loop() throws ConnectionLostException {
        GoldRush();


    }

    public void dragRace() throws ConnectionLostException {
        readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
        driveDirect(500, 500);
        if (isBumpLeft()) {
            driveDirect(-500, -500);
            SystemClock.sleep(300);
            driveDirect(-100, 100);
            SystemClock.sleep(400);
        } else if (isBumpRight()) {
            driveDirect(-500, -500);
            SystemClock.sleep(300);
            driveDirect(100, -100);
            SystemClock.sleep(400);
        }
    }

    public void Maze() throws ConnectionLostException {
        driveDirect(500, 100);
        readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
        if (isBumpLeft() || isBumpRight() == true) {
            driveDirect(-500, -500);
            SystemClock.sleep(100);
            driveDirect(-500, 500);
            SystemClock.sleep(300);
        }


    }

    public void GoldRush() throws ConnectionLostException {
        for (int i = 0; i < 65; i++) {
            driveDirect(500, 500);
            readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
            if (isBumpLeft() && isBumpRight() == true) {
                driveDirect(-500, -500);
                SystemClock.sleep(450);
                driveDirect(500, -500);
                SystemClock.sleep(400);
            } else if (isBumpLeft() == true) {
                driveDirect(-500, -500);
                SystemClock.sleep(550);
                driveDirect(-500, 500);
                SystemClock.sleep(250);
            } else if (isBumpRight() == true) {
                driveDirect(-500, -500);
                SystemClock.sleep(550);
                driveDirect(500, -500);
                SystemClock.sleep(250);
            }
        }
        readSensors(SENSORS_INFRARED_BYTE);
        if (getInfraredByte() == 255 && getInfraredByte() == 240 && getInfraredByte() == 242) {
            beaconRead();
        }
//      SystemClock.sleep(3000);

//    for (int j = 0; j < 13; j++) {
//        readSensors(SENSORS_INFRARED_BYTE);
//        dashboard.log("" + getInfraredByte());
//        if (getInfraredByte() != 255 && getInfraredByte() != 240 && getInfraredByte() != 242) {
//            beaconRead();
//        }
//        SystemClock.sleep(150);

//    }

    }

    public void beaconRead() throws ConnectionLostException {
        boolean robotIsInBeacon = true;

        while (robotIsInBeacon == true) {
            readSensors(SENSORS_INFRARED_BYTE);

            if (getInfraredByte() == 255 || getInfraredByte() == 240 || getInfraredByte() == 242) {
                driveDirect(-500, 500);
                for (int j = 0; j < 13; j++) {
                    readSensors(SENSORS_INFRARED_BYTE);
                    dashboard.log("" + getInfraredByte());
                    if (getInfraredByte() != 255 && getInfraredByte() != 240 && getInfraredByte() != 242) {
                        beaconRead();
                    }
                    SystemClock.sleep(150);
                }

                robotIsInBeacon = false;
            }

            if (getInfraredByte() == 244) {
                driveDirect(250, 500);
            }
            if (getInfraredByte() == 252) {
                driveDirect(500, 500);
            }
            if (getInfraredByte() == 248) {
                driveDirect(500, 250);
            }
            if (getInfraredByte() == 246) {
                driveDirect(500, 500);
                readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
//                if (isBumpLeft() || isBumpRight()){
//                    victoryDance();
//                }
            }
            if (getInfraredByte() == 254) {
                driveDirect(500, 500);
                readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
//                if (isBumpLeft() || isBumpRight()){
//                    victoryDance();
//                }
            }
            if (getInfraredByte() == 250) {
                driveDirect(500, 500);
                readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
//                if (isBumpLeft() || isBumpRight()){
////                    victoryDance();
//                }
            }

        }

    }


    //     public void turnRight(int degrees) throws ConnectionLostException
//     {
//
//         driveDirect(300,0);
//         readSensors(getAngle());
//         if (totalAngle <= degrees)
//         {
//             return;
//         }
//         else
//         {
//         totalAngle= 0;
//             driveDirect(0,0);
//             return;
//         }
//     }
    public void turnLeft(int degrees) throws ConnectionLostException {

        int currentAngle = 0;
        while (currentAngle <= degrees) {
            driveDirect(100, -100);
            readSensors(SENSORS_ANGLE);
            currentAngle += getAngle();
        }
        stop();
    }

    public void turnRight(int degrees) throws ConnectionLostException {
        int currentAngle = 0;
        while (currentAngle <= degrees) {
            driveDirect(-100, 100);
            readSensors(SENSORS_ANGLE);
            currentAngle -= getAngle();
            //dashboard.log("GetAngle is " + getAngle());
            //dashboard.log("CurrentAngle is " + currentAngle);
        }
        stop();
    }

    public void stop() throws ConnectionLostException {
        driveDirect(0, 0);
    }

    public void beaconReads() throws ConnectionLostException
    {
        if (getInfraredByte() == 244) {
            driveDirect(250, 500);
        }
        if (getInfraredByte() == 252) {
            driveDirect(500, 500);
        }
        if (getInfraredByte() == 248) {
            driveDirect(500, 250);
        }
        if (getInfraredByte() == 246) {
            driveDirect(500, 500);
            readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
//                if (isBumpLeft() || isBumpRight()){
//                    victoryDance();
//                }
        }
        if (getInfraredByte() == 254) {
            driveDirect(500, 500);
            readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
//                if (isBumpLeft() || isBumpRight()){
//                    victoryDance();
//                }
        }
        if (getInfraredByte() == 250) {
            driveDirect(500, 500);
            readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
//                if (isBumpLeft() || isBumpRight()){
////                    victoryDance();
//                }
        }


//        if (/*getInfraredByte() == 244 ||*/ getInfraredByte() == 246 )//Green Buoy
//        {
//            driveDirect(300,300);
//        }
//        if (/*getInfraredByte() == 248 ||*/ getInfraredByte() == 250 )//Red Buoy
//        {
//           driveDirect(300,300);
//        }
//        if (getInfraredByte() == 254 || getInfraredByte() == 252)//Both
//        {
//            driveDirect(300,300);
//        }

    }




//    public void searchForBeacon() throws ConnectionLostException
//    {
//        int angleTurned = 0;
//        boolean beaconFound = false;
//        while (beaconFound == false && angleTurned <= 360)
//        {
//            turnRight(45);
//            angleTurned += 45;
//            readSensors(SENSORS_INFRARED_BYTE);
//            if (getInfraredByte() >= 244 && getInfraredByte() <=254)
//            {
//                beaconFound = true;
//            }
//        }
//    }
    public void fasterRightTurn(int degrees) throws ConnectionLostException
    {
        int currentAngle = 0;
        while (currentAngle <= degrees)
        {
            driveDirect(-500,500);
            readSensors(SENSORS_ANGLE);
            currentAngle -= getAngle();
            //dashboard.log("GetAngle is " + getAngle());
            //dashboard.log("CurrentAngle is " + currentAngle);
        }
        stop();
    }
    public void fasterLeftTurn(int degrees) throws ConnectionLostException
    {
        int currentAngle = 0;
        while (currentAngle <= degrees)
        {
            driveDirect(500,-500);
            readSensors(SENSORS_ANGLE);
            currentAngle += getAngle();
            //dashboard.log("GetAngle is " + getAngle());
            //dashboard.log("CurrentAngle is " + currentAngle);
        }
        stop();
    }
    public void randomTurn() throws ConnectionLostException
    {
        int turning = (int)(Math.random() * 2);
        if (turning == 0)
        {
            turnLeft(90);
        }
        else if(turning == 1)
        {
            turnRight(90);
        }
    }

    public void printBeacon() throws ConnectionLostException
    {
        readSensors(SENSORS_INFRARED_BYTE);
        int displayByte = getInfraredByte();
        if (displayByte == 248)
        {
            dashboard.log("Red");
        }
        if (displayByte == 244)
        {
            dashboard.log("Green");
        }
        if (displayByte == 242)
        {
            dashboard.log("Force Field");
        }
        if (displayByte == 252)
        {
            dashboard.log("Red and Green buoy");
        }
        if (displayByte == 250)
        {
            dashboard.log("Red Buoy and Force Field");
        }
        if (displayByte == 246)
        {
            dashboard.log("Green buoy and Force Field");
        }
        if (displayByte == 254)
        {
            dashboard.log("Red Buoy Green Buoy and Force Field");
        }
        if (displayByte == 255)
        {
            dashboard.log("Nothing");
        }
    }
}

