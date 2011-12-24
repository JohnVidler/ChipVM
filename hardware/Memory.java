package hardware;

import java.util.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.*;

/**
 * A class to model SRAM memory in microcontrollers.
 * 
 * Functions by means of event pushing.
 * 
 * @author John Vidler
 */
public class Memory implements TableModel
{
	public static int UNIMPLEMENTED = 1;
	
	protected TreeMap<String, Integer> aliases = null;
	protected RegisterMeta meta[];
	protected int sRAM[];
	protected int max;
	protected int width;
	
	public boolean debug = false;
	
	/* TableModel variables */
	protected Vector<TableModelListener>listeners = new Vector<TableModelListener>();
	protected String[] columnNames = { "Offset", "Alias", "Value" };
	protected int lastOffset = 0;
	
	
	/**
	 * Create a new memory area, defining the number of registers and the bit-width
	 * 
	 * @param size The number of registers to create in this memory area.
	 * @param width The bit-width to simulate
	 */
	public Memory( int size, int width )
	{
		aliases = new TreeMap<String, Integer>();
		sRAM = new int[ size ];
		meta = new RegisterMeta[ size ];
		for( int i=0; i<size; i++ )
		{
			sRAM[i] = 0;
			meta[i] = new RegisterMeta();
		}
		
		this.width = width;
		this.max = (int)Math.pow( 2, width );
	}
	
	/**
	 * Creates a non-functional, uninitialized memory area. Should only be used by
	 * internal methods to create a 'bare' Memory object for complex initialization
	 * 
	 * @param width The bit-width to simulate
	 */
	protected Memory( int width )
	{
		/* Stub, so we can extend the class with minimal constructors */
		this.width = width;
		this.max = (int)Math.pow( 2, width );
	}
	
	/**
	 * Sets an alias for a given memory offset in this area.
	 * 
	 * <b>Note:</b> Aliases are unique per memory area, if two locations are set to the same
	 * address, only the last one will have the alias.
	 * 
	 * @param offset The register offset to set the alias for.
	 * @param name The alias to use for this register.
	 */
	public void setAlias( int offset, String name )
	{
		aliases.put( name, offset );
		notifyListeners( offset );
	}
	
	/**
	 * Removes an alias.
	 * 
	 * @param name The alias to remove - as there can only be one, no offset is required.
	 */
	public void clearAlias( String name )
	{
		int offset = aliases.get( name );
		aliases.remove( name );
		
		notifyListeners( offset );
	}
	
	/**
	 * Sets the metadata object for a given register.
	 * 
	 * @param offset The register offset to set metadata for.
	 * @param metaData The new metadata object to use for this register.
	 */
	public void setMeta( int offset, RegisterMeta metaData )
	{
		meta[offset] = metaData;
	}
	
	/**
	 * Returns the RegisterMeta object for the register at 'offset'
	 * 
	 * @param offset The register to query for metadata.
	 * @return The RegisterMeta object for the register at 'offset'
	 * 
	 * @see RegisterMeta
	 */
	public RegisterMeta getMeta( int offset )
	{
		return meta[offset];
	}
	
