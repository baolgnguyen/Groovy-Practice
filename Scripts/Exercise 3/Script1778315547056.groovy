import custom.EmployeeUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.kms.katalon.core.configuration.RunConfiguration

// Define project directory
def projectDir = RunConfiguration.getProjectDir()

import custom.FileImporter
import custom.FileExporter

// Import employees from CSV
def employees = CustomKeywords.'custom.FileImporter.readCsv'(
	projectDir + "/employees.csv",
	["name", "position", "office", "age", "startDate", "salary"],
	{ cols, idx ->
		if (cols.size() < 6) return null
		[
			name      : cols[0]?.trim(),
			position  : cols[1]?.trim(),
			office    : cols[2]?.trim(),
			age       : cols[3]?.trim()?.toInteger(),
			startDate : cols[4]?.trim(),
			salary    : cols[5]?.trim()?.toDouble()
		]
	}
)
// Import exchange from CSV (take the first row)
def exchangeList = CustomKeywords.'custom.FileImporter.readCsv'(
	projectDir + "/exchange.csv",
	["USD", "VND"],
	{ cols, idx ->
		[
			USD: cols[0]?.toDouble(),
			VND: cols[1]?.toDouble()
		]
	}
)
def exchange = exchangeList ? exchangeList[0] : null

println exchange
println employees

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
def employeesWith3InAge = EmployeeUtils.filterByField(employees, "age", "contains", "3")
println "People with the number 3 in their age: " + employeesWith3InAge

// People with start date from 1/1/2011 onwards
def dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy")
def targetDate = LocalDate.of(2011, 1, 1)
def employeesFrom2011 = EmployeeUtils.filterEmployees(employees) { emp ->
	def empDate = LocalDate.parse(emp.startDate, dtf)
	empDate >= targetDate
}
println "People with start date from 1/1/2011 onwards" + employeesFrom2011

// People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from other CSV file)
def qualifiedEmployees = EmployeeUtils.filterEmployees(employees) { emp ->
	["Accountant", "Software Engineer"].contains(emp.position) && (emp.salary * exchange.VND < 5000000)
}
println "People with position as Accountant or Software Engineer and salary < 5 million VND " + qualifiedEmployees

