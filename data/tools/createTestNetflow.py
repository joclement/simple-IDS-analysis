import csv
import sys

srcCsvfilename = sys.argv[1]
dstCsvfilename = sys.argv[2]

with open(srcCsvfilename, 'r+', newline='') as srcCsvfile,\
     open(dstCsvfilename, 'w+', newline='') as dstCsvfile:

    reader = csv.reader(srcCsvfile, dialect='unix',
                        delimiter=',', quotechar='|')
    writer = csv.writer(dstCsvfile, dialect='unix', delimiter=',',
                        quotechar='|', quoting=csv.QUOTE_MINIMAL)

    headers = next(reader)
    writer.writerow(headers)

    usedLabels = []

    for row in reader:
        if not row[-1] in usedLabels:
            usedLabels.append(row[-1])
            print(row)
            writer.writerow(row)
