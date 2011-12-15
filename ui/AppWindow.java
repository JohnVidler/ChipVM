package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import hardware.Processor;

public class AppWindow extends JFrame
{
	
	
	public AppWindow( Processor processor )
	{
		super( "PicVM" );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		
		JFrame memoryWindow = new JFrame( "Memory Viewer" );
		memoryWindow.add( new MemoryPanel( processor.getRAM() ) );
		memoryWindow.pack();
		memoryWindow.setVisible( true );
		
		setPreferredSize( new Dimension( 640, 480 ) );
		setMinimumSize( new Dimension( 640, 480 ) );
		setVisible( true );
	}
	
	
	
}
