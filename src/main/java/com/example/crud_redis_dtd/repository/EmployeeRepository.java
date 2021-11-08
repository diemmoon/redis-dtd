package com.example.crud_redis_dtd.repository;

import com.example.crud_new.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class EmployeeRepository {

    private HashOperations hashOperations;//crud hash
    private ListOperations listOperations;
    private SetOperations setOperations;
    private String LIST_KEY = "EMPLOYEE-LIST";
    private String SET_KEY = "EMPLOYEE-SET";

    @Autowired
    private RedisTemplate redisTemplate;

    public EmployeeRepository(RedisTemplate redisTemplate) {

        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations= redisTemplate.opsForList();
        this.setOperations= redisTemplate.opsForSet();
        this.redisTemplate = redisTemplate;

    }

    public void saveEmployee(Employee employee){

//        hashOperations.put("EMPLOYEE", employee.getId(), employee);
//        listOperations.leftPush("LIST_KEY", employee) ;
        setOperations.add("SET_KEY",employee);
    }

    public Set<Employee> findAll(){

//        return hashOperations.values("EMPLOYEE");

//        Long lastIndex = listOperations.size("LIST_KEY") - 1 ;
//
//        return listOperations.range("LIST_KEY", 0, lastIndex);

        return setOperations.members("SET_KEY");
    }
    public Employee findById(Integer id){

//        return (Employee) hashOperations.get("EMPLOYEE", id);
//        crud list

//        List<Employee> employees = findAll();

        Set<Employee> employees = findAll();

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
//        listOperations.rightPopAndLeftPush("LIST_KEY", id);

        setOperations.remove("SET_KEY", findById(id));

    }
}
