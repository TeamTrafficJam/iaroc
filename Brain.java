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


    public Brain(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard)
            throws ConnectionLostException {
        super(create);
        sonar = new UltraSonicSensors(ioio);
        this.dashboard = dashboard;
    }

    /* This method is executed when the robot first starts up. */
    public void initialize() throws ConnectionLostException {
        dashboard.log("Hello! I'm a Clever Robot!");
        turnLeft(50);
        //what would you like me to do, Clever Human?




    }
    /* This method is called repeatedly. */
    public void loop() throws ConnectionLostException
    {
        GoldRush();
    }

    public void dragRace() throws ConnectionLostException
    {
        readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
       driveDirect(500,500);
        if (isBumpLeft())
        {
            driveDirect(-500, -500);
            SystemClock.sleep(300);
            driveDirect(-100,100);
            SystemClock.sleep(400);
        }
        else if (isBumpRight())
        {
            driveDirect(-500, -500);
            SystemClock.sleep(300);
            driveDirect(100,-100);
            SystemClock.sleep(400);
        }
    }

    public void Maze() throws ConnectionLostException
    {
        driveDirect(500, 100);
        readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
        if (isBumpLeft() || isBumpRight() == true) {
            driveDirect(-500, -500);
            SystemClock.sleep(100);
            driveDirect(-500, 500);
            SystemClock.sleep(300);
        }


    }
    public void GoldRush() throws ConnectionLostException
    {
        for (int i = 0; i < 65; i++) {
            driveDirect(500, 500);
            readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
            if (isBumpLeft() && isBumpRight() == true) {
                driveDirect(-500, -500);
                SystemClock.sleep(650);
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
            readSensors(SENSORS_INFRARED_BYTE);
            if (getInfraredByte() == 255 && getInfraredByte() == 240 && getInfraredByte() == 242)
            {
                searchForBeacon();
            }
            else {
                beaconRead();
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
    public void turnRight(int degrees) throws ConnectionLostException
    {

        int currentAngle = 0;
        while (currentAngle <= degrees)
        {
            driveDirect(100,-100);
            readSensors(SENSORS_ANGLE);
            currentAngle += getAngle();
        }

    }
    public void turnLeft(int degrees) throws ConnectionLostException
    {
        int currentAngle = 0;
        while (currentAngle <= degrees)
        {
            driveDirect(-100,100);
            readSensors(SENSORS_ANGLE);
            currentAngle -= getAngle();
            dashboard.log("GetAngle is " + getAngle());
            dashboard.log("CurrentAngle is " + currentAngle);
        }
    }
    public void stop() throws ConnectionLostException
    {
        driveDirect(0,0);
    }

    public void beaconRead() throws ConnectionLostException
    {
        if (getInfraredByte() == 244||getInfraredByte() == 246 )//Green Buoy
        {
            turnLeft(5);
        }
        if (getInfraredByte() == 248 || getInfraredByte() == 250 )//Red Buoy
        {
            turnRight(5);
        }
        if (getInfraredByte() == 254 || getInfraredByte() == 252)//Both
        {
            driveDirect(300,300);
        }

    }


    public void searchForBeacon() throws ConnectionLostException
    {
        int angleTurned = 0;
        boolean beaconFound = false;
        while (beaconFound == false && angleTurned <360)
        {
            turnRight(45);
            angleTurned += 45;
            readSensors(SENSORS_INFRARED_BYTE);
            if (getInfraredByte() >= 244 && getInfraredByte() <=254)
            {
                beaconFound = true;
            }
        }
    }
}

