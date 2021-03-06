package de.hft_stuttgart.djsww.tetris;

import com.googlecode.lanterna.input.Key;
import static de.hft_stuttgart.djsww.tetris.Tetris.*;
import java.io.*;
import java.util.LinkedList;

public class Endbildschirm
{

    static int                  x_Offset    = 0;
    static int                  y_Offset    = 0;
    static String               name;
    static LinkedList<Integer>  highscores  = new LinkedList<>();
    static LinkedList<String>   namen       = new LinkedList<>();

    public static void beendeSpiel()
    {
        // lokale Variablen
        String game_Over = "GAME OVER";

        terminal.clearScreen();

        // let game over fly into the screen
        terminal.applyForegroundColor(255, 0, 0);
        x_Offset = 46;
        for (int i = 0; y_Offset < 6; y_Offset++)
        {
            terminal.moveCursor(x_Offset, y_Offset);
            for (char temp : game_Over.toCharArray())
            {
                terminal.putCharacter(temp);
            }
            try
            {
                Thread.sleep(25);
            }
            catch (InterruptedException e)
            {
                // Nothing needed to do, Thread sleep was just called
                // for optical reasons. Has no impact onto the function itself
                e.printStackTrace();
            }

            if (y_Offset < 4)
            {
                terminal.moveCursor(x_Offset, y_Offset);
                for (int j=0; j < game_Over.length(); j++)
                {
                    terminal.putCharacter(' ');
                }
            }
            y_Offset++;
        }

        terminal.applyForegroundColor(255, 255, 255);
        y_Offset = 7;
        x_Offset = 25;

        terminal.moveCursor(x_Offset, y_Offset);

        for (char temp : "Deine Punkte: ".toCharArray())
        {
            terminal.putCharacter(temp);
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                // Nothing needed to do, Thread sleep was just called
                // for optical reasons. Has no impact onto the function itself
                e.printStackTrace();
            }
        }

        terminal.applyForegroundColor(255, 140, 0);

        for (char temp : String.valueOf(spielfeld.punktestand).toCharArray())
        {
            terminal.putCharacter(temp);
        }

        terminal.applyForegroundColor(255, 255, 255);
        x_Offset += 8;
        y_Offset += 2;
        terminal.moveCursor(x_Offset, y_Offset);

        for (char temp : "Name: ".toCharArray())
        {
            terminal.putCharacter(temp);
            x_Offset++;
        }

        enterName();
        leseWerte();
        rangliste();
        if (name != null)
        {
            schreibeWerte();
        }

        terminal.moveCursor(35, 28);
        for (char temp : "Zum Beenden beliebige Taste druecken...".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        Key eingabe;

        while (true)
        {
            eingabe = terminal.readInput();
            if (eingabe != null)
            {
                System.exit(0);
            }
        }
    }

    public static void enterName()
    {
        name = "";  // init name empty

        Key eingabe;

        while (true)
        {
            eingabe = terminal.readInput();

            if (eingabe != null)
            {
                if (eingabe.getKind() == Key.Kind.Enter)    // leave input loop
                {
                    if (name.equals(""))// check if an empty name was given, as we can't use an empty name String to save into the highscores file
                    {
                        name += " ";
                    }
                    break;
                }

                if (eingabe.getKind() == Key.Kind.Escape)
                {
                    name = null;        // if the playes scores shouldn't be saved, set name null and leave input loop
                    break;
                }

                if (eingabe.getKind() == Key.Kind.NormalKey)
                {
                    name += eingabe.getCharacter(); // add the given character at the end of the current name String
                }

                if (eingabe.getKind() == Key.Kind.Backspace)
                {
                    char[] temp = name.toCharArray();           // put current name String into char array
                    name = "";                                  // clear current name String

                    for (int i = 0; i < temp.length - 1; i++)   // put back all chars except the last one
                    {
                        name += temp[i];
                    }

                }

                terminal.moveCursor(x_Offset, y_Offset);
                for (int i = 0; i < 30; i++)
                {
                    terminal.putCharacter(' ');         // overwrite with spaces
                }

                terminal.moveCursor(x_Offset, y_Offset);
                for (char temp : name.toCharArray())
                {
                    terminal.putCharacter(temp);        // print the new state of the name String
                }

            }
        }
    }

