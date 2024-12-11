package com.example.joypadjourney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.joypadjourney.Security.BCrypt;
import com.example.joypadjourney.model.Request.RegisterCustomerRequest;
import com.example.joypadjourney.model.entity.Customer;
import com.example.joypadjourney.model.entity.Role;
import com.example.joypadjourney.model.entity.User;
import com.example.joypadjourney.repository.CustomerRepository;
import com.example.joypadjourney.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterCustomerRequest request){
       validationService.validate(request);

        //pengecekan apakah username udah ada
        if(customerRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username tersebut sudah digunakan");
        }
        // Cek apakah email sudah digunakan
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email tersebut sudah digunakan");
        }
        
        //menyimpan ke tabel user dulu
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);

        Customer customer = new Customer();
        customer.setUsername(user.getUsername());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNum());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customerRepository.save(customer);


    }
}
