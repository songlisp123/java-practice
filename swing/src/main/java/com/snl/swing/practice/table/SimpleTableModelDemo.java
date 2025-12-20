package com.snl.swing.practice.table;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class SimpleTableModelDemo implements TableModel , PropertyChangeListener {

    protected Object[][] rowData;
    protected String[] columnNames;
    protected final List<TableModelListener> listeners =
            new ArrayList<>();

    protected static final BlockingQueue<Path> musicPath =
            new LinkedBlockingQueue<>();
    protected final Path rootPath =
            Path.of(System.getProperty("user.dir"));

    public SimpleTableModelDemo() {
        columnNames = new String[] {
                "序号","文件名","播放/试听"
        };
        //TODO 如何获取文件夹信息?可以优
        Task task = new Task();
        task.execute();
        try {
            //TODO 逻辑非常糟糕！
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        init();

    }

    private void init() {
        //获取行数据
        rowData = createRowData();
    }

    private Object[][] createRowData() {
        int rows = musicPath.size();
        Object[][] outLayer = new Object[rows][];
        for (int i = 0;i<rows;i++) {
            Object[] other = null;
            Path path = null;
            try {
                path = musicPath.take();
//                System.out.println("path = " + path);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            other = createInnerLayer(path);
            outLayer[i] = other;
        }
        return outLayer;
    }

    private Object[] createInnerLayer(Path path) {
        Object[] result = new Object[getColumnCount()];
        for (int i = 0;i < getColumnCount();i++) {
            if (i == 0) {
                result[i] = LocalDateTime.now();
            }else if ( i == 1){
                result[i] = path.getFileName();
            }else {
                //TODO 这里面有点逻辑
                result[i] = new ImageIcon("sound.gif");
            }
        }
        return result;
    }

    @Override
    public int getRowCount() {
        return rowData.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex > getColumnCount() || columnIndex < 0) return "";
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() != 0) {
            for (int i =0;i<getColumnCount();i++) {
                Object o = rowData[0][i];
                if (o == null) continue;
                return o.getClass();
            }
        }
        return columnNames[columnIndex].getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //默认不可编辑
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object value = this.getValueAt(rowIndex, columnIndex);
        if (Objects.equals(aValue,value)) {
            System.out.println("无改变");
        }else {
            rowData[rowIndex][columnIndex] = aValue;
            fireChangeEvent(rowIndex,columnIndex);
        }
    }

    private void fireChangeEvent(int rowIndex, int columnIndex) {
        System.out.println("触发事件……");
        TableModelEvent tableModelEvent = new TableModelEvent(this,rowIndex,rowIndex,columnIndex);
        for(TableModelListener listener : listeners) {
            listener.tableChanged(tableModelEvent);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    private void findMusicPath(Path rootPath) {
       try(Stream<Path> pathStream = Files.walk(rootPath)) {
           pathStream.filter(path -> path.toString().endsWith("wav"))
                   .forEach(musicPath::add);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (newValue instanceof SwingWorker.StateValue) {
            if (newValue  == SwingWorker.StateValue.STARTED) {
                System.out.println("后台任务开始");
            }else {
                System.out.println("后台任务结束");
            }
        }
    }

    class Task extends SwingWorker<Void,Path> {

        @Override
        protected Void doInBackground() throws Exception {
            findMusicPath(rootPath);
            return null;
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
