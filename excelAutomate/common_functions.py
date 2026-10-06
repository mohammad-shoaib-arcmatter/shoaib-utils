from datetime import datetime, timedelta
import calendar

def getCellNo(sheet, row, column):
    return sheet.cell(row, column).coordinate

def next_month(currDate):
    currDate = datetime.strptime(currDate, '%d-%m-%Y')
    tmpDate = currDate + timedelta(days=1)

    nextMonthDays = calendar.monthrange(tmpDate.year, tmpDate.month)[1]

    nextDate = currDate + timedelta(days=nextMonthDays)
    return nextDate.strftime('%d-%m-%Y')
