package de.hft_stuttgart.djsww.tetris.objects;

import java.util.Scanner;

public class Farbe implements Cloneable
{

    public int r = 255; // the lanterna Terminal we used for our Tetris can set
    public int g = 255; // colors by RGB values. Therefore we made our structured
    public int b = 255; // variable for colores with an int for each color

    @Override
    public Farbe clone()
    {
        try
        {
            return (Farbe) super.clone();
        } catch (CloneNotSupportedException e)
        {
            Scanner in = new Scanner(System.in, "utf-8");
            e.printStackTrace();
            System.out.print("Enter zum fortsetzen...");
            in.nextLine();
        }

        return this;
    }
}
