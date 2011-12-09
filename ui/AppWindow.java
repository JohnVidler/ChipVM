package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppWindow extends JFrame
{
	
	
	public AppWindow()
	{
		super( "PicVM" );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		setPreferredSize( new Dimension( 640, 480 ) );
		setMinimumSize( new Dimension( 640, 480 ) );
		setVisible( true );
	}
	
	
	
}
