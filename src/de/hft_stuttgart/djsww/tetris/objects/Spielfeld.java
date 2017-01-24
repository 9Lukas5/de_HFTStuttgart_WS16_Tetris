/**
 * Don't ask me WHY EXACTLY we splitted this into two classes, but I guess it was, cause we wanted
 * to split the things done on the playfield sides and the ones happening IN the matrix itself.
 * 
 * So in short:
 * - We initialize the playfield matrix with empty cells.
 * - generateing the body around the matrix and the info area
 * - place the different words above the score and level counters
 * 
 * Then we can starting the game logic and use the methods implemented in this object
 * to lift/drop stones, validate positions and print frames to the terminal
 */


package de.hft_stuttgart.djsww.tetris.objects;

import com.googlecode.lanterna.terminal.Terminal;
import java.util.LinkedList;

public class Spielfeld extends Brett
{
    public      int punktestand = 0;    // counter for user score
    public      int level       = 0;    // counter for the current level == game speed

    public Spielfeld(int zeilen, int spalten, Terminal terminal)
    {
        super(zeilen, spalten, terminal, new Punkt());
        init();
    }

    public Spielfeld(int zeilen, int spalten, int streckung, Terminal terminal)
    {
        super(zeilen, spalten, streckung, terminal, new Punkt());
        init();
    }

    private void init()
    {
        // lokale Variablen
        Punkt cursor = super.start.clone();

        //============Eck links-oben, Rahmen links, Eck links-unten==========\\
        super.terminal.moveCursor(cursor.x, cursor.y);
        super.terminal.putCharacter('\u250c');       // links-oberes Eckfeld

        super.terminal.moveCursor(cursor.x, cursor.y);

        for (Zelle[] zeile : super.spielfeld)
        {
            cursor.y++;
            super.terminal.moveCursor(cursor.x, cursor.y);
            super.terminal.putCharacter('\u2502');   // linke Rahmenfelder
        }

        cursor.y++;
        super.terminal.moveCursor(cursor.x, cursor.y);
        super.terminal.putCharacter('\u2514');       // links-unters Eckfeld
        //===================================================================\\

        //==============Rahmen oben/unten bis Mitte + T-Stuecke==============\\
        cursor.x = super.start.x;
        cursor.y = super.start.y;
        cursor.x++;
        super.terminal.moveCursor(cursor.x, cursor.y);

        for (int i = 0; i < (spielfeld.get(0).length) * super.STRECKUNG; i++)
        {
            super.terminal.putCharacter('\u2500');  // obere Rahmenfelder bis
            cursor.x++;                             // Mitte
        }

        super.terminal.putCharacter('\u252c');      // oberes T-Stueck

        for (Zelle[] zeile : super.spielfeld)
        {
            cursor.y++;
            super.terminal.moveCursor(cursor.x, cursor.y);
            super.terminal.putCharacter('\u2502');  // mittlere Rahmenfelder
        }

        cursor.y++;
        super.terminal.moveCursor(cursor.x, cursor.y);
        super.terminal.putCharacter('\u2534');      // unteres T-Stueck

        cursor.x = super.start.x + 1;
        super.terminal.moveCursor(cursor.x, cursor.y);

        for (int i = 0; i < (spielfeld.get(0).length) * super.STRECKUNG; i++)
        {
            super.terminal.putCharacter('\u2500');  // untere Rahmenfelder bis
            cursor.x++;                             // Mitte
        }

        //===================================================================\\
        //============Rahmen oben/unten ab Mitte + Eckstuecke rechts=========\\
        int breite_InfoLeiste = (super.next_Stein.getFirst().length * super.STRECKUNG);
        if (breite_InfoLeiste < 16)
        {
            breite_InfoLeiste = 16;
        }

        cursor.x++;
        super.terminal.moveCursor(cursor.x, cursor.y);

        for (int i = 0; i < breite_InfoLeiste; i++)
        {
            super.terminal.putCharacter('\u2500');  // untere Rahmenfelder ab
            cursor.x++;                             // Mitte
        }

        super.terminal.putCharacter('\u2518');      // rechts-unteres Eckfeld

        for (Zelle[] zeile : super.spielfeld)
        {
            cursor.y--;
            super.terminal.moveCursor(cursor.x, cursor.y);
            super.terminal.putCharacter('\u2502');
        }

        cursor.y--;
        super.terminal.moveCursor(cursor.x, cursor.y);
        super.terminal.putCharacter('\u2510');      // rechts-oberes Eckfeld

        for (int i = 0; i < breite_InfoLeiste; i++)
        {
            cursor.x--;                             // obere Rahmenfelder ab
            super.terminal.moveCursor(cursor.x, cursor.y);
            super.terminal.putCharacter('\u2500');  // Mitte
        }
        //===================================================================\\

        super.infoBereich = cursor.x;

        cursor.x = super.infoBereich;
        cursor.y = super.start.y + 4;
        super.terminal.moveCursor(cursor.x, cursor.y);

        for (char temp : "Punkte".toCharArray())
        {
            super.terminal.putCharacter(temp);
        }
        erhoehePunkte(0);

        cursor.x = super.infoBereich;
        cursor.y = super.start.y + 9;
        super.terminal.moveCursor(cursor.x, cursor.y);

        for (char temp : "Level".toCharArray())
        {
            super.terminal.putCharacter(temp);
        }

        cursor.x = super.infoBereich;
        cursor.y = super.start.y + 13;
        super.terminal.moveCursor(cursor.x, cursor.y);

        for (char temp : "naechster Stein:".toCharArray())
        {
            super.terminal.putCharacter(temp);
        }
        erhoeheLevel();
    }

    public void erhoehePunkte(int zusaetzliche_Punkte)
    {
        this.punktestand += zusaetzliche_Punkte;    // simply add the additional points given as parameter

        // lokale Variablen
        Punkt cursor = new Punkt();

        cursor.x = this.infoBereich;
        cursor.y = super.start.y + 5;

        switch (Integer.toString(this.punktestand).length())
        {                                           // dependant on the length of the new score, set the cursor
            case 1:
                cursor.x++;

            case 2:
                cursor.x++;

            case 3:
                cursor.x++;

            case 4:
                cursor.x++;

            case 5:
                cursor.x++;
        }

        terminal.moveCursor(cursor.x, cursor.y);    // then move it to that point
        for (char temp : Integer.toString(this.punktestand).toCharArray())
        {
            terminal.putCharacter(temp);            // and print the new score into the info area
        }
    }

    public void erhoeheLevel()
    {
        // basically the same as the method above for the score, just that you can't give a paramter how much
        // the level should be raised. Level-Up is always only one step after the other.

        // lokale Variablen
        Punkt cursor = new Punkt();
        cursor.x = this.infoBereich;

        cursor.y = super.start.y + 10;
        super.terminal.moveCursor(cursor.x, cursor.y);
        for (int i = 0; i < 5; i++)
        {
            super.terminal.putCharacter(' ');
        }

        this.level++;

        cursor.x = this.infoBereich;
        switch (Integer.toString(this.level).length())
        {
            case 1:
                cursor.x++;

            case 2:
                cursor.x++;

            case 3:
                cursor.x++;

            case 4:
                cursor.x++;
        }

        terminal.moveCursor(cursor.x, cursor.y);
        for (char temp : Integer.toString(this.level).toCharArray())
        {
            super.terminal.putCharacter(temp);
        }
    }

    public boolean pruefeVollstaendigeZeilen()
    {
        // lokale Variablen

        boolean vollstaendig;   // this get's returned to the caller, so he *can use it if he/she wants to
        int counter = 0;        // counts how many rows are complete
        int neuePkt = 0;        // used to sum the new points the user got

        for (int i = 0; i < this.spielfeld.size(); i++)     // check every line
        {
            vollstaendig = true;                            // make boolean on row start true
            for (Zelle zeichen : this.spielfeld.get(i))     // and check every column of the current row
            {
                if (!zeichen.gesperrt)                      // if we found atleast one cell NOT locked
                {
                    vollstaendig = false;                   // the current row wasn't complete, so set the boolean to false
                }
            }

            if (vollstaendig)                               // if the current row is complete
            {
                this.spielfeld.remove(i);                   // remove it and add a new one on top with the same length as the others
                this.spielfeld.addFirst(new Zelle[this.spielfeld.get(0).length]);   // reference for the row length is the current first row

                for (int j = 0; j < this.spielfeld.getFirst().length; j++)
                {
                    this.spielfeld.getFirst()[j] = new Zelle(); // fill all columns of the now new inserted first row with new cells
                }
                counter++;                                  // higher the counter complete rows in this cycle
                neuePkt += 10;                              // each complete row adds 10 points
            }
        }
        erhoehePunkte(neuePkt * counter);                   // after we checked all rows, we multiply the summed points with the counter of complete rows
        return counter != 0 && neuePkt != 0;                // if the counter and point variables aren't zero, we had atlest one complete row and can return true
    }
}

