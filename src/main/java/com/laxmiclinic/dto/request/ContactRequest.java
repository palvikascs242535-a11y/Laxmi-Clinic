package com.laxmiclinic.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ContactRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    @NotBlank(message = "Contact number is required")
    private String mobile;

    private String subject;

    @NotBlank(message = "Message is required")
    private String message;

    public ContactRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
