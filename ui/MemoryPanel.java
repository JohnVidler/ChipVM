package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
