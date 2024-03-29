package in.xfiles.web.servlets;

import in.xfiles.core.ejb.FileManagerLocal;
import in.xfiles.core.ejb.LogManagerLocal;
import in.xfiles.core.ejb.UserManagerLocal;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.CommonConstants;
import in.xfiles.core.helpers.CryptoHelper;
import in.xfiles.core.helpers.StringUtils;
import in.xfiles.core.wrappers.UploadedFileWrapper;
import in.xfiles.web.utils.SessionUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * A simple servlet for uploading files.
 * Relies on MultipartRequestFilter and Apache Commons FileUpload library.
 *
 * @author danon
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = {"/operations/fileupload"})
public class FileUploadServlet extends HttpServlet {
    
    private static final Logger log = Logger.getLogger(FileUploadServlet.class);
    
    private static final String SERVLET_ENCODING = "ISO-8859-1";
    
    public static final String REDIRECT_PARAM = "redirect";
    public static final String ERROR_REDIRECT_PARAM = "error_redirect";
    public static final String FILE_PARAM = "file";
    public static final String ENCRYPTION_TYPE_PARAM = "encryption_type";
    public static final String ACCESS_TYPE_PARAM = "access_type";
    public static final String PARENT_FILE_ID_PARAM = "parent_file_id";
    public static final String GROUP_ID_PARAM = "group_id";
    public static final String SECRET_KEY_PARAM = "secret_key";
    public static final String GROUP_USERS_PARAM = "group_users[]";
    
    private String redirect, errorRedirect;
    
    @EJB
    private FileManagerLocal fm;
    
    @EJB
    private UserManagerLocal um;

    @EJB
    private LogManagerLocal lm;
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = SessionUtils.getSession(request, true);
        
        redirect = getValidParameter(request, REDIRECT_PARAM, request.getContextPath()+"/faces/cabinet/operations/upload.xhtml?success=1");
        errorRedirect = getValidParameter(request, ERROR_REDIRECT_PARAM, request.getContextPath()+"/faces/cabinet/operations/upload.xhtml?error=1");
        
        Long userId = SessionUtils.getUserId(session);
        if(userId == null) {
            log.debug("Permission denied. Log in.");
            redirect(response, errorRedirect);
            return;
        }
        
        String encryptionType = getValidParameter(request, ENCRYPTION_TYPE_PARAM);
        String accessType = getValidParameter(request, ACCESS_TYPE_PARAM);
        String parentFileId = getValidParameter(request, PARENT_FILE_ID_PARAM);
        String groupId = getValidParameter(request, GROUP_ID_PARAM);
        String secretKey = getValidParameter(request, SECRET_KEY_PARAM);
        String[] groupUsers = request.getParameterValues(GROUP_USERS_PARAM);
        
        Object o = request.getAttribute(FILE_PARAM);
        if(o == null) {
            log.debug("File is required!");
            redirect(response, errorRedirect);
            return;
        }
        if(o instanceof FileItem) {
            
            lm.addRecord(userId, CommonConstants.UPLOAD_REQUEST, "Upload Start", "", session.getId());
            try {
                FileItem fi = (FileItem)o;
                UploadedFileWrapper ufw = new UploadedFileWrapper();
                ufw.setName(fi.getName());
                ufw.setContentType(fi.getContentType());
                ufw.setSize(fi.getSize());
                if(!StringUtils.isEmpty(secretKey))
                    ufw.setKey(CryptoHelper.SHA256(secretKey));
                else ufw.setKey(null);
                File tmpFile = File.createTempFile("xfiles_", ".upload");
                fi.write(tmpFile);
                log.debug("Temporary file created: "+tmpFile.getAbsolutePath());
                ufw.setFile(tmpFile);
                ufw.setChecksum(FileUtils.checksumCRC32(tmpFile));
                ufw.setEncryptionType(encryptionType);
                ufw.setAccessType(accessType);
                ufw.setUploadedBy(um.getUserById(userId));
                if(groupUsers != null && groupUsers.length > 2) {
                    Set<User> groupUsersSet = new HashSet<User>();
                    groupUsersSet.add(um.getUserById(userId));
                    for(String s: groupUsers) {
                        if(!StringUtils.isValidInteger(s)) {
                            throw new IllegalArgumentException("Invalid userId: "+s);
                        }
                        User gu = um.getUserById(Long.parseLong(s));
                        if(gu == null) {
                            throw new IllegalArgumentException("Invalid userId: "+s);
                        }
                        groupUsersSet.add(gu);
                    }
                    if(groupUsersSet.size() < 3) {
                        throw new IllegalArgumentException("We need at least 3 users to create a group");
                    }    
                    ufw.setGroupUsers(groupUsersSet);
                }
                fm.processFile(ufw);
                log.info("File is sent to the core for processing: "+ufw);
            } catch (Exception ex) {
                log.error("Failed to upload file.", ex);
                redirect(response, errorRedirect);
                return;
            }
        } else {
            log.error("File is not uploaded");
            log.error("Exception: ",(FileUploadException)o);
            redirect(response, errorRedirect);
            return;
        }
        redirect(response, redirect);
    }
    
    private String getValidParameter(HttpServletRequest request, String name) {
        return StringUtils.decode(StringUtils.getValidString(request.getParameter(name)), SERVLET_ENCODING);
    }
    
    private String getValidParameter(HttpServletRequest request, String name, String defualtValue) {
        final String param = getValidParameter(request, name);
        return param.isEmpty() ? defualtValue : param; 
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for uploading files";
    }

    private void redirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (Exception ex) {
            if(log.isDebugEnabled()) {
                log.debug("Failed to redirect to "+url, ex);
            }
        }
    }
}
