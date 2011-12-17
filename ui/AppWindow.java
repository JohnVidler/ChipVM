package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import hardware.Processor;
import hardware.ManualPulseGen;
import hardware.Clock;

public class AppWindow extends JFrame
{
	protected ManualPulseGen pulseGen = new ManualPulseGen();
	protected Clock clk = new Clock();
	
	public AppWindow( final Processor processor )
	{
		super( "PicVM" );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setLayout( new BorderLayout() );
		
		
		
		
		// Determine the new location of the window
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension window = getSize();
		int x = (dim.width/2) - window.width;
		int y = 100;

		// Move the window
		setLocation(x, y);
		
		
		
		
		// Build the left side bar
		JTabbedPane memoryTabs = new JTabbedPane();
		add( memoryTabs, BorderLayout.WEST );
		
		memoryTabs.addTab( "File Registers", new JScrollPane( new MemoryPanel( processor.getRAM() ) ) );
		memoryTabs.addTab( "Stack", new JScrollPane( new MemoryPanel( processor.getStack() ) ) );
		memoryTabs.addTab( "Program Memory", new JScrollPane( new MemoryPanel( processor.getROM() ) ) );
		
		
		
		
		// Temp
		add( new LEDLinePanel( processor.getRAM(), 0x06 ), BorderLayout.SOUTH );
		
		
		
		
		// Build the chip control toolbar
		JToolBar chipControlBar = new JToolBar();
		add( chipControlBar, BorderLayout.NORTH );
		
		JButton stepBtn = new JButton( "Step >" );
		stepBtn.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				pulseGen.attach( processor.clkWire );
				pulseGen.pulse();
			}
		});
		chipControlBar.add( stepBtn );
		
		JButton runBtn = new JButton( "Run >>" );
		runBtn.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				clk.attach( processor.clkWire );
				clk.setFrequency( 5 );
			}
		});
		chipControlBar.add( runBtn );
		
		setPreferredSize( new Dimension( 640, 480 ) );
		setMinimumSize( new Dimension( 640, 480 ) );
		setVisible( true );
	}
	
	
	
}
