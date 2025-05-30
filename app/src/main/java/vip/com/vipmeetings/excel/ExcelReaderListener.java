package vip.com.vipmeetings.excel;

import java.util.List;

public interface ExcelReaderListener {
    void onReadExcelCompleted(List<String> stringList);
}