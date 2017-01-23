package de.hft_stuttgart.djsww.tetris.objects;

public class Tetromino_I extends Tetromino
{

    public Tetromino_I()
    {
        farbe.r = 0;    // each stone get's initialized with it's specific
        farbe.g = 191;  // RGB color code
        farbe.b = 255;

        drehung = 0;    // and the rotation zero
    }

    @Override
    public Punkt[] getKoordinaten(Punkt start)
    {
        Punkt[] koordinaten = new Punkt[4];             // the rotation is done for each sort of stone differently
                                                        // because they have to look all different:
        for (int i = 0; i < koordinaten.length; i++)    // First we create a new point[] with length four and init
        {                                               // every instance with a new point.
            koordinaten[i] = new Punkt();
        }

        switch (drehung)                                // dependant of the current rotation, we calculate starting from the
        {                                               // given start point we got from the calling program part.
            case 0:
            case 2:
                koordinaten[0] = start.clone();         // the first point we give back, is always the one we got as parameter
                koordinaten[1].x = start.x;             // the other three are calculated now starting from there.
                koordinaten[1].y = start.y - 1;
                koordinaten[2].x = start.x;
                koordinaten[2].y = start.y + 1;
                koordinaten[3].x = start.x;
                koordinaten[3].y = start.y + 2;
                break;

            case 1:
            case 3:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x - 1;
                koordinaten[1].y = start.y;
                koordinaten[2].x = start.x + 1;
                koordinaten[2].y = start.y;
                koordinaten[3].x = start.x + 2;
                koordinaten[3].y = start.y;
                break;
        }
        return koordinaten;
    }
}
