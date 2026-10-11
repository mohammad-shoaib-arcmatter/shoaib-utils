# Excel Automate (Java)

This Java 17 application reads the finance data JSON and fills the `Debt` worksheet in an existing `.xlsx` workbook.

## Run

From this directory, provide the workbook and JSON file paths:

```powershell
mvn compile exec:java '-Dexec.args="C:\path\to\Finance - Debt.xlsx" "C:\path\to\data.json"'
```

The workbook must already exist and contain a sheet named `Debt`. Formula cells are written for Excel to recalculate when the workbook is opened.

To advance the stored month and reduce each remaining loan EMI by one, invoke `ExcelAutomate.advanceMonth(Path)` from Java. Before writing the JSON, this operation copies the source file to `data_backup.json` in the current working directory.
