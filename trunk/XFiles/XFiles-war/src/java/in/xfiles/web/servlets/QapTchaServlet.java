package in.xfiles.web.servlets;

import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.utils.SessionHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This servlet is intended to process requests from QapTcha.
 * It updates "qaptcha_key" parameter of session.
 * 
 * @author danon
 */
@WebServlet(name = "QapTchaServlet", urlPatterns = {"/qaptcha"})
public class QapTchaServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        try {
            if(request != null) {
                HttpSession session = request.getSession(true);
                SessionHelper.setSessionAttribute(session, "qaptcha_key", null);
                if(StringUtils.getValidString(request.getParameter("action")).equals("qaptcha")) {
                    SessionHelper.setSessionAttribute(request.getSession(true), "qaptcha_key", request.getParameter("qaptcha_key"));
                    out.print("{\"error\":false}");
                    return;
                }
            }
            out.print("{\"error\":true}");
        } finally {
            out.close();
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for QapTcha";
    }// </editor-fold>
}