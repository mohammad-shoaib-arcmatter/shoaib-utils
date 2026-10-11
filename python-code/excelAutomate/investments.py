def write_invest_details(sheet, rowNo, investments, bankDet, dateDet):

    totInvest = 0
    for invest in investments:
        investAmt = float(invest['investAmt'])
        investDate = int(invest['investDate'])
        sheet.cell(rowNo, 1).value = investDate
        sheet.cell(rowNo, 2).value = invest['investName']
        sheet.cell(rowNo, 3).value = investAmt
        sheet.cell(rowNo, 4).value = 0
        sheet.cell(rowNo, 5).value = 0
        rowNo += 1

        totInvest += investAmt
        bankName = invest['investBank']
        bankAmount = bankDet.get(bankName)
        if bankAmount is None:
            bankDet[bankName] = investAmt
        else:
            bankDet[bankName] = bankAmount + investAmt

        dateAmount = dateDet.get(investDate)
        if dateAmount is None:
            dateDet[investDate] = investAmt
        else:
            dateDet[investDate] = dateAmount + investAmt

    return rowNo, bankDet, dateDet, totInvest