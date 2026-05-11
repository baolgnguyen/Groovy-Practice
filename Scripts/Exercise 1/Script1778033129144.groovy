import custom.EmployeeUtils
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

import custom.FileImporter

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput
import custom.FileExporter

// Get the current Katalon project directory
// Example: C:/Users/.../Katalon Studio/BasicGroovy
def projectDir = RunConfiguration.getProjectDir()
// Path to the Excel file containing employee and exchange data
def excelPath = projectDir + "/data.xlsx"

// Import employees from Excel sheet "employee"
def employees = CustomKeywords.'custom.FileImporter.readExcelSheet'(
	excelPath,
	"employee",
	["name", "position", "office", "age", "startDate", "salary"],
	{ row, idx ->
		// Skip empty rows (no name)
		if (!row?.getCell(0)?.toString()?.trim()) return null
		// Map each row to an employee map
		[
			name      : row.getCell(0)?.toString()?.trim(),
			position  : row.getCell(1)?.toString()?.trim(),
			office    : row.getCell(2)?.toString()?.trim(),
			age       : row.getCell(3)?.numericCellValue?.toInteger() ?: 0,
			startDate : row.getCell(4)?.dateCellValue,
			salary    : row.getCell(5)?.numericCellValue ?: 0
		]
	}
)

// Import exchange rate from Excel sheet "exchange rate" (take the first row)
def exchangeList = CustomKeywords.'custom.FileImporter.readExcelSheet'(
	excelPath,
	"exchange rate",
	["USD", "VND"],
	{ row, idx ->
		[
			USD: row.getCell(0)?.numericCellValue,
			VND: row.getCell(1)?.numericCellValue
		]
	}
)
// Use the first row as the exchange data
def exchangeData = exchangeList ? exchangeList[0] : null

// Print imported employees and exchange data
println employees
println exchangeData

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
def sdf = new SimpleDateFormat("dd/MM/yyyy")
def targetDate = sdf.parse("01/01/2011")
def employeesFrom2011 = CustomKeywords.'custom.EmployeeUtils.filterEmployees'(employees) { emp -> emp.startDate >= targetDate }
println "People with start date from 1/1/2011 onwards" + employeesFrom2011

// People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from the sheet)
def qualifiedEmployees = CustomKeywords.'custom.EmployeeUtils.filterEmployees'(employees) { emp ->
	["Accountant", "Software Engineer"].contains(emp.position) && (emp.salary * exchangeData.VND < 5000000)
}
println "People with position as Accountant or Software Engineer and salary < 5 million VND: " + qualifiedEmployees

// Write all employee data to a JSON file using the custom keyword
// Convert startDate to string for JSON serialization
def employeesForJson = employees.collect { emp ->
	def newEmp = emp.clone()
	newEmp.startDate = sdf.format(emp.startDate)
	newEmp
}
def jsonOutputPath = projectDir + "/employees.json"
CustomKeywords.'custom.FileExporter.writeJsonFile'(employeesForJson, jsonOutputPath)

// Write exchange data to a JSON file using the custom keyword
def exchangeOutputPath = projectDir + "/exchange.json"
CustomKeywords.'custom.FileExporter.writeJsonFile'(exchangeData, exchangeOutputPath)