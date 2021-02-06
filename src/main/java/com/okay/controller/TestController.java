package com.okay.controller;

import com.okay.model.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRJpaDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class TestController {

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