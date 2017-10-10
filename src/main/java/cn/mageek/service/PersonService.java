package cn.mageek.service;

import cn.mageek.pojo.Person;

import java.util.List;

public interface PersonService {
    List<Person> findByAddress(String address);
}
