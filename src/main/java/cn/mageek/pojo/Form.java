package cn.mageek.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Form {
    private String name;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate birthDate ;
    private List<String> hobbies;

    //加了构造函数的话，表单的placeholder 就不管用了
    public Form() {
        this.name = "空";
        this.email = "空";
        this.birthDate = LocalDate.now();
        this.hobbies = new ArrayList<>();
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "name:" + name + " email:" + email + " birthDate:" + birthDate.toString() + " hobbies:" + hobbies.toString();
    }
}
