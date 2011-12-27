package hardware;

import java.util.*;

/**
 * Should have very little functionality, just a complex storage type for the Memory class
 */
public class RegisterMeta
{
	protected boolean isImplemented = true;
	protected Vector<RegisterListener> listeners = new Vector<RegisterListener>();
	
	public RegisterMeta() { /* STUB - should leave everything at the defaults */ }
	public RegisterMeta( boolean implemented )
	{
		isImplemented = implemented;
	}
	
	
	public void addListener( RegisterListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeListener( RegisterListener listener )
	{
		listeners.remove( listener );
	}
}
