package hardware;

public class RegisterChangedEvent
{
	protected Memory memory = null;
	protected int offset = 0x00;
	
	public RegisterChangedEvent( Memory memory, int offset )
	{
		this.memory = memory;
		this.offset = offset;
	}
	
	public int getOffset() { return this.offset; }
	public Memory getMemory() { return this.memory; }
	
}
