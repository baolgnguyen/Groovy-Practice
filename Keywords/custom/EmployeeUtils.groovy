package custom

import com.kms.katalon.core.annotation.Keyword

class EmployeeUtils {
    /**
     * Filter employees with a custom closure condition
     */
    @Keyword
    static List filterEmployees(List employees, Closure condition) {
        employees.findAll(condition)
    }

    /**
     * Filter employees by field, operator, and value
     * op: eq, gt, lt, gte, lte, contains
     */
    @Keyword
    static List filterByField(List employees, String field, String op, Object value) {
        employees.findAll { emp ->
            def v = emp[field]
            switch(op) {
                case "eq": return v == value
                case "gt": return v > value
                case "lt": return v < value
                case "gte": return v >= value
                case "lte": return v <= value
                case "contains": return v?.toString()?.contains(value.toString())
                default: return false
            }
        }
    }
}
