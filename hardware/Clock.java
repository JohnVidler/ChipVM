package hardware;

public class Clock
{
	protected Thread internalThread;
	protected int frequency = 1;
	protected boolean running = false;
	
	protected WireState state = WireState.LOW;
	protected WireListener listener = null;
	
	public Clock()
	{
		frequency = 1;
		running = false;
	}
	
	public void setFrequency( int hz )
	{
		if( hz < 1 )
			return;
		
		running = true;
		frequency = 4 * hz;
		
		try
		{
			// Wait for the old one to end!
			if( internalThread != null )
			{
				running = false;
				// Wait up to 5 seconds
				internalThread.join( 5000 );
			}
			
			// Create a new thread :D
			internalThread = new Thread( new Runnable(){
					public void run()
					{
						while( running )
						{
							switch( state )
							{
								case LOW:		state = WireState.RISING;	break;
								case RISING:	state = WireState.HIGH;		break;
								case HIGH:		state = WireState.FALLING;	break;
								case FALLING:	state = WireState.LOW;		break;
							}
							
							if( listener != null )
							{
								listener.stateChange( state );
							}
							
							/*try { Thread.sleep( 1000/frequency ); }
							catch( Exception e ) { System.err.println( "Clock overrun! Timings may be off!" ); }*/
						}
					}
				} );
			internalThread.start();
		}
		catch( InterruptedException err )
		{
			err.printStackTrace();
			System.exit(1);
		}
	}
	
	
	public void attach( WireListener l )
	{
		listener = l;
	}
}
