package de.hft_stuttgart.djsww.tetris.objects;

abstract public class Tetromino
{

    public Farbe    farbe   = new Farbe();
    public int      drehung;

    public Tetromino()
    {

    }

    public void drehe()
    {
        drehung++;
        drehung %= 4;
    }

    public void dreheZurueck()
    {
        drehung--;
        if (drehung < 0)
        {
            drehung = 3;
        }
    }

    public Punkt[] getKoordinaten(Punkt start)
    {
        return new Punkt[1];
    }
}
