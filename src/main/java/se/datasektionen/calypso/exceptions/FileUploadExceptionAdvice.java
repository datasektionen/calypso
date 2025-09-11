package se.datasektionen.calypso.exceptions;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize; 

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RedirectView handleMaxSizeException(MaxUploadSizeExceededException exc,
                                               HttpServletRequest request,
                                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("imageError", "Bildens filstorlek får inte överskrida " + maxFileSize);

        String referer = request.getHeader("Referer");
        return new RedirectView(referer != null ? referer : "/");
    }
}
