package in.xfiles.core.helpers;

/**
 * Contains common constants.
 * 
 * @author danon
 */
public interface CommonConstants {
    Long ADMINISTRATOR_USER_TYPE    = 1L;
    Long PRIVATE_FILE_TYPE          = 2L;
    Long PUBLIC_FILE_TYPE           = 3L;
    Long GROUP_FILE_TYPE            = 4L;
    Long BROADCAST_MESSAGE          = 5L;
    Long PRIVATE_MESSAGE            = 6L;
    Long PRIVATE_GROUP              = 7L;
    Long AES_ENCRYPTION_TYPE        = 8L;
    Long PLAIN_ENCRYPTION_TYPE      = 9L;
    Long GROUP_ACCESS_MESSAGE       = 10L;
    
    Long REGESTRATION_OF_USER       = 1L;
    Long USER_LOGOUT                = 2L;
    Long USER_LOGIN                 = 3L;
    Long DOWNLOAD_REQUEST           = 4L;
    Long UPLOAD_REQUEST             = 5L;
    Long DOWNLOAD_COMPLETE          = 6L;
    Long CHANGE_PROFILE             = 7L;
    Long GROUP_CREATION             = 8L;
    Long PARTIAL_APPROVE            = 9L;
    Long SUCCESS_LOGIN              = 10L;
    Long SESSION_CREATED            = 11L;
    Long SESSION_DESTROYED          = 12L;
    Long SESSION_MODIFYED           = 13L;
    Long UPLOAD_COMPLETE            = 14L;
    String SECRET_KEY = "78f6b8cbd16dc2158fffeb96ead43c36";
}
