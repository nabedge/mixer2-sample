package org.mixer2.sample.web.view.helper;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.InputType;
import org.mixer2.sample.web.Const;

public class TransactionTokenHelper {

    /**
     * create token rondomly, add it to HttpSession, create input hidden tag, and add it to form tag object.
     */
    public static void addToken(HttpSession session, Form form) {

        // create token
        String transactionToken = DigestUtils.sha256Hex(String.valueOf(Math
                .random()));

        // set token to session
        session.setAttribute(Const.transactionTokenAttributeName,
                transactionToken);

        // set hidden input tag having token into form
        Input input = new Input();
        input.setName(Const.transactionTokenAttributeName);
        input.setType(InputType.HIDDEN);
        input.setValue(transactionToken);
        form.getContent().add(input);
    }

    /**
     * check token.
     * @param session HttpSession
     * @param transactionToken value of token in ActionForm
     * @return
     */
    public static boolean checkToken(HttpSession session,
            String transactionToken) {
        String token = (String) session
                .getAttribute(Const.transactionTokenAttributeName);
        if (StringUtils.isNotEmpty(token) && token.equals(transactionToken)) {
            return true;
        } else {
            return false;
        }
    }

    public static void removeToken(HttpSession session) {
        session.removeAttribute(Const.transactionTokenAttributeName);
    }

}
