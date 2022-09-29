package stoyanov.stanimir.taulia.assignment.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "Invoice")
@XmlAccessorType(XmlAccessType.FIELD)
public class Invoice {
    @CsvBindByName
    @XmlTransient
    private String buyer;
    @CsvBindByName(column = "image_name")
    @XmlElement(name = "ImageName")
    private String imageName;
    @CsvBindByName(column = "invoice_image")
    @XmlTransient
    private String invoiceImage;
    @CsvBindByName(column = "invoice_due_date")
    @XmlElement(name = "InvoiceDueDate")
    private String invoiceDueDate;
    @CsvBindByName(column = "invoice_number")
    @XmlElement(name = "InvoiceNumber")
    private String invoiceNumber;
    @CsvBindByName(column = "invoice_amount")
    @XmlElement(name = "InvoiceAmount")
    private String invoiceAmount;
    @CsvBindByName(column = "invoice_currency")
    @XmlElement(name = "InvoiceCurrency")
    private String invoiceCurrency;
    @CsvBindByName(column = "invoice_status")
    @XmlElement(name = "InvoiceStatus")
    private String invoiceStatus;
    @CsvBindByName
    @XmlElement(name = "Supplier")
    private String supplier;
}
