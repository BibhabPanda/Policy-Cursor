package com.mercury.pas.service.impl;

import com.mercury.pas.exception.NotFoundException;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.service.PolicyPdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PolicyPdfServiceImpl implements PolicyPdfService {

    private final PolicyRepository policyRepository;

    public PolicyPdfServiceImpl(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public ByteArrayOutputStream generatePolicyPdf(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new NotFoundException("Policy not found"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLUE);
            Paragraph title = new Paragraph("Mercury Insurance Policy", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Policy Details
            Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Policy Details", headingFont));
            document.add(new Paragraph("Policy Number: " + policy.getPolicyNumber(), normalFont));
            document.add(new Paragraph("Status: " + policy.getStatus(), normalFont));
            document.add(new Paragraph("Term: " + (policy.getTerm() != null ? policy.getTerm().getValue() : "N/A"), normalFont));
            document.add(new Paragraph("Premium Amount: $" + policy.getPremiumAmount(), normalFont));
            
            if (policy.getStartDate() != null) {
                document.add(new Paragraph("Start Date: " + policy.getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), normalFont));
            }
            if (policy.getEndDate() != null) {
                document.add(new Paragraph("End Date: " + policy.getEndDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), normalFont));
            }

            document.add(new Paragraph("\n", normalFont));

            // Customer Information
            if (policy.getCustomer() != null) {
                document.add(new Paragraph("Customer Information", headingFont));
                document.add(new Paragraph("Name: " + policy.getCustomer().getFirstName() + " " + policy.getCustomer().getLastName(), normalFont));
                document.add(new Paragraph("Email: " + policy.getCustomer().getEmail(), normalFont));
                document.add(new Paragraph("\n", normalFont));
            }

            // Vehicle Information
            if (policy.getVehicle() != null) {
                document.add(new Paragraph("Vehicle Information", headingFont));
                document.add(new Paragraph("Make: " + policy.getVehicle().getMake(), normalFont));
                document.add(new Paragraph("Model: " + policy.getVehicle().getModel(), normalFont));
                document.add(new Paragraph("Year: " + policy.getVehicle().getYear(), normalFont));
                document.add(new Paragraph("VIN: " + policy.getVehicle().getVin(), normalFont));
                document.add(new Paragraph("\n", normalFont));
            }

            // Agent Information
            if (policy.getAgent() != null) {
                document.add(new Paragraph("Agent Information", headingFont));
                document.add(new Paragraph("Name: " + policy.getAgent().getFirstName() + " " + policy.getAgent().getLastName(), normalFont));
                document.add(new Paragraph("Email: " + policy.getAgent().getEmail(), normalFont));
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return outputStream;
    }
}

