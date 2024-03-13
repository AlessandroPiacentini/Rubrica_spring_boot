package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileController {
    String filePathUsers = "C:\\scuola\\tecnologie\\java\\test_springboot\\demo\\src\\main\\java\\com\\example\\demo\\static\\users.csv";
    public FileController() {

    }
    
    public String appendToFile(String content, String filePath) {

        try (FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(content);
            bufferedWriter.newLine(); // Add newline if needed
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while writing to file.";
        }

        return "Content successfully appended to file.";
    }
    public boolean checkToken(String token) {
        

        try (BufferedReader br = new BufferedReader(new FileReader(filePathUsers))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[2].equals(token)) {
                    // Token trovato nel file
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // In caso di errore durante la lettura del file
            return false;
        }

        // Token non trovato nel file
        return false;
    }
    public boolean checkUser(String username, String password) {
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePathUsers))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[0].equals(username) && parts[1].equals(password)) {
                    // Utente trovato nel file
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // In caso di errore durante la lettura del file
            return false;
        }

        // Utente non trovato nel file
        return false;
    }
    public String getToken(String username, String password) {
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePathUsers))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[0].equals(username) && parts[1].equals(password)) {
                    // Utente trovato nel file
                    return parts[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // In caso di errore durante la lettura del file
            return null;
        }

        // Utente non trovato nel file
        return null;
    }
}
