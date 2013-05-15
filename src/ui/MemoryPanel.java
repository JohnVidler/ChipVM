package ui;

import javax.swing.*;

import hardware.Memory;

public class MemoryPanel extends JPanel
{
	protected JTable table = null;
	
	public MemoryPanel( Memory data )
	{
		table = new JTable( data );
		add( table );
	}
	
}