	public int getSize()
	{
		return sRAM.length;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public String getOffsetAlias( int offset )
	{
		for( Map.Entry<String, Integer> entry : aliases.entrySet() )
		{
			if( entry.getValue() == offset )
				return entry.getKey();
		}
		return null;
	}
	
	public int set( String name, int value ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return set( offset, value );
	}
	
	public int set( int offset, int value ) throws NoSuchAliasException
	{
		if( debug )
			System.out.println( "[Mem]\t0x" +Integer.toHexString(offset)+ " = " +Integer.toHexString(value) );
		
		if( aliases.containsKey( "INDF" ) && aliases.containsKey( "FSR" ) )
		{
			if( offset == aliases.get("INDF") )
				offset = get( "FSR" );
		}
		
		// Store only positive integers...
		while( value < 0 )
			value = max + value;
		
		// ...that actually fit!
		sRAM[offset] = value % max;
		
		notifyListeners( offset );
		
		return sRAM[offset];
	}
	
	public int setBit( String name, int bit ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return setBit( offset, bit );
	}
	public int setBit( int offset, int bit ) throws NoSuchAliasException
	{
		return set( offset, get( offset ) | (1 << bit) );
	}
	
	public int clearBit( String name, int bit ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return clearBit( offset, bit );
	}
	public int clearBit( int offset, int bit ) throws NoSuchAliasException
	{
		int allMask = (int)Math.pow(2, width)-1;
		
		return set( offset, get( offset ) & (allMask ^ (1 << bit)) );
	}
	
		
	public int get( String name ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return get( offset );
	}
	
	public int get( int offset ) throws NoSuchAliasException
	{
		if( debug )
			System.out.println( "[Mem]\t0x" +Integer.toHexString(offset)+ " = " +Integer.toHexString(sRAM[offset]) );
		
		if( !meta[offset].isImplemented )
			return 0x00;
		
		if( aliases.containsKey( "INDF" ) && aliases.containsKey( "FSR" ) )
		{
			if( offset == aliases.get("INDF") )
				offset = get( "FSR" );
		}
		
		return sRAM[offset];
	}
	
	public int inc( String name ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return inc( offset );
	}
	
	public int inc( int offset ) throws NoSuchAliasException
	{
		return set( offset, get( offset ) + 1 );
	}
	
	public int dec( String name ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return dec( offset );
	}
	
	public int dec( int offset ) throws NoSuchAliasException
	{
		return set( offset, get( offset ) - 1 );
	}
	
	
	protected void notifyListeners( int offset )
	{
		for( TableModelListener l : listeners )
		{
			try
			{
				l.tableChanged( new TableModelEvent( this, offset ) );
			}
			catch( Throwable t )
			{
				System.err.println( "Could not notify listener '" +l+ "'" );
			}
		}
		lastOffset = offset;
	}
	
	
	/** Register meta listeners - for pseudo-interrupt driven stuff */
	
	public void addRegisterListener( String name, RegisterListener l ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		addRegisterListener( offset, l );
	}
	public void addRegisterListener( int offset, RegisterListener l )
	{
		meta[offset].addListener( l );
	}
	
	public void removeRegisterListener( String name, RegisterListener l ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		removeRegisterListener( offset, l );
	}
	public void removeRegisterListener( int offset, RegisterListener l )
	{
		meta[offset].removeListener( l );
	}
	
	
	/** TableModel required methods **/
	
	/** Adds a listener to the list that is notified each time a change to the data model occurs. **/
	public void addTableModelListener( TableModelListener l )
	{
		listeners.add( l );
	}

	/** Returns the most specific superclass for all the cell values in the column. **/
	public Class<?> getColumnClass( int columnIndex )
	{
		return String.class;
	}

	/** Returns the number of columns in the model. **/
	public int getColumnCount()
	{
		return 3;
	}

	/** Returns the name of the column at columnIndex. **/
	public String getColumnName( int columnIndex )
	{
		return columnNames[columnIndex];
	}

	/** Returns the number of rows in the model. **/
	public int getRowCount()
	{
		return sRAM.length;
	}

	/** Returns the value for the cell at columnIndex and rowIndex. **/
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		String buffer = "???";
		switch( columnIndex )
		{
			case 0:
				buffer = "0x" + Integer.toHexString( rowIndex );
				break;
				
			case 1:
				String alias = getOffsetAlias( rowIndex );
				buffer = ( alias != null ? alias : "" );
				break;
				
			case 2:
				int value = 0;
				try{ value = get( rowIndex ); }catch( NoSuchAliasException err ){ err.printStackTrace(); }
				buffer = "0x" + Integer.toHexString( value );
				
				if( lastOffset == rowIndex )
					buffer = buffer + " <--";
				
				break;
		}		
		
		return buffer;
	}

	/** Returns true if the cell at rowIndex and columnIndex is editable. **/
	public boolean isCellEditable( int rowIndex, int columnIndex )
	{
		return false;
	}

	/** Removes a listener from the list that is notified each time a change to the data model occurs. **/
	public void removeTableModelListener( TableModelListener l )
	{
		listeners.remove( l );
	}

	/** Sets the value in the cell at columnIndex and rowIndex to aValue. **/
	public void setValueAt( Object aValue, int rowIndex, int columnIndex )
	{
		/* Stub! No editing for you! */
	}
	
}
