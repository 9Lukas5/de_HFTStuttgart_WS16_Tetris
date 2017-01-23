package de.hft_stuttgart.djsww.tetris.objects;

import java.util.Scanner;

public class Punkt implements Cloneable
{

    public int x = 0;   // an point/coordinate has always an x and y value. We handled these first with
    public int y = 0;   // int[] and placed x and y in different indexes. That was a bit awkward to use.

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
