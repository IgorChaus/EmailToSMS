package com.example.emailtosms.model.email;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class GetMulti {

    public String getTextFromMessage(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return (String)part.getContent();
        }

        if (part.isMimeType("multipart/alternative")) {
            Multipart multiPart = (Multipart)part.getContent();
            String result = null;
            for (int i = 0; i < multiPart.getCount(); i++) {
                Part bodyPart = multiPart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    if (result == null)
                        result = getTextFromMessage(bodyPart);
                } else if (bodyPart.isMimeType("text/html")) {
                    result = getTextFromMessage(bodyPart);
                    if (result != null)
                        return result;
                } else {
                    return getTextFromMessage(bodyPart);
                }
            }
            return result;
        } else if (part.isMimeType("multipart/*")) {
            Multipart multiPart = (Multipart)part.getContent();
            for (int i = 0; i < multiPart.getCount(); i++) {
                String result = getTextFromMessage(multiPart.getBodyPart(i));
                if (result != null)
                    return result;
            }
        }

        return null;
    }
}
