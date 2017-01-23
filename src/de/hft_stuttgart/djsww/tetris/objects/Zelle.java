package de.hft_stuttgart.djsww.tetris.objects;

import java.util.Scanner;

public class Zelle implements Cloneable
{

    public Farbe    farbe       = new Farbe();  // In our playfield matrix we needed to for each field:
    public boolean  gesperrt    = false;        // what color has it, is it locked by another stone,
    public String   inhalt      = "  ";         // and what's the printable content.

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
