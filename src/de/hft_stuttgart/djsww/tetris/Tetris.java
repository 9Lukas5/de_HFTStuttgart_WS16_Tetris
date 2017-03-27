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

    static String               version         = "v1.1.1";                // version String shown on program start

    static Terminal             terminal        = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
    static Spielfeld            spielfeld;                                  // playfield variable
    static Tetromino            akt_Stein;                                  // current Tetronimo variable
    static Tetromino            next_Stein      = new Tetromino_I();        // next    Tetronimo variable

    static Punkt                start_Pkt       = new Punkt();              // point from which the other three coordinates of the active Tetronimo are calculated

    static int                  zyklus          = 0;                        // flow counter
    static int                  verzoegerung    = 750;                      // cycle length
    static int                  vollstaendig    = 0;                        // counter for completed rows

    static TetrisMusikPlayer    music           = new TetrisMusikPlayer();  // new instance of the configured music player

    public static void main(String[] args)
    {
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);

        Startbildschirm.starteSpiel();                          // show startscreen
        music.startBackgroundMusik();                           // start background music as loop

        spielfeld = new Spielfeld(20, 10, 2, terminal);         // generate a new playfield instance with 20 rows, 10 columns, aspect ratio and a pointer to the used terminal instance

        neuerStein();                                           // generate a new random Tetronimo
        neuerStein();                                           // and a second one, that the first Tetronimo in game is a random one yet

        ingame:
        while (true)                                            // enter ingame
        {
            Key eingabe = terminal.readInput();                 // read if an input is done
            Punkt naechster_StartPkt = start_Pkt.clone();       // generate a new copy of the current start point
            int pruefung = -1;                                  // init with -1, as 0 would be all fine. But we don't know yet if it's all fine

            zyklus++;                                           // higher flow counter

            if (eingabe != null && zyklus % verzoegerung != 0)  // if an input was made AND the current flow isn't an complete cycle
            {
                if (eingabe.getKind() == Key.Kind.NormalKey)
                {
                    checkHidden(eingabe);
                }

                if (eingabe.getKind() == Key.Kind.ArrowLeft)
                {
                    naechster_StartPkt.x--;                     // move next start coordinate one column left
                }

                if (eingabe.getKind() == Key.Kind.ArrowRight)
                {
                    naechster_StartPkt.x++;                     // move next start coordinate one column right
                }

                if (eingabe.getKind() == Key.Kind.ArrowDown)
                {
                    naechster_StartPkt.y++;                     // move next start coordinate one row down
                }

                if (Character.toUpperCase(eingabe.getCharacter()) == 'P')
                {
                    pause();                                    // pause game
                }

                if (Character.toUpperCase(eingabe.getCharacter()) == 'M')
                {
                    music.unMute();                             // mute/unmute sounds
                }

                if (eingabe.getKind() == Key.Kind.ArrowUp)
                {
                    dreheStein();                               // rotate active stone
                    continue ingame;                            // afterwards start a new flow, as on rotating the check is done seperately
                }

                if (eingabe.getKind() == Key.Kind.Escape)
                {
                    break ingame;                               // on escape end the ingame
                }
            }

            if (zyklus % verzoegerung == 0)                     // if a cycle is complete, move down the active stone automatically
            {
                naechster_StartPkt.y++;
            }

            while (pruefung != 0)
            {                                                   // check the new desired start coordinate
                switch (pruefung = pruefeNeuePos(naechster_StartPkt.clone()))
                {
                    case 1:
                    case 2:
                    case -2:
                        continue ingame;                        // for all these cases, the desired move is impossible and new flow gets started

                    case -1:                                    // that means, the active stone is on it's end position. Therefore
                        brenneStein();                          // write it finally into the playfield matrix
                        switch (neuerStein())                   // generate a new stone
                        {
                            case -1:                                // if the generation returns -1, it was impossible to put the new stone on playfield top
                                break ingame;                       // that means: GAME OVER! => end ingame

                            case 0:                                 // if the generation returned 0, all is fine, and we have to replace
                                naechster_StartPkt = start_Pkt;     // the new start point with the current one again. Otherwise we would trip us up
                                break;                              // a few lines down
                        }
                        pruefung = 0;                           // set check to 0 after a new stone was generated and successfully placed on the playfields top middle
                        break;
                }
            }

            hebeStein();                                        // lift active stone from playfield matrix
            start_Pkt = naechster_StartPkt;                     // change start coordinate to new and checked start coordinate
            setzeStein();                                       // drop active stone at new position to the playfield matrix

            spielfeld.erneuereAusgabe();                        // refresh playfield matrix in terminal

            try
            {
                Thread.sleep(1);
            } catch (InterruptedException e)
            {

            }
        }
        music.stopBackgroundMusik();    // after ingame stop background music

        Endbildschirm.beendeSpiel();    // and start the end
    }

    public static int neuerStein()
    {
        // lokale Variablen
        int temp = spielfeld.spielfeld.getFirst().length;   // get number of columns
        int startX;
        int startY;
        Tetromino[] auswahl = new Tetromino[7];
        int pruefung = -1;
        Punkt naechster_StartPkt;

        auswahl[0] = new Tetromino_I();                     // fill array with each stone
        auswahl[1] = new Tetromino_J();
        auswahl[2] = new Tetromino_L();
        auswahl[3] = new Tetromino_O();
        auswahl[4] = new Tetromino_S();
        auswahl[5] = new Tetromino_T();
        auswahl[6] = new Tetromino_Z();

        akt_Stein = next_Stein;                             // make next stone active
        next_Stein = auswahl[randInt(0, 6)];                // make a random from the array the next stone
        spielfeld.aendereNaechstenStein(next_Stein);        // show new next stone on the lower right from playfield matrix

        startX = (temp / 2) - 1;                            // calculate middle of columns
        startY = 0;
        start_Pkt.x = startX;                               // set start coordinate to the top middle again for the new stone
        start_Pkt.y = startY;

        if (spielfeld.pruefeVollstaendigeZeilen())                      // if we have completed any rows
        {
            vollstaendig++;                                 // higher counter
            music.playSuccess();                            // play a success sound
            if (verzoegerung > 150 && vollstaendig == spielfeld.level)  // and the cycle length is higher than 150ms AND we have
            {                                                           // as many rows completed as the level we are currently on
                verzoegerung -= 50;                             // lower cycle length
                spielfeld.erhoeheLevel();                       // and higher level shown on terminal
                vollstaendig = 0;                               // reset completed row counter
            }
        }

        naechster_StartPkt = start_Pkt.clone();             // make a clone of the new start coordinate
        while (pruefung != 0)                               // validate it
        {
            switch (pruefung = pruefeNeuePos(naechster_StartPkt.clone()))
            {
                case 3:
                    naechster_StartPkt.y++;                 // if the stone is not fully on the field, try again one row deeper
                    break;

                case -1:                                    // in those two cases, return -1, which get's handled as GAME OVER
                case -2:                                    // we just can't break the ingame loop directly from here.
                    return -1;
            }
        }
        start_Pkt = naechster_StartPkt;                     // if validation was successful, make possibly corrected start coordinate the used one
        return 0;                                           // and return 0
    }

    public static int pruefeNeuePos(Punkt neuer_Startpkt)
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(neuer_Startpkt.clone());

        for (Punkt feld : neue_Koord)
        {
            if (feld.x < 0)
            {
                return 1;   // too far to the left
            }

            if (feld.x > spielfeld.spielfeld.getFirst().length - 1)
            {
                return 2;   // too far to the right
            }

            if (feld.y < 0)
            {
                return 3;   // too far up
            }

            if (feld.y > spielfeld.spielfeld.size() - 1)
            {
                return -1;  // too far down
            }

            if (spielfeld.spielfeld.get(feld.y)[feld.x].gesperrt)
            {
                if (start_Pkt.y < neuer_Startpkt.y)
                {
                    return -1;  // sat on a locked field from above
                } else
                {
                    return -2; // sat on a locked field from side
                }
            }
        }
        return 0;
    }

    public static void hebeStein()
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(start_Pkt.clone());   // get all four coordinates of the active stone at current position

        for (Punkt feld : neue_Koord)
        {
            spielfeld.hebeAktivenKoerper(feld);                             // lift the stone from playfield matrix
        }
    }

    public static void setzeStein()
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(start_Pkt.clone());   // get all four coordinates of the active stone

        for (Punkt feld : neue_Koord)
        {
            spielfeld.setzeAktivenKoerper(feld, akt_Stein.farbe);           // drop stone on playfield matrix
        }
    }

    public static void brenneStein()
    {
        Punkt[] neue_Koord = akt_Stein.getKoordinaten(start_Pkt.clone());   // get all four coordinates of the active stone

        for (Punkt feld : neue_Koord)
        {
            spielfeld.sperreZelle(feld, akt_Stein.farbe);                   // drop stone on playfield matrix and lock those fields
        }
    }

    public static void dreheStein()
    {
        Punkt naechster_StartPkt = start_Pkt.clone();   // get a clone of the current start point
        int pruefung = -1;

        hebeStein();                                    // lift stone from playfield matrix
        akt_Stein.drehe();                              // rotate it

        while (pruefung != 0)                           // check if the rotation is a possible move
        {
            switch (pruefung = pruefeNeuePos(naechster_StartPkt.clone()))
            {
                case 1:
                    naechster_StartPkt.x += 1;          // if we got too far left through the rotation, move right
                    break;

                case 2:
                    naechster_StartPkt.x -= 1;          // if we got too far right through the rotation, move left
                    break;

                case 3:
                    naechster_StartPkt.y += 1;          // if we got too far up through the rotation, move down
                    break;

                case -1:                                // if we would get on a locked field by rotation, we can't rotate
                case -2:                                // therefore rotate back to original position
                    akt_Stein.dreheZurueck();
                    return;

                case 0:
                    start_Pkt = naechster_StartPkt;     // if the validation was successful, set the new start point 
                    break;
            }
        }

        setzeStein();                                   // drop stone back to playfield matrix in its new rotation
        spielfeld.erneuereAusgabe();                    // print next frame to terminal
    }

    /**
     * Don't use this, it's not ment for fair players :p
     * If you still use it, don't tell anyone about it. Shhhh!
     */
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
        for (char temp : "Pause".toCharArray())     // print 'Pause' below the playfield
        {
            terminal.putCharacter(temp);
        }

        music.stopBackgroundMusik();                // stop background music
        while (true)                                // and enter an loop, until 'P' is pressed again, so the game is "paused"
        {
            eingabe = terminal.readInput();
            if (eingabe != null && Character.toUpperCase(eingabe.getCharacter()) == 'P')
            {
                break;                              // after second 'P' break the "pause"
            }
        }

        music.startBackgroundMusik();               // start background music again
        terminal.moveCursor(8, 22);                 // and remove 'Pause' from below the playfield
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
