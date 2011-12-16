package hardware;

public abstract class PicProcessor extends Processor
{
	public int wReg = 0;
	protected int maxValue = (int)Math.pow(2, 8);
	
	public PicProcessor( int[] program )
	{
		super( program );
	}
	
}
