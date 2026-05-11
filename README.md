
# Katalon Groovy Practice Project

## Purpose

This project is designed for practicing Groovy scripting and custom keyword development in Katalon Studio. It includes 3 exercises that guide you through reading, filtering, and exporting data using Excel, JSON, and CSV files.

## Exercises

### 1. Exercise 1: Excel Data Filtering & Export to JSON

Use Katalon to get the local Excel file and filter data by these conditions:

- Salary of Bradley
- People with Salary > $400
- First 3 people with office in Tokyo
- People <= 40 years old
- People with the number 3 in their age
- People with start date from 1/1/2011 onwards
- People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from the sheet)
- Write all data to JSON file

### 2. Exercise 2: JSON Data Filtering & Export to CSV

Get data from the JSON file in the previous exercise and filter data by these conditions:

- Salary of Bradley
- People with Salary > $400
- First 3 people with office in Tokyo
- People <= 40 years old
- People with the number 3 in their age
- People with start date from 1/1/2011 onwards
- People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from other JSON file)
- Write all data to CSV file

### 3. Exercise 3: CSV Data Filtering

Get data from the CSV file in the previous exercise and filter data by these conditions:

- Salary of Bradley
- People with Salary > $400
- First 3 people with office in Tokyo
- People <= 40 years old
- People with the number 3 in their age
- People with start date from 1/1/2011 onwards
- People with position as Accountant or Software Engineer and salary < 5 million VND (take the exchange rate from other CSV file)


## Structure

- **Keywords/custom/**: Generic, reusable custom keywords for file IO and employee filtering.
  - `FileImporter.groovy`: Read Excel, JSON, CSV files (with @Keyword annotation).
  - `FileExporter.groovy`: Write JSON, CSV files (with @Keyword annotation).
  - `EmployeeUtils.groovy`: Generic employee filtering/searching (with @Keyword annotation).
- **Scripts/Exercise 1/2/3**: Example scripts showing how to use the custom keywords for real-world data processing tasks.
- **Data Files**: Example input/output files (CSV, JSON, Excel).
  - `employees.xlsx`: Sample Excel file with employee and exchange rate data (input for Exercise 1).
  - `employees.json`: Output from Exercise 1, input for Exercise 2.
  - `exchange.json`: Output from Exercise 1, input for Exercise 2.
  - `employees.csv`: Output from Exercise 2, input for Exercise 3.
  - `exchange.csv`: Output from Exercise 2, input for Exercise 3.

### Example File Formats

**employees.xlsx** (sheet: employee)

| name           | position           | office | age | startDate  | salary |
|----------------|--------------------|--------|-----|------------|--------|
| Bradley Greer  | Software Engineer  | London | 41  | 13/10/2012 | 400    |
| ...            | ...                | ...    | ... | ...        | ...    |

**exchange.xlsx** (sheet: exchange rate)

| USD | VND     |
|-----|---------|
| 1   | 23000   |

**employees.json**
```json
[
  {
    "name": "Bradley Greer",
    "position": "Software Engineer",
    "office": "London",
    "age": 41,
    "startDate": "13/10/2012",
    "salary": 400
  },
  ...
]
```

**exchange.json**
```json
{
  "USD": 1,
  "VND": 23000
}
```

**employees.csv**
```
name,position,office,age,startDate,salary
Bradley Greer,Software Engineer,London,41,13/10/2012,400
...
```

**exchange.csv**
```
USD,VND
1,23000
```

## Usage

1. **Custom Keywords**
   - All utility methods are exposed as Katalon custom keywords (annotated with `@Keyword`).
   - Call them in scripts or test cases using:
     ```groovy
     CustomKeywords.'custom.FileImporter.readExcelSheet'(...)
     CustomKeywords.'custom.EmployeeUtils.filterByField'(...)
     CustomKeywords.'custom.FileExporter.writeJsonFile'(...)
     ```

2. **Exercises**
   - Each script in `Scripts/Exercise 1/2/3` demonstrates:
     - Importing data from Excel/JSON/CSV
     - Filtering/searching employee data
     - Writing results to JSON/CSV
     - Using closure-based, generic utilities for maintainability
