package com.dparadig.auth_server.service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

/**
 * @author jlabarca
 */
@CommonsLog
@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendMail(String toEmail, String subject, String message, InternetAddress from) {
        try {
            MimeMessage mMessage = javaMailSender.createMimeMessage();
            mMessage.setFrom(from);
            MimeMessageHelper helper = new MimeMessageHelper(mMessage);
            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText("<html><body>" + message + "</body></html>", true);
            javaMailSender.send(mMessage);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public boolean sendConfirmationEmail(String toEmail, String url){
        try {
            InternetAddress from = new InternetAddress("dParadig <sgi@dparadig.com>");
            String subject = "Activate your Account";
            String message = "Confirm your new registered email address with this button: <br> <a href='"+url+"'>Confirm</a>";
            return sendMail(toEmail,subject,message,from);
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendConfirmationEmailESP(String toEmail, String url){
        try {
            InternetAddress from = new InternetAddress("dParadig <sgi@dparadig.com>");
            String subject = "Activa tu Cuenta";
            String message = "Confirma tu nueva direccion de correo electronico registrada con este boton: <br> <a href='"+url+"'>Confirmar</a>";
            //String message = "Confirm your new registered email address with this button: <br> <a href='"+url+"'>Confirm</a>";
            return sendMail(toEmail,subject,message,from);
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendPassResetEmail(String toEmail, String url){
        try {
            InternetAddress from = new InternetAddress("dParadig <sgi@dparadig.com>");
            String subject = "Reset Your Password";
            String message = "Reset Your Password with this button: <br> <a href='"+url+"'>Reset</a>";
            return sendMail(toEmail,subject,message,from);
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendPassResetEmailESP(String toEmail, String url){
        try {
            InternetAddress from = new InternetAddress("dParadig <sgi@dparadig.com>");
            String subject = "Reinicia tu Contrase\u00f1a - Portal de Notificaciones";
            String message = assembleMail(url);
            return sendMail(toEmail,subject,message,from);
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String assembleMail(String url) {
        String html = "";
        html += getHeader();
        html += getBody(url);
        html += getFooter();
        return html;
    }

    private String getHeader() {
        return "<!doctype html>\r\n" +
                "<html>\r\n" +
                "  <head>\r\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\">\r\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" +
                "    <title>Cambio de Contrase&ntilde;a</title>\r\n" +
                "    <style>\r\n" +
                "    @media only screen and (max-width: 620px) {\r\n" +
                "      table[class=body] h1 {\r\n" +
                "        font-size: 28px !important;\r\n" +
                "        margin-bottom: 10px !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] p,\r\n" +
                "            table[class=body] ul,\r\n" +
                "            table[class=body] ol,\r\n" +
                "            table[class=body] td,\r\n" +
                "            table[class=body] span,\r\n" +
                "            table[class=body] a {\r\n" +
                "        font-size: 16px !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] .wrapper,\r\n" +
                "            table[class=body] .article {\r\n" +
                "        padding: 10px !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] .content {\r\n" +
                "        padding: 0 !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] .container {\r\n" +
                "        padding: 0 !important;\r\n" +
                "        width: 100% !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] .main {\r\n" +
                "        border-left-width: 0 !important;\r\n" +
                "        border-radius: 0 !important;\r\n" +
                "        border-right-width: 0 !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] .btn table {\r\n" +
                "        width: 100% !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] .btn a {\r\n" +
                "        width: 100% !important;\r\n" +
                "      }\r\n" +
                "      table[class=body] .img-responsive {\r\n" +
                "        height: auto !important;\r\n" +
                "        max-width: 100% !important;\r\n" +
                "        width: auto !important;\r\n" +
                "      }\r\n" +
                "    }\r\n" +
                "    @media all {\r\n" +
                "      .ExternalClass {\r\n" +
                "        width: 100%;\r\n" +
                "      }\r\n" +
                "      .ExternalClass,\r\n" +
                "            .ExternalClass p,\r\n" +
                "            .ExternalClass span,\r\n" +
                "            .ExternalClass font,\r\n" +
                "            .ExternalClass td,\r\n" +
                "            .ExternalClass div {\r\n" +
                "        line-height: 100%;\r\n" +
                "      }\r\n" +
                "      .apple-link a {\r\n" +
                "        color: inherit !important;\r\n" +
                "        font-family: inherit !important;\r\n" +
                "        font-size: inherit !important;\r\n" +
                "        font-weight: inherit !important;\r\n" +
                "        line-height: inherit !important;\r\n" +
                "        text-decoration: none !important;\r\n" +
                "      }\r\n" +
                "      .btn-primary table td:hover {\r\n" +
                "        background-color: #34495e !important;\r\n" +
                "      }\r\n" +
                "      .btn-primary a:hover {\r\n" +
                "        background-color: #34495e !important;\r\n" +
                "        border-color: #34495e !important;\r\n" +
                "      }\r\n" +
                "    }\r\n" +
                "    </style>\r\n" +
                "  </head>\r\n";
    }

    private String getBody(String url) {
        return "<body class=\"\" style=\"background-color: #f6f6f6; font-family: sans-serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\r\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background-color: #f6f6f6;\">\r\n" +
                "      <tr>\r\n" +
                "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">&nbsp;</td>\r\n" +
                "        <td class=\"container\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; display: block; Margin: 0 auto; max-width: 580px; padding: 10px; width: 580px;\">\r\n" +
                "          <div class=\"content\" style=\"box-sizing: border-box; display: block; Margin: 0 auto; max-width: 580px; padding: 10px;\">\r\n" +
                "\r\n" +
                "            <span class=\"preheader\" style=\"color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0;\">Ingresa para ver el enlace para cambiar tu contrase&ntilde;a.</span>\r\n" +
                "            <table class=\"main\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background: #ffffff; border-radius: 3px;\">\r\n" +
                "\r\n" +
                "              <tr>\r\n" +
                "                <td class=\"wrapper\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; box-sizing: border-box; padding: 20px;\">\r\n" +
                "                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\">\r\n" +
                "                    <tr>\r\n" +
                "                      <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">\r\n" +
                "\r\n" +
                "                        <img src='http://dparadig.com/wp-content/uploads/dParadig7.png'></img><br>\r\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Detectamos que pediste un cambio de contrase&ntilde;a. Para hacerlo, haz click en el siguiente bot&oacute;n y ser&aacute;s redirigido al Portal.</p>\r\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; box-sizing: border-box;\">\r\n" +
                "                          <tbody>\r\n" +
                "                            <tr>\r\n" +
                "                              <td align=\"left\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; padding-bottom: 15px;\">\r\n" +
                "                                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: auto;\">\r\n" +
                "                                  <tbody>\r\n" +
                "                                    <tr>\r\n" +
                "                                      <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; background-color: #3498db; border-radius: 5px; text-align: center;\"> <a href=\"" + url + "\" target=\"_blank\" style=\"display: inline-block; color: #ffffff; background-color: #3498db; border: solid 1px #3498db; border-radius: 5px; box-sizing: border-box; cursor: pointer; text-decoration: none; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; border-color: #3498db;\">Cambiar contrase&ntilde;a</a> </td>\r\n" +
                "                                    </tr>\r\n" +
                "                                  </tbody>\r\n" +
                "                                </table>\r\n" +
                "                              </td>\r\n" +
                "                            </tr>\r\n" +
                "                          </tbody>\r\n" +
                "                        </table>\r\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Si no pediste el cambio de contrase&ntilde;a, puedes ignorar este correo y no se har&aacute;n cambios en tu cuenta.</p>\r\n" +
                "                      </td>\r\n" +
                "                    </tr>\r\n" +
                "                  </table>\r\n" +
                "                </td>\r\n" +
                "              </tr>\r\n" +
                "            </table>";
    }

    private String getFooter() {
        return "<div class=\"footer\" style=\"clear: both; Margin-top: 10px; text-align: center; width: 100%;\">\r\n" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\">\r\n" +
                "                <tr>\r\n" +
                "                  <td class=\"content-block\" style=\"font-family: sans-serif; vertical-align: top; padding-bottom: 10px; padding-top: 10px; font-size: 12px; color: #999999; text-align: center;\">\r\n" +
                "                    <span class=\"apple-link\" style=\"color: #999999; font-size: 12px; text-align: center;\">&copy; 2018 - <a href=\"http://www.dparadig.com\" target=\"_blank\" style=\"color: #999999; font-size: 12px; text-align: center; text-decoration: none;\">dParadig</a></span>\r\n" +
                "                  </td>\r\n" +
                "                </tr>\r\n" +
                "              </table>\r\n" +
                "            </div>\r\n" +
                "          </div>\r\n" +
                "        </td>\r\n" +
                "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">&nbsp;</td>\r\n" +
                "      </tr>\r\n" +
                "    </table>\r\n" +
                "  </body>\r\n" +
                "</html>";
    }
}