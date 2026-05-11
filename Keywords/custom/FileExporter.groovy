package custom

import com.kms.katalon.core.annotation.Keyword
import groovy.json.JsonOutput

class FileExporter {
    /**
     * Write any object (List or Map) to a JSON file (pretty print)
     * @param data List or Map
     * @param filePath Output file path
     */
    @Keyword
    static void writeJsonFile(Object data, String filePath) {
        def json = JsonOutput.prettyPrint(JsonOutput.toJson(data))
        new File(filePath).withWriter { writer ->
            writer.write(json)
        }
    }

    /**
     * Write a list of maps to a CSV file
     * @param data List of maps (each map is a row)
     * @param headers List of header names (order)
     * @param filePath Output file path
     */
    @Keyword
    static void writeListToCsvFile(List<Map> data, List<String> headers, String filePath) {
        new File(filePath).withWriter { writer ->
            writer.writeLine(headers.join(","))
            data.each { row ->
                def line = headers.collect { h -> row[h] }
                writer.writeLine(line.join(","))
            }
        }
    }

    /**
     * Write a map to a single-row CSV file (with header)
     * @param data Map object
     * @param headers List of header names (order)
     * @param filePath Output file path
     */
    @Keyword
    static void writeMapToCsvFile(Map data, List<String> headers, String filePath) {
        new File(filePath).withWriter { writer ->
            writer.writeLine(headers.join(","))
            writer.writeLine(headers.collect { h -> data[h] }.join(","))
        }
    }
}
