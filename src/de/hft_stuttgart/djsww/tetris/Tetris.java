package de.hft_stuttgart.djsww.tetris;

// imports
import de.hft_stuttgart.djsww.tetris.objects.*;
import java.nio.charset.Charset;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.Key;
import java.util.Random;

public class Tetris
{

    static String               version         = "v.1.0.0";

    static Terminal             terminal        = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
    static Spielfeld            spielfeld;
    static Tetromino            akt_Stein;
    static Tetromino            next_Stein      = new Tetromino_I();

    static Punkt                start_Pkt       = new Punkt();

    static int                  zyklus          = 0;
    static int                  verzoegerung    = 750;

    static TetrisMusikPlayer    music           = new TetrisMusikPlayer();

    public static void main(String[] args)
    {
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);

        Startbildschirm.starteSpiel();
        music.startBackgroundMusik();

        spielfeld = new Spielfeld(20, 10, 2, terminal);

        neuerStein();
        neuerStein();

        ingame:
        while (true)
        {
            Key eingabe = terminal.readInput();
            Punkt naechster_StartPkt = start_Pkt.clone();
            int pruefung = -1;

            zyklus++;

            if (eingabe != null && zyklus % verzoegerung != 0)
            {
                if (eingabe.getKind() == Key.Kind.NormalKey)
                {
                    checkHidden(eingabe);
                }

                if (eingabe.getKind() == Key.Kind.ArrowLeft)
                {
                    naechster_StartPkt.x--;
                }

                if (eingabe.getKind() == Key.Kind.ArrowRight)
                {
                    naechster_StartPkt.x++;
                }

                if (eingabe.getKind() == Key.Kind.ArrowDown)
                {
                    naechster_StartPkt.y++;
                }

                if (Character.toUpperCase(eingabe.getCharacter()) == 'P')
                {
                    pause();
                }

                if (Character.toUpperCase(eingabe.getCharacter()) == 'M')
                {
                    music.unMute();
                }

                if (eingabe.getKind() == Key.Kind.ArrowUp)
                {
                    dreheStein();
                    continue ingame;
                }

                if (eingabe.getKind() == Key.Kind.Escape)
                {
                    break ingame;
                }
            }

            if (zyklus % verzoegerung == 0)
            {
                naechster_StartPkt.y++;
            }

            while (pruefung != 0)
            {
                switch (pruefung = pruefeNeuePos(naechster_StartPkt.clone()))
                {
                    case 1:
                    case 2:
                    case -2:
                        continue ingame;

                    case -1:
                        brenneStein();
                        switch (neuerStein())
                        {
                            case -1:
                                break ingame;

                            case 0:
                                naechster_StartPkt = start_Pkt;
                                break;
                        }
                        pruefung = 0;
                        break;
                }
            }

            hebeStein();
            start_Pkt = naechster_StartPkt;
            setzeStein();

            spielfeld.erneuereAusgabe();

            try
            {
                Thread.sleep(1);
            } catch (InterruptedException e)
            {

            }
        }
        music.stopBackgroundMusik();

