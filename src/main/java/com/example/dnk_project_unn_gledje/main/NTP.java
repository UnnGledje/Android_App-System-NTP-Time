/* This class handles the NTP Server Connection */

package com.example.dnk_project_unn_gledje.main;

import android.util.Log;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import java.io.IOException;
import java.net.InetAddress;

import java.util.Date;

public class NTP {

    private static final String TIME_SERVER = "pool.ntp.org";
    private volatile long offset;
    private static TimeInfo timeInfo;

    public Date getNTPTime() throws IOException {

        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(10_000);

        long ntpTime;
        Date nTime;

        try {
            client.open();
            InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
            timeInfo = client.getTime(inetAddress);
            timeInfo.computeDetails();   // Includes the offset and the delay

            // Get clock offset to match local clock with remote clock
            if (timeInfo.getOffset() != null) {
                this.offset = timeInfo.getOffset();
            }

            long currentTime = System.currentTimeMillis();
            ntpTime = TimeStamp.getNtpTime(currentTime + offset).getTime();

            nTime = new Date(ntpTime);
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
            return new Date();
        }
        return nTime;
    }

}
