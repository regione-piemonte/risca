package it.csi.risca.riscaboweb.util;

import it.csi.risca.riscaboweb.util.AppServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppServletContextListener implements ServletContextListener {

    private static ServletContext sc;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* Sets the context in a static variable */
        AppServletContextListener.sc = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    public static ServletContext getServletContext() {
        return sc;
    }
}