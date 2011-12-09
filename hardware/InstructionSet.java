package hardware;

import java.util.*;

public class InstructionSet
{
	ArrayList<Instruction> instructions = null;
	
	public InstructionSet()
	{
		instructions = new ArrayList<Instruction>();
	}
	
	public void add( Instruction i )
	{
		instructions.add( i );
		Collections.sort( instructions );
		Collections.reverse( instructions );
		
		System.out.println( "Added instruction " +i.getOpcode() );
	}
	
	public void dump()
	{
		for( int i=0; i<instructions.size(); i++ )
		{
			System.out.println( instructions.get(i).getOpcode() +" -> "+ instructions.get(i).getName() + " [" +Integer.toBinaryString(instructions.get(i).getMask())+ "]" );
		}
	}
	
	public int doOp( Processor p, Memory m, int op ) throws Throwable
	{
		for( int i=0; i<instructions.size(); i++ )
		{
			if( (op & instructions.get(i).getMask()) == instructions.get(i).getOpcode() )
				return instructions.get(i).operator( p, m, op );
		}
		throw new NoSuchOpcodeException( "No opcode matched '" +op+ "'" );
	}
	
}
