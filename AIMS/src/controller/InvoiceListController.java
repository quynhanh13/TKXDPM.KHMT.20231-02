package controller;

import entity.invoice.Invoice;

import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceListController extends BaseController {

    public ArrayList<Invoice> getListInvoice() throws SQLException {
        return Invoice.getListInvoice();
    }
}
