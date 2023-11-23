package com.example.myapplication;

public class ApplicantData {
    private String name;
    private String email;
    private String role;
    private String applicantId;
    private String userDob;
    private String userPhone;
    private String userCollege;
    private String userDept;
    private String userPgMark;
    private String userUgMark;
    private String userPlusTwoMark;
    private String userTenthMark;
    private String userResume;
    private String userSkills;

    public ApplicantData(
            String name, String email, String role, String applicantId,
            String userDob, String userPhone, String userCollege,
            String userDept, String userPgMark, String userUgMark,
            String userPlusTwoMark, String userTenthMark,
            String userResume, String userSkills) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.applicantId = applicantId;
        this.userDob = userDob;
        this.userPhone = userPhone;
        this.userCollege = userCollege;
        this.userDept = userDept;
        this.userPgMark = userPgMark;
        this.userUgMark = userUgMark;
        this.userPlusTwoMark = userPlusTwoMark;
        this.userTenthMark = userTenthMark;
        this.userResume = userResume;
        this.userSkills = userSkills;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public String getUserDob() {
        return userDob;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserCollege() {
        return userCollege;
    }

    public String getUserDept() {
        return userDept;
    }

    public String getUserPgMark() {
        return userPgMark;
    }

    public String getUserUgMark() {
        return userUgMark;
    }

    public String getUserPlusTwoMark() {
        return userPlusTwoMark;
    }

    public String getUserTenthMark() {
        return userTenthMark;
    }

    public String getUserResume() {
        return userResume;
    }

    public String getUserSkills() {
        return userSkills;
    }
}
