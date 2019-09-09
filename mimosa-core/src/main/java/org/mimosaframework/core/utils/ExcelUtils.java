package org.mimosaframework.core.utils;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.mimosaframework.core.json.ModelObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void writes(OutputStream outputStream, ExcelWriteCallback callback) throws Exception {
        SXSSFWorkbook wb = null;
        try {
            wb = new SXSSFWorkbook();
            //压缩临时文件
            wb.setCompressTempFiles(true);
            List<SheetMate> sheetMates = callback.getSheets();
            if (sheetMates != null) {
                int totalNum = 0;
                for (SheetMate mate : sheetMates) {
                    Sheet sheet = wb.createSheet(mate.name);
                    List<RowMate> rowMates = mate.rowMates;

                    int rowNum = 0;

                    if (mate.writeHeader && rowMates != null) {
                        rowNum = 1;
                        Row row = sheet.createRow(0);
                        for (int i = 0; i < rowMates.size(); i++) {
                            Cell cell = row.createCell(i);
                            cell.setCellValue(rowMates.get(i).title);
                        }
                    }

                    int loopCount = 0;
                    while (mate.isLoop() || loopCount == 0) {
                        loopCount++;
                        List<ModelObject> objects = callback.getWrites(mate, totalNum, rowNum);
                        if (objects != null) {
                            for (ModelObject object : objects) {
                                Row row = sheet.createRow(rowNum);
                                rowNum++;
                                totalNum++;
                                if (rowMates != null) {
                                    for (int i = 0; i < rowMates.size(); i++) {
                                        String key = rowMates.get(i).key;
                                        Object value = object.get(key);
                                        Cell cell = row.createCell(i);
                                        if (value == null) {
                                            cell.setCellValue("");
                                        } else if (value instanceof Date) {
                                            cell.setCellValue(format.format((Date) value));
                                        } else {
                                            cell.setCellValue(String.valueOf(value));
                                        }
                                    }
                                } else {
                                    Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();
                                    int cellNum = 0;
                                    while (iterator.hasNext()) {
                                        Map.Entry<Object, Object> entry = iterator.next();
                                        String key = String.valueOf(entry.getKey());
                                        Object value = entry.getValue();
                                        Cell cell = row.createCell(cellNum);
                                        if (value == null) {
                                            cell.setCellValue("");
                                        } else if (value instanceof Date) {
                                            cell.setCellValue(format.format((Date) value));
                                        } else {
                                            cell.setCellValue(String.valueOf(value));
                                        }

                                        cellNum++;
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            }

            wb.write(outputStream);
        } finally {
            if (wb != null) {
                // 删除临时文件
                wb.dispose();
            }
        }
    }

    public static void reads(InputStream inputStream, String[] keys, ExcelReadCallback callback) throws Exception {
        reads(inputStream, keys, callback, 0);
    }

    public static void reads(InputStream inputStream, String[] keys, ExcelReadCallback callback, int skip) throws Exception {
        OPCPackage pkg = null;
        InputStream sheetInputStream = null;

        try {
            // pkg = OPCPackage.open(filename, PackageAccess.READ);
            pkg = OPCPackage.open(inputStream);
            XSSFReader xssfReader = new XSSFReader(pkg);

            StylesTable styles = xssfReader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            sheetInputStream = xssfReader.getSheetsData().next();

            processSheet(styles, strings, sheetInputStream, keys, callback, skip);
        } finally {
            if (sheetInputStream != null) {
                try {
                    sheetInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            if (pkg != null) {
                try {
                    pkg.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }

    private static void processSheet(StylesTable styles,
                                     ReadOnlySharedStringsTable strings,
                                     InputStream sheetInputStream,
                                     String[] keys,
                                     ExcelReadCallback callback,
                                     int skip) throws SAXException, ParserConfigurationException, IOException {
        MySheetContentsHandler contentsHandler = new MySheetContentsHandler(keys, callback, skip);
        XMLReader sheetParser = SAXHelper.newXMLReader();
        sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, contentsHandler, false));
        sheetParser.parse(new InputSource(sheetInputStream));
        contentsHandler.readLast();
    }

    private static class MySheetContentsHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
        private String[] keys;
        private ExcelReadCallback callback;
        private int cellIndex = 0;
        private List<ModelObject> list = new ArrayList<>();
        private ModelObject object;
        int skip = 0;

        public MySheetContentsHandler(String[] keys, ExcelReadCallback callback, int skip) {
            this.keys = keys;
            this.callback = callback;
            this.skip = skip;
        }

        @Override
        public void startRow(int rowNum) {
            cellIndex = 0;
            if (skip <= rowNum) {
                object = new ModelObject();
            }
        }

        @Override
        public void endRow(int rowNum) {
            cellIndex = 0;
            if (object != null) {
                list.add(object);
                if (list.size() == 1000) {
                    callback.reads(list);
                    list.clear();
                }
            }
        }

        public void readLast() {
            if (list.size() > 0) {
                callback.reads(list);
                list.clear();
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue,
                         XSSFComment comment) {
            if (keys.length > cellIndex && object != null) {
                object.put(keys[cellIndex], formattedValue);
            }
            cellIndex++;
        }
    }

    public static class SheetMate {
        private String name;
        private int flag;
        private List<RowMate> rowMates;
        private boolean writeHeader = true;
        private boolean loop = false;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public List<RowMate> getRowMates() {
            return rowMates;
        }

        public void setRowMates(List<RowMate> rowMates) {
            this.rowMates = rowMates;
        }

        public boolean isWriteHeader() {
            return writeHeader;
        }

        public void setWriteHeader(boolean writeHeader) {
            this.writeHeader = writeHeader;
        }

        public boolean isLoop() {
            return loop;
        }

        public void setLoop(boolean loop) {
            this.loop = loop;
        }
    }

    public static class RowMate {
        private String title;
        private String key;

        public RowMate() {
        }

        public RowMate(String title, String key) {
            this.title = title;
            this.key = key;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public interface ExcelWriteCallback {
        List<SheetMate> getSheets();

        List<ModelObject> getWrites(SheetMate sheet, int totalIndex, int index) throws Exception;
    }

    public interface ExcelReadCallback {
        void reads(List<ModelObject> objects);
    }
}
