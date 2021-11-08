package com.example.crud_redis_dtd.repository;

import com.example.crud_new.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    private HashOperations hashOperations;//crud hash
    private ListOperations listOperations;
    private String LIST_KEY = "EMPLOYEE-LIST";
    private String SET_KEY = "EMPLOYEE-SET";

    @Autowired
    private RedisTemplate redisTemplate;

    public EmployeeRepository(RedisTemplate redisTemplate) {

        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations= redisTemplate.opsForList();
        this.redisTemplate = redisTemplate;

    }

    public void saveEmployee(Employee employee){

//        hashOperations.put("EMPLOYEE", employee.getId(), employee);
        listOperations.leftPush("LIST_KEY", employee) ;
    }

    public List<Employee> findAll(){

//        return hashOperations.values("EMPLOYEE");

        Long lastIndex = listOperations.size("LIST_KEY") - 1 ;
        System.out.println("last index:"+ listOperations.size("list-employee"));
        System.out.println("list:"+ listOperations.range("list-employee", 0, lastIndex));

        return listOperations.range("LIST_KEY", 0, lastIndex);

    }
    public Employee findById(Integer id){

//        return (Employee) hashOperations.get("EMPLOYEE", id);
//        crud list

        List<Employee> employees = findAll();
        for (Employee employee : employees) {
            if (employee.getId() == id)
                return employee;
        }
        return new Employee();
    }

    public void update(Employee employee){
        saveEmployee(employee);
    }
    public void delete(Integer id){

//        hashOperations.delete("EMPLOYEE", id);

//        crud list
        listOperations.rightPopAndLeftPush("LIST_KEY", id);
    }
}
