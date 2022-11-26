package org.booking.system.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

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
}
