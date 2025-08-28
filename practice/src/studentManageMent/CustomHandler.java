package studentManageMent;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class CustomHandler extends ConsoleHandler {
    public CustomHandler(Level l) {
        super();
        this.setLevel(l);
    }

    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

}
