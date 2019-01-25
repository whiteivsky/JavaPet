package ru.BlackAndWhite.CuteJavaPet.statuses.enums;

//todo UNKNOW - не хорошо
public enum UploadStatuses {
    EMPTY("warning_empty"),
    WRONG_FORMAT("warning_wrong_format"),
    SUCCESS("success"),
    BAD_ENCODE("can't encode to Base64"),
    UNKNOW("unknow");

    private String msg;

    UploadStatuses(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getStatus(String fileName) {
        return getMsg() + "'" + fileName + "' ";
    }

}
