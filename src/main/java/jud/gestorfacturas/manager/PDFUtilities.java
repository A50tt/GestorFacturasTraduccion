package org.apache.pdfbox.examples.pdmodel;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * Create a blank PDF and write the contents to a file.
 */
public final class PDFUtilities
{
    private PDFUtilities()
    {        
    }
    
    public static void main(String[] args) throws IOException
    {
        if (args.length != 1)
        {
            System.err.println("usage: " + PDFUtilities.class.getName() + " <outputfile.pdf>");
            System.exit(1);
        }

        String filename = args[0];

        try (PDDocument doc = new PDDocument())
        {
            // a valid PDF document requires at least one page
            PDPage blankPage = new PDPage();
            doc.addPage(blankPage);
            doc.save(filename);
        }
    }
}