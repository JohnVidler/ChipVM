package hardware;

import java.util.Vector;

public class ManualPulseGen
{
	protected Vector<WireListener> listeners = new Vector<WireListener>();
	
	public void attach( WireListener l )
        {
            listeners.add( l );
        }
	
	public void pulse()
	{
            for( WireListener l : listeners )
            {
		l.stateChange( WireState.RISING );
		l.stateChange( WireState.HIGH );
		l.stateChange( WireState.FALLING );
		l.stateChange( WireState.LOW );
            }
	}
}
