package hardware;

import java.util.*;

public class Memory
{
	protected TreeMap<String, Integer> aliases = null;
	protected int sRAM[];
	protected int max;
	protected int width;
	
	public Memory( int size, int width )
	{
		aliases = new TreeMap<String, Integer>();
		sRAM = new int[ size ];
		for( int i=0; i<size; i++ )
			sRAM[i] = 0;
		
		this.width = width;
		this.max = (int)Math.pow( 2, width );
	}
	
	public void setAlias( int offset, String name )
	{
		aliases.put( name, offset );
	}
	
	public void clearAlias( String name )
	{
		aliases.remove( name );
	}
	
	public int set( String name, int value ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return set( offset, value );
	}
	
	public int set( int offset, int value )
	{
		System.out.println( "[Mem]\t0x" +Integer.toHexString(offset)+ " = " +Integer.toHexString(value) );
		sRAM[offset] = value;
		return sRAM[offset];
	}
	
	public int get( String name ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return get( offset );
	}
	
	public int get( int offset )
	{
		System.out.println( "[Mem]\t0x" +Integer.toHexString(offset)+ " = " +Integer.toHexString(sRAM[offset]) );
		return sRAM[offset];
	}
	
	public int inc( String name ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return inc( offset );
	}
	
	public int inc( int offset )
	{
		return ++sRAM[offset];
	}
	
	public int dec( String name ) throws NoSuchAliasException
	{
		Integer offset = aliases.get( name );
		if( offset == null )
			throw new NoSuchAliasException( "No such alias '" +name+ "'" );
		
		return dec( offset );
	}
	
	public int dec( int offset )
	{
		return --sRAM[offset];
	}
	
}
