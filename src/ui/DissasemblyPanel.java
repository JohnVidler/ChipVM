package ui;

import hardware.Instruction;
import hardware.Memory;
import hardware.NoSuchAliasException;
import hardware.pic.PicInstructionSet;
import java.awt.Dimension;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author John Vidler
 */
public class DissasemblyPanel extends JPanel
{
    protected JTable table = null;
    protected DissasemblyTable code = null;
	
    public DissasemblyPanel( Memory data )
    {
        code = new DissasemblyTable( data );
        table = new JTable( code );
        add( table );
    }
    
    public void setPC( int npc )
    {
        code.setPC( npc );
    }
    
    
    protected class DissasemblyTable implements TableModel
    {
        protected Memory mData = null;
        protected Vector<TableModelListener> listeners = new Vector<TableModelListener>();
        protected int pc = 0;
        
        public DissasemblyTable( Memory data )
        {
            mData = data;
        }
        
        public void setPC( int npc )
        {
            notify( pc );
            pc = npc;
            notify( npc );
        }
        
        @Override
        public int getRowCount()
        {
            return mData.getSize();
        }

        @Override
        public int getColumnCount()
        {
            return 4;
        }

        @Override
        public String getColumnName( int column )
        {
            switch( column )
            {
                case 0: return "Address";
                case 1: return "Code";
                case 2: return "Instruction";
            }
            return "Unknown";
        }

        @Override
        public Class<?> getColumnClass( int column )
        {
            return String.class;
        }

        @Override
        public boolean isCellEditable( int row, int column )
        {
            return false;
        }

        @Override
        public Object getValueAt( int row, int column )
        {
            switch( column )
            {
                case 0:
                    return "0x" + Integer.toHexString(row);
                case 1:
                    return mData.getValueAt(row, 2);
                case 2:
                    try {
                        return translate( mData.get(row) );
                    } catch (NoSuchAliasException ex) {
                        return "Err! Unknown opcode";
                    }
                case 3:
                    if( pc == row )
                        return "<--";
                    return "";
            }
            return "???";
        }

        @Override
        public void setValueAt( Object o, int row, int column )
        {
            /* Do nothing */
        }
        
        protected void notify( int offset )
        {
            for( TableModelListener l : listeners )
            {
                try
                {
                    l.tableChanged( new TableModelEvent( this, offset ) );
                }
                catch( Throwable t )
                {
                    System.err.println( "Could not notify listener '" +l+ "'" );
                }
            }
        }
        
        @Override
        public void addTableModelListener( TableModelListener tl )
        {
            listeners.add( tl );
        }

        @Override
        public void removeTableModelListener( TableModelListener tl )
        {
            listeners.remove( tl );
        }

        private String translate( int valueAt )
        {
            PicInstructionSet instructionSet = new PicInstructionSet();
            Instruction instruction = instructionSet.translate( valueAt );
            
            if( instruction != null )
                return instruction.getDissasembly( valueAt );
            
            return "???";
        }
        
    }
}
