package org.booking.system.Controller;

import org.booking.system.Exception.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Validator {

    public HttpHeaders getHeaders(String contentType){
        HttpHeaders headers = new HttpHeaders();
        if(contentType!=null && contentType.equals("application/xml")){
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        else {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return headers;
    }
    public boolean matchEmailPattern(String email){
        if(email==null || email.trim().equals("")){
            return true;
        }
        //check email is valid
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);
        return mat.matches();
    }
    public boolean matchPhoneNumberPattern(String phone){
        if(phone==null || phone.trim().equals("")){
            return true;
        }
        //check email is valid
        Pattern pattern = Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$");
        Matcher mat = pattern.matcher(phone);
        return mat.matches();
    }

    public void validateBody(Errors errors){
        if (errors.hasErrors()) {
            // Extract ConstraintViolation list from body errors
            List<ConstraintViolation<?>> violationsList = new ArrayList<>();
            for (ObjectError e : errors.getAllErrors()) {
                violationsList.add(e.unwrap(ConstraintViolation.class));
            }

            String exceptionMessage = "";

            // Construct a helpful message for each input violation
            for (ConstraintViolation<?> violation : violationsList) {
                exceptionMessage += violation.getPropertyPath() + " " + violation.getMessage() + "\n";
            }
            throw new BadRequestException(String.format("Request input is invalid:\n%s", exceptionMessage));
        }
    }
}