    public static void leseWerte()
    {
        // local vars
        String[] bestenliste = new String[10];  // size ten as the highscore list should contain only the best 10
        String[] splitted;                      // after read, split score and name and store it in here
        WhereAmI myPath = new WhereAmI();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(myPath.myPath + "values.conf"), "utf-8"))
                )
        {
            String line;
            for (int i=0; (line = br.readLine()) != null && i < 10; i++)    // read until file end OR 10 lines where read in
            {
                bestenliste[i] = line;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (String temp : bestenliste)
        {
            if (temp != null)
            {
                splitted = temp.split(":");                     // split all read lines
                highscores.add(Integer.valueOf(splitted[0]));   // first ist score
                namen.add(splitted[1]);                         // and second one is name
            }
        }

    }

    public static void rangliste()
    {
        // lokale Variablen
        terminal.clearScreen();

        for (int i = 0; i < highscores.size(); i++)
        {
            if (name == null)   // if name is null, none wanted to be entered, therefore stop changing the list
            {
                break;
            }
            if (spielfeld.punktestand > highscores.get(i))  // if the player made a score higher than one on the list
            {
                highscores.add(i, spielfeld.punktestand);   // add this new score at that position
                namen.add(i, name);                         // aswell as the name typed in
                break;
            }

            if (spielfeld.punktestand <= highscores.getLast())  // if the played score is the same or lower than the last one
            {
                highscores.addLast(spielfeld.punktestand);      // add the played score behind the last one
                namen.addLast(name);                            // aswell as the name typed in
                break;
            }
        }

        if (highscores.size() == 0 && name != null) // if the highscores list is emtpy
        {
            highscores.add(spielfeld.punktestand);  // simply add the played score
            namen.add(name);                        // and given name as first highscore
        }

        x_Offset = 45;
        y_Offset = 2;

        terminal.moveCursor(x_Offset, y_Offset);
        terminal.applyForegroundColor(255, 255, 255);

        for (char temp : "Bestenliste".toCharArray())
        {
            terminal.putCharacter(temp);
        }

        y_Offset = 5;

        for (int i = 0; i < 10; i++)
        {
            if (i >= highscores.size())
            {
                break;
            }

            x_Offset = 40;

            terminal.moveCursor(x_Offset, y_Offset);
            if (highscores.get(i) == spielfeld.punktestand && namen.get(i).equals(name))
            {                                               // if highscore AND name from the list matches the player values
                terminal.applyForegroundColor(255, 140, 0); // change foreground color to highlight just played score on list
            }

            for (char temp : String.valueOf(highscores.get(i)).toCharArray())
            {
                terminal.putCharacter(temp);
            }

            x_Offset = 55;

            terminal.moveCursor(x_Offset, y_Offset);

            for (char temp : namen.get(i).toCharArray())
            {
                terminal.putCharacter(temp);
            }
            y_Offset++;

            terminal.applyForegroundColor(255, 255, 255);
        }

    }

    public static void schreibeWerte()
    {
        // vars
        WhereAmI myPath = new WhereAmI();
        File file = new File(myPath.myPath + "values.conf");

        try (PrintWriter out = new PrintWriter(file, "utf-8"))
        {
            for (int i = 0; i < highscores.size(); i++)
            {
                out.print(highscores.get(i));   // write score
                out.print(":");                 // write seperator
                out.println(namen.get(i));      // write name
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            System.err.println("Dude what the heck of a System doesn't support utf-8??");
        }
    }
}

/**
 * 
 * This inner class is used to get the directory in an absolute path
 * in which our running jar file is placed.
 * Using this, because we want to have the highscores file searched and placed
 * always into the dir the jar is, OS independant.
 */
class WhereAmI
{
    String myPath;

    public WhereAmI()
    {
        String[] temp;

        myPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        if (System.getProperty("os.name").indexOf("win") >= 0 || System.getProperty("os.name").indexOf("Win") >= 0)
        {
            temp = myPath.split("/", 2);
            myPath = temp[1];
        }
        else
        {
            temp = myPath.split(":", 2);
            myPath = temp[1];
        }

        if (myPath.contains(".jar"))
        {
            temp = myPath.split("/");
            myPath = "";
            for (int i=0; i < temp.length - 1; i++)
            {
                myPath += temp[i];
                myPath += "/";
            }
        }
        else
        {
            myPath = "";
        }
    }
}
