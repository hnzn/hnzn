package com.jq.util;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hnznw on 2018/1/25.
 */
public class ClassScannerUtil {

    private static ClassScannerUtil cs = new ClassScannerUtil();

    private ClassScannerUtil(){
    }

    public static ClassScannerUtil getInstance(){
        return cs;
    }

    private String path;

    public void setPath(String path){
        this.path = path;
    }

    public  String getPath(){
        Class c = ClassScannerUtil.class;
      //  return "C:\\Users\\hnznw\\IdeaProjects\\star\\src\\main\\java\\";
        return path;
    }

    public  List<String> findFiles(String path){
        File f = new File(path);
        File[] files = f.listFiles();
        List<String> list = new ArrayList<String>();
        for (File file : files) {
            if(file.isFile() && checkFile(file.getName())){
                //System.out.println(getClassPath(file.getAbsolutePath()));
                list.add(getClassPath(file.getAbsolutePath()));
            }else if(file.isDirectory()){
                list.addAll(findFiles(file.getPath()));
            }
        }
        return list;
    }

    public  String getClassPath(String path){
        return path.replace(getPath().replace("/","\\"),"").replace("\\",".").replace(".java","").replace(".class","");
    }

    public  boolean checkFile(String name){
        Pattern p = Pattern.compile("\\.java|\\.class");
        return  p.matcher(name).find();
    }

    public  void addUrl(String url) throws Exception{
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        boolean accessible = method.isAccessible();
        try {
            if (accessible == false) {
                method.setAccessible(true);
            }
            // 设置类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            // 将当前类路径加入到类加载器中
            method.invoke(classLoader, new URL(url));
        } finally {
            method.setAccessible(accessible);
        }
    }

    public  List<Class> getAllClass() throws Exception{
        List<String> list =findFiles(getPath());
        List<Class> classes = new ArrayList<Class>();
        for (String s: list) {
            System.out.println(s);
            try {
                Class cl = Class.forName(s);
                classes.add(cl);
            }catch (NoClassDefFoundError e){
            }
        }
        return classes;
    }

    public  String getMethodContent(Class c,Method m) throws Exception{
        InputStreamReader is = new InputStreamReader(new FileInputStream(getPath()+c.getPackage().getName().replace(".","\\")+"\\"+c.getSimpleName()+".java"));
        BufferedReader bi = new BufferedReader(is);
        String s = null;
        String cont = "";
        while ((s= bi.readLine())!=null){
            cont += s;
        }
        bi.close();
        is.close();
        cont =cont.substring(cont.indexOf(m.getName()));
        String result = "";
        if(!c.isInterface()){
            cont =cont.substring(cont.indexOf("{"));
            int left = 0;
            int right = 0;
            for (String ct : cont.split("")){
                if("{".equals(ct)){
                    left++;
                }
                if("}".equals(ct)){
                    right++;
                }
                if(left!=0 && left == right){
                    break;
                }
                result += ct;
            }
        }
        return result;
    }

    public  InvokClassInfo findInvokClass(InvokClassInfo c) throws Exception{
        if (c.className.isInterface()){
            return null;
        }
        String cont =getMethodContent(c.className,c.methodName);
        List<InvokClassInfo> invokClassInfos = new ArrayList<InvokClassInfo>();
        for (Field f : c.className.getDeclaredFields()) {
            while (cont.indexOf(f.getName()+".") != -1){
                cont = cont.substring(cont.indexOf(f.getName()+"."));
                cont = cont.substring(cont.indexOf(".")+1,cont.indexOf("("));
                for (Method m : f.getType().getMethods()) {
                    if (!m.getName().equals(cont.trim())){
                        continue;
                    }
                    InvokClassInfo ic = new InvokClassInfo();
                    ic.className = f.getType();
                    ic.methodName = m;
                    findInvokClass(ic);
                    invokClassInfos.add(ic);
                }
            }
        }
        c.invokClassInfos = invokClassInfos;
        return  null;
    }



    public InvokClassInfo findAllInvokClass() throws Exception{
        boolean is = false;
        InvokClassInfo ic = new InvokClassInfo();
        for (Class s: getAllClass()) {
            for (Method m: s.getMethods()) {
                for (Annotation a :m.getAnnotations()) {
                    if(a.toString().indexOf("/test")>0){
                        ic.className = s;
                        ic.methodName = m;
                        findInvokClass(ic);
                        is = true;
                    }
                    if (is) break;
                }
                if (is) break;
            }
            if (is) break;
        }
        return ic;
    }

    class InvokClassInfo{
        public Class className;
        public Method methodName;
        public List<InvokClassInfo> invokClassInfos;

        @Override
        public String toString(){
            StringBuffer sb = new StringBuffer();
            if (className != null)
              sb.append(className).append("\n\r");
            if (methodName != null)
              sb.append(methodName).append("\n\r");
            if (invokClassInfos != null){
                for (InvokClassInfo c : invokClassInfos) {
                    sb.append(c.toString());
                }
            }
            return sb.toString();
        }
    }



    public static void main(String[] args) throws Exception {
        ClassScannerUtil.getInstance().setPath("C:\\Users\\hnznw\\IdeaProjects\\star\\src\\main\\java\\");
       //ClassScannerUtil.getInstance().setPath("C:\\myJob\\SpringMVC-Mybatis-Shiro-redis-0.2\\target\\classes\\");
       // ClassScannerUtil.getInstance().addUrl("file://C:\\myJob\\SpringMVC-Mybatis-Shiro-redis-0.2\\target\\classes\\");
        //ClassScannerUtil.getInstance().addUrl("file://C:\\myJob\\maven\\localWareHouse\\commons-lang\\commons-lang\\2.6\\commons-lang-2.6.jar");
        System.out.println(ClassScannerUtil.getInstance().findAllInvokClass().toString());
    }


}
