package com.pinyougou.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Properties;

public class PathFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /**
         * web应用程序启动时,web服务器将创建Filter的实例对象,并调用其init方法,
         * 读取web.xml配置,完成对象的初始化功能,从而为后续的用户请求作好拦截的准备工作
         *  (filter对象之后创建一次,init方法也之后执行一次)。
         *  开发人员通过init方法的参数,可获得代表当前filter配置信息的FilterConfig对象
         */
//        System.out.println("初始化");
        ServletContext servletContext = filterConfig.getServletContext();

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /**
         * 该方法完成实际的过滤操作,当用户请求方法与过滤器设置匹配的URL时,
         * Servlet容器将先调用过滤器的doFilter方法。
         * FilterChain用户访问后续过滤器
         */

        HttpServletRequest req = (HttpServletRequest)request;

        //获取全路径
        String realPath = req.getSession().getServletContext().getRealPath("/");



//        //创建属性对象
//        Properties properties = new Properties();
//
//        //获取到path属性文件
////        InputStream input = getClass().getResourceAsStream("path.properties");
//        InputStream input = new FileInputStream("path.properties");
//        //从输入流中读取属性列表(键和元素对)
//        properties.load(input);
//
//        //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
//        OutputStream out = new FileOutputStream("path.properties");
//
//        properties.setProperty("prefixPath",realPath);
//        //以适合使用 load 方法加载到 Properties 表中的格式，
//        //将此 Properties 表中的属性列表（键和元素对）写入输出流
//        properties.store(out, "Update  prefixPath name");


//        System.out.println("路径获取成功");
//        System.out.println(realPath);

        //回调函数
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        /**
         * Servlet容器在销毁过滤器实例前调用该方法,在该方法中释放Servlet过滤器占用的资源
         */
        System.out.println("过滤路径销毁");
    }
}
