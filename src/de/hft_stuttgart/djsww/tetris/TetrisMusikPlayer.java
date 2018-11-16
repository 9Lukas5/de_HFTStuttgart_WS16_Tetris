package de.hft_stuttgart.djsww.tetris;

import de.wiest_lukas.lib.AACPlayer;
import java.io.File;

public class TetrisMusikPlayer
{

    //Clip    background_Clip;            // Clip for looping background music
    AACPlayer   backgroundmusic;
    AACPlayer   success;
    boolean     mute            = false;    // needed to be able to mute during ingame

    public TetrisMusikPlayer()
    {
        String absPWD = new MyPath().toString();
        backgroundmusic = new AACPlayer(new File(absPWD + "music/background.m4a"));
        success = new AACPlayer(new File(absPWD + "music/line_complete.m4a"));
    }

    public void startBackgroundMusik()
    {
        if (!mute)  // check if sounds are muted right now
        {
            backgroundmusic.enableLoop();
            backgroundmusic.play();
        }
    }

    public void stopBackgroundMusik()
    {
        if (!mute)  // check if sounds are muted right now
        {
            backgroundmusic.stop();
        }
    }

    public void playSuccess()
    {
        if (!mute)  // check if sounds are muted right now
        {
            if (success.isPlaying())
                success.stop();
            success.play();
        }
    }

    public void unMute()
    {
        if (!mute)  // check if sounds are muted right now
        {
            stopBackgroundMusik();  // stop background music
            mute = true;            // set sounds muted
            return;                 // leave method
        }

        else        // check if sounds are muted right now
        {
            mute = false;           // set sounds unmuted
            startBackgroundMusik(); // start background music again
        }
    }

    protected static class MyPath
    {
        private final String path;

        public MyPath()
        {
            // local vars
            String myPath;
            String[] temp;

            myPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();

            if (System.getProperty("os.name").indexOf("win") >= 0 || System.getProperty("os.name").indexOf("Win") >= 0)
            {
                temp = myPath.split("/", 2);
                myPath = temp[1];
            }
            else
            {
                temp = myPath.split(":", 2);
                myPath = temp[1];
            }

            StringBuilder returnPath = new StringBuilder();
            if (myPath.contains(".jar"))
            {
                temp = myPath.split("/");
                for (int i=0; i < temp.length - 1; i++)
                {
                    returnPath.append(temp[i]);
                    returnPath.append("/");
                }
            }

            this.path = returnPath.toString();
        }

        @Override
        public String toString()
        {
            return this.path;
        }
    }
}