        Endbildschirm.beendeSpiel();
    }

    public static int neuerStein()
    {
        // lokale Variablen
        int temp = spielfeld.spielfeld.getFirst().length;
        int startX;
        int startY;
        Tetromino[] auswahl = new Tetromino[7];
        int pruefung = -1;
        Punkt naechster_StartPkt;

        auswahl[0] = new Tetromino_I();
        auswahl[1] = new Tetromino_J();
        auswahl[2] = new Tetromino_L();
        auswahl[3] = new Tetromino_O();
        auswahl[4] = new Tetromino_S();
        auswahl[5] = new Tetromino_T();
        auswahl[6] = new Tetromino_Z();

        akt_Stein = next_Stein;
        next_Stein = auswahl[randInt(0, 6)];
        spielfeld.aendereNaechstenStein(next_Stein);

        startX = (temp / 2) - 1;
        startY = 0;
        start_Pkt.x = startX;
        start_Pkt.y = startY;

        if (spielfeld.pruefeVollstaendigeZeilen())
        {
            if (verzoegerung > 150)
            {
                verzoegerung -= 50;
                spielfeld.erhoeheLevel();
            }
            music.playSuccess();
        }

        naechster_StartPkt = start_Pkt.clone();
        while (pruefung != 0)
        {
            switch (pruefung = pruefeNeuePos(naechster_StartPkt.clone()))
            {
                case 3:
                    naechster_StartPkt.y++;
                    break;

                case -1:
                case -2:
                    return -1;
            }
        }
        start_Pkt = naechster_StartPkt;
        return 0;
    }

    public static int pruefeNeuePos(Punkt neuer_Startpkt)
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(neuer_Startpkt.clone());

        for (Punkt feld : neue_Koord)
        {
            if (feld.x < 0)
            {
                return 1;   // zu weit links
            }

            if (feld.x > spielfeld.spielfeld.getFirst().length - 1)
            {
                return 2;   // zu weit rechts
            }

            if (feld.y < 0)
            {
                return 3;   // zu weit oben
            }

            if (feld.y > spielfeld.spielfeld.size() - 1)
            {
                return -1;  // zu weit unten
            }

            if (spielfeld.spielfeld.get(feld.y)[feld.x].gesperrt)
            {
                if (start_Pkt.y < neuer_Startpkt.y)
                {
                    return -1;  // von oben auf gesperrtes Feld aufgesessen
                } else
                {
                    return -2; // von der Seite beim drehen auf gesperrtes
                }
            }                   // Feld aufgesessen
        }
        return 0;
    }

    public static void hebeStein()
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(start_Pkt.clone());

        for (Punkt feld : neue_Koord)
        {
            spielfeld.hebeAktivenKoerper(feld);
        }
    }

    public static void setzeStein()
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(start_Pkt.clone());

        for (Punkt feld : neue_Koord)
        {
            spielfeld.setzeAktivenKoerper(feld, akt_Stein.farbe);
        }
    }

    public static void brenneStein()
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(start_Pkt.clone());

        for (Punkt feld : neue_Koord)
        {
            spielfeld.sperreZelle(feld, akt_Stein.farbe);
        }
    }

    public static void dreheStein()
    {
        Punkt naechster_StartPkt = start_Pkt.clone();
        int pruefung = -1;

        hebeStein();
        akt_Stein.drehe();

        while (pruefung != 0)
        {
            switch (pruefung = pruefeNeuePos(naechster_StartPkt.clone()))
            {
                case 1:
                    naechster_StartPkt.x += 1;
                    break;

                case 2:
                    naechster_StartPkt.x -= 1;
                    break;

                case 3:
                    naechster_StartPkt.y += 1;
                    break;

                case -1:
                case -2:
                    akt_Stein.dreheZurueck();
                    return;

                case 0:
                    start_Pkt = naechster_StartPkt;
                    break;
            }
        }

        setzeStein();
        spielfeld.erneuereAusgabe();
    }

    public static void checkHidden(Key eingabe)
    {
        if (Character.toUpperCase(eingabe.getCharacter()) == 'I')
        {
            next_Stein = new Tetromino_I();
        }
        if (Character.toUpperCase(eingabe.getCharacter()) == 'J')
        {
            next_Stein = new Tetromino_J();
        }
        if (Character.toUpperCase(eingabe.getCharacter()) == 'L')
        {
            next_Stein = new Tetromino_L();
        }
        if (Character.toUpperCase(eingabe.getCharacter()) == 'O')
        {
            next_Stein = new Tetromino_O();
        }
        if (Character.toUpperCase(eingabe.getCharacter()) == 'S')
        {
            next_Stein = new Tetromino_S();
        }
        if (Character.toUpperCase(eingabe.getCharacter()) == 'T')
        {
            next_Stein = new Tetromino_T();
        }
        if (Character.toUpperCase(eingabe.getCharacter()) == 'Z')
        {
            next_Stein = new Tetromino_Z();
        }

    }

    public static void pause()
    {
        Key eingabe;

        terminal.moveCursor(8, 22);
        for (char temp : "Pause".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        music.stopBackgroundMusik();
        while (true)
        {
            eingabe = terminal.readInput();
            if (eingabe != null && Character.toUpperCase(eingabe.getCharacter()) == 'P')
            {
                break;
            }
        }

        music.startBackgroundMusik();
        terminal.moveCursor(8, 22);
        for (char temp : "     ".toCharArray())
        {
            terminal.putCharacter(temp);
        }
    }

    public static int randInt(int min, int max)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
