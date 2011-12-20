package hardware;

import java.util.*;
import java.io.*;

import util.DebugPrintStream;

public class InstructionSet
{
	protected ArrayList<Instruction> instructions = new ArrayList<Instruction>();
	protected static DebugPrintStream out = new DebugPrintStream( System.out );
	
	public InstructionSet()
	{
		out.setEnabled( false );
	}
	
	public void add( Instruction i )
	{
		instructions.add( i );
		Collections.sort( instructions );
		Collections.reverse( instructions );
		
		out.println( "Added instruction " +i.getOpcode() );
	}
	
	public void remove( String mnemonic )
	{
		for( int i=0; i<instructions.size(); i++ )
		{
			if( instructions.get(i).getName().equalsIgnoreCase( mnemonic ) )
				instructions.remove( i );
		}
	}
	
	public void dump()
	{
		for( int i=0; i<instructions.size(); i++ )
		{
			System.out.println( instructions.get(i).getOpcode() +"\t->\t"+ instructions.get(i).getName() + "\t[" +Integer.toBinaryString(instructions.get(i).getMask())+ "]" );
		}
	}
	
	public int doOp( Processor p, Memory m, int op ) throws Throwable
	{
		for( int i=0; i<instructions.size(); i++ )
		{
			if( (op & instructions.get(i).getMask()) == instructions.get(i).getOpcode() )
				return instructions.get(i).operator( p, m, op );
		}
		throw new NoSuchOpcodeException( "No opcode matched '" +Integer.toBinaryString(op)+ "'" );
	}
	
}
