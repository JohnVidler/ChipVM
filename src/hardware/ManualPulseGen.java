package hardware;

public class ManualPulseGen
{
	protected WireListener listener = null;
	
	public void attach( WireListener l )
	{
		listener = l;
	}
	
	public void pulse()
	{
		listener.stateChange( WireState.RISING );
		listener.stateChange( WireState.HIGH );
		listener.stateChange( WireState.FALLING );
		listener.stateChange( WireState.LOW );
	}
}
