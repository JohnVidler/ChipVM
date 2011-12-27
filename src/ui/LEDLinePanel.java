package ui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

import hardware.Memory;
import hardware.NoSuchAliasException;

public class LEDLinePanel extends JPanel
{
	protected JLabel indicator[] = null;
	protected int width = 0;
	protected Memory memoryReference = null;
	protected int offset = 0;
	
	protected TableModelListener changeListener = new TableModelListener()
	{
		public void tableChanged( TableModelEvent evt )
		{
			if( evt.getFirstRow() == offset )
			{
				try
				{
					int value = memoryReference.get( offset );
					
					for( int i=0; i<width; i++ )
					{
						int mask = (1 << i);
						
						if( (value & mask) == mask )
							indicator[i].setText( "X" );
						else
							indicator[i].setText( " " );
					}
				}
				catch( NoSuchAliasException err )
				{
					System.err.println( "Could not access memory!" );
					err.printStackTrace();
				}
			}
		}
	};
	
	public LEDLinePanel( Memory data, int offset )
	{
		memoryReference = data;
		this.offset = offset;
		width = data.getWidth();
		
		setLayout( new GridLayout( 1, width ) );
		
		indicator = new JLabel[width];
		for( int i=0; i<width; i++ )
		{
			indicator[i] = new JLabel( " " );
			add( indicator[i] );
		}
		
		
		data.addTableModelListener( changeListener );
		
	}
	
}

