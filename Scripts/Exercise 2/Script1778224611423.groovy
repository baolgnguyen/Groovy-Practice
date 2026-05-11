import custom.EmployeeUtils
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import com.kms.katalon.core.configuration.RunConfiguration
import custom.FileImporter
import custom.FileExporter

// Define project directory
def projectDir = RunConfiguration.getProjectDir()

// Import employees and exchange from JSON
def employees = CustomKeywords.'custom.FileImporter.readJson'(projectDir + "/employees.json")
def exchange = CustomKeywords.'custom.FileImporter.readJson'(projectDir + "/exchange.json")

println employees
println exchange

// Salary of Bradley
def bradley = CustomKeywords.'custom.EmployeeUtils.filterByField'(employees, "name", "eq", "Bradley Greer")
println "Bradley Salary: " + bradley?.salary

// People with Salary > $400
def highSalaryEmployees = CustomKeywords.'custom.EmployeeUtils.filterByField'(employees, "salary", "gt", 400)
println "People with Salary > \$400: " + highSalaryEmployees

// First 3 people with office in Tokyo
def first3TokyoEmployees = CustomKeywords.'custom.EmployeeUtils.filterByField'(employees, "office", "eq", "Tokyo").take(3)
println "First 3 people with office in Tokyo: " + first3TokyoEmployees

// People <= 40 years old
def youngEmployees = CustomKeywords.'custom.EmployeeUtils.filterByField'(employees, "age", "lte", 40)
println "People <= 40 years old: " + youngEmployees

// People with the number 3 in their age
def employeesWith3InAge = CustomKeywords.'custom.EmployeeUtils.filterByField'(employees, "age", "contains", "3")
println "People with the number 3 in their age: " + employeesWith3InAge

// People with start date from 1/1/2011 onwards
def dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy")
def targetDate = LocalDate.of(2011, 1, 1)
def employeesFrom2011 = CustomKeywords.'custom.EmployeeUtils.filterEmployees'(employees) { emp ->
	def empDate = LocalDate.parse(emp.startDate, dtf)
	empDate >= targetDate
}
println "People with start date from 1/1/2011 onwards" + employeesFrom2011

// People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from other JSON file)
def qualifiedEmployees = CustomKeywords.'custom.EmployeeUtils.filterEmployees'(employees) { emp ->
	["Accountant", "Software Engineer"].contains(emp.position) && (emp.salary * exchange.VND < 5000000)
}
println "People with position as Accountant or Software Engineer and salary < 5 million VND " + qualifiedEmployees

// Write all data to CSV file using custom keyword
def employeesCsvPath = projectDir + "/employees.csv"
def employeeHeaders = ["name", "position", "office", "age", "startDate", "salary"]
CustomKeywords.'custom.FileExporter.writeListToCsvFile'(employees, employeeHeaders, employeesCsvPath)

def exchangeCsvPath = projectDir + "/exchange.csv"
def exchangeHeaders = ["USD", "VND"]
CustomKeywords.'custom.FileExporter.writeMapToCsvFile'(exchange, exchangeHeaders, exchangeCsvPath)
