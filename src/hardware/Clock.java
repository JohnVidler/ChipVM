package hardware;

import java.util.Vector;

public class Clock
{
	protected Thread internalThread;
	protected int frequency = 1;
        protected long realFrequency = 0;
	protected boolean running = false;
	
	protected WireState state = WireState.LOW;
	protected Vector<WireListener> listeners = new Vector<WireListener>();

	protected long clocksPerSec = 0;
	
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

						long step = 1000000000 / frequency;
						long _until = 0;
						long clocks = 0;
						long clocksFrame = 0;
						while( running )
						{
							switch( state )
							{
								case LOW:	state = WireState.RISING;	break;
								case RISING:	state = WireState.HIGH;		break;
								case HIGH:	state = WireState.FALLING;	break;
								case FALLING:	state = WireState.LOW;		break;
							}
							
							for( WireListener l : listeners )
								l.stateChange( state );
							
							try
							{
								_until = System.nanoTime() + step;
								while( System.nanoTime() < _until )
								{
									/* Spin */
								}
							}
							catch( Exception e )
							{
								System.err.println( "Clock overrun! Timings may be off!" );
							}
                                                        
							clocks++;
							if( System.currentTimeMillis() > clocksFrame )
							{
								clocksFrame = System.currentTimeMillis() + 1000;
								realFrequency = clocks;
								clocks = 0;
							}
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
		listeners.add( l );
	}
        
        public long getRealFrequency()
        {
            return realFrequency;
        }
}
