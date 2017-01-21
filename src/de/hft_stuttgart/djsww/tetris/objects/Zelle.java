package de.hft_stuttgart.djsww.tetris.objects;

import java.util.Scanner;

public class Zelle implements Cloneable
{

    public Farbe    farbe       = new Farbe();

    public boolean  gesperrt    = false;

    public String   inhalt      = "  ";

    @Override
    public Zelle clone()
    {
        try
        {
            return (Zelle) super.clone();
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
