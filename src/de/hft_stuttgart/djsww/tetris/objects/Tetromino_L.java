package de.hft_stuttgart.djsww.tetris.objects;

public class Tetromino_L extends Tetromino
{

    public Tetromino_L()
    {
        farbe.r = 255;
        farbe.g = 140;
        farbe.b = 0;

        drehung = 0;
    }

    @Override
    public Punkt[] getKoordinaten(Punkt start)
    {
        Punkt[] koordinaten = new Punkt[4];

        for (int i = 0; i < koordinaten.length; i++)
        {
            koordinaten[i] = new Punkt();
        }

        switch (drehung)
        {
            case 0:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x;
                koordinaten[1].y = start.y - 1;
                koordinaten[2].x = start.x;
                koordinaten[2].y = start.y + 1;
                koordinaten[3].x = start.x + 1;
                koordinaten[3].y = start.y + 1;
                break;

            case 1:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x + 1;
                koordinaten[1].y = start.y;
                koordinaten[2].x = start.x - 1;
                koordinaten[2].y = start.y;
                koordinaten[3].x = start.x - 1;
                koordinaten[3].y = start.y + 1;
                break;

            case 2:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x;
                koordinaten[1].y = start.y + 1;
                koordinaten[2].x = start.x;
                koordinaten[2].y = start.y - 1;
                koordinaten[3].x = start.x - 1;
                koordinaten[3].y = start.y - 1;
                break;

            case 3:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x - 1;
                koordinaten[1].y = start.y;
                koordinaten[2].x = start.x + 1;
                koordinaten[2].y = start.y;
                koordinaten[3].x = start.x + 1;
                koordinaten[3].y = start.y - 1;
                break;
        }
        return koordinaten;
    }
}
