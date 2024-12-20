package jud.gestorfacturas.manager;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import jud.gestorfacturas.gui.FacturaController;
import jud.gestorfacturas.gui.FacturaView;
import jud.gestorfacturas.model.*;
import jud.gestorfacturas.manager.Utils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public final class PDFGenerator {

    public String RESOURCE_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    public String IMAGES_DIRECTORY = RESOURCE_DIRECTORY + "img\\";
    private String fileName;
    //private String filePath;
    PDPage page;
    PDDocument document;
    PDPageContentStream contentStream;
    double height; //792.00
    double width; //612.00

    public PDFGenerator(String _fileName) {
        this.fileName = _fileName;
        initialize();
    }
 
    public PDFGenerator(File _file) {
        this.fileName = _file.getName();
        initialize();
    }

    public void initialize() {
        document = new PDDocument();
        this.page = new PDPage();
        this.height = this.page.getCropBox().getHeight();
        this.width = this.page.getCropBox().getWidth();
    }

    public void createBlankPDF() throws IOException {
        try (PDDocument doc = new PDDocument()) {
            // a valid PDF document requires at least one page
            PDPage blankPage = new PDPage();
            doc.addPage(blankPage);
            doc.save(this.fileName);
        }
    }

    public PDDocument generaPDDocumentFactura(Factura factura, File path) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            document.addPage(page);

            contentStream = new PDPageContentStream(document, page);
            contentStream.setLeading(14.5f);

            //Logo
            PDImageXObject logoImage = PDImageXObject.createFromFile(IMAGES_DIRECTORY + "logo.png", document);
            contentStream.drawImage(logoImage, 55f, (float) height - 30f - logoImage.getHeight());

            //Encabezado datos factura
            contentStream.beginText();
            insertTextWithOffset("FACTURA", Standard14Fonts.FontName.HELVETICA_BOLD, 30, 380, 80);
            insertNewLine();
            insertNewLine();
            insertText("N.º Factura: " + factura.getNumFactura(), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertNewLine();
            insertText("Fecha emisión: " + df.format(factura.getFechaEmision()), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertText("Fecha vencimiento: " + df.format(factura.getFechaVencimiento()), Standard14Fonts.FontName.HELVETICA, 12);
            contentStream.endText();

            //Encabezado datos Cliente
            contentStream.beginText();
            insertTextWithOffset("CLIENTE", Standard14Fonts.FontName.HELVETICA_BOLD, 14, 70, 220);
            insertNewLine();
            insertNewLine();
            insertText(factura.getCliente().getNombre(), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertNewLine();
            insertText(factura.getCliente().getDireccion(), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertText(factura.getCliente().getCodigoPostal(), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertText("CIF: " + factura.getCliente().getNif(), Standard14Fonts.FontName.HELVETICA, 12);
            contentStream.endText();

            contentStream.beginText();
            insertTextWithOffset("EMISOR", Standard14Fonts.FontName.HELVETICA_BOLD, 14, 380, 220);
            insertNewLine();
            insertNewLine();
            insertText(factura.getEmisor().getNombre(), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertNewLine();
            insertText(factura.getEmisor().getDireccion(), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertText(factura.getEmisor().getCodigoPostal(), Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            insertText(factura.getEmisor().getNif(), Standard14Fonts.FontName.HELVETICA, 12);
            contentStream.endText();

            //Black Shape
            PDImageXObject bsConceptsImage = PDImageXObject.createFromFile(IMAGES_DIRECTORY + "black_shape.png", document);
            contentStream.drawImage(bsConceptsImage, 55, 420);
            
            //Cuerpo: Descripción conceptos
            contentStream.beginText();
            contentStream.setNonStrokingColor(1f,1f,1f);
            insertTextWithOffset("DESCRIPCIÓN", Standard14Fonts.FontName.HELVETICA_BOLD, 12, 70, 360);
            contentStream.setNonStrokingColor(0f,0f,0f);
            insertNewLine();
            for (Servicio servicio : factura.getListaServicios()) {
                insertNewLine();
                insertText(servicio.getCombinacionIdiomas(), Standard14Fonts.FontName.HELVETICA, 12);
                insertNewLine();
                insertText(servicio.getDescripcion(), Standard14Fonts.FontName.HELVETICA, 12);
                insertSeparationLine();
                
            }
            contentStream.endText();

            //Cuerpo: Cantidad
            contentStream.beginText();
            contentStream.setNonStrokingColor(1f,1f,1f);
            insertTextWithOffset("CANTIDAD", Standard14Fonts.FontName.HELVETICA_BOLD, 12, 300, 360);
            contentStream.setNonStrokingColor(0f,0f,0f);
            contentStream.endText();
            
            contentStream.beginText();
            insertTextWithOffset("", Standard14Fonts.FontName.HELVETICA, 12, 300, 398);
            contentStream.setLeading(15f);
            for (Servicio servicio : factura.getListaServicios()) {
                if (servicio.getCantidad() % (int)servicio.getCantidad() == 0) { //is integer without decimal numbers
                    insertText("      " + Utils.formatDecimalNumberToStringIfNecessary(servicio.getCantidad(), 0), Standard14Fonts.FontName.HELVETICA, 12);
                } else { //has decimals
                    insertText("    " + Utils.formatDecimalNumberToStringIfNecessary(servicio.getCantidad(), 2), Standard14Fonts.FontName.HELVETICA, 12);
                }
                insertNewLine();
                insertNewLine();
                insertNewLine();
            }
            contentStream.setLeading(14.5f);
            contentStream.endText();

            //Cuerpo: Precio unitario
            contentStream.beginText();
            contentStream.setNonStrokingColor(1f,1f,1f);
            insertTextWithOffset("PRECIO", Standard14Fonts.FontName.HELVETICA_BOLD, 12, 410, 360);
            contentStream.setNonStrokingColor(0f,0f,0f);
            insertNewLine();
            contentStream.setLeading(15f);
            for (Servicio servicio : factura.getListaServicios()) {
                insertNewLine();
                insertText(Utils.formatDecimalNumberToStringAlways(servicio.getPrecioUnitario() ,3), Standard14Fonts.FontName.HELVETICA, 12);
                insertNewLine();
                insertText("€/ " + servicio.getTipo(), Standard14Fonts.FontName.HELVETICA, 12);
                insertNewLine();
            }
            contentStream.setLeading(14.5f);
            contentStream.endText();

            //Cuerpo: Precio Total/servicio
            contentStream.beginText();
            contentStream.setNonStrokingColor(1f,1f,1f);
            insertTextWithOffset("TOTAL", Standard14Fonts.FontName.HELVETICA_BOLD, 12, 510, 360);
            contentStream.setNonStrokingColor(0f,0f,0f);
            contentStream.endText();
            contentStream.beginText();
            insertTextWithOffset("", Standard14Fonts.FontName.HELVETICA, 12, 510, 395);
            contentStream.setLeading(15f);
            for (Servicio servicio : factura.getListaServicios()) {
                insertText(Utils.formatDecimalNumberToStringAlways(servicio.getPrecioFinal(), 2) + " €", Standard14Fonts.FontName.HELVETICA, 12);
                insertNewLine();
                insertNewLine();
                insertNewLine();
            }
            contentStream.setLeading(14.5f);
            contentStream.endText();
            
            //Pie: Información de pago
            contentStream.beginText();
            insertTextWithOffset("INFORMACIÓN DE PAGO", Standard14Fonts.FontName.HELVETICA_BOLD, 12, 70, 620);
            contentStream.endText();
            contentStream.beginText();
            insertTextWithOffset("Nombre", Standard14Fonts.FontName.HELVETICA, 10, 70, 660);
            insertNewLine();
            insertText("Forma de pago", Standard14Fonts.FontName.HELVETICA, 10);
            insertNewLine();
            if (factura.getFormaPago().equals("Transferencia bancaria")) {
                insertText("N.º de cuenta", Standard14Fonts.FontName.HELVETICA, 10);
            }
            contentStream.endText();
            
            contentStream.beginText();
            insertTextWithOffset(factura.getEmisor().getNombreCompleto(), Standard14Fonts.FontName.HELVETICA, 10, 150, 661);
            insertNewLine();
            insertText(factura.getFormaPago(), Standard14Fonts.FontName.HELVETICA, 10);
            insertNewLine();
            if (factura.getFormaPago().equals("Transferencia bancaria")) {
                insertText(factura.getEmisor().getIban(), Standard14Fonts.FontName.HELVETICA, 10);
            }
            insertNewLine();
            contentStream.endText();
            
            //Pie: Shape negra en precio final
            PDImageXObject bspriceImage = PDImageXObject.createFromFile(IMAGES_DIRECTORY + "small_black_shape.png", document);
            contentStream.drawImage(bspriceImage, 310, 97);
            
            //Pie: Precios Finales Factura
            contentStream.beginText();
            contentStream.setLeading(22f);
            insertTextWithOffset("BASE IMPONIBLE", Standard14Fonts.FontName.HELVETICA, 14, 320, 620);
            insertNewLine();
            insertText("IVA (" + Utils.formatDecimalNumberToStringAlways(factura.getTASA_IVA(), 2) + " %)", Standard14Fonts.FontName.HELVETICA, 14);
            insertNewLine();
            insertText("IRPF (" + Utils.formatDecimalNumberToStringAlways(factura.getTASA_IRPF(), 2) + " %)", Standard14Fonts.FontName.HELVETICA, 14);
            insertNewLine();
            contentStream.setNonStrokingColor(1f,1f,1f);
            insertText("TOTAL", Standard14Fonts.FontName.HELVETICA, 14);
            contentStream.setNonStrokingColor(0f,0f,0f);
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setLeading(22f);
            insertTextWithOffset(Utils.formatDecimalNumberToStringAlways(factura.getBaseImponible(), 2) + " €", Standard14Fonts.FontName.HELVETICA, 14, 480, 620);
            insertNewLine();
            insertText(Utils.formatDecimalNumberToStringAlways(factura.getIva(), 2) + " €", Standard14Fonts.FontName.HELVETICA, 14);
            insertNewLine();
            insertText(Utils.formatDecimalNumberToStringAlways(factura.getIrpf(), 2) + " €", Standard14Fonts.FontName.HELVETICA, 14);
            insertNewLine();
            contentStream.setNonStrokingColor(1f,1f,1f);
            insertText(Utils.formatDecimalNumberToStringAlways(factura.getImporteTotal(), 2) + " €", Standard14Fonts.FontName.HELVETICA, 14);
            contentStream.setNonStrokingColor(0f,0f,0f);
            contentStream.endText();
            
            contentStream.close();
            
            return document;
        } catch (IOException ex) {
            Logger.getLogger(PDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void openTempPDF(File tempFile) {
        try {
            Desktop.getDesktop().open(tempFile);
        } catch (IOException ex) {
            Logger.getLogger(FacturaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertSeparationLine() {
        try {
            contentStream.setLeading(8f);
            contentStream.setNonStrokingColor(0.5f);
            insertNewLine();
            insertText("__________________________________________________________________________", Standard14Fonts.FontName.HELVETICA, 12);
            insertNewLine();
            contentStream.setNonStrokingColor(0f, 0f, 0f);
            contentStream.setLeading(14.5f);
        } catch (IOException ex) {
            Logger.getLogger(PDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertTextWithOffset(String text, Standard14Fonts.FontName font, int fontSize, float x, float y) {
        try {
            contentStream.newLineAtOffset(x, (float) height - y);
            contentStream.setFont(new PDType1Font(font), fontSize);
            contentStream.showText(text);
        } catch (IOException ex) {
            Logger.getLogger(PDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertText(String text, Standard14Fonts.FontName font, int fontSize) {
        try {
            contentStream.setFont(new PDType1Font(font), fontSize);
            contentStream.showText(text);
        } catch (IOException ex) {
            Logger.getLogger(PDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertNewLine() {
        try {
            contentStream.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addImageToPage(PDDocument doc, PDImageXObject image, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            double height = page.getCropBox().getHeight();
            contentStream.drawImage(image, 0, (float) this.height - (float) image.getHeight());
            contentStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