//==========================================================================================================================\\
//                                                                                                                          \\
//==========================================================================================================================\\

class Brett
{
    protected           Terminal            terminal;                           // here we store the pointer to the terminal instance we get from the constructor
    public              LinkedList<Zelle[]> spielfeld   = new LinkedList<>();   // THIS is our playfield matrix
    protected           LinkedList<Zelle[]> next_Stein  = new LinkedList<>();   // that's only a four * four matrix to show the next stone in pipeline
    protected   final   int                 STRECKUNG;                          // this is two get the output on terminal in correct aspect ratio
    protected           String              leer        = "";                   // every empty cell get's this as content string
    protected           String              gefuellt    = "";                   // and every cell with stone on it, get's this one
    
    protected   int     infoBereich;                                            // this stores the x-Coordinate from the first column out on terminal where we can print information
    protected   final   Punkt               start;                              // this sets marks the upper left corner, on this point everything else is based

    public Brett(int zeilen, int spalten, Terminal terminal, Punkt p)
    {
        this.STRECKUNG = 1;
        this.terminal = terminal;
        this.start = p;
        init(zeilen, spalten);
    }

    public Brett(int zeilen, int spalten, int streckung, Terminal terminal, Punkt p)
    {
        this.STRECKUNG = streckung;
        this.terminal = terminal;
        this.start = p;
        init(zeilen, spalten);
    }

    /**
     * The playfield is designed flexible. You could create a new instance with a different count of rows and/or columns.
     * At first we had a check implemented on program start, if the user maybe started from the console and gave us a few
     * arguments. If yes, we generated the playfield matrix according to the given params if possible.
     * 
     * As we should publish it now, and insert a Logo and other information, we decided to remove this possibility in our
     * own Tetris, because it would on genertion overwrite the placed information.
     * 
     */

    private void init(int zeilen, int spalten)
    {
        for (int i = 0; i < this.STRECKUNG; i++)
        {
            leer += " ";                        // fill our empty and locked content Strings with the correct
            gefuellt += "\u2588";               // count of chars for the choosen extension
        }

        for (int i = 0; i < zeilen; i++)        // fill every row with a new cell array with the length of the given columns count
        {
            this.spielfeld.add(new Zelle[spalten]);
        }

        for (Zelle[] y : this.spielfeld)        // iterate through each row
        {
            for (int x = 0; x < y.length; x++)
            {
                y[x] = new Zelle();             // and fill each column with a new cell and give the newly created cell
                y[x].inhalt = this.leer;        // as content the generated empty String
            }
        }

        for (int i = 0; i < 4; i++)
        {
            this.next_Stein.add(new Zelle[4]);  // the matrix for the next stone is always four*four
        }

        for (Zelle[] y : this.next_Stein)
        {
            for (int x = 0; x < y.length; x++)  // the same as above, fill each field with a new empty cell and the empty String
            {
                y[x] = new Zelle();
                y[x].inhalt = this.leer;
            }
        }
    }

