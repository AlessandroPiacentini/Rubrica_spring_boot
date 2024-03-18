package com.example.demo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RubricaController {
    private db_connector db;

    public RubricaController() {
        this.db = new db_connector();
    }

    @GetMapping("/regist")
    public Map<String, String> createToken(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        Map<String, String> result = new HashMap<>();
        Map<String, Object> where = new HashMap<>();
        where.put("username", username);
        ResultSet existingUser = db.readTable("user", where);
        try {
            if (existingUser.next()) {
                result.put("message", "utente già esistente");
                result.put("status", "error");
            } else {
                String token = generaStringaAlfanumerica(32);
                db.insert("user", Map.of("username", username, "password", password, "token", token));
                result.put("status", "ok");
                result.put("token", token);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        Map<String, Object> where = new HashMap<>();
        where.put("username", username);
        where.put("password", password);
        ResultSet user = db.readTable("user", where);
        try {
            if (user.next()) {
                result.put("status", "ok");
                String token = generaStringaAlfanumerica(32);
                db.updateTable("user", Map.of("token", token), where);

                result.put("token", token);
            } else {
                result.put("message", "utente non esistente");
                result.put("status", "error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GetMapping("/addContact")
    public Map<String, String> addContact(@RequestParam("name") String name, @RequestParam("number") String number, @RequestParam("token") String token, @RequestParam(value = "group_name", required = false) String group_name) {
        Map<String, String> result = new HashMap<>();
        Map<String, Object> userWhere = new HashMap<>();
        userWhere.put("token", token);
        ResultSet user = db.readTable("user", userWhere);
        try {
            if (user.next()) {
                Map<String, Object> contact_where = new HashMap<>();
                contact_where.put("name", name);
                contact_where.put("number", number);
                contact_where.put("id_user", user.getInt("id"));
                contact_where.put("group_name", group_name);
                
                db.insert("contact", contact_where);
                result.put("status", "ok");
                result.put("result", "contatto aggiunto");
            } else {
                result.put("message", "token non valido");
                result.put("status", "error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/getContacts")
    public Map<String, Object> getcontact(@RequestParam("token") String token) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> userWhere = new HashMap<>();
        userWhere.put("token", token);
        ResultSet user = db.readTable("user", userWhere);
        try {
            if (user.next()) {
                ResultSet contact = db.readTable("contact", Map.of("id_user", user.getInt("id")));
                List<String> listaContatti = new ArrayList<>();
                while (contact.next()) {
                    listaContatti.add(contact.getInt("id") + "-" + contact.getString("name") + "-" + contact.getString("number") + "-" + contact.getString("group_name"));
                }
                result.put("status", "ok");
                result.put("result",    listaContatti);
            } else {
                result.put("message", "token non valido");
                result.put("status", "error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @GetMapping("/deleteContact")
    public Map<String, String> deleteContact(@RequestParam("id") Long id, @RequestParam("token") String token)  {
        Map<String, String> result = new HashMap<>();
        Map<String, Object> where = new HashMap<>();
        ResultSet user = db.readTable("user", Map.of("token", token));
        try {
            if(user.next()) {
                where.put("id", id);
                ResultSet contact = db.readTable("contact", where);
                try {
                    if (contact.next()) {
                        db.delete("contact", where);
                        result.put("status", "ok");
                        result.put("result", "contatto eliminato");
                    } else {
                        result.put("message", "token non valido");
                        result.put("status", "error");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return result;
            } else {
                result.put("message", "token non valido");
                result.put("status", "error");
                return result;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    @GetMapping("/getGroups")
    public Map<String, Object> getGroups(@RequestParam("token") String token) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> userWhere = new HashMap<>();
        userWhere.put("token", token);
        ResultSet user = db.readTable("user", userWhere);
        try {
            if (user.next()) {
                result.put("status", "ok");
                result.put("result", db.findDistinctGroupNames(user.getInt("id")));
            } else {
                result.put("message", "token non valido");
                result.put("status", "error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
