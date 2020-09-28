package com.cb.authorization;

import com.cb.authorization.api.AuthResource;
import com.cb.authorization.database.DAO.TokenDAO;
import com.cb.authorization.database.Database;
import com.cb.authorization.services.Auth;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class MainApplication extends io.dropwizard.Application<Database> {

    @Override
    public void initialize(Bootstrap<Database> bootstrap) {

    }

    @Override
    public void run(Database configuration, Environment environment) throws Exception {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");


        final DBIFactory dbiFactory = new DBIFactory();
        final DBI dbi = dbiFactory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final TokenDAO tokenDAO = dbi.onDemand(TokenDAO.class);
        Auth auth = new Auth(tokenDAO);
        AuthResource authResource = new AuthResource(auth);
        environment.jersey().register(authResource);
    }
    public static void main(String[] args) throws Exception{
        new MainApplication().run(args);
    }
}
