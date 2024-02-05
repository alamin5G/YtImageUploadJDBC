package com.goonok;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class ImageDao {
    private static final String url = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String user = "root";
    private static final String pass = "252646";
    private String image_path = "C:\\Users\\alami\\Downloads\\image3.jpg";
    private String folder_path = "C:\\Users\\alami\\Downloads\\";
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private String InsertQuery = "INSERT INTO imagedb(image_data) VALUES(?)";
    private String fetchQuery = "SELECT image_data FROM imagedb WHERE id = ?";

    private Connection connection;

    public ImageDao(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Established successfully!");
            fileInputStream = new FileInputStream(image_path);

        }catch (ClassNotFoundException c){
            System.err.println("JDBC Driver Class not Found at ImageDao- " +  c.getMessage());
        }catch (SQLException s){
            System.out.println("Database Connection Failed - " + s.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void imageStore(){
        try {
            byte[] imageData = new byte[fileInputStream.available()];
            fileInputStream.read(imageData);
            PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);
            preparedStatement.setBytes(1, imageData);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected>0){
                System.out.println("Insertion success!");
            }else {
                System.out.println("Image not inserted!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void retrieveImage(){

        try {
            int imageID = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);
            preparedStatement.setInt(1, imageID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                byte[] imageData = resultSet.getBytes("image_data");
                String img_path = folder_path + "extractedImg.jpg";
                fileOutputStream = new FileOutputStream(img_path);
                fileOutputStream.write(imageData);

            }else {
                System.out.println("Image not found!");
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


    }


}
