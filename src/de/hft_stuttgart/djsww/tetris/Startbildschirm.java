package de.hft_stuttgart.djsww.tetris;

import com.googlecode.lanterna.input.Key;
import static de.hft_stuttgart.djsww.tetris.Tetris.*;
import de.hft_stuttgart.djsww.tetris.objects.Zelle;
import java.util.LinkedList;

public class Startbildschirm
{

    static LinkedList<Zelle[]> logo         = new LinkedList<>();
    static LinkedList<Zelle[]> logo_Klein   = new LinkedList<>();

    public static void starteSpiel()
    {
        // lokale Variablen
        Key eingabe;

        initLogo();
        zeichne();

        while (true)
        {
            eingabe = terminal.readInput();

            if (eingabe != null)
            {
                if (eingabe.getKind() == Key.Kind.Enter)
                {
                    ladeBildschirm();
                    break;
                }

                if (eingabe.getKind() == Key.Kind.Escape)
                {
                    System.exit(0);
                }
            }
        }

        terminal.clearScreen();

        logoKlein();
        int y_offset = 1;
        int x_offset = 40;

        terminal.applyForegroundColor(logo_Klein.getFirst()[0].farbe.r, logo_Klein.getFirst()[0].farbe.g, logo_Klein.getFirst()[0].farbe.b);

        for (int y = 0; y < logo_Klein.size(); y++)
        {
            terminal.moveCursor(x_offset, y_offset);
            for (int x = 0; x < logo_Klein.getFirst().length; x++)
            {
                Zelle temp;
                temp = logo_Klein.get(y)[x];

                terminal.putCharacter(temp.inhalt.toCharArray()[0]);
            }
            y_offset++;
        }

        x_offset += 27;
        y_offset = 1;
        terminal.moveCursor(x_offset, y_offset);
        for (char temp : "Hochschule".toCharArray())
        {
            terminal.putCharacter(temp);
        }
        y_offset++;

        terminal.moveCursor(x_offset, y_offset);
        for (char temp : "fuer Technik".toCharArray())
        {
            terminal.putCharacter(temp);
        }
        y_offset++;

        terminal.moveCursor(x_offset, y_offset);
        for (char temp : "Stuttgart".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        terminal.applyForegroundColor(255, 255, 255);

        x_offset = 40;

        y_offset = 7;
        terminal.moveCursor(x_offset, y_offset);
        for (char temp : "Informatik/Mathematik".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        y_offset++;
        terminal.moveCursor(x_offset, y_offset);
        for (char temp : "Semester 1+ Vorschaltsemester".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        y_offset += 3;
        terminal.moveCursor(x_offset, y_offset);
        for (char temp : "kostenlos herunterladen / free download".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        y_offset++;
        terminal.moveCursor(x_offset, y_offset);
        terminal.applyForegroundColor(0, 113, 218);
        for (char temp : "github.com/9Lukas5/de_HFTStuttgart_WS16_Tetris/releases".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        terminal.applyForegroundColor(255, 255, 255);
    }

    public static void zeichne()
    {
        int y_offset = 12;
        int x_offset = 14;

        try
        {
            Thread.sleep(500);

            terminal.clearScreen();

            for (int y = 0; y < logo.size(); y++)
            {
                terminal.moveCursor(x_offset, y + y_offset);
                for (int x = 0; x < logo.getFirst().length; x++)
                {
                    Zelle temp;
                    temp = logo.get(y)[x];
                    terminal.applyForegroundColor(temp.farbe.r, temp.farbe.g, temp.farbe.b);
                    terminal.putCharacter(temp.inhalt.toCharArray()[0]);

                }
                Thread.sleep(25);
            }

            for (int intro = y_offset; y_offset > 0; y_offset--)
            {
                terminal.clearScreen();

                for (int y = 0; y < logo.size(); y++)
                {
                    terminal.moveCursor(x_offset, y + y_offset);
                    for (int x = 0; x < logo.getFirst().length; x++)
                    {
                        Zelle temp;
                        temp = logo.get(y)[x];
                        terminal.applyForegroundColor(temp.farbe.r, temp.farbe.g, temp.farbe.b);
                        terminal.putCharacter(temp.inhalt.toCharArray()[0]);
                    }
                }
                Thread.sleep(25);
            }

            terminal.applyForegroundColor(0, 100, 255);
            terminal.moveCursor(x_offset, y_offset + 20);

            for (char temp : "Enter: Spiel starten".toCharArray())
            {
                terminal.putCharacter(temp);
                Thread.sleep(25);
            }

            terminal.moveCursor(x_offset, y_offset + 21);

            for (char temp : "  ESC: Beenden".toCharArray())
            {
                terminal.putCharacter(temp);
                Thread.sleep(25);
            }

            terminal.moveCursor(x_offset + 23, y_offset + 25);

            terminal.applyForegroundColor(148, 0, 211);
            terminal.putCharacter('T');
            Thread.sleep(25);

            terminal.applyForegroundColor(255, 255, 0);
            terminal.putCharacter('E');
            Thread.sleep(25);

            terminal.applyForegroundColor(0, 0, 255);
            terminal.putCharacter('T');
            Thread.sleep(25);

            terminal.applyForegroundColor(255, 140, 0);
            terminal.putCharacter('R');
            Thread.sleep(25);

            terminal.applyForegroundColor(0, 191, 255);
            terminal.putCharacter('I');
            Thread.sleep(25);

            terminal.applyForegroundColor(34, 139, 34);
            terminal.putCharacter('S');
            Thread.sleep(25);

            terminal.applyForegroundColor(255, 0, 0);
            for (char temp : " WS16 Projektarbeit".toCharArray())
            {
                terminal.putCharacter(temp);
                Thread.sleep(25);
            }

            terminal.moveCursor(x_offset + 23, y_offset + 26);
            terminal.applyForegroundColor(255, 255, 255);

            for (char temp : version.toCharArray())
            {
                terminal.putCharacter(temp);
            }

            terminal.moveCursor(0, 29);
            terminal.applyForegroundColor(255, 255, 255);

            for (char temp : "Authors: Philipp Doerich, Philip Jaeger, Anne Streil, Lukas Wiest, Erik Wolf".toCharArray())
            {
                terminal.putCharacter(temp);
                Thread.sleep(10);
            }

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        terminal.applyForegroundColor(255, 255, 255);
    }

    public static void ladeBildschirm()
    {
        terminal.clearScreen();

        try
        {
            terminal.moveCursor(39, 14);
            for (int i = 0; i < 20; i++)
            {
                if (i == 0)
                {
                    terminal.applyForegroundColor(255, 0, 0);
                }
                if (i == 7)
                {
                    terminal.applyForegroundColor(0, 255, 0);
                }
                if (i == 14)
                {
                    terminal.applyForegroundColor(0, 0, 255);
                }

                terminal.putCharacter('\u2588');

                Thread.sleep(70);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void initLogo()
    {

        Zelle leer = new Zelle();
        Zelle voll = new Zelle();

        leer.inhalt = " ";
        voll.inhalt = "\u2588";
        voll.farbe.r = 180;
        voll.farbe.g = 0;
        voll.farbe.b = 0;

        Zelle[] temp = new Zelle[70];

        //=====H
        for (int i = 0; i < temp.length; i++)
        {
            temp[i] = leer;
        }

        for (int i = 0; i < 18; i++)
        {
            logo.add(temp.clone());
        }

        for (int y = 0; y < 12; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int y = 5; y < 7; y++)
        {
            for (int x = 4; x < 12; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int y = 0; y < 12; y++)
        {
            for (int x = 12; x < 16; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        //=====F
        for (int y = 0; y < 12; y++)
        {
            for (int x = 20; x < 24; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int y = 0; y < 2; y++)
        {
            for (int x = 20; x < 32; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int y = 5; y < 7; y++)
        {
            for (int x = 20; x < 28; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        //=====T
        for (int y = 0; y < 2; y++)
        {
            for (int x = 36; x < 52; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int y = 2; y < 12; y++)
        {
            for (int x = 42; x < 46; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        //=====S
        for (int x = 0; x < 6; x++)
        {
            logo.get(13)[x] = voll;
        }

        for (int y = 14; y < 16; y++)
        {
            for (int x = 0; x < 2; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int x = 2; x < 6; x++)
        {
            logo.get(15)[x] = voll;
        }

        for (int y = 16; y < 18; y++)
        {
            for (int x = 4; x < 6; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int x = 0; x < 6; x++)
        {
            logo.get(17)[x] = voll;
        }

        //=====T
        for (int x = 8; x < 14; x++)
        {
            logo.get(13)[x] = voll;
        }

        for (int y = 14; y < 18; y++)
        {
            for (int x = 10; x < 12; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        //=====U
        for (int y = 13; y < 18; y++)
        {
            for (int x = 16; x < 18; x++)
            {
                logo.get(y)[x] = voll;
            }

            for (int x = 20; x < 22; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int x = 18; x < 20; x++)
        {
            logo.get(17)[x] = voll;
        }

        //=====T
        for (int x = 24; x < 30; x++)
        {
            logo.get(13)[x] = voll;
        }

        for (int y = 14; y < 18; y++)
        {
            for (int x = 26; x < 28; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        //=====T
        for (int x = 32; x < 38; x++)
        {
            logo.get(13)[x] = voll;
        }

        for (int y = 14; y < 18; y++)
        {
            for (int x = 34; x < 36; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        //=====G
        for (int x = 40; x < 46; x++)
        {
            logo.get(13)[x] = voll;
        }

        for (int y = 13; y < 18; y++)
        {
            for (int x = 40; x < 42; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int y = 15; y < 18; y++)
        {
            for (int x = 44; x < 46; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int x = 42; x < 44; x++)
        {
            logo.get(15)[x] = voll;
            logo.get(17)[x] = voll;
        }

        //=====A
        for (int y = 13; y < 18; y++)
        {
            for (int x = 48; x < 50; x++)
            {
                logo.get(y)[x] = voll;
            }

            for (int x = 52; x < 54; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int x = 50; x < 52; x++)
        {
            logo.get(13)[x] = voll;
            logo.get(15)[x] = voll;
        }

        //=====R
        for (int y = 13; y < 18; y++)
        {
            for (int x = 56; x < 62; x++)
            {
                logo.get(y)[x] = voll;
            }
        }

        for (int x = 58; x < 60; x++)
        {
            logo.get(14)[x] = leer;
            logo.get(16)[x] = leer;
            logo.get(17)[x] = leer;
        }

        for (int x = 60; x < 62; x++)
        {
            logo.get(15)[x] = leer;
        }

        //=====T
        for (int x = 64; x < 70; x++)
        {
            logo.get(13)[x] = voll;
        }

        for (int y = 14; y < 18; y++)
        {
            for (int x = 66; x < 68; x++)
            {
                logo.get(y)[x] = voll;
            }
        }
    }

    public static void logoKlein()
    {
        Zelle leer = new Zelle();
        Zelle voll = new Zelle();

        leer.inhalt = " ";
        voll.inhalt = "\u2588";
        voll.farbe.r = 180;
        voll.farbe.g = 0;
        voll.farbe.b = 0;

        Zelle[] temp = new Zelle[26];

        //=====H
        for (int i = 0; i < temp.length; i++)
        {
            temp[i] = leer;
        }

        for (int i = 0; i < 5; i++)
        {
            logo_Klein.add(temp.clone());
        }

        for (int y = 0; y < 5; y++)
        {
            for (int x = 0; x < 2; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }

        for (int y = 2; y < 3; y++)
        {
            for (int x = 2; x < 6; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }

        for (int y = 0; y < 5; y++)
        {
            for (int x = 6; x < 8; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }

        //=====F
        for (int y = 0; y < 5; y++)
        {
            for (int x = 10; x < 12; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }

        for (int y = 0; y < 1; y++)
        {
            for (int x = 12; x < 18; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }

        for (int y = 2; y < 3; y++)
        {
            for (int x = 12; x < 16; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }

        //=====T
        for (int y = 0; y < 1; y++)
        {
            for (int x = 20; x < 26; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }

        for (int y = 1; y < 5; y++)
        {
            for (int x = 22; x < 24; x++)
            {
                logo_Klein.get(y)[x] = voll;
            }
        }
    }
}
