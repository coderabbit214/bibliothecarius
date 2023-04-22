package com.coderabbit214.bibliothecarius.document.file;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr_J
 */
@Component
public class FileFactory {

    private final PdfService pdfService;

    private final MdService mdService;

    private final TxtService txtService;

    public FileFactory(TxtService txtService, MdService mdService, PdfService pdfService) {
        this.txtService = txtService;
        this.mdService = mdService;
        this.pdfService = pdfService;
    }

    public FileInterface getFileService(String type) {
        type = type.toLowerCase();

        return switch (type) {
            case FileInterface.FILE_TYPE_TXT -> txtService;
            case FileInterface.FILE_TYPE_MD -> mdService;
            case FileInterface.FILE_TYPE_PDF -> pdfService;
            default -> throw new BusinessException("unsupported file type");
        };

    }
}
