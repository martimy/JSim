//    JSim is a discrete event simulator of an M/M/1 queue system.
//    Copyright (C) 2007-2012  Maen Artimy
//
//    This file is part of JSim.
//
//    JSim is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    JSim is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with JSim.  If not, see <http://www.gnu.org/licenses/>.
package jsim;

import java.io.*;
import java.util.*;

public class Settings {

    private Properties defaultProps, appProps;
    private static Settings uniqueInstance;

    private Settings() {
        //Set default values
        defaultProps = new Properties();
        defaultProps.setProperty("SEED", "1234");
        defaultProps.setProperty("ITERATIONS", "10");
        defaultProps.setProperty("ARRIVAL", "100");
        defaultProps.setProperty("SERVICE", "50");
        defaultProps.setProperty("QUEUE", "100");
        defaultProps.setProperty("TIME", "3600");

        //init application settings
        appProps = new Properties(defaultProps);
    }

    public static Settings instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Settings();
        }
        return uniqueInstance;
    }

    public String get(String key) {
        return appProps.getProperty(key);
    }

    public void set(String key, String value) {
        appProps.setProperty(key, value);
    }

    public void load() {
        try {
            FileReader sf = new FileReader("settings.ini");
            appProps.load(sf);
            sf.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void save() {
        try {
            FileWriter sf = new FileWriter("settings.ini");
            appProps.store(sf, "Simulation Settings");
            sf.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
