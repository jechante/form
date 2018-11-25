package com.schinta.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FormData {
    private String form;
    private String form_name;
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    static class Entry {
        private String serial_number;
        private String field_16;
        private String field_3;
        private String[] field_11;
        private String field_12;
        private String field_4;
        private String field_13;
        private String field_14;
        private String field_15;
        private String field_7;
        private String field_9;
        private String[] field_10;
        private String creator_name;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private String info_remote_ip;

        public String getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(String serial_number) {
            this.serial_number = serial_number;
        }

        public String getField_16() {
            return field_16;
        }

        public void setField_16(String field_16) {
            this.field_16 = field_16;
        }

        public String getField_3() {
            return field_3;
        }

        public void setField_3(String field_3) {
            this.field_3 = field_3;
        }

        public String[] getField_11() {
            return field_11;
        }

        public void setField_11(String[] field_11) {
            this.field_11 = field_11;
        }

        public String getField_12() {
            return field_12;
        }

        public void setField_12(String field_12) {
            this.field_12 = field_12;
        }

        public String getField_4() {
            return field_4;
        }

        public void setField_4(String field_4) {
            this.field_4 = field_4;
        }

        public String getField_13() {
            return field_13;
        }

        public void setField_13(String field_13) {
            this.field_13 = field_13;
        }

        public String getField_14() {
            return field_14;
        }

        public void setField_14(String field_14) {
            this.field_14 = field_14;
        }

        public String getField_15() {
            return field_15;
        }

        public void setField_15(String field_15) {
            this.field_15 = field_15;
        }

        public String getField_7() {
            return field_7;
        }

        public void setField_7(String field_7) {
            this.field_7 = field_7;
        }

        public String getField_9() {
            return field_9;
        }

        public void setField_9(String field_9) {
            this.field_9 = field_9;
        }

        public String[] getField_10() {
            return field_10;
        }

        public void setField_10(String[] field_10) {
            this.field_10 = field_10;
        }

        public String getCreator_name() {
            return creator_name;
        }

        public void setCreator_name(String creator_name) {
            this.creator_name = creator_name;
        }

        public LocalDateTime getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            Instant instant = Instant.parse(created_at);
            this.created_at = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }

        public LocalDateTime getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
//            this.created_at = LocalDateTime.parse(updated_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
            Instant instant = Instant.parse(updated_at);
            this.updated_at = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }

        public String getInfo_remote_ip() {
            return info_remote_ip;
        }

        public void setInfo_remote_ip(String info_remote_ip) {
            this.info_remote_ip = info_remote_ip;
        }
    }

}
