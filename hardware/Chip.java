package hardware;

public class Chip
{
	protected WireState pins[] = null;
	
	public Chip( int pinCount )
	{
		pins = new WireState[ pinCount ];
		for( int i=0; i<pinCount; i++ )
			pins[i] = WireState.LOW;
	}
	
}
