package ui;

import javax.swing.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;

import hardware.Processor;
import hardware.ManualPulseGen;
import hardware.Clock;
import hardware.WireListener;
import hardware.WireState;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import util.Resources;

public class AppWindow extends JFrame
{
    protected Logger syslog = Logger.getLogger( Resources.SYSLOG );
    protected ManualPulseGen pulseGen = new ManualPulseGen();
    protected Clock clk = new Clock();

    protected Timer updateUITimer = new Timer();

    public AppWindow( final Processor processor )
    {
        super( "PicVM" );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setLayout( new BorderLayout() );

        syslog.setUseParentHandlers( false );
        syslog.addHandler( new Handler(){

            @Override
            public void publish( LogRecord lr )
            {
                System.out.println( "[" +lr.getLevel().getLocalizedName()+ "] " + lr.getMessage() );
            }

            @Override
            public void flush()
            {
                System.out.flush();
            }

            @Override
            public void close() throws SecurityException
            {
                System.out.close();
            }
        });


        // Build the left side bar
        JTabbedPane memoryTabs = new JTabbedPane();
        add( memoryTabs, BorderLayout.WEST );

        memoryTabs.addTab( "File Registers", new JScrollPane( new MemoryPanel( processor.getRAM() ) ) );
        memoryTabs.addTab( "Stack", new JScrollPane( new MemoryPanel( processor.getStack() ) ) );
        //memoryTabs.addTab( "Program Memory", new JScrollPane( new MemoryPanel( processor.getROM() ) ) );


        final DissasemblyPanel dissasembly = new DissasemblyPanel( processor.getROM() );
        add( new JScrollPane( dissasembly ), BorderLayout.CENTER );

        pulseGen.attach( processor.clkWire );
        pulseGen.attach( new WireListener(){

            @Override
            public void stateChange(WireState state)
            {
                dissasembly.setPC( processor.getPC() );
            }

        });

        clk.attach( processor.clkWire );
        clk.attach( new WireListener(){

            @Override
            public void stateChange(WireState state)
            {
                dissasembly.setPC( processor.getPC() );
            }

        });


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
                        pulseGen.pulse();
                }
        });
        chipControlBar.add( stepBtn );

        JButton runBtn = new JButton( "Run >>" );
        runBtn.addActionListener( new ActionListener()
        {
                public void actionPerformed( ActionEvent e )
                {
                        clk.setFrequency( 100 );
                }
        });
        chipControlBar.add( runBtn );

        setPreferredSize( new Dimension( 640, 480 ) );
        setMinimumSize( new Dimension( 640, 480 ) );
        setVisible( true );


        // Determine the new location of the window
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = getSize();
        int x = (dim.width/2) - (window.width / 2);
        int y = (dim.height/2) - (window.height / 2);

        // Move the window
        setLocation(x, y);
    }



}
