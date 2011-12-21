package hardware;

import java.util.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.*;

public class ProxyMemory extends Memory implements TableModel
{
	protected Memory parent = null;
	protected int baseOffset = 0x00;
	
	public ProxyMemory( Memory parent, int width, int offset )
	{
		super( width );
		this.parent = parent;
		baseOffset = offset;
	}
	
	@Override
	public int get( int offset ) throws NoSuchAliasException
	{
		return parent.get( baseOffset + offset );
	}
	
	public int set( int offset, int value ) throws NoSuchAliasException
	{
		return parent.set( baseOffset + offset, value );
	}
	
}
