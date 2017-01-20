package de.hft_stuttgart.djsww.tetris.objects;

import com.googlecode.lanterna.terminal.Terminal;
import java.util.LinkedList;

public class Spielfeld extends Brett
{
    public		int punktestand = 0;
    protected   int level = 0;
    
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
        
        for (Zelle[] zeile: super.spielfeld)
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
        cursor.x = super.start.x; cursor.y = super.start.y;
        cursor.x++;
        super.terminal.moveCursor(cursor.x, cursor.y);
        
        for (int i=0; i < (spielfeld.get(0).length) * super.STRECKUNG; i++)
        {
            super.terminal.putCharacter('\u2500');  // obere Rahmenfelder bis
            cursor.x++;                             // Mitte
        }
        
        super.terminal.putCharacter('\u252c');      // oberes T-Stueck
        
        for (Zelle[] zeile: super.spielfeld)
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
        
        for (int i=0; i < (spielfeld.get(0).length) * super.STRECKUNG; i++)
        {
            super.terminal.putCharacter('\u2500');  // untere Rahmenfelder bis
            cursor.x++;                             // Mitte
        }
        
        //===================================================================\\
        
        //============Rahmen oben/unten ab Mitte + Eckstuecke rechts=========\\
        int breite_InfoLeiste = (super.next_Stein.getFirst().length * super.STRECKUNG);
        if (breite_InfoLeiste < 16) breite_InfoLeiste = 16;
        
        cursor.x++;
        super.terminal.moveCursor(cursor.x, cursor.y);
        
        for (int i=0; i < breite_InfoLeiste; i++)
        {
            super.terminal.putCharacter('\u2500');  // untere Rahmenfelder ab
            cursor.x++;                             // Mitte
        }
        
        super.terminal.putCharacter('\u2518');      // rechts-unteres Eckfeld
        
        for (Zelle[] zeile: super.spielfeld)
        {
            cursor.y--;
            super.terminal.moveCursor(cursor.x, cursor.y);
            super.terminal.putCharacter('\u2502');
        }
        
        cursor.y--;
        super.terminal.moveCursor(cursor.x, cursor.y);
        super.terminal.putCharacter('\u2510');      // rechts-oberes Eckfeld
        
        for (int i=0; i < breite_InfoLeiste; i++)
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
        
        for (char temp: "Punkte".toCharArray())
        {
            super.terminal.putCharacter(temp);
        }
        erhoehePunkte(0);
        
        cursor.x = super.infoBereich;
        cursor.y = super.start.y + 9;
        super.terminal.moveCursor(cursor.x, cursor.y);
        
        for (char temp: "Level".toCharArray())
        {
            super.terminal.putCharacter(temp);
        }
        
        cursor.x = super.infoBereich;
        cursor.y = super.start.y + 13;
        super.terminal.moveCursor(cursor.x, cursor.y);
        
        for (char temp: "naechster Stein:".toCharArray())
        {
            super.terminal.putCharacter(temp);
        }
        erhoeheLevel();
    }
    
    public void erhoehePunkte(int zusaetzliche_Punkte)
	{
		this.punktestand += zusaetzliche_Punkte;
		
		// lokale Variablen
        Punkt cursor = new Punkt();
        
		cursor.x = this.infoBereich;
        cursor.y = super.start.y + 5;
		
        
		switch(Integer.toString(this.punktestand).length())
		{
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
		
		terminal.moveCursor(cursor.x, cursor.y);
		for (char temp: Integer.toString(this.punktestand).toCharArray())
		{
			terminal.putCharacter(temp);
		}
	}
	
	public void erhoeheLevel()
	{
		// lokale Variablen
		Punkt cursor = new Punkt();
        cursor.x = this.infoBereich;
		
        cursor.y = super.start.y + 10;
        super.terminal.moveCursor(cursor.x, cursor.y);
        for (int i=0; i < 5; i++) super.terminal.putCharacter(' ');
        
		this.level++;
        
        cursor.x = this.infoBereich;
		switch(Integer.toString(this.level).length())
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
		for (char temp: Integer.toString(this.level).toCharArray())
		{
			super.terminal.putCharacter(temp);
		}
	}
    
    public boolean pruefeVollstaendigeZeilen()
    {
        // lokale Variablen
        
        boolean vollstaendig;
        int     counter = 0;
        int     neuePkt = 0;
        
        for (int i=0; i < this.spielfeld.size(); i++)
        {
            vollstaendig = true;
            for (Zelle zeichen: this.spielfeld.get(i))
            {
                if (!zeichen.gesperrt) vollstaendig = false;
            }
            
            if (vollstaendig)
            {
                this.spielfeld.remove(i);
                this.spielfeld.addFirst(new Zelle[this.spielfeld.get(0).length]);
                
                for (int j=0; j < this.spielfeld.getFirst().length; j++)
                {
                    this.spielfeld.getFirst()[j] = new Zelle();
                }
                counter++;
                neuePkt += 10;
            }
        }
        erhoehePunkte(neuePkt * counter);
        return counter != 0 && neuePkt != 0;
    }
}

class Brett
{
    protected           Terminal            terminal;
    public              LinkedList<Zelle[]> spielfeld   = new LinkedList<>();
    protected           LinkedList<Zelle[]> next_Stein  = new LinkedList<>();
    protected   final   int                 STRECKUNG;
    protected           String              leer        = "";
    protected           String              gefuellt    = "";
    
    protected   int     infoBereich;
    protected   final   Punkt               start;
    
    
    public Brett (int zeilen, int spalten, Terminal terminal, Punkt p)
    {
        this.STRECKUNG = 1;
        this.terminal = terminal;
        this.start = p;
        init(zeilen, spalten);
    }
    
    public Brett (int zeilen, int spalten, int streckung, Terminal terminal, Punkt p)
    {
        this.STRECKUNG = streckung;
        this.terminal = terminal;
        this.start = p;
        init(zeilen, spalten);
    }
    
    private void init(int zeilen, int spalten)
    {
        for (int i=0; i < this.STRECKUNG; i++)
        {
            leer        += " ";
            gefuellt    += "\u2588";
        }
        
        for (int i=0; i < zeilen; i++) this.spielfeld.add(new Zelle[spalten]);
        
        for (Zelle[] y: this.spielfeld)
        {
            for (int x=0; x < y.length; x++)
            {
                y[x] = new Zelle();
                y[x].inhalt = this.leer;
            }
        }
        
        for (int i=0; i < 4; i++)
        {
            this.next_Stein.add(new Zelle[4]);
        }
        
        for (Zelle[] y: this.next_Stein)
        {
            for (int x=0; x < y.length; x++)
            {
                y[x] = new Zelle();
                y[x].inhalt = this.leer;
            }
        }
    }
    
    public void erneuereAusgabe()
    {
        // lokale Variablen
        
        Punkt cursor = this.start.clone();
        cursor.x++;
        cursor.y++;
        
        for (Zelle[] zeile: this.spielfeld)
        {
            this.terminal.moveCursor(cursor.x, cursor.y);
            
            for (Zelle zeichen: zeile)
            {
                this.terminal.applyForegroundColor(zeichen.farbe.r, zeichen.farbe.g, zeichen.farbe.b);
                for (char character: zeichen.inhalt.toCharArray())
                {
                    this.terminal.putCharacter(character);
                }
            }
            cursor.y++;
        }
        
        cursor.x = this.infoBereich;
        cursor.y = 14;
        
        for (Zelle[] zeile: this.next_Stein)
        {
            this.terminal.moveCursor(cursor.x, cursor.y);
            
            for (Zelle zeichen: zeile)
            {
                this.terminal.applyForegroundColor(zeichen.farbe.r, zeichen.farbe.g, zeichen.farbe.b);
                for (char character: zeichen.inhalt.toCharArray())
                {
                    this.terminal.putCharacter(character);
                }
            }
            cursor.y++;
        }
        
        this.terminal.applyForegroundColor(255, 255, 255);
    }
    
    public void setzeAktivenKoerper(Punkt feld, Farbe farbe)
    {
        // lokale Variablen
        
        Zelle temp      = this.spielfeld.get(feld.y)[feld.x];
        
        temp.farbe      = farbe;
        temp.inhalt     = this.gefuellt;
    }
    
    public void aendereNaechstenStein(Tetromino nextStein)
    {
        Punkt[] next = nextStein.getKoordinaten(new Punkt());
        int position = -1;
        
        while (position != 0)
        {
            for (Punkt zeichen: next)
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
                    for (Punkt p: next)
                    {
                        p.x++;
                    }
                    break;
                    
                case 2:
                    for (Punkt p: next)
                    {
                        p.y++;
                    }
                    break;
                    
                case 3:
                    for (Punkt p: next)
                    {
                        p.x--;
                    }
                    break;
                    
                case 4:
                    for (Punkt p: next)
                    {
                        p.y--;
                    }
                    break;
            }
        }
        
        for (int i=0; i < 4; i++)
        {
            next_Stein.removeLast();
            next_Stein.addFirst(new Zelle[4]);
        }
        
        for (Zelle[] zeile: next_Stein)
        {
            for (int i=0; i < zeile.length; i++)
            {
                zeile[i] = new Zelle();
            }
        }
        
        for (int i=0; i < next_Stein.size(); i++)
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
        
        Zelle temp      = this.spielfeld.get(feld.y)[feld.x];
        
        temp.farbe      = new Farbe();
        temp.inhalt     = this.leer;
    }
    
    public void sperreZelle(Punkt feld, Farbe farbe)
    {
        // lokale Variablen
        
        Zelle temp      = this.spielfeld.get(feld.y)[feld.x];
        
        temp.gesperrt   = true;
        temp.farbe      = farbe;
        temp.inhalt     = this.gefuellt;
    }
}