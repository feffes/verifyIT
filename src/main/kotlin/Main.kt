package main.kotlin
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.poi.ss.usermodel.DataFormatter
import java.io.File
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.util.*



val verCollumn = 0
val verDateCollumn = 1
val verTextCollumn = 7

val ksCollumn = 5
val accountNumberCollumn = 3
val accountNameCollumn = 4
val debetCollumn = 9
val creditCollumn = 10








fun main(args : Array<String>) {




    val dataFormatter = DataFormatter()
    val workbook = WorkbookFactory.create(File(args[0]))
    val sheet = workbook.getSheetAt(0)
    val verifications = ArrayList<Verification>()


    println(sheet.lastRowNum.toString())



    var lastVerificationNumber = ""

    for (i in 1..sheet.lastRowNum){

        val currentRow = sheet.getRow(i)
        val verificationNumber = dataFormatter.formatCellValue(currentRow.getCell(verCollumn))
        val verificationDate = dataFormatter.formatCellValue(currentRow.getCell(verDateCollumn))
        val verificationText = dataFormatter.formatCellValue(currentRow.getCell(verTextCollumn))

        val ks = dataFormatter.formatCellValue(currentRow.getCell(ksCollumn))
        val accountNumber = dataFormatter.formatCellValue(currentRow.getCell(accountNumberCollumn))
        val accountName = dataFormatter.formatCellValue(currentRow.getCell(accountNameCollumn))
        val debet = dataFormatter.formatCellValue(currentRow.getCell(debetCollumn))
        val credit = dataFormatter.formatCellValue(currentRow.getCell(creditCollumn))

        if (lastVerificationNumber != verificationNumber)
            verifications.add(Verification(verificationNumber, verificationText,verificationDate,"")) //Descriptions not fully implemented.



        verifications.get(verifications.lastIndex).addRow(ks, accountName, accountNumber, debet, credit)


        // Someone please fix this whole monstrosity
        if (debet!="") {
            val number = debet.replace(",","")
            val longNumber = number.toLong()
            println(longNumber)
            verifications.get(verifications.lastIndex).total += longNumber
        }
        lastVerificationNumber = verificationNumber

    }


    for(verification in verifications){




        //val verifikatmall = File(ClassLoader.getSystemClassLoader().getResource("Verifikatmall.pdf").file)
        val verifikatmall = ClassLoader.getSystemClassLoader().getResourceAsStream("Verifikatmall.pdf")



        val pdf = PDDocument.load(verifikatmall)
        val docCatalog = pdf.documentCatalog
        val acroForm = docCatalog.acroForm

        val fields = acroForm.fields


        val numberField = fields.get(33)
        val textField = fields.get(0)
        val dateField = fields.get(1)
        val totalField = fields.get(2)


        /* val ks1 = fields.get(3)
        val ks2 = fields.get(4)
        val ks3 = fields.get(5)
        val ks4 = fields.get(6)
        val ks5 = fields.get(7)
        val ks6 = fields.get(8)
        val debet1 = fields.get(9)
        val debet2 = fields.get(10)
        val debet3 = fields.get(11)
        val debet4 = fields.get(12)
        val debet5 = fields.get(13)
        val debet6 = fields.get(14)             This stuff is mostly to keep track of which indices the forms are on.
        val credit1 = fields.get(15)
        val credit2 = fields.get(16)
        val credit3 = fields.get(17)
        val credit4 = fields.get(18)
        val credit5 = fields.get(19)
        val credit6 = fields.get(20)
        val accountNumber1 = fields.get(21)
        val accountNumber2 = fields.get(22)
        val accountNumber3 = fields.get(23)
        val accountNumber4 = fields.get(24)
        val accountNumber5 = fields.get(25)
        val accountNumber6 = fields.get(26)
        val accountName2 = fields.get(27)
        val accountName1 = fields.get(28)
        val accountName3 = fields.get(29)
        val accountName4 = fields.get(30)
        val accountName5 = fields.get(31)
        val accountName6 = fields.get(32)
        */


        numberField.setValue(verification.verificationNumber)
        dateField.setValue(verification.verificationDate)
        textField.setValue(verification.verificationText)


        totalField.setValue(verification.getTotalString())

        println(verification.verificationRows.get(1))
        for(i in 0..verification.verificationRows.lastIndex) {

            fields.get(i+3).setValue(verification.verificationRows.get(i).ks)
            fields.get(i+9).setValue(verification.verificationRows.get(i).debet)
            fields.get(i+15).setValue(verification.verificationRows.get(i).credit)
            fields.get(i+21).setValue(verification.verificationRows.get(i).accountNumber)
            if (i==0)
                fields.get(28).setValue(verification.verificationRows.get(i).accountName)//// the account names are in the wrong order in the blueprint for some reason
            else if (i==1)
                fields.get(27).setValue(verification.verificationRows.get(i).accountName)
            else
                fields.get(i+27).setValue(verification.verificationRows.get(i).accountName)


        }
        pdf.save(args[1]+"/"+verification.verificationNumber+".pdf")

    }





}


data class Verification(val verificationNumber: String, val verificationText: String, val verificationDate: String, val verificationDescription: String){

    val verificationRows = ArrayList<VerificationRow>()
    var total = 0L


    fun addRow(ks: String, accountName: String, accountNumber: String, debet: String, credit: String){
        this.verificationRows.add(VerificationRow(ks, accountName, accountNumber, debet, credit))
    }
    fun getTotalString(): String {
        var string = total.toString()
        return string.substring(0, string.length-2)+","+string.substring(string.length-2, string.length)
    }


}

data class VerificationRow(val ks: String, val accountName: String, val accountNumber: String, val debet: String, val credit: String)