package de.hft_stuttgart.djsww.tetris;

import com.googlecode.lanterna.input.Key;
import static de.hft_stuttgart.djsww.tetris.Tetris.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class Endbildschirm
{
		static int x_Offset = 0;
		static int y_Offset = 0;
		static String name;
		static LinkedList<Integer> highscores = new LinkedList<>();
		static LinkedList<String>  namen = new LinkedList<>();
		
	public static void beendeSpiel()
	{
		// lokale Variablen
		
		String game_Over = "GAME OVER";
		
		terminal.clearScreen();
		
		try
		{
			terminal.applyForegroundColor(255, 0, 0);
			x_Offset = 46;
			for (int i=0; y_Offset < 6; y_Offset++)
			{
				terminal.moveCursor(x_Offset, y_Offset);
				for (char temp: game_Over.toCharArray())
				{
					terminal.putCharacter(temp);
				}
				Thread.sleep(25);
				
				if (y_Offset < 4)
				{
					terminal.moveCursor(x_Offset, y_Offset);
					for (char temp: game_Over.toCharArray())
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
			
			for (char temp: "Deine Punkte: ".toCharArray())
			{
				terminal.putCharacter(temp);
				Thread.sleep(10);
			}
			
			terminal.applyForegroundColor(255, 140, 0);
			
			for (char temp: String.valueOf(spielfeld.punktestand).toCharArray())
			{
				terminal.putCharacter(temp);
			}
			
			terminal.applyForegroundColor(255, 255, 255);
			x_Offset += 8;
			y_Offset += 2;
			terminal.moveCursor(x_Offset, y_Offset);
			
			for (char temp: "Name: ".toCharArray())
			{
				terminal.putCharacter(temp);
				x_Offset++;
			}
			
			enterName();
			leseWerte();
			rangliste();
            if (name != null) schreibeWerte();
            
            terminal.moveCursor(35, 28);
            for (char temp: "Zum Beenden beliebige Taste druecken...".toCharArray())
            {
                terminal.putCharacter(temp);
            }
            
            Key eingabe;
            
            while (true)
            {
                eingabe = terminal.readInput();
                if (eingabe != null) System.exit(0);
            }
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void enterName()
	{
		name = "";
		
		Key eingabe;
		
		while (true)
		{
			eingabe = terminal.readInput();
			
			if (eingabe != null)
			{
				if (eingabe.getKind() == Key.Kind.Enter)
                {
                    if (name.equals("")) name += " ";
                    break;
                }
                
                if (eingabe.getKind() == Key.Kind.Escape)
                {
                    name = null;
                    break;
                }
				
				if (eingabe.getKind() == Key.Kind.NormalKey)
				{
					name += eingabe.getCharacter();
				}
				
				if (eingabe.getKind() == Key.Kind.Backspace)
				{
					char[] temp = name.toCharArray();
					name = "";
					
					for (int i=0; i < temp.length -1; i++) name += temp[i]; 
					
				}
				
				terminal.moveCursor(x_Offset, y_Offset);
				for (int i=0; i < 30; i++) terminal.putCharacter(' ');

				terminal.moveCursor(x_Offset, y_Offset);
				for (char temp: name.toCharArray()) terminal.putCharacter(temp);
				
			}
		}
	}
    
	public static void leseWerte()
	{
		// local vars
        BufferedReader br = null;
		String[] bestenliste = new String[10];
		String[] splitted;
		
		try
		{
			br = new BufferedReader (new InputStreamReader(new FileInputStream("values.conf")));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null && i < 10)
			{
				bestenliste[i] = line;
				i++;
			}
			br.close();
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
        
		for (String temp: bestenliste)
		{
            if (temp != null)
            {
                splitted = temp.split(":");
                highscores.add(Integer.valueOf(splitted[0]));
                namen.add(splitted[1]);
            }
		}
		
	}
    
	public static void rangliste()
	{
		// lokale Variablen
		terminal.clearScreen();
		
        for (int i=0; i < highscores.size(); i++)
        {
            if (name == null) break;
            if (spielfeld.punktestand > highscores.get(i))
            {
                highscores.add(i, spielfeld.punktestand);
                namen.add(i, name);
                break;
            }
            
            if (spielfeld.punktestand <= highscores.getLast())
            {
                highscores.addLast(spielfeld.punktestand);
                namen.addLast(name);
                break;
            }
        }
        
        x_Offset = 45;
        y_Offset =  2;
        
        terminal.moveCursor(x_Offset, y_Offset);
        terminal.applyForegroundColor(255, 255, 255);
        
        for (char temp: "Bestenliste".toCharArray()) terminal.putCharacter(temp);
        
        y_Offset =  5;
        
        for (int i=0; i < 10; i++)
        {
            if (i >= highscores.size()) break;
            
            x_Offset = 40;
            
            terminal.moveCursor(x_Offset, y_Offset);
            if (highscores.get(i) == spielfeld.punktestand && namen.get(i).equals(name))
            {
                terminal.applyForegroundColor(255, 140, 0);
            }
            
            for (char temp: String.valueOf(highscores.get(i)).toCharArray())
            {
                terminal.putCharacter(temp);
            }
            
            x_Offset = 55;
            
            terminal.moveCursor(x_Offset, y_Offset);
            
            for (char temp: namen.get(i).toCharArray())
            {
                terminal.putCharacter(temp);
            }
            y_Offset++;
            
            terminal.applyForegroundColor(255, 255, 255);
        }
		
	}
    
    public static void schreibeWerte()
    {
        try
        {
            File file = new File ("values.conf");
            
            file.createNewFile();
            
            PrintWriter out = new PrintWriter(file, "utf-8");
            
            for (int i=0; i < highscores.size(); i++)
            {
                out.print(highscores.get(i));
                out.print(":");
                out.println(namen.get(i));
            }
            out.flush();
            out.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
