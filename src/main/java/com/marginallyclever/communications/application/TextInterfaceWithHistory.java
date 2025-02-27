package com.marginallyclever.communications.application;

import com.marginallyclever.convenience.log.Log;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serial;

/**
 * A chat style interface with a history of commands.  The history is a list of commands sent and received.
 * @author Dan Royer
 */
public class TextInterfaceWithHistory extends JPanel {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 5542831703742185676L;
	private final TextInterfaceToListeners myInterface = new TextInterfaceToListeners();
	private final ConversationHistoryList myHistory = new ConversationHistoryList();
	
	public TextInterfaceWithHistory() {
		super();

		//this.setBorder(BorderFactory.createTitledBorder(TextInterfaceWithHistory.class.getName()));
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=1;
		add(myHistory,c);
		myHistory.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty=0;
		add(myInterface,c);
		
		myInterface.addActionListener((e)->addToHistory("You",e.getActionCommand()));
		myHistory.addListSelectionListener((e)->{
			if(e.getValueIsAdjusting()) return;
			int i = myHistory.getSelectedIndex();
			if(i!=-1) myInterface.setCommand(myHistory.getSelectedValue());
		});
	}

	public void addToHistory(String who,String actionCommand) {
		myHistory.addElement(who,actionCommand);
	}
	
	public void addActionListener(ActionListener e) {
		myInterface.addActionListener(e);
	}
	
	public void removeActionListener(ActionListener e) {
		myInterface.removeActionListener(e);
	}

	public String getCommand() {
		return myInterface.getCommand();
	}

	public void setCommand(String str) {
		myInterface.setCommand(str);
	}

	public void sendCommand(String str) {
		myInterface.sendCommand(str);
	}
	
	@Override
	public void setEnabled(boolean state) {
		super.setEnabled(state);
		myInterface.setEnabled(state);
	}

	public static void main(String[] args) {
		Log.start();
		JFrame frame = new JFrame(TextInterfaceWithHistory.class.getName());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new TextInterfaceWithHistory());
		frame.pack();
		frame.setVisible(true);
	}
}
