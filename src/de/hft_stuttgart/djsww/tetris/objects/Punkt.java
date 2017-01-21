package de.hft_stuttgart.djsww.tetris.objects;

import java.util.Scanner;

public class Punkt implements Cloneable
{

    public int x = 0;
    public int y = 0;

    @Override
    public Punkt clone()
    {
        try
        {
            return (Punkt) super.clone();
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
