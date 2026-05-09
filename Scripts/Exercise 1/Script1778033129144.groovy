import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

import org.apache.poi.ss.usermodel.WorkbookFactory

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput

// Get current Katalon project directory
// Example: C:/Users/.../Katalon Studio/BasicGroovy
def projectDir = RunConfiguration.getProjectDir()
def excelPath = projectDir + "/data.xlsx"

// Open workbook and target sheets
def workbook = WorkbookFactory.create(new File(excelPath))
def empSheet = workbook.getSheet("employee")
def rateSheet = workbook.getSheet("exchange rate")

// Read exchange rate sheet
def exchangeData = [
	USD: rateSheet.getRow(1).getCell(0).numericCellValue,
	VND: rateSheet.getRow(1).getCell(1).numericCellValue
]

// Read employee sheet
def employees = []
//skip the header row
empSheet.drop(1).each { row ->
	
	// Skip empty row
	if (!row?.getCell(0)?.toString()?.trim()) {
        return
    }

	// // Map Excel cells to a structured Map object
    def employee = [
        name      : row.getCell(0)?.toString()?.trim(),
        position  : row.getCell(1)?.toString()?.trim(),
        office    : row.getCell(2)?.toString()?.trim(),
        age       : row.getCell(3)?.numericCellValue?.toInteger() ?: 0,
        startDate : row.getCell(4)?.dateCellValue,
        salary    : row.getCell(5)?.numericCellValue ?: 0
    ]

    employees.add(employee)
}

println employees
println exchangeData

// Salary of Bradley
def bradley = employees.find { emp ->
	emp.name == "Bradley Greer" }
println "Bradley Salary: " + bradley?.salary

// People with Salary > $400
def highSalaryEmployees = employees.findAll{ emp ->
	emp.salary > 400 } 
println "People with Salary > \$400: " + highSalaryEmployees

// First 3 people with office in Tokyo
def first3TokyoEmployees = employees.findAll { emp ->
	emp.office == "Tokyo" }.take(3)
println "First 3 people with office in Tokyo: " + first3TokyoEmployees

// People <= 40 years old
def youngEmployees = employees.findAll {emp ->
	emp.age <= 40 }
println "People <= 40 years old: " + youngEmployees

// People with the number 3 in their age
def employeesWith3InAge = employees.findAll {emp ->
	emp.age.toString().contains("3")
}
println "People with the number 3 in their age: " + employeesWith3InAge

// People with start date from 1/1/2011 onwards
def sdf = new SimpleDateFormat("dd/MM/yyyy")
def targetDate = sdf.parse("01/01/2011")

def employeesFrom2011 = employees.findAll { emp ->
	emp.startDate >= targetDate
}
println "People with start date from 1/1/2011 onwards" + employeesFrom2011

// People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from the sheet)
def qualifiedEmployees = employees.findAll { emp ->
	def salaryVND = emp.salary * exchangeData.VND
	def isTargetJob = emp.position == "Accountant" || emp.position == "Software Engineer"
	
	isTargetJob && salaryVND < 5000000
}
println "People with position as Accountant or Software Engineer and salary < 5 million VND: " + qualifiedEmployees

// Write all data to JSON file
// employee.json
// Convert Date objects to formatted Strings
def employeesForJson = employees.collect { emp ->
    def newEmp = emp.clone()
    newEmp.startDate = sdf.format(emp.startDate)
    newEmp
}
	
def json = JsonOutput.prettyPrint(
	JsonOutput.toJson(employeesForJson))
def jsonOutputPath = projectDir + "/employees.json"

new File(jsonOutputPath).withWriter { writer ->
	writer.write(json)
}

// exchange.json
def exchangeJson = JsonOutput.prettyPrint(
	JsonOutput.toJson(exchangeData))
def exchangeOutputPath = projectDir + "/exchange.json"

new File(exchangeOutputPath).withWriter { writer ->
	writer.write(exchangeJson)
}

workbook.close()
