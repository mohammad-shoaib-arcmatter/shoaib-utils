def write_expense_details(sheet, rowNo, expenses, bankDet, dateDet):

    totExp = 0

    for exp in expenses:
        expAmt = float(exp['expAmt'])
        expDate = int(exp['expDate'])
        sheet.cell(rowNo, 1).value = expDate
        sheet.cell(rowNo, 2).value = exp['expName']
        sheet.cell(rowNo, 3).value = expAmt
        sheet.cell(rowNo, 4).value = 0
        sheet.cell(rowNo, 5).value = 0
        rowNo += 1

        totExp += expAmt
        bankName = exp['expBank']
        bankAmount = bankDet.get(bankName)
        if bankAmount is None:
            bankDet[bankName] = expAmt
        else:
            bankDet[bankName] = bankAmount + expAmt

        dateAmount = dateDet.get(expDate)
        if dateAmount is None:
            dateDet[expDate] = expAmt
        else:
            dateDet[expDate] = dateAmount + expAmt

    return rowNo, bankDet, dateDet, totExp