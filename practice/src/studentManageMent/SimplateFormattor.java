package studentManageMent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimplateFormattor extends Formatter {
    private int id;
    private LocalDateTime localDate;
    public SimplateFormattor() {
        id = 0;
    }
    @Override
    public String format(LogRecord record) {
        localDate = LocalDateTime.now();
        id++;
        return "[序列"+id+"]  "+"["+localDate.toLocalDate()+"  "+
                localDate.toLocalTime()+"  "+record.getClass()+record.getLoggerName()+
                "\n"+"["+record.getLevel()+"]  "+record.getMessage();
    }
}
