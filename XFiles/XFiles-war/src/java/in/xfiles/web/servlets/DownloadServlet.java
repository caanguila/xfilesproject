package in.xfiles.web.servlets;

import in.xfiles.core.ejb.FileManagerLocal;
import in.xfiles.core.entity.DownloadRequest;
import in.xfiles.core.helpers.FileUtils;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.web.utils.SessionUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author danon
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/download"})
public class DownloadServlet extends HttpServlet {

    @EJB
    private FileManagerLocal fileMan;
    
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
        OutputStream out = response.getOutputStream();            
        try {
            String id = getValidParameter(request, "id");
            Long userId = SessionUtils.getUserId(request.getSession());
            if(userId == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if(!StringUtils.isEmpty(id)) {
                DownloadRequest f = fileMan.getRequestsById(Long.valueOf(id));
                if(f != null) {
                    if(!fileMan.canDownload(userId, f.getFile().getFileId())) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    if(f.getStatus() == DownloadRequest.READY_STATUS)
                        sendFile(response, f);
                    else response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            
        } catch (FileNotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } finally {            
            out.close();
        }
    }
    
    public String getValidParameter(HttpServletRequest request, String name) {
        String param = request.getParameter(name);
        return param == null ? "" : param;
    }

    public String getValidParameter(HttpServletRequest request, String name, String defaultValue) {
        final String param = getValidParameter(request, name);
        return param.isEmpty() ? defaultValue : param;
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
        return "Short description";
    }// </editor-fold>

    private void sendFile(HttpServletResponse response, DownloadRequest f) throws FileNotFoundException, IOException {
        final File file = new File(f.getOutputFile());
        response.setContentType(f.getFile().getContentType());
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\""+f.getFile().getName()+"\"");
        OutputStream out = response.getOutputStream();
        InputStream in = new FileInputStream(file);
        FileUtils.copyStream(in, out);
        in.close();
    }
}