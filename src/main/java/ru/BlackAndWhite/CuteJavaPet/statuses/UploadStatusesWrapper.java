package ru.BlackAndWhite.CuteJavaPet.statuses;

import lombok.extern.log4j.Log4j;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

@Log4j
public class UploadStatusesWrapper {
    private static String getStatusDescription(UploadStatuses statuses) {
        switch (statuses) {
            case EMPTY:
                return "warning_empty";
            case WRONG_FORMAT:
                return "warning_wrong_format";
            case SUCCESS:
                return "success";
            case BAD_ENCODE:
                return "can't encode to Base64";
            default:
                return "unknow";
        }
    }

    public static String getStatus(UploadStatuses uploadStatus, String fileName) {
        return getStatusDescription(uploadStatus) + "'" + fileName + "' ";
    }
}
