package de.hft_stuttgart.djsww.tetris;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class TetrisMusikPlayer
{

    Clip background_Clip;

    public TetrisMusikPlayer()
    {
        try
        {
            InputStream         resource_Path       = getClass().getResourceAsStream("music/background.wav");
            AudioInputStream    background_Stream   = AudioSystem.getAudioInputStream(new BufferedInputStream(resource_Path));
            AudioFormat         background_Format   = background_Stream.getFormat();
            DataLine.Info       background_Info     = new DataLine.Info(Clip.class, background_Format);

            background_Clip = (Clip) AudioSystem.getLine(background_Info);
            background_Clip.open(background_Stream);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void startBackgroundMusik()
    {
        try
        {
            background_Clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusik()
    {
        try
        {
            background_Clip.stop();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void playSuccess()
    {
        Clip success_Clip = null;
        try
        {
            InputStream         resource_Path   = getClass().getResourceAsStream("music/line_complete.wav");
            AudioInputStream    success_Stream  = AudioSystem.getAudioInputStream(new BufferedInputStream(resource_Path));
            AudioFormat         success_Format  = success_Stream.getFormat();
            DataLine.Info       success_Info    = new DataLine.Info(Clip.class, success_Format);

            success_Clip = (Clip) AudioSystem.getLine(success_Info);
            success_Clip.open(success_Stream);
            success_Clip.start();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
