import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonSlurper

// Define project directory
def projectDir = RunConfiguration.getProjectDir()

def jsonSlurper = new JsonSlurper()

// Parse JSON data
// employees.json
def employees = jsonSlurper.parse(
	new File(projectDir + "/employees.json")
)

// exchange.json
def exchange = jsonSlurper.parse(
	new File(projectDir + "/exchange.json")
)

println employees
println exchange

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
def dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy")
def targetDate = LocalDate.of(2011, 1, 1)

def employeesFrom2011 = employees.findAll { emp ->
	def empDate = LocalDate.parse(emp.startDate, dtf)
	empDate >= targetDate
}

println "People with start date from 1/1/2011 onwards" + employeesFrom2011

// People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from other JSON file)
def qualifiedEmployees = employees.findAll { emp ->
	def salaryVND = emp.salary * exchange.VND
	def isTargetJob = emp.position == "Accountant" || emp.position == "Software Engineer"
	
	isTargetJob && salaryVND < 5000000
}
println "People with position as Accountant or Software Engineer and salary < 5 million VND " + qualifiedEmployees

// Write all data to CSV file
// employees.csv
def employeesCsvPath = projectDir + "/employees.csv"

new File(employeesCsvPath).withWriter { writer ->

	// Write CSV Header
	writer.writeLine("Name,Position,Office,Age,StartDate,Salary")

	employees.each { emp ->
		writer.writeLine(
			"${emp.name},${emp.position},${emp.office},${emp.age},${emp.startDate},${emp.salary}"
		)
	}
}

// exchange.csv
def exchangeCsvPath = projectDir + "/exchange.csv"

new File(exchangeCsvPath).withWriter { writer ->

	// Write CSV Header
	writer.writeLine("USD,VND")

	// data row
	writer.writeLine("${exchange.USD},${exchange.VND}")
}

