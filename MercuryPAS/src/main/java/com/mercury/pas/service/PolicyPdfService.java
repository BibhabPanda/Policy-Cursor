package com.mercury.pas.service;

import java.io.ByteArrayOutputStream;

public interface PolicyPdfService {
    ByteArrayOutputStream generatePolicyPdf(Long policyId);
}

