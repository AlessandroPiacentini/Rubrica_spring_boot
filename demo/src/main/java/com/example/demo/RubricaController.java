package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RubricaController {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public RubricaController(UserRepository userRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    @GetMapping("/createtoken")
    public Map<String, String> createToken(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        Map<String, String> result = new HashMap<>();
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            result.put("message", "utente già esistente");
            result.put("status", "error");
        } else {
            String token = generaStringaAlfanumerica(32);
            User user = new User(username, password, token);
            userRepository.save(user);
            result.put("status", "ok");
            result.put("token", token);
        }
        return result;
    }

    private String generaStringaAlfanumerica(int length) {
        String caratteri = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (caratteri.length() * Math.random());
            sb.append(caratteri.charAt(index));
        }
        return sb.toString();
    }

    @GetMapping("/getToken")
    public Map<String, String> getToken(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        Map<String, String> result = new HashMap<>();
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            result.put("status", "ok");
            String token = generaStringaAlfanumerica(32);
            user.get().setToken(token);


            userRepository.save(user.get());

            contactRepository.findAll().forEach(contact -> {
                if (contact.getToken().equals(user.get().getToken())) {
                    contact.setToken(token);
                    contactRepository.save(contact);
                }
            });

            result.put("token",token);
        } else {
            result.put("message", "utente non esistente");
            result.put("status", "error");
        }
        return result;
    }
    
    @GetMapping("/addContact")
    public Map<String, String> addContact(@RequestParam("name") String name, @RequestParam("number") String number, @RequestParam("token") String token, @RequestParam(value = "group_name", defaultValue = " ") String group_name) {
        Map<String, String> result = new HashMap<>();
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            Contact contact = new Contact(name, number, group_name, token);
            contactRepository.save(contact);
            result.put("status", "ok");
            result.put("result", "contatto aggiunto");
        } else {
            result.put("message", "token non valido");
            result.put("status", "error");
        }
        return result;
    }

    @GetMapping("/getContacts")
    public Map<String, Object> getContacts(@RequestParam("token") String token) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            result.put("status", "ok");
            result.put("result", contactRepository.findAll());
        } else {
            result.put("message", "token non valido");
            result.put("status", "error");
        }
        return result;
    }


    @GetMapping("/deleteContact")
    public Map<String, String> deleteContact(@RequestParam("id") Long id, @RequestParam("token") String token) {
        Map<String, String> result = new HashMap<>();
        Optional<Contact> contact = contactRepository.findByTokenAndId(token, id);
        if (contact.isPresent()) {
            contactRepository.deleteById(id);
            result.put("status", "ok");
            result.put("result", "contatto eliminato");
        } else {
            result.put("message", "token non valido");
            result.put("status", "error");
        }
        return result;
    }

    @GetMapping("/getGroups")
    public Map<String, Object> getGroups(@RequestParam("token") String token) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            result.put("status", "ok");
            result.put("result", contactRepository.findDistinctGroupName());
        } else {
            result.put("message", "token non valido");
            result.put("status", "error");
        }
        return result;
    }
}