    public void erneuereAusgabe()
    {
        // lokale Variablen

        Punkt cursor = this.start.clone();                              // get a clone of the start point
        cursor.x++;                                                     // and go one field right and one down, that the cursor is
        cursor.y++;                                                     // on the top left matrix field (terminal wise)

        for (Zelle[] zeile : this.spielfeld)                            // iterate through the matrix rows
        {
            this.terminal.moveCursor(cursor.x, cursor.y);               // tell terminal where we want to print next

            for (Zelle zeichen : zeile)                                 // iterate through the columns of current row
            {                                                           // set for each cell the color reported
                this.terminal.applyForegroundColor(zeichen.farbe.r, zeichen.farbe.g, zeichen.farbe.b);
                for (char character : zeichen.inhalt.toCharArray())     // and print the content reported
                {
                    this.terminal.putCharacter(character);
                }
            }
            cursor.y++;                                                 // after each row, raise cursors y and repeat until we iterated all rows
        }

        cursor.x = this.infoBereich;                                    // set cursor to info area start x
        cursor.y = 14;                                                  // and y to line 14, as we print the next stone from here

        for (Zelle[] zeile : this.next_Stein)                           // same procedure as for the playfield matrix
        {
            this.terminal.moveCursor(cursor.x, cursor.y);

            for (Zelle zeichen : zeile)
            {
                this.terminal.applyForegroundColor(zeichen.farbe.r, zeichen.farbe.g, zeichen.farbe.b);
                for (char character : zeichen.inhalt.toCharArray())
                {
                    this.terminal.putCharacter(character);
                }
            }
            cursor.y++;
        }

        this.terminal.applyForegroundColor(255, 255, 255);              // after we printed the next frame to the terminal, reset terminal foreground color to white
    }

    public void setzeAktivenKoerper(Punkt feld, Farbe farbe)
    {
        // lokale Variablen

        Zelle temp = this.spielfeld.get(feld.y)[feld.x];

        temp.farbe = farbe;                                             // tell the given cell the new color
        temp.inhalt = this.gefuellt;                                    // and insert the String for filled cells. But DO NOT lock it. We're still moving it around
    }

    public void aendereNaechstenStein(Tetromino nextStein)
    {
        Punkt[] next = nextStein.getKoordinaten(new Punkt());
        int position = -1;                                              // this is basically a copy of the validation method from the main source file

        while (position != 0)                                           // but that was the easiest way I saw to place the next stone correctly into the four*four matrix
        {
            for (Punkt zeichen : next)
            {
                if (zeichen.x < 0)
                {
                    position = 1;
                    break;
                }

                if (zeichen.x > 3)
                {
                    position = 3;
                    break;
                }

                if (zeichen.y < 0)
                {
                    position = 2;
                    break;
                }

                if (zeichen.y > 3)
                {
                    position = 4;
                    break;
                }

                position = 0;
            }

            switch (position)
            {
                case 1:
                    for (Punkt p : next)
                    {
                        p.x++;
                    }
                    break;

                case 2:
                    for (Punkt p : next)
                    {
                        p.y++;
                    }
                    break;

                case 3:
                    for (Punkt p : next)
                    {
                        p.x--;
                    }
                    break;

                case 4:
                    for (Punkt p : next)
                    {
                        p.y--;
                    }
                    break;
            }
        }

        for (int i = 0; i < 4; i++)
        {
            next_Stein.removeLast();
            next_Stein.addFirst(new Zelle[4]);
        }

        for (Zelle[] zeile : next_Stein)
        {
            for (int i = 0; i < zeile.length; i++)
            {
                zeile[i] = new Zelle();
            }
        }

        for (int i = 0; i < next_Stein.size(); i++)
        {
            Zelle zeichen;

            zeichen = next_Stein.get(next[i].y)[next[i].x];
            zeichen.farbe = nextStein.farbe.clone();
            zeichen.inhalt = this.gefuellt;
        }
    }

    public void hebeAktivenKoerper(Punkt feld)
    {
        // lokale Variablen

        Zelle temp = this.spielfeld.get(feld.y)[feld.x];

        temp.farbe = new Farbe();                                       // create a new instance of color as stock values are white
        temp.inhalt = this.leer;                                        // insert the String for empty
    }

    public void sperreZelle(Punkt feld, Farbe farbe)
    {
        // lokale Variablen

        Zelle temp = this.spielfeld.get(feld.y)[feld.x];

        temp.gesperrt = true;                                           // calling this method, means the stone has reached it's final position. Therefore, now we
        temp.farbe = farbe;                                             // lock the cells apart from applying the given color and insert the String for filled cells.
        temp.inhalt = this.gefuellt;
    }
}
