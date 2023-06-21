package com.marginallyclever.robotoverlord.preferences;

import com.marginallyclever.robotoverlord.components.GCodePathComponent;
import com.marginallyclever.robotoverlord.parameters.BooleanParameter;
import com.marginallyclever.robotoverlord.parameters.IntParameter;

import java.util.prefs.Preferences;

/**
 * Container for graphics preferences
 * @since 2.7.0
 * @author Dan Royer
 */
public class GraphicsPreferences {
    public static final String[] FSAA_NAMES = {"off","2","4","8"};
    private static final Preferences preferences = Preferences.userRoot().node("RobotOverlord").node("Graphics");

    public static final BooleanParameter verticalSync = new BooleanParameter("vertical sync",true);
    public static final BooleanParameter glDebug = new BooleanParameter("opengl debug",false);
    public static final BooleanParameter glTrace = new BooleanParameter("opengl trace",false);
    public static final BooleanParameter hardwareAccelerated = new BooleanParameter("hardware accelerated",true);
    public static final BooleanParameter backgroundOpaque = new BooleanParameter("background opaque",true);
    public static final BooleanParameter doubleBuffered = new BooleanParameter("double buffered",true);
    public static final BooleanParameter antialiasing = new BooleanParameter("antialiasing",true);
    public static final IntParameter framesPerSecond = new IntParameter("fps",30);
    public static final IntParameter fsaaSamples = new IntParameter("FSAA samples",2);

    public static void save() {
        preferences.putBoolean("verticalSync",verticalSync.get());
        preferences.putBoolean("glDebug",glDebug.get());
        preferences.putBoolean("glTrace",glTrace.get());
        preferences.putBoolean("hardwareAccelerated",hardwareAccelerated.get());
        preferences.putBoolean("backgroundOpaque",backgroundOpaque.get());
        preferences.putBoolean("doubleBuffered",doubleBuffered.get());
        preferences.putInt("framesPerSecond",framesPerSecond.get());
        preferences.putInt("fsaaSamples",fsaaSamples.get());
    }

    public static void load() {
        verticalSync.set(preferences.getBoolean("verticalSync",verticalSync.get()));
        glDebug.set(preferences.getBoolean("glDebug",glDebug.get()));
        glTrace.set(preferences.getBoolean("glTrace",glTrace.get()));
        hardwareAccelerated.set(preferences.getBoolean("hardwareAccelerated",hardwareAccelerated.get()));
        backgroundOpaque.set(preferences.getBoolean("backgroundOpaque",backgroundOpaque.get()));
        doubleBuffered.set(preferences.getBoolean("doubleBuffered",doubleBuffered.get()));
        framesPerSecond.set(preferences.getInt("framesPerSecond",framesPerSecond.get()));
        fsaaSamples.set(preferences.getInt("fsaaSamples",fsaaSamples.get()));
    }
}
