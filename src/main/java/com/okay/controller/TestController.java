package com.okay.controller;

import com.okay.model.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class TestController {

    private final DataSource dataSource;

    public TestController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @ResponseBody
    @GetMapping(value = "/test")
    public void test(HttpServletResponse response, @RequestParam(value = "type") String type, @RequestParam(value = "count") int count) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/jasper/" + type + ".jrxml");

            List<User> userList = createUserList(count);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("dataSource", dataSource);
            parameters.put("tableName", type.toUpperCase(Locale.ROOT));

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(print, outputStream);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @GetMapping(value = "/db")
    public void db(HttpServletResponse response, @RequestParam(value = "nameQuery", required = false) String nameQuery, @RequestParam(value = "surnameQuery", required = false) String surnameQuery) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/jasper/db.jrxml");


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("nameQuery", nameQuery == null ? "" : nameQuery);
            parameters.put("surnameQuery", surnameQuery == null ? "" : surnameQuery);

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(print, outputStream);
        } catch (IOException | JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    private List<User> createUserList(int count) {
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            userList.add(createUser(i));
        }

        return userList;
    }

    private User createUser(int i) {
        User user = new User();
        user.setName("Ahmet" + i);
        user.setSurname("Okay" + ThreadLocalRandom.current().nextInt(1, 11));
        user.setEmail("ahmet@ahmet.com");
        user.setAddress("Türkiye" + ThreadLocalRandom.current().nextInt(1, 11));
        return user;
    }
}