package com.vbs.demo.controller;


import com.vbs.demo.dto.DisplayDto;
import com.vbs.demo.dto.LoginDto;
import com.vbs.demo.dto.UpdateDto;
import com.vbs.demo.models.History;
import com.vbs.demo.models.User;
import com.vbs.demo.repositories.HistoryRepo;
import com.vbs.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController: Iska matlab ye class web requests (API calls) handle karegi
// aur data (JSON) return karegi, HTML page nahi.
@RestController
// @CrossOrigin: Security todne ke liye (ache way me).
// Browser normally alag port (React) se alag port (Java) pe data rokta hai.
// Ye annotation allow karta hai ki koi bhi (*) isse baat kar sake.
@CrossOrigin(origins = "*")
//port data cant be shared data sharing is offsed for security reasons
//cross-origin allow to croos communicate that is help to get the
//reguest,post from the user

public class UserController {
    @Autowired//ye deta
    // hai ki interface ka object baneka ka power
    UserRepo userRepo;
    @Autowired
    HistoryRepo historyRepo;
    @PostMapping("/register")
    public String register(@RequestBody User user) //pass poora kiya class name object name jo ki hoga RequestBody me may be??
    {
        userRepo.save(user);
        History h1 = new History();
        if(user.getRole().equalsIgnoreCase("admin"))
            h1.setDescription("Admin self Created : "+user.getUsername());
        else
            h1.setDescription("User self Created : "+user.getUsername());
        historyRepo.save(h1);
        return "Successfully Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto u)
    {
        User user = userRepo.findByUsername(u.getUsername());
        if(user == null)
        {
            return "User Not Found";
        }
        if(!u.getPassword().equals(user.getPassword()))
        {
            return "Password Incorrect";
        }
        if(!u.getRole().equals(user.getRole()))
        {
            return "Role incorrect";
        }
        return String.valueOf(user.getId());

    }

    @GetMapping("/get-details/{id}")
    public DisplayDto display (@PathVariable int id)
    {
        User user = userRepo.findById(id).orElseThrow(()-> new RuntimeException("User Not Found"));
        DisplayDto displayDto = new DisplayDto();
        displayDto.setUsername(user.getUsername());
        displayDto.setBalance(user.getBalance());
        return displayDto;
    }

    @PostMapping("/update")
    public String update(@RequestBody UpdateDto obj)
    {
        User user = userRepo.findById(obj.getId()).orElseThrow(()-> new RuntimeException("NOt Found"));
        History h1 = new History();
        if(obj.getKey().equalsIgnoreCase("name"))
        {
            if(user.getName().equalsIgnoreCase(obj.getValue())) return "Cannot be same";
            h1.setDescription("User Changed Name from "+user.getUsername()+" to "+obj.getValue());
            user.setName(obj.getValue());
        }
        else if(obj.getKey().equalsIgnoreCase("password"))
        {
            if(user.getPassword().equalsIgnoreCase(obj.getValue())) return "Cannot be same";
            h1.setDescription("User Changed Password: "+user.getUsername());
            user.setPassword(obj.getValue());
        }
        else if(obj.getKey().equalsIgnoreCase("email"))
        {
            User userwithsame = userRepo.findByEmail(obj.getValue());
            if(userwithsame != null) return "Email already taken";
            if(user.getEmail().equalsIgnoreCase(obj.getValue())) return "Cannot be same";
            h1.setDescription("User Changed Email from "+user.getEmail()+" to "+obj.getValue());
            user.setEmail(obj.getValue());
        }
        else {
            return "Invalid Key";
        }
        historyRepo.save(h1);
        userRepo.save(user);
        return "Update Done Successfully";
    }

    @PostMapping("/add/{adminId}")
    public String add(@RequestBody User user,@PathVariable int adminId)
    {
        History h1 = new History();
        h1.setDescription("User "+user.getUsername()+" Created By Admin "+ adminId);
        historyRepo.save(h1);
        userRepo.save(user);
        return "Successfully added";
    }

    @GetMapping("/users")
    public List<User> getall(@RequestParam String sortBy ,@RequestParam String order)
    {
        Sort sort; // this is the sort function inside the springboot library that allow us to sort but before using must create object
        if(order.equalsIgnoreCase("desc"))
        {
            sort = Sort.by(sortBy).descending();
        }
        else
        {
            sort = Sort.by(sortBy).ascending();
        }

        return userRepo.findAllByRole("customer",sort);
        //request param send the variable for get mapping but they are not compulsory that if not passed then also the code will work
        //while when passed using pathvariable they are must to be used
    }

    @GetMapping("/users/{keyword}")
    public List<User> getusers(@PathVariable String keyword)
    {
        return userRepo.findByUsernameContainingIgnoreCaseAndRole(keyword,"customer");
    }

    @DeleteMapping("/delete-user/{userId}/admin/{adminId}")
    public String deleteuser(@PathVariable int userId , @PathVariable int adminId)
    {
        User user = userRepo.findById(userId).orElseThrow(()-> new RuntimeException("NOT FOUND"));
        if(user.getBalance()>0) return "Balance should be Zero";
        History h1 = new History();
        h1.setDescription("User "+user.getUsername()+" Deleted By Admin "+ adminId);
        historyRepo.save(h1);
        userRepo.delete(user);
        return "User Deleted Successfully";
    }
}


