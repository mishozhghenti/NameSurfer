/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.xml.internal.ws.api.message.Message;

import acm.program.Program;

public class NameSurfer extends Program implements NameSurferConstants {

	/* Method: init() */
	/**
	 * This method has the responsibility for reading in the data base and
	 * initializing the interactors at the bottom of the window.
	 */
	public void init() {
		createObjects();
		addObjects();
		addListeners();
	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are clicked, so
	 * you will have to define a method to respond to button actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == graphButtom || e.getSource() == enterName) {
			text = enter(enterName.getText());
			person = dBase.findEntry(text);
			if (person != null) {
				graph.addEntry(person);
				graph.update();
			}
			enterName.setText("");
		}
		if (e.getSource() == clearButtom) {
			graph.clear();
			graph.update();
		}
		if (e.getSource() == deleteButtom || e.getSource() == deleteName) {
			text = enter(deleteName.getText());
			person = dBase.findEntry(text);
			if (person != null) {
				graph.removeEntry(person);
				graph.update();
			}
			deleteName.setText("");
		}
	}

	/* creates objects(JLabel,JTextField,JButtom,NameSurferGraph) */
	private void createObjects() {
		label = new JLabel("Name");
		enterName = new JTextField(15);
		deleteName = new JTextField(15);
		graphButtom = new JButton("Graph");
		clearButtom = new JButton("Clear");
		deleteButtom = new JButton("Delete");
		graph = new NameSurferGraph();
	}

	/* add objects */
	private void addObjects() {
		add(label, SOUTH);
		add(enterName, SOUTH);
		add(graphButtom, SOUTH);
		add(clearButtom, SOUTH);
		add(deleteName, SOUTH);
		add(deleteButtom, SOUTH);
		add(graph);
	}

	/* add listeners */
	private void addListeners() {
		enterName.addActionListener(this);
		graphButtom.addActionListener(this);
		clearButtom.addActionListener(this);
		deleteName.addActionListener(this);
		deleteButtom.addActionListener(this);
	}

	/*
	 * makes entered name as we want(first char should be upperCase and other
	 * should be lowerCase, because list we have of people there are like that
	 * method)
	 */
	private String enter(String name) {
		String tmp = "";
		for (int i = 0; i < name.length(); i++) {
			if (i == 0) {
				tmp = tmp + Character.toUpperCase(name.charAt(i));
			} else {
				tmp = tmp + Character.toLowerCase(name.charAt(i));
			}
		}
		return tmp;
	}

	/* Private instance variables */
	private NameSurferGraph graph;
	private JLabel label;
	private JTextField enterName;
	private JTextField deleteName;
	private JButton graphButtom;
	private JButton clearButtom;
	private JButton deleteButtom;
	private NameSurferDataBase dBase = new NameSurferDataBase(NAMES_DATA_FILE);
	private String text;
	private NameSurferEntry person;
}
