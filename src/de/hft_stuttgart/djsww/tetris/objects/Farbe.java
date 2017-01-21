package de.hft_stuttgart.djsww.tetris.objects;

import java.util.Scanner;

public class Farbe implements Cloneable
{

    public int r = 255;
    public int g = 255;
    public int b = 255;

    @Override
    public Farbe clone()
    {
        try
        {
            return (Farbe) super.clone();
        } catch (CloneNotSupportedException e)
        {
            Scanner in = new Scanner(System.in);
            e.printStackTrace();
            System.out.print("Enter zum fortsetzen...");
            in.nextLine();
        }

        return this;
    }
}
