package de.hft_stuttgart.djsww.tetris.objects;

public class Tetromino_T extends Tetromino
{

    public Tetromino_T()
    {
        farbe.r = 148;
        farbe.g = 0;
        farbe.b = 211;

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
                koordinaten[1].x = start.x - 1;
                koordinaten[1].y = start.y;
                koordinaten[2].x = start.x + 1;
                koordinaten[2].y = start.y;
                koordinaten[3].x = start.x;
                koordinaten[3].y = start.y + 1;
                break;

            case 1:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x;
                koordinaten[1].y = start.y - 1;
                koordinaten[2].x = start.x;
                koordinaten[2].y = start.y + 1;
                koordinaten[3].x = start.x - 1;
                koordinaten[3].y = start.y;
                break;

            case 2:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x - 1;
                koordinaten[1].y = start.y;
                koordinaten[2].x = start.x + 1;
                koordinaten[2].y = start.y;
                koordinaten[3].x = start.x;
                koordinaten[3].y = start.y - 1;
                break;

            case 3:
                koordinaten[0] = start.clone();
                koordinaten[1].x = start.x;
                koordinaten[1].y = start.y - 1;
                koordinaten[2].x = start.x;
                koordinaten[2].y = start.y + 1;
                koordinaten[3].x = start.x + 1;
                koordinaten[3].y = start.y;
                break;

            default:
                throw new RuntimeException("rotate T failed catastrophically!");
        }
        return koordinaten;
    }
}
