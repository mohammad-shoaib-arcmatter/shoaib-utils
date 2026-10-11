package com.shoaib.excelautomate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class ExcelAutomate {
    private static final ObjectMapper JSON = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-uuuu");

    private ExcelAutomate() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: mvn exec:java -Dexec.args=\"<workbook.xlsx> <data.json>\"");
            System.exit(2);
        }
        writeExcel(Path.of(args[0]), Path.of(args[1]));
    }

    public static void writeExcel(Path workbookPath, Path dataPath) throws IOException {
        FinanceData data = readDataFile(dataPath);
        CurrentDetails current = data.currentDetails.get(0);
        Map<String, Double> bankTotals = new LinkedHashMap<>();
        Map<Integer, Double> dateTotals = new TreeMap<>();
        Path absoluteWorkbookPath = workbookPath.toAbsolutePath();
        Path temporaryWorkbook = Files.createTempFile(
                absoluteWorkbookPath.getParent(), "excel-automate-", ".xlsx");

        try {
            try (Workbook workbook = WorkbookFactory.create(absoluteWorkbookPath.toFile())) {
                Sheet sheet = workbook.getSheet("Debt");
                if (sheet == null) {
                    throw new IOException("Workbook does not contain a 'Debt' sheet: " + workbookPath);
                }
                clearSheet(sheet);

                int row = writeHeader(sheet);
                Totals expenseTotals = writeExpenseDetails(sheet, row, data.expenses, bankTotals, dateTotals);
                row = expenseTotals.nextRow;
                Totals investmentTotals = writeInvestmentDetails(sheet, row, data.investments, bankTotals, dateTotals);
                row = investmentTotals.nextRow;
                LoanTotals loanTotals = writeLoanDetails(sheet, row, data.loans, bankTotals, dateTotals);
                row = loanTotals.nextRow;

                Summary summary = writeSummary(
                        sheet, row, current, bankTotals,
                        investmentTotals.total, expenseTotals.total, loanTotals.total, data.customerLoans);
                row = writeDatewise(sheet, summary.nextRow, dateTotals);

                double remainingLoan = loanTotals.pending;
                double customerLoanTotal = data.customerLoans.stream()
                        .mapToDouble(loan -> parseDouble(loan.amount, "custloanAmt"))
                        .sum();
                if (customerLoanTotal > 0) {
                    remainingLoan += customerLoanTotal;
                }

                if (loanTotals.maxEmi > 0) {
                    writeLoanEmiDetails(
                            sheet, current, data.loans, loanTotals.maxEmi,
                            summary.expenseCell, summary.investmentCell, remainingLoan,
                            row);
                }

                workbook.setForceFormulaRecalculation(true);
                try (var output = Files.newOutputStream(temporaryWorkbook)) {
                    workbook.write(output);
                }
            }
            try {
                Files.move(temporaryWorkbook, absoluteWorkbookPath,
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryWorkbook, absoluteWorkbookPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryWorkbook);
        }
    }

    public static void advanceMonth(Path dataPath) throws IOException {
        Path backupPath = Path.of("data_backup.json");
        Files.copy(dataPath, backupPath, StandardCopyOption.REPLACE_EXISTING);

        JsonNode root = JSON.readTree(dataPath.toFile());
        if (!(root instanceof ObjectNode data)) {
            throw new IOException("Data file must contain a JSON object: " + dataPath);
        }
        JsonNode currentDetails = data.path("currDet");
        if (!currentDetails.isArray() || currentDetails.isEmpty()
                || !(currentDetails.get(0) instanceof ObjectNode current)) {
            throw new IOException("Data file must contain currDet[0]");
        }
        String currentDate = requiredText(current, "date");
        current.put("date", nextMonth(currentDate));

        JsonNode loanDetails = data.path("loans");
        if (!loanDetails.isArray()) {
            throw new IOException("Data file must contain a loans array");
        }
        ArrayNode loans = (ArrayNode) loanDetails;
        List<JsonNode> sortedLoans = new ArrayList<>();
        loans.forEach(sortedLoans::add);
        sortedLoans.sort(Comparator.comparingInt(
                loan -> parseInteger(requiredText(loan, "loanEMI"), "loanEMI")).reversed());

        ArrayNode updatedLoans = JSON.createArrayNode();
        for (JsonNode loan : sortedLoans) {
            int emi = parseInteger(requiredText(loan, "loanEMI"), "loanEMI");
            if (emi > 0) {
                ((ObjectNode) loan).put("loanEMI", Integer.toString(emi - 1));
                updatedLoans.add(loan);
            }
        }
        data.set("loans", updatedLoans);

        JSON.writeValue(dataPath.toFile(), data);
    }

    private static FinanceData readDataFile(Path dataPath) throws IOException {
        FinanceData data = JSON.readValue(dataPath.toFile(), FinanceData.class);
        if (data.currentDetails == null || data.currentDetails.isEmpty()
                || data.currentDetails.get(0) == null) {
            throw new IOException("Data file must contain at least one currDet entry");
        }
        if (data.expenses == null || data.investments == null || data.loans == null
                || data.customerLoans == null) {
            throw new IOException("Data file must contain expenses, investments, loans, and custloans arrays");
        }

        data.loans.sort(Comparator.comparingInt(
                loan -> parseInteger(loan.emi, "loanEMI")).reversed());
        data.investments.sort(Comparator.comparingInt(
                investment -> parseInteger(investment.date, "investDate")));
        data.expenses.sort(Comparator.comparingInt(
                expense -> parseInteger(expense.date, "expDate")));
        return data;
    }

    private static void clearSheet(Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setBlank();
            }
        }
    }

    private static int writeHeader(Sheet sheet) {
        setCell(sheet, 1, 1, "Date");
        setCell(sheet, 1, 2, "Expense Type");
        setCell(sheet, 1, 3, "Amount");
        setCell(sheet, 1, 4, "EMI Left");
        setCell(sheet, 1, 5, "Total Amount");
        return 2;
    }

    private static Totals writeExpenseDetails(
            Sheet sheet, int row, List<Expense> expenses,
            Map<String, Double> bankTotals, Map<Integer, Double> dateTotals) {
        double total = 0;
        for (Expense expense : expenses) {
            double amount = parseDouble(expense.amount, "expAmt");
            int date = parseInteger(expense.date, "expDate");
            setCell(sheet, row, 1, date);
            setCell(sheet, row, 2, expense.name);
            setCell(sheet, row, 3, amount);
            setCell(sheet, row, 4, 0);
            setCell(sheet, row, 5, 0);
            row++;
            total += amount;
            addTotal(bankTotals, required(expense.bank, "expBank"), amount);
            addTotal(dateTotals, date, amount);
        }
        return new Totals(row, total);
    }

    private static Totals writeInvestmentDetails(
            Sheet sheet, int row, List<Investment> investments,
            Map<String, Double> bankTotals, Map<Integer, Double> dateTotals) {
        double total = 0;
        for (Investment investment : investments) {
            double amount = parseDouble(investment.amount, "investAmt");
            int date = parseInteger(investment.date, "investDate");
            setCell(sheet, row, 1, date);
            setCell(sheet, row, 2, investment.name);
            setCell(sheet, row, 3, amount);
            setCell(sheet, row, 4, 0);
            setCell(sheet, row, 5, 0);
            row++;
            total += amount;
            addTotal(bankTotals, required(investment.bank, "investBank"), amount);
            addTotal(dateTotals, date, amount);
        }
        return new Totals(row, total);
    }

    private static LoanTotals writeLoanDetails(
            Sheet sheet, int row, List<Loan> loans,
            Map<String, Double> bankTotals, Map<Integer, Double> dateTotals) {
        double total = 0;
        double pendingTotal = 0;
        int maxEmi = 0;
        for (Loan loan : loans) {
            double amount = parseDouble(loan.amount, "loanAmt");
            int date = parseInteger(loan.date, "loanDate");
            int emi = parseInteger(loan.emi, "loanEMI");
            setCell(sheet, row, 1, date);
            setCell(sheet, row, 2, loan.name);
            setCell(sheet, row, 3, amount);
            setCell(sheet, row, 4, emi);
            setFormula(sheet, row, 5, cellAddress(row, 3) + " * " + cellAddress(row, 4));
            row++;

            total += amount;
            pendingTotal += amount * emi;
            addTotal(bankTotals, required(loan.bank, "loanBank"), amount);
            addTotal(dateTotals, date, amount);
            maxEmi = Math.max(maxEmi, emi);
        }
        return new LoanTotals(row, total, maxEmi, pendingTotal);
    }

    private static Summary writeSummary(
            Sheet sheet, int row, CurrentDetails current, Map<String, Double> bankTotals,
            double totalInvestments, double totalExpenses, double totalLoans,
            List<CustomerLoan> customerLoans) {
        setCell(sheet, row, 2, "Total Exp");
        setFormula(sheet, row, 3, "SUM(" + cellAddress(2, 3) + ":" + cellAddress(row - 1, 3) + ")");

        if (totalLoans > 0) {
            setCell(sheet, row, 4, "Total EMI");
            setFormula(sheet, row, 5, "SUM(" + cellAddress(2, 5) + ":" + cellAddress(row - 1, 5) + ")");
            setCell(sheet, row + 1, 2, "Date");
            setCell(sheet, row + 1, 3, required(current.date, "date"));
        }

        row++;
        int customerLoanCount = writeCustomerLoanDetails(sheet, row, customerLoans);
        row += customerLoanCount;

        if (totalLoans > 0 || customerLoanCount > 0) {
            setCell(sheet, row, 4, "Total Loan");
            setFormula(sheet, row, 5,
                    "SUM(" + cellAddress(row - customerLoanCount - 1, 5) + ":"
                            + cellAddress(row - 1, 5) + ")");
            row++;
            setCell(sheet, row, 4, "Year Starting");
            setCell(sheet, row, 5, parseInteger(current.yearStartingAmount, "yearStAmt"));
            row++;
        }

        row++;
        setCell(sheet, row, 2, "Investments");
        setCell(sheet, row, 3, totalInvestments);
        String investmentCell = absoluteCellAddress(row, 3);

        row++;
        setCell(sheet, row, 2, "Expense");
        setCell(sheet, row, 3, totalExpenses);
        String expenseCell = absoluteCellAddress(row, 3);

        if (totalLoans > 0) {
            row++;
            setCell(sheet, row, 2, "Debt");
            setCell(sheet, row, 3, totalLoans);
            row++;
        }

        row++;
        for (Map.Entry<String, Double> bank : bankTotals.entrySet()) {
            setCell(sheet, row, 2, bank.getKey());
            setCell(sheet, row, 3, bank.getValue());
            row++;
        }
        return new Summary(row + 1, expenseCell, investmentCell);
    }

    private static int writeCustomerLoanDetails(
            Sheet sheet, int row, List<CustomerLoan> customerLoans) {
        for (CustomerLoan loan : customerLoans) {
            setCell(sheet, row, 4, required(loan.name, "custloanName"));
            setCell(sheet, row, 5, parseDouble(loan.amount, "custloanAmt"));
            row++;
        }
        return customerLoans.size();
    }

    private static int writeDatewise(Sheet sheet, int row, Map<Integer, Double> dateTotals) {
        setCell(sheet, row, 2, "Date-wise");
        setCell(sheet, row, 3, "Amount");
        setCell(sheet, row, 4, "Commulative");

        TreeMap<Integer, Double> sortedTotals = new TreeMap<>(dateTotals);
        Map<Integer, Double> cumulative = new TreeMap<>();
        double runningTotal = 0;
        for (Map.Entry<Integer, Double> entry : sortedTotals.descendingMap().entrySet()) {
            runningTotal += entry.getValue();
            cumulative.put(entry.getKey(), runningTotal);
        }

        row++;
        for (Map.Entry<Integer, Double> entry : sortedTotals.entrySet()) {
            setCell(sheet, row, 2, entry.getKey());
            setCell(sheet, row, 3, entry.getValue());
            setCell(sheet, row, 4, cumulative.get(entry.getKey()));
            row++;
        }
        return row + 1;
    }

    private static void writeLoanEmiDetails(
            Sheet sheet, CurrentDetails current, List<Loan> loans, int maxEmi,
            String expenseCell, String investmentCell, double remainingLoan,
            int row) {
        String currentDate = required(current.date, "date");
        setCell(sheet, row, 1, "Month");
        setCell(sheet, row, 2, "Total EMI");
        setCell(sheet, row, 3, "Remaining Amount");
        setCell(sheet, row, 4, "Total exp");
        row++;

        setCell(sheet, row, 3, remainingLoan);
        row++;
        for (int i = 0; i <= maxEmi; i++) {
            double totalEmi = 0;
            for (Loan loan : loans) {
                double amount = parseDouble(loan.amount, "loanAmt");
                int emi = parseInteger(loan.emi, "loanEMI");
                if (emi - i > 0) {
                    totalEmi += amount;
                }
            }
            if (totalEmi > 0) {
                currentDate = nextMonth(currentDate);
                remainingLoan -= totalEmi;
                setCell(sheet, row, 1, currentDate);
                setCell(sheet, row, 2, totalEmi);
                setFormula(sheet, row, 3,
                        cellAddress(row - 1, 3) + "-" + cellAddress(row, 2));
                setFormula(sheet, row, 4,
                        expenseCell + "+" + investmentCell + "+" + totalEmi);
                row++;
            }
        }
    }

    private static String nextMonth(String currentDate) {
        try {
            LocalDate date = LocalDate.parse(currentDate, DATE_FORMAT);
            YearMonth nextMonth = YearMonth.from(date.plusDays(1));
            return date.plusDays(nextMonth.lengthOfMonth()).format(DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid date; expected dd-MM-yyyy: " + currentDate, exception);
        }
    }

    private static void addTotal(Map<String, Double> totals, String key, double amount) {
        totals.merge(key, amount, Double::sum);
    }

    private static void addTotal(Map<Integer, Double> totals, int key, double amount) {
        totals.merge(key, amount, Double::sum);
    }

    private static void setCell(Sheet sheet, int rowNumber, int columnNumber, String value) {
        getOrCreateCell(sheet, rowNumber, columnNumber).setCellValue(value);
    }

    private static void setCell(Sheet sheet, int rowNumber, int columnNumber, int value) {
        getOrCreateCell(sheet, rowNumber, columnNumber).setCellValue(value);
    }

    private static void setCell(Sheet sheet, int rowNumber, int columnNumber, double value) {
        getOrCreateCell(sheet, rowNumber, columnNumber).setCellValue(value);
    }

    private static void setFormula(Sheet sheet, int rowNumber, int columnNumber, String formula) {
        getOrCreateCell(sheet, rowNumber, columnNumber).setCellFormula(formula);
    }

    private static Cell getOrCreateCell(Sheet sheet, int rowNumber, int columnNumber) {
        Row row = sheet.getRow(rowNumber - 1);
        if (row == null) {
            row = sheet.createRow(rowNumber - 1);
        }
        Cell cell = row.getCell(columnNumber - 1);
        return cell == null ? row.createCell(columnNumber - 1) : cell;
    }

    private static String cellAddress(int rowNumber, int columnNumber) {
        return CellReference.convertNumToColString(columnNumber - 1) + rowNumber;
    }

    private static String absoluteCellAddress(int rowNumber, int columnNumber) {
        return "$" + CellReference.convertNumToColString(columnNumber - 1) + "$" + rowNumber;
    }

    private static String required(String value, String field) {
        if (value == null) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return value;
    }

    private static String requiredText(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return value.asText();
    }

    private static double parseDouble(String value, String field) {
        try {
            return Double.parseDouble(required(value, field));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid number for " + field + ": " + value, exception);
        }
    }

    private static int parseInteger(String value, String field) {
        try {
            return Integer.parseInt(required(value, field));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid integer for " + field + ": " + value, exception);
        }
    }

    public static final class FinanceData {
        @com.fasterxml.jackson.annotation.JsonProperty("currDet")
        public List<CurrentDetails> currentDetails;
        public List<Expense> expenses;
        public List<Investment> investments;
        public List<Loan> loans;
        @com.fasterxml.jackson.annotation.JsonProperty("custloans")
        public List<CustomerLoan> customerLoans;

        public FinanceData() {
        }

    }

    public static final class CurrentDetails {
        @com.fasterxml.jackson.annotation.JsonProperty("date")
        public String date;
        @com.fasterxml.jackson.annotation.JsonProperty("yearStAmt")
        public String yearStartingAmount;

        public CurrentDetails() {
        }
    }

    public static final class Expense {
        @com.fasterxml.jackson.annotation.JsonProperty("expAmt")
        public String amount;
        @com.fasterxml.jackson.annotation.JsonProperty("expBank")
        public String bank;
        @com.fasterxml.jackson.annotation.JsonProperty("expDate")
        public String date;
        @com.fasterxml.jackson.annotation.JsonProperty("expName")
        public String name;

        public Expense() {
        }
    }

    public static final class Investment {
        @com.fasterxml.jackson.annotation.JsonProperty("investAmt")
        public String amount;
        @com.fasterxml.jackson.annotation.JsonProperty("investBank")
        public String bank;
        @com.fasterxml.jackson.annotation.JsonProperty("investDate")
        public String date;
        @com.fasterxml.jackson.annotation.JsonProperty("investName")
        public String name;

        public Investment() {
        }
    }

    public static final class Loan {
        @com.fasterxml.jackson.annotation.JsonProperty("loanAmt")
        public String amount;
        @com.fasterxml.jackson.annotation.JsonProperty("loanBank")
        public String bank;
        @com.fasterxml.jackson.annotation.JsonProperty("loanDate")
        public String date;
        @com.fasterxml.jackson.annotation.JsonProperty("loanEMI")
        public String emi;
        @com.fasterxml.jackson.annotation.JsonProperty("loanName")
        public String name;

        public Loan() {
        }
    }

    public static final class CustomerLoan {
        @com.fasterxml.jackson.annotation.JsonProperty("custloanAmt")
        public String amount;
        @com.fasterxml.jackson.annotation.JsonProperty("custloanName")
        public String name;

        public CustomerLoan() {
        }
    }

    private record Totals(int nextRow, double total) {
    }

    private record LoanTotals(int nextRow, double total, int maxEmi, double pending) {
    }

    private record Summary(int nextRow, String expenseCell, String investmentCell) {
    }
}
