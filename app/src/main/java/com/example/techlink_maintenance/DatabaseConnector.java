package com.example.techlink_maintenance;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    Connection con;
    String uname, pass, ip, port, database;
    @SuppressLint("NewApi")
    public Connection connectionClass()
    {
        ip = "172.16.0.12";
        database = "DEVICES_MAINTENANCE";
        uname = "ERPUSER";
        pass = "12345";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ "databasename="+ database+";user="+uname+";password="+pass+";";
            connection = DriverManager.getConnection(ConnectionURL);
        }catch (Exception ex)
        {
            Log.e("Error ", ex.getMessage());
        }

        return connection;
    }
}
