package org.mixer2.sample.web.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
        "classpath:mvc-dispatcher-servlet.xml" })
public class ItemControllerTest {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private Mixer2Engine mixer2Engine;

    @Autowired
    private DataSource dataSource;

    private IDatabaseConnection getConnection() throws Exception {
        Connection con = dataSource.getConnection();
        IDatabaseConnection connection = new H2Connection(con, null);
        return connection;
    }

    @Before
    public void before() throws Exception {
        // insert test data into database.
        // see testdata/ItemControllerTestData.xls
        File file = ResourceUtils
                .getFile("classpath:testdata/ItemControllerTest.xls");
        IDataSet dataset = new XlsDataSet(file);
        DatabaseOperation.INSERT.execute(getConnection(), dataset);
    }

    @Test
    public void showItem_exists() throws Exception {
        int itemId = 10001;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RequestContextHolder
                .setRequestAttributes(new ServletRequestAttributes(request));

        // request init here
        request.setMethod("GET");
        request.setRequestURI("/item/" + itemId);

        // execute Controller method and get result as String
        Object handler = handlerMapping.getHandler(request).getHandler();
        ModelAndView modelAndView = handlerAdapter.handle(request, response,
                handler);

        // reverse html string to Html object.
        String viewName = modelAndView.getViewName();
        assertThat(viewName, is("item"));
    }

}
