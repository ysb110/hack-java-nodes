Java代码如何以字符串的形式在后台动态执行  
现在有一个需求。用户输入自己的代码,加入自己引用的依赖。就可以动态的执行,将结果返回。这样的例子网站上也有，比如https://c.runoob.com/compile/10在线编译工具。我们这里是一个升级版，可以引入自定义的maven依赖。

我们的场景是对于动态执行的Java代码。我们有个根目录。在根目录下指定一个pom.xml文件。当得到用户输入的依赖字符串之后，我们新建一个pom.xml替换之前的pom.xml。对他们的内容和路径拼接的字符串哈希运算。得到一个数值。
```java
public static String md5(String content) throws NoSuchAlgorithmException{
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(content.getBytes());
    BigInteger bi = new BigInteger(1, md5.digest());
    return bi.toString(16);
}
```
首先，检测pom.xml是否有变动？使用哈希什对文件内容字符串进行hash,
得到的结果与之前进行比对。如果不一致则发生了变更。变更后，首先要进行clean操作。
操作分为三步。  
第一步：将当前限定上下文切换到POM文件目录下。
```java
ProcessBuilder pb = new ProcessBuilder(args);
pb.directory(new File(JAR_PATH));
pb.redirectErrorStream(true);
```
第二步：将对应pom中的依赖项拷贝过来  
`cmd /c D:\ycq_java\apache-maven-3.6.3\bin\mvn clean`
  
第三步：将pom.xml中的依赖jar包拷贝到当前目录下的：target/dependency目录下  
`cmd /c D:\ycq_java\apache-maven-3.6.3\bin\mvn -f pom.xml dependency:copy-dependencies`

获取所有的jar包，所有jar包的路径作为参数，新建一个class loader，Parent是当前线程的类加载器。
获取jar包地址的方法如下
```java
public static List<File> findJars() throws IOException {
    List<File> findAllJar = new ArrayList<>();

    Files.walkFileTree(new File(JAR_PATH).toPath(), new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            File f = file.toFile();
            if (f.getName().toLowerCase().endsWith(".jar")) {
                findAllJar.add(f);
            }
            return FileVisitResult.CONTINUE;
        }
    });
    return findAllJar;
}
```
实例化一个class loader。  
`URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());`  
当前初始化工作已经完成。  
接下来我们举个例子  
用户输入一个Java代码,并传入所依赖的Maven。  
首先maven被写入到新的pom.xml文件中,先clean、引入依赖项,然后重新设置这个上下文的class loader。  
初始化完成后，首先会执行如下代码。
```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
compiler.getStandardFileManager(diagnostics, null, null）；
```
编译的过程主要调用compiler的getTask方法。该方法需要传入一个fileManager，还有构造的源代码对象。
```java
/**
     * 自定义一个字符串的源码对象
     */
    private class StringJavaFileObject extends SimpleJavaFileObject {
        //等待编译的源码字段
        private String contents;

        //java源代码 => StringJavaFileObject对象 的时候使用
        public StringJavaFileObject(String className, String contents) {
            super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }

        //字符串源码会调用该方法
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return contents;
        }

    }
```

```java
JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
```
在编译的过程中，fileManager会被填充包括它的主类和所有的内部类，那么我们通过加入自己的行为，就可以获取所有当前类和内部类。  
可以得到他的一个list数组。可以获取classloader等。主类放在list尾部。    
```java 
public class ClassFileManager extends ForwardingJavaFileManager {
  private List<JavaClassObject> javaClassObjectList;
  public ClassFileManager(StandardJavaFileManager standardManager) {
      super(standardManager);
      this.javaClassObjectList = new ArrayList<JavaClassObject>();
  }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String className, JavaFileObject.Kind kind, FileObject sibling)
            throws IOException {
        JavaClassObject jclassObject = new JavaClassObject(className, kind);
        this.javaClassObjectList.add(jclassObject);
        return jclassObject;
    }
```
`boolean success = task.call();` 
如果是假的话。真使用刚才的diagnostics对错误日志进行输出。
```java
StringBuilder error = new StringBuilder();
for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
    error.append(compilePrint(diagnostic));
}
throw new Exception(error.toString());
```
如果编译成功，则从filemanager中取出主类和内部类的。filemanager是一个自定义的JavaFileManage来控制编译之后字节码的输出位置。类的实现是通过继承ForwardingJavaFileManager。  
override他的getJavaFileForOutput方法。返回指定位置处指定类型的指定类。
```java
 //获取输出的文件对象，它表示给定位置处指定类型的指定类。
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            ByteJavaFileObject javaFileObject = new ByteJavaFileObject(className, kind);
            javaFileObjectMap.put(className, javaFileObject);
            return javaFileObject;
        }
    }
```
ByteJavaFileObject则是自定义一个编译之后的字节码对象。通过继承SimpleJavaFileObject,添加一个内部变量,自定义一个输出流。覆盖的方法openOutputStream会将这个输出流给使用的地方进行填充，  
需要的时候我们可以去这个输出流动获取字节码。使用自己的类加载器从字节码中来加载这个类。
```java
 /**
     * 自定义一个编译之后的字节码对象
     */
    private class ByteJavaFileObject extends SimpleJavaFileObject {
        //存放编译后的字节码
        private ByteArrayOutputStream outPutStream;

        public ByteJavaFileObject(String className, Kind kind) {
            super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), kind);
        }

        //StringJavaFileManage 编译之后的字节码输出会调用该方法（把字节码输出到outputStream）
        @Override
        public OutputStream openOutputStream() {
            outPutStream = new ByteArrayOutputStream();
            return outPutStream;
        }

        //在类加载器加载的时候需要用到
        public byte[] getCompiledBytes() {
            return outPutStream.toByteArray();
        }
    }
```
那加载器刚才我们已经实现了。通过maven指定的jar包。我们创建了自定义的类加载器。通过刚才得到的字节码。我们得到这个类。  
在具体的项目中，我们可以对这个类加入一些自定义的注解。注解的作用就是用来为我们的类进行一个动态代码替换，加入自己的一些行为。我们可以自定义一个@Single。当类包含这个single这个注解的时候。我们可以将它视为单例。也可以加，比如说execute注解。表示这个方法是需要在定时任务中立即执行的方法等等。
好了，我们来看一下最后方法是如何执行的。
```java
private static final Object[] DEFAULT_ARG = new Object[0];
Object invoke = method.invoke(objInstance, DEFAULT_ARG);
```
objInstance的伪代码Class.newInstance();

还有一个问题。在执行动态载入的类代码的时候。她所使用的类加载器,是我们自定义的类加载器。而当前大环境中的类加载器并不是我们自定义的类加载器。因此我们在执行动态这载入的类的是之前，要先将线程类加载器进行一个备份。在执行动态载入类方法之后，重新将线程上下文类加载器还原。见如下代码。  
```java
public void run() {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
        Thread.currentThread().setContextClassLoader(DynamicEngine.getInstance().getParentClassLoader());
        new JavaRunner(task).compile().instance().execute() ;
    } catch (Exception e) {
        e.printStackTrace();
        LOG.error(e.getMessage(),e);
    } finally {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
        ThreadManager.removeTaskIfOver(this.getName());
    }
}
```

  
