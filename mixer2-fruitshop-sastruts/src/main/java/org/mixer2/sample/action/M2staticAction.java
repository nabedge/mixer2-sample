package org.mixer2.sample.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mixer2.sample.form.M2staticForm;
import org.seasar.framework.util.MimeTypeUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

@TransactionAttribute(TransactionAttributeType.NEVER)
public class M2staticAction {

    private Logger log = Logger.getLogger(M2staticAction.class);

    @ActionForm
    @Resource
    protected M2staticForm m2staticForm;

    /**
     * output static resource. resource path should be specified by "path" parameter.
     * @see org.mixer2.sample.view.M2staticHelper#replaceM2staticPath(org.mixer2.xhtml.AbstractJaxb)
     * @return
     * @throws IOException
     */
    @Execute(validator = false, urlPattern = "m2static")
    public String outputImage() throws IOException {

        // create static file path
        String path = "m2mockup/m2static/" + m2staticForm.path;
        log.debug("static file path = " + path);

        // set contentType
        HttpServletResponse response = ResponseUtil.getResponse();
        String contentType = MimeTypeUtil.guessContentType(path);
        if (contentType == null
                && "css".equals(ResourceUtil.getExtension(path))) {
            response.setContentType("text/css");
        } else {
            response.setContentType(contentType);
        }

        // set cache-control header
        response.addHeader("Cache-Control", "max-age=60");

        // output image
        InputStream inputStream = ResourceUtil
                .getResourceAsStreamNoException(path);
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        // need not view
        return null;
    }
}
