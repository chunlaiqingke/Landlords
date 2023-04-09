package com.handsome.landlords.client.javafx.listener;

import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.ui.UIService;
import com.handsome.landlords.print.SimplePrinter;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ClientListenerUtils {

    /** code - listener 映射 */
    private static final Map<ClientEventCode, ClientListener> LISTENER_MAP = new HashMap<>(16);

    /**
     * 获取code对应的事件监听器
     *
     * @param code 事件编码
     * @return  code对应的事件监听器，如果不存在对应的事件监听器则返回null
     */
    public static ClientListener getListener(ClientEventCode code) {
        return LISTENER_MAP.get(code);
    }

    public static ClientEventCode[] supportCodes() {
        return LISTENER_MAP.keySet().toArray(new ClientEventCode[] {});
    }

    public static void setUIService(UIService uiService) {
        for (ClientListener listener : LISTENER_MAP.values()) {
            listener.setUIService(uiService);
        }
    }

    private static final String JAR_FILE_NAME = "landlords-client";

    static {
        List<Class<ClientListener>> listenerClassList;
        try {
            listenerClassList = findListener();
        } catch (NullPointerException var9) {
            String userDir = System.getProperty("user.dir");
            File userDirFile = new File(userDir);
            File jarFile = Stream.of(userDirFile.listFiles())
                                .filter((file) -> file.getName().contains(JAR_FILE_NAME))
                                .findFirst()
                                .orElseThrow(() ->
                                        new RuntimeException("当前 " + userDir + " 目录下找不到 " + "landlords-client" + ".jar 包"));

            try {
                listenerClassList = findListenerInJarFile(new JarFile(jarFile.getAbsoluteFile()));
                System.out.println("size: " + listenerClassList.size());
            } catch (IOException var8) {
                throw new RuntimeException(var8);
            }
        }

        Iterator<Class<ClientListener>> var1 = listenerClassList.iterator();

        while(var1.hasNext()) {
            Class<ClientListener> clazz = var1.next();

            try {
                ClientListener listener = clazz.newInstance();
                LISTENER_MAP.put(listener.getCode(), listener);
            } catch (InstantiationException var6) {
                SimplePrinter.printNotice(clazz.getName() + " 不能被实例化");
                var6.printStackTrace();
            } catch (IllegalAccessException var7) {
                SimplePrinter.printNotice(clazz.getName() + " 没有默认构造函数或默认构造函数不可访问");
                var7.printStackTrace();
            }
        }

    }

    private static List<Class<ClientListener>> findListener() {
        URL classWorkPath = ClientListenerUtils.class.getResource("");
        File classWorkDir = new File(classWorkPath.getPath());

        List<File> dirs = Arrays.stream(classWorkDir.listFiles()).filter(File::isDirectory).collect(Collectors.toList());

        //当前的类添加进去
        List<Class<ClientListener>> result = new ArrayList<>(findListener(classWorkDir));
        //把目录下的类添加进去
        for(File file : dirs) {
            result.addAll(findListener(file));
        }

        return result;
    }

    private static List<Class<ClientListener>> findListener(File classWorkDir){
        ClassLoader defaultClassLoader = ClientListenerUtils.class.getClassLoader();
        return loadClasses(defaultClassLoader, classWorkDir.listFiles((FileFilter) ClientListenerUtils::isNormalClass))
                .stream()
                .filter(clazz -> clazz.getSuperclass() == AbstractClientListener.class)
                .map(clazz -> (Class<ClientListener>) clazz)
                .collect(Collectors.toList());
    }

    private static boolean isNormalClass(File file) {
        return isNormalClass(file.getName());
    }

    private static boolean isNormalClass(String entryName) {
        boolean isClassFile = entryName.endsWith(".class");
        boolean isNotInnerClassFile = !entryName.matches("[A-Z]\\w+\\$\\w+.class");
        return isClassFile && isNotInnerClassFile;
    }

    private static List<Class<?>> loadClasses(ClassLoader classLoader, File[] classFiles) {
        String classpath = classLoader.getResource("").getPath();

        List<Class<?>> classList = new ArrayList<>(classFiles.length);
        for (File classFile : classFiles) {
            String absolutePath = classFile.getAbsolutePath();
            String classFullName = absolutePath.substring(classpath.length(), absolutePath.lastIndexOf("."))
                    .replace(File.separator, ".");

            try {
                classList.add(classLoader.loadClass(classFullName));
            } catch (ClassNotFoundException e) {
                SimplePrinter.printNotice("默认类加载器在 " +classpath+ " 路径下没有找到 "+classFullName+" 类");
            }
        }

        return classList;
    }

    private static final String PACKAGE_NAME = "com/handsome/landlords/client/javafx/listener";

    private static List<Class<ClientListener>> findListenerInJarFile(JarFile jarFile) {
        List<Class<?>> classes = new ArrayList(10);
        ClassLoader defaultClassLoader = ClientListenerUtils.class.getClassLoader();
        Enumeration enumeration = jarFile.entries();

        System.out.println("start");
        while(enumeration.hasMoreElements()) {
            System.out.println("while inner");
            JarEntry jarEntry = (JarEntry)enumeration.nextElement();
            String entryName = jarEntry.getName();

            boolean isMaybeListenerClass = entryName.contains(PACKAGE_NAME) && isNormalClass(entryName);
            if (isMaybeListenerClass) {
                String classFullName = entryName.substring(entryName.lastIndexOf(PACKAGE_NAME), entryName.lastIndexOf(".")).replace("/", ".");

                try {
                    classes.add(defaultClassLoader.loadClass(classFullName));
                } catch (ClassNotFoundException var9) {
                    SimplePrinter.printNotice("默认类加载器在 "+jarFile.getName()+" jar包中下没有找到 "+classFullName+" 类");
                }
            }
        }

        return (List) classes.stream()
                             .filter((clazz) -> clazz.getSuperclass() == AbstractClientListener.class)
                             .collect(Collectors.toList());
    }
}