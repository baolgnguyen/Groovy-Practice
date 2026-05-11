package custom

import com.kms.katalon.core.annotation.Keyword
import org.apache.poi.ss.usermodel.WorkbookFactory
import groovy.json.JsonSlurper

class FileImporter {
    /**
     * Read an Excel sheet into a List<Map> with mapping and row-mapping closure
     * @param filePath Path to Excel file
     * @param sheetName Sheet name
     * @param columns List of columns (in order)
     * @param rowMapper Closure receives (row, idx) and returns Map or object
     */
    @Keyword
    static List readExcelSheet(String filePath, String sheetName, List<String> columns, Closure rowMapper) {
        def workbook = WorkbookFactory.create(new File(filePath))
        def sheet = workbook.getSheet(sheetName)
        def result = []
        sheet.drop(1).eachWithIndex { row, idx ->
            if (!row) return
            def mapped = rowMapper(row, idx)
            if (mapped != null) result << mapped
        }
        workbook.close()
        return result
    }

    /**
     * Read JSON (list or map)
     */
    @Keyword
    static def readJson(String filePath) {
        def jsonSlurper = new JsonSlurper()
        return jsonSlurper.parse(new File(filePath))
    }

    /**
     * Read CSV into List<Map> with mapping and row-mapping closure
     * @param filePath Path to CSV file
     * @param columns List of columns (in order)
     * @param rowMapper Closure receives (cols, idx) and returns Map or object
     */
    @Keyword
    static List readCsv(String filePath, List<String> columns, Closure rowMapper) {
        def lines = new File(filePath).readLines()
        def result = []
        lines.drop(1).eachWithIndex { line, idx ->
            def cols = line.split(',(?=(?:[^"]*"[^"]*")*[^"]*)').collect { it.replaceAll('^"|"$', '') }
            def mapped = rowMapper(cols, idx)
            if (mapped != null) { result << mapped }
        }
        return result
    }
	}
