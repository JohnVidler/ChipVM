package hardware;

/**
 *
 * @author John Vidler
 */
public class NonUniformMemory extends Memory
{
    protected boolean validMemory[];
    
    public NonUniformMemory( int size, int width )
    {
        super( size, width );
        
        validMemory = new boolean[size];
        for( int i=0; i<size; i++ )
            validMemory[i] = false;
    }
    
    @Override
    public int set( int offset, int value ) throws NoSuchAliasException
    {
        if( validMemory[offset] )
            return super.set( offset, value );
        return 0;
    }
    
    @Override
    public int get( int offset ) throws NoSuchAliasException
    {
        if( validMemory[offset] )
            return super.get( offset );
        return 0;
    }
    
    @Override
    public Object getValueAt( int rowIndex, int columnIndex )
    {
        if( !validMemory[rowIndex] )
            return "N/A";
        return super.getValueAt( rowIndex, columnIndex );
    }
}
