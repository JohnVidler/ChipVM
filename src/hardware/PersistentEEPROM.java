package hardware;

import java.io.*;

public class PersistentEEPROM extends EEPROM
{
	protected File file = null;
	protected long versionID = 1L;
	protected long versionMagic = 348359238362303L;
	
	public PersistentEEPROM( int size, File file )
	{
		super( size );
		this.file = file;
	}
	
	public void set( int offset, int value )
	{
		super.set( offset, value );
	}
	
	public int get( int offset )
	{
		return super.get( offset );
	}

	public boolean writeOut()
	{
		try
		{
			DataOutputStream out = new DataOutputStream( new BufferedOutputStream( new FileOutputStream( file ) ) );
			out.writeLong( versionID );			// Version
			out.writeLong( versionMagic );		// Magic
			out.writeInt( size );				// The storage size
			for( int i=0; i<size; i++ )
			{
				out.writeInt( storage[i] );
			}
			out.writeLong( versionMagic );		// Magic
			out.flush();
			out.close();
			return true;
		}
		catch ( Throwable t ) { t.printStackTrace(); }
		return false;
	}
	
	public boolean readIn()
	{
		try
		{
			DataInputStream in = new DataInputStream( new BufferedInputStream( new FileInputStream( file ) ) );
			long version = in.readLong();			// Version
			long magic = in.readLong();				// Magic
			int length = in.readInt();			// The storage size
			
			if( version != versionID )
			{
				System.err.println( "EEPROM version invalid!" );
				return false;
			}
			
			if( magic != versionMagic )
			{
				System.err.println( "EEPROM magic invalid!" );
				return false;
			}
			
			if( length > 0 )
			{
				storage = new int[length];
				for( int i=0; i<length; i++ )
				{
					storage[i] = in.readInt();
				}
			}
			in.close();
			return true;
		}
		catch ( Throwable t ) { t.printStackTrace(); }
		return false;
	}
}
