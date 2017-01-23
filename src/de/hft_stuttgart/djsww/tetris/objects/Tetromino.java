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
        drehung++;              // the stones have max four rotations. So on rotation
        drehung %= 4;           // higher the current rotation, then modulo four.
    }

    public void dreheZurueck()
    {
        drehung--;              // if we need to rotate back, we have to ensure that
        if (drehung < 0)        // we don't get a negative rotation
        {
            drehung = 3;        // so if we get under zero, the legit rotation is three
        }
    }

    public Punkt[] getKoordinaten(Punkt start)
    {
        return new Punkt[1];
    }
}
