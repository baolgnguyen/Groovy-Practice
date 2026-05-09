import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.kms.katalon.core.configuration.RunConfiguration

// Define project directory
def projectDir = RunConfiguration.getProjectDir()

// Parse CSV data
// employees.csv
def employeesCsv = new File(projectDir + "/employees.csv").readLines()

def employees = []

employeesCsv.drop(1).each { line ->
	// Regex explanation: Splits by comma only if it's not inside a pair of double quotes
	def cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*\$)").collect { it.replaceAll('^\"|\"$', '') }

	// Skip malformed rows that do not meet the expected column count
	if (cols.size() < 6) return

	employees << [
		name      : cols[0]?.trim(),
		position  : cols[1]?.trim(),
		office    : cols[2]?.trim(),
		age       : cols[3]?.trim()?.toInteger(),
		startDate : cols[4]?.trim(),
		salary    : cols[5]?.trim()?.toDouble()
	]
}

// exchange.csv
def exchangeCsv = new File(projectDir + "/exchange.csv").readLines()

def exchangeRow = exchangeCsv[1].split(",")

def exchange = [
	USD: exchangeRow[0]?.toDouble(),
	VND: exchangeRow[1]?.toDouble()
]

println exchange
println employees

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

// People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from other CSV file)
def qualifiedEmployees = employees.findAll { emp ->
	def salaryVND = emp.salary * exchange.VND
	def isTargetJob = emp.position == "Accountant" || emp.position == "Software Engineer"
	
	isTargetJob && salaryVND < 5000000
}
println "People with position as Accountant or Software Engineer and salary < 5 million VND " + qualifiedEmployees


