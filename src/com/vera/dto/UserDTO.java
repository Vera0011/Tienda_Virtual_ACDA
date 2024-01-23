package com.vera.dto;

import com.vera.dao.UserDAO;

import java.util.HashMap;
import java.util.Objects;

public class UserDTO
{
    private int id;
    private String full_name;
    private String user_;
    private String email;
    private String password_;
    private String creation_date;
    private String modification_date;

    public UserDTO(int id, String full_name, String user_, String email, String password_, String creation_date, String modification_date) {
        this.id = id;
        this.full_name = full_name;
        this.user_ = user_;
        this.email = email;
        this.password_ = password_;
        this.creation_date = creation_date;
        this.modification_date = modification_date;
    }

    /* Manual creation */
    public UserDTO(String full_name, String user_, String email, String password_) {
        this.full_name = full_name;
        this.user_ = user_;
        this.email = email;
        this.password_ = password_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_() {
        return user_;
    }

    public void setUser_(String user_) {
        this.user_ = user_;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_() {
        return password_;
    }

    public void setPassword_(String password_) {
        this.password_ = password_;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getModification_date() {
        return modification_date;
    }

    public void setModification_date(String modification_date) {
        this.modification_date = modification_date;
    }

    public UserDTO getProductByID(int id)
    {
        HashMap<String, HashMap<String, String>> columnNames = new HashMap<>();
        HashMap<String, String> dataID = new HashMap<>();

        dataID.put("Integer", id + "");
        columnNames.put("id", dataID);

        return UserDAO.getByID(columnNames);
    }

    public UserDTO insertIntoDatabase()
    {
        HashMap<String, HashMap<String, String>> columnNames = new HashMap<>();
        HashMap<String, String> dataFullName = new HashMap<>();
        HashMap<String, String> dataUser = new HashMap<>();
        HashMap<String, String> dataEmail = new HashMap<>();
        HashMap<String, String> dataPassword = new HashMap<>();

        dataFullName.put("String", this.full_name);
        dataUser.put("String", this.user_);
        dataEmail.put("String", this.email);
        dataPassword.put("String", "" + this.password_);

        columnNames.put("full_name", dataFullName);
        columnNames.put("user_", dataUser);
        columnNames.put("email", dataEmail);
        columnNames.put("password_", dataPassword);

        return UserDAO.insertIntoDatabase(columnNames);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", full_name='" + full_name + '\'' +
                ", user_='" + user_ + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password_ + '\'' +
                ", creation_date='" + creation_date + '\'' +
                ", modification_date='" + modification_date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id && Objects.equals(full_name, userDTO.full_name) && Objects.equals(user_, userDTO.user_) && Objects.equals(email, userDTO.email) && Objects.equals(password_, userDTO.password_) && Objects.equals(creation_date, userDTO.creation_date) && Objects.equals(modification_date, userDTO.modification_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, full_name, user_, email, password_, creation_date, modification_date);
    }
}
