package brainfuckInterpreter;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.UIManager;

public class Interpreter {

	//Static Variabeln für beide Threads
	static boolean schrittAusfuehren = false;
	static boolean echtzeit = false;
	static boolean langsam = false;
	static boolean eingabeGebraucht = false;

	public static void main(String Args[]) {

		//Variabeln
		String brainfuckCode = "";
		String ausgabe = "";
		char befehl = 0;
		int speicherGroesse = 1000;
		int speicher[] = new int[speicherGroesse];
		int codeIndex = 0;
		int schleifenZähler = 0;
		int speicherIndex = 0;
		int durchgang = 0;
		ArrayList<Integer> eingabeBuffer = new ArrayList<Integer>();

		//Aussehen des Fensters an das System anpassen
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		//Neues Fenster erstellen
		Gui fenster = new Gui();
		fensterErstellen(fenster);

		//Datei mit Code erstellen und öffnen
		File brainfuckFile;

		if(new File("brainfuckCode/brainfuck.txt")	.exists()) {
			brainfuckFile = new File("brainfuckCode/brainfuck.txt");
		}else {
			fenster.error("Kann Datei \"brainfuckCode/brainfuck.txt\" nicht finden!");
			return;
		}

		//Scanner um die Datei zu lesen
		Scanner scFile = null;
		try {
			scFile = new Scanner(brainfuckFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//Datei lesen, nur die Brainfuck befehle einlesen alle anderen Zeichen nicht einlesen
		while(scFile.hasNext()) {
			String temp = scFile.next();
			for(int i = 0; i < temp.length(); i++) {
				if(temp.charAt(i) == '+' || temp.charAt(i) == '-' || temp.charAt(i) == '.' || temp.charAt(i) == ',' ||
				temp.charAt(i) == '<' || temp.charAt(i) == '>' || temp.charAt(i) == '[' || temp.charAt(i) == ']')
				{
					brainfuckCode += temp.charAt(i);
				}
			}
		}
		scFile.close();

		//Hauptschleife
		while(codeIndex <= brainfuckCode.length()) {

			//Endlos, langsam oder Schritt Betrieb
			while(schrittAusfuehren == false && echtzeit == false && langsam == false) {
				fenster.update(speicher, speicherIndex, brainfuckCode, codeIndex, ausgabe, eingabeGebraucht);
			}
			schrittAusfuehren = false;

			//Richtigen Brainfuck Befehl zuweisen
			if(codeIndex < brainfuckCode.length()) befehl = brainfuckCode.charAt(codeIndex);

			//Befehl auswerten
			switch(befehl) {
			case '+':
				speicher[speicherIndex]++;
				break;
			case '-':
				speicher[speicherIndex]--;
				break;
			case '<':
				speicherIndex--;
				break;
			case '>':
				speicherIndex++;
				break;
			case '[':
				//Wenn der Speicher beim Index 0 ist, zum Ende von der Schleife gehen
				if(speicher[speicherIndex] == 0) {
					while(brainfuckCode.charAt(codeIndex) != ']' || schleifenZähler != 0) {
						if(brainfuckCode.charAt(codeIndex) == '[') {
							schleifenZähler++;
						}else if(brainfuckCode.charAt(codeIndex) == ']') {
							schleifenZähler--;
							if(schleifenZähler == 0) {
								break;
							}
						}
						codeIndex++;
						if(codeIndex > brainfuckCode.length() - 1) {
							fenster.error("Unausgeglichene Klammer!");
							return;
						}
					}
				}
				break;
			case ']':
				//Wenn der Speicher beim Index nicht 0 ist, zum Anfang der Schleife gehen
				if(speicher[speicherIndex] != 0) {
					while(brainfuckCode.charAt(codeIndex) != '[' || schleifenZähler != 0) {
						if(brainfuckCode.charAt(codeIndex) == ']') {
							schleifenZähler++;
						}else if(brainfuckCode.charAt(codeIndex) == '[') {
							schleifenZähler--;
							if(schleifenZähler == 0) {
								break;
							}
						}
						codeIndex--;
						if(codeIndex < 0) {
							fenster.error("Unausgeglichene Klammer!");
							return;
						}
					}
				}
				break;
			case ',':
				//Eingabe
				if(eingabeBuffer.size() > 0) {
					speicher[speicherIndex] = eingabeBuffer.remove(0);
					codeIndex++;
					continue;
				}

				eingabeGebraucht = true;

				while(eingabeGebraucht) {
					fenster.update(speicher, speicherIndex, brainfuckCode, codeIndex, ausgabe, eingabeGebraucht);
				}

				String input = fenster.txtEingabe.getText();

				for(int i = 0; i < input.length(); i++) {
					eingabeBuffer.add((int)input.charAt(i));
				}

				//Eingabe Ende in Ascii = 13
				eingabeBuffer.add(13);

				speicher[speicherIndex] = eingabeBuffer.remove(0);

				break;
			case '.':
				//Ausgabe
				ausgabe += (char)speicher[speicherIndex];
				break;
			default:
				break;
			}

			//Specher Index größer oder kleiner als Speicher
			if(speicherIndex < 0 || speicherIndex > speicher.length - 1) {
				fenster.error("Speicher Zugansverletzung! bei: " + codeIndex);
				return;
			}

			codeIndex++;
			durchgang++;

			//Zellen über 255 = 0 und unter 0 = 255
			if(speicher[speicherIndex] > 255) speicher[speicherIndex] = 0;
			if(speicher[speicherIndex] < 0) speicher[speicherIndex] = 255;

			//Zu viele Durchgänge
			if(durchgang > 10000000) {
				fenster.error("Zu viele Durchgänge!");
				return;
			}

			//Echtzeit ohne Fenster Updates
			if(echtzeit == false) fenster.update(speicher, speicherIndex, brainfuckCode, codeIndex, ausgabe, eingabeGebraucht);
		}

		//Wenn Brainfuck Programm fertig ist, Fenster weiter updaten
		while(true) {
			fenster.update(speicher, speicherIndex, brainfuckCode, codeIndex, ausgabe, eingabeGebraucht);
		}
	}

	//Fenster erstellen
	static void fensterErstellen(Gui window){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}