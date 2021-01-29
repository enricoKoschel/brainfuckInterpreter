package brainfuckInterpreter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Gui {

	//Komponenten
	JFrame frame;
	JLabel lblSpeicher = new JLabel();
	JLabel lblCode = new JLabel();
	String speicherString;
	String codeString;
	ButtonGroup btngrpAnzeigeModus = new ButtonGroup();
	JRadioButton rdbtnDezimal = new JRadioButton("Dezimal");
	JRadioButton rdbtnHexadezimal = new JRadioButton("Hexadezimal");
	JRadioButton rdbtnAscii = new JRadioButton("ASCII");
	ButtonGroup btngrpGeschwindigkeit = new ButtonGroup();
	JRadioButton rdbtnSchritt = new JRadioButton("Schritt");
	JRadioButton rdbtnEchtzeit = new JRadioButton("Echtzeit");
	JRadioButton rdbtnLangsam = new JRadioButton("Langsam");
	JButton btnSchrittAusfuehren = new JButton("Schritt ausfuehren");
	JTextField txtEingabe = new JTextField();;
	JButton btnEingeben = new JButton("Eingeben");
	JLabel lblAusgabe = new JLabel("Ausgabe:");
	JLabel lblEingabe = new JLabel("Eingabe:");
	JTextPane txtpnAusgabe = new JTextPane();
	
	public Gui() {
		initialisieren();
	}

	//Komponenten initialisieren
	void initialisieren() {
		frame = new JFrame("Brainfuck Interpreter");
		frame.setBounds(100, 100, 1200, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblSpeicher.setBounds(10, 11, 1164, 198);
		frame.getContentPane().add(lblSpeicher);
		
		rdbtnDezimal.setSelected(true);
		rdbtnDezimal.setBounds(10, 464, 109, 23);
		frame.getContentPane().add(rdbtnDezimal);
		
		rdbtnHexadezimal.setBounds(10, 490, 109, 23);
		frame.getContentPane().add(rdbtnHexadezimal);
		
		rdbtnAscii.setBounds(10, 516, 109, 23);
		frame.getContentPane().add(rdbtnAscii);
		
		btngrpAnzeigeModus.add(rdbtnDezimal);
		btngrpAnzeigeModus.add(rdbtnHexadezimal);
		btngrpAnzeigeModus.add(rdbtnAscii);
		rdbtnSchritt.setSelected(true);
		
		rdbtnSchritt.setBounds(134, 516, 109, 23);
		frame.getContentPane().add(rdbtnSchritt);
		
		rdbtnEchtzeit.setBounds(134, 464, 109, 23);
		frame.getContentPane().add(rdbtnEchtzeit);
		
		rdbtnLangsam.setBounds(134, 490, 109, 23);
		frame.getContentPane().add(rdbtnLangsam);
		
		btngrpGeschwindigkeit.add(rdbtnSchritt);
		btngrpGeschwindigkeit.add(rdbtnEchtzeit);
		btngrpGeschwindigkeit.add(rdbtnLangsam);
		
		btnSchrittAusfuehren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Interpreter.schrittAusfuehren = true;
			}
		});
		
		btnSchrittAusfuehren.setBounds(134, 542, 121, 23);
		frame.getContentPane().add(btnSchrittAusfuehren);
		
		lblCode.setBounds(10, 222, 1164, 198);
		frame.getContentPane().add(lblCode);
		
		lblEingabe.setBounds(10, 581, 46, 14);
		frame.getContentPane().add(lblEingabe);
		
		lblAusgabe.setBounds(264, 406, 46, 14);
		frame.getContentPane().add(lblAusgabe);
		txtEingabe.setBounds(66, 578, 86, 20);
		frame.getContentPane().add(txtEingabe);
		txtEingabe.setColumns(10);
		
		btnEingeben.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Interpreter.eingabeGebraucht = false;
			}
		});
		
		btnEingeben.setBounds(165, 576, 90, 23);
		frame.getContentPane().add(btnEingeben);
		
		txtpnAusgabe.setEditable(false);
		txtpnAusgabe.setBounds(264, 431, 910, 319);
		frame.getContentPane().add(txtpnAusgabe);
	}
	
	void update(int[] speicher, int speicherIndex, String brainfuckCode, int codeIndex, String ausgabe, boolean eingabeGebraucht) {
		speicherString = "<html>";
		for(int i = 0; i < speicher.length; i++) {
			if(i == speicherIndex) speicherString += "<b><u>";
			
			if(rdbtnDezimal.isSelected()) {
				speicherString += speicher[i];
			}else if(rdbtnHexadezimal.isSelected()) {
				speicherString += Integer.toHexString(speicher[i]);
			}else if(rdbtnAscii.isSelected()) {
				speicherString += (char)speicher[i];
			}
			
			if(i == speicherIndex) speicherString += "</u></b>";
			
			speicherString += " ";
		}
		speicherString += "</html>";
		lblSpeicher.setText(speicherString);
		
		codeString = "<html>";
		for(int i = 0; i < brainfuckCode.length(); i++) {
			if(i == codeIndex) codeString += "<b><u>";
			
			if(brainfuckCode.charAt(i) == '<') {
				codeString += "&lt;";
			}else if(brainfuckCode.charAt(i) == '>') {
				codeString += "&gt;";
			}else {
				codeString += brainfuckCode.charAt(i);
			}
						
			if(i == codeIndex) codeString += "</u></b>";
			
			codeString += " ";
		}
		codeString += "</html>";
		lblCode.setText(codeString);
		
		btnSchrittAusfuehren.setEnabled(rdbtnSchritt.isSelected());
		Interpreter.echtzeit = rdbtnEchtzeit.isSelected();
		Interpreter.langsam = rdbtnLangsam.isSelected();
		
		btnEingeben.setEnabled(eingabeGebraucht);
		
		txtpnAusgabe.setText(ausgabe);
	}
	
	void error(String errorNachricht) {
		txtpnAusgabe.setBackground(Color.red);
		txtpnAusgabe.setForeground(Color.white);
		txtpnAusgabe.setText(errorNachricht);
	}
}
