package hardware;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import util.Resources;

public class Chip
{
    protected Logger chipLog = Logger.getAnonymousLogger();
    protected WireState pins[] = null;

    public Chip( int pinCount )
    {
        pins = new WireState[ pinCount ];
        for( int i=0; i<pinCount; i++ )
            pins[i] = WireState.LOW;
        
        chipLog.setUseParentHandlers( false );
        chipLog.addHandler( chipLogHandler );
    }
    
    public Logger getLog() { return chipLog; }
    
    private Handler chipLogHandler = new Handler()
    {
        protected Logger syslog = Logger.getLogger( Resources.SYSLOG );
        
        @Override
        public void publish( LogRecord lr )
        {
            syslog.log( lr );
        }

        @Override
        public void flush()
        {
            /* stub */
        }

        @Override
        public void close() throws SecurityException
        {
            /* stub */
        }
    };
	
}
