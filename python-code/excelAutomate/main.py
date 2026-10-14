import os
from write_excel import write_excel, update_json_by_month

if __name__ == '__main__':
    #folderName = r"C:\Users\Shoaib\Desktop\Z6Copy"
    folderName = r"D:\OneDrives\Shoaib - OneDrive\OneDrive\3 Additional Files\Excel Files"
    filename = "Finance - Debt.xlsx"
    dataFile = "data.json"
    fullFilename = os.path.join(folderName, filename)
    fullDataFile = os.path.join(folderName, dataFile)
    #update_json_by_month(fullDataFile)
    write_excel(fullFilename, fullDataFile)
