/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ $Id:
 * Driver.java,v 1.1 ga.sotelo69 Exp $ Universidad de los Andes (Bogotá -
 * Colombia) Departamento de Ingeniería de Sistemas y Computación Licenciado
 * bajo el esquema Academic Free License version 2.1
 *
 * Ejercicio: Taller 1 - anotaciones Autor: German Augusto Sotelo
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package anotaciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Clase encargada de la instanciación y el manejo de las anotaciones
 *
 * @author Germán Sotelo
 */
public class Driver {

    /**
     * Anotaciones que tienen inserciones de codigo. Toda anotación que tenga un
     * método que la represente en la clase CodigoInserciones debe estar en este
     * arreglo para que sea efectiva
     */
    public static Class[] anotacionesInsercion = {Init.class, Log.class};

    /**
     * Estructura encargada de contener las clases y sus proxys
     */
    private static Map<Class, Class> proxys = new TreeMap<Class, Class>(new Comparator<Class>() {
        public int compare(Class o1, Class o2) {
            String h1 = "" + o1, h2 = "" + o2;
            return h1.compareTo(h2);
        }
    });

    /**
     * Permite saber si el método que se le pasa como parametro tiene
     * inyecciones de código o no
     *
     * @param method
     * @return
     */
    private static boolean tieneInyeccion(Method method) {
        for (Class anotacion : anotacionesInsercion) {
            if (method.isAnnotationPresent(anotacion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dado un tipo retorna un String que lo representa
     *
     * @param tipo
     * @return
     */
    private static String getRealType(Class tipo) {
        if (tipo.equals(Boolean.TYPE)) {
            return "Boolean.TYPE";
        }
        if (tipo.equals(Double.TYPE)) {
            return "Double.TYPE";
        }
        if (tipo.equals(Integer.TYPE)) {
            return "Integer.TYPE";
        }
        if (tipo.equals(Short.TYPE)) {
            return "Short.TYPE";
        }
        if (tipo.equals(Float.TYPE)) {
            return "Float.TYPE";
        }
        if (tipo.equals(Long.TYPE)) {
            return "Long.TYPE";
        }
        if (tipo.equals(Character.TYPE)) {
            return "Character.TYPE";
        }
        return tipo.getSimpleName() + ".class";
    }

    /**
     * Crea un proxy de la clase que se le pasa como parámetro
     *
     * @param objetivo
     * @return
     */
    private static Class crearProxy(Class objetivo) {
        Class proxy = proxys.get(objetivo);
        if (proxy != null) {
            return proxy;
        }
        File classes = new File("./build/classes/"), classpa = new File("./src/");

        // Este archivo es el archivo .java del proxy
        File source = new File("./src/mundo/" + objetivo.getSimpleName() + "Proxy.java");
        File librerias = new File("./lib/");
        PrintWriter pw = null;
        try {
            // La insercion sólo se realiza si el objetivo es una clase
            if (!objetivo.isInterface() && !objetivo.isEnum()) {
                String texto = "";
                boolean tieneInyecciones = false;
                pw = new PrintWriter(source);
                String paquete = objetivo.getPackage().getName();

                // Se agregan todas las importaciones necesarias
                pw.println("package " + paquete + ";");
                BufferedReader br = new BufferedReader(new FileReader("./src/" + paquete.replace(".", "/") + "/" + objetivo.getSimpleName() + ".java"));
                for (String h; (h = br.readLine()) != null;) {
                    h = h.trim();
                    if (h.startsWith("import")) {
                        pw.println(h);
                    }
                }
                pw.println("import anotaciones.*;");
                pw.println("import java.lang.reflect.*;");
                pw.println("import java.lang.annotation.Annotation;");
                pw.println("import java.io.BufferedWriter;");
                pw.println("import java.io.FileNotFoundException;");
                pw.println("import java.io.FileOutputStream;");
                pw.println("import java.io.IOException;");
                pw.println("import java.io.OutputStreamWriter;");
                pw.println("import java.io.UnsupportedEncodingException;");
                pw.println("import java.io.Writer;");
                pw.println("import java.util.Date;");

                br.close();

                // El proxy extiende del objetivo
                pw.println("public class " + objetivo.getSimpleName() + "Proxy extends " + objetivo.getSimpleName() + "{");

                // Se crea el constructor por defecto del proxy
                pw.println("public " + objetivo.getSimpleName() + "Proxy(){");
                pw.println("super();");
                pw.println("}");

                // Se revisan todos los métodos
                for (Method method : objetivo.getDeclaredMethods()) {
                    int modifi = method.getModifiers();
                    // Se verifica si tiene una anotación con inyección, los métodos estáticos no se han contemplado
                    if (tieneInyeccion(method) && !Modifier.isStatic(modifi)) {
                        // Se imprime la declaración del metodo
                        pw.print(Modifier.toString(modifi));
                        Class tipoRetorno = method.getReturnType();
                        pw.print(" " + tipoRetorno.getSimpleName());
                        pw.print(" " + method.getName() + "(");
                        Class[] para = method.getParameterTypes();
                        for (int e = 0; e < para.length; e++) {
                            pw.print(para[e].getSimpleName() + " arg" + e + (e == para.length - 1 ? "" : ","));
                        }
                        pw.println(")");
                        Class[] excep = method.getExceptionTypes();
                        if (excep.length > 0) {
                            pw.print("throws ");
                        }
                        for (int e = 0; e < excep.length; e++) {
                            pw.print(excep[e].getSimpleName() + (e == excep.length - 1 ? "" : ","));
                        }
                        pw.println("{");

                        // Se verifica e inyecta el código de cada anotación presente
                        for (Class annotation : anotacionesInsercion) {
                            if (method.isAnnotationPresent(Init.class) && annotation.getName() == Init.class.getName()) {
                                pw.println("{try{");
                                pw.print("Method meth = " + objetivo.getSimpleName() + ".class.getMethod(\"" + method.getName() + "\",new Class[]{");
                                for (int e = 0; e < para.length; e++) {
                                    pw.print(getRealType(para[e]) + (e == para.length - 1 ? "" : ","));
                                }
                                pw.println("});");
                                pw.println("Annotation an = meth.getAnnotation(" + annotation.getName() + ".class);");
                                pw.println("CodigoInserciones." + annotation.getSimpleName() + "(this," + objetivo.getSimpleName() + ".class,an,meth);");
                                pw.println("} catch (NoSuchMethodException ex) {");
                                pw.println("ex.printStackTrace();");
                                pw.println("} catch (SecurityException ex) {");
                                pw.println("ex.printStackTrace();");
                                pw.println("}catch (Exception ex) {");
                                pw.println("ex.printStackTrace();");
                                pw.println("}}");

                            } else if (annotation.getName() == Log.class.getName()) {
                                texto = ""
                                        + " Date fecha=new Date();\n"
                                        + "        try (\n"
                                        + "            Writer writer = new BufferedWriter(\n"
                                        + "            new OutputStreamWriter(\n"
                                        + "            new FileOutputStream(\"log.txt\",true), \"utf-8\"))\n"
                                        + "                ) {\n"
                                        + "                writer.write(\"\\n" + objetivo.getSimpleName() + " " + method.getName() + " \"+fecha);\n"
                                        + "            } catch (UnsupportedEncodingException ex) {\n"
                                        + "                System.out.println(ex);\n"
                                        + "            } catch (FileNotFoundException ex) {\n"
                                        + "                System.out.println(ex);\n"
                                        + "            } catch (IOException ex) {\n"
                                        + "                System.out.println(ex);\n"
                                        + "            }";
                            }
                        }

                        if (!tipoRetorno.equals(Void.TYPE)) {
                            pw.print(method.getReturnType().getName() + " retorno = super." + method.getName() + "(");
                            for (int e = 0; e < para.length; e++) {
                                pw.print("arg" + e + (e == para.length - 1 ? "" : ","));
                            }
                            pw.println(");");
                        }

                        // Se llama al correspondiente método de la super clase
                        if (!tipoRetorno.equals(Void.TYPE)) {
                            pw.print(texto);
                            pw.print("return retorno;");
                        } else {
                            pw.print("super." + method.getName() + "(");
                            for (int e = 0; e < para.length; e++) {
                                pw.print("arg" + e + (e == para.length - 1 ? "" : ","));
                            }
                            pw.println(");");
                            pw.print(texto);
                        }
                        pw.println("}");
                        tieneInyecciones = true;
                    }
                }

                pw.println("}");
                pw.close();

                // Si el objetivo tiene inyecciones, el proxy se compila y se carga dinámicamente
                if (tieneInyecciones) {
                    String libs = "";
                    for (File jar : librerias.listFiles()) {
                        libs += "\"" + librerias.getCanonicalPath() + "\\" + jar.getName() + "\";";
                    }
                    // Se compila, para esto es necesario el JDK
                    Process b = Runtime.getRuntime().exec("javac -d \"" + classes.getCanonicalPath() + "\" -classpath " + libs + "\"" + classpa.getCanonicalPath() + "\" \"" + source.getCanonicalPath() + "\"");

                    BufferedReader error = new BufferedReader(new InputStreamReader(b.getErrorStream()));
                    for (String h; (h = error.readLine()) != null;) {
                        System.out.println(h);
                    }
                    error = new BufferedReader(new InputStreamReader(b.getInputStream()));
                    for (String h; (h = error.readLine()) != null;) {
                        System.out.println(h);
                    }

                    source.delete();

                    // Se carga el proxy
                    Class ret = Class.forName(paquete + "." + objetivo.getSimpleName() + "Proxy");
                    if (ret != null) {
                        proxys.put(objetivo, ret);
                        return ret;
                    } else {
                        proxys.put(objetivo, objetivo);
                        return objetivo;
                    }
                } else {
                    // Si el objetivo no tiene inyecciones no se usa proxy
                    source.delete();
                    proxys.put(objetivo, objetivo);
                    return objetivo;
                }

            } else {
                //USO ANOTACIÓN INTERFACE
                if (objetivo.isInterface()) {
                    if (objetivo.isAnnotationPresent(InterfaceDetector.class)) {
                        pw = new PrintWriter(source);

                        for (Method method : objetivo.getDeclaredMethods()) {
                            Date fecha = new Date();
                            try (
                                    Writer writer = new BufferedWriter(
                                            new OutputStreamWriter(
                                                    new FileOutputStream("interfaceLog.txt", true), "utf-8"))) {
                                                writer.write("\n" + method.getName() + " " + objetivo.getSimpleName() + " " + fecha);
                                            } catch (UnsupportedEncodingException ex) {
                                                Logger.getLogger(ex.toString());
                                            } catch (FileNotFoundException ex) {
                                                Logger.getLogger(ex.toString());
                                            } catch (IOException ex) {
                                                Logger.getLogger(ex.toString());
                                            }
                        }
                    }
                }

                // Si el objetivo es una interfaz no se usa un proxy
                proxys.put(objetivo, objetivo);
                return objetivo;
            }
        } catch (Exception e) {
            // Si ocurre alguna excepción se elimina el proxy y se utiliza al objetivo como su propio
            // proxy
            if (pw != null) {
                pw.close();
            }
            source.delete();
        }
        proxys.put(objetivo, objetivo);
        return objetivo;
    }

    /**
     * Método encargado de retornar una instancia de la clase pasada como
     * parámetro
     *
     * @param c clase a instanciar
     * @return
     */
    public static Object instanciar(Class c) {

        // Se carga el proxy de la clase
        Class implementacion = crearProxy(c);
        if (!c.isInterface()) {
            try {
// Se busca su constructor por defecto
                Constructor constructor = implementacion.getConstructor();

                // Se invoca el constructor
                Object objeto = constructor.newInstance();

                // Se verifica si la clase tiene la anotación Init
                if (c.isAnnotationPresent(Init.class)) {
                    // Se inicializan los atributos afectador por las anotaciones Init
                    CodigoInserciones.Init(objeto, c, c.getAnnotation(Init.class), null);
                }

                for (Field f : c.getDeclaredFields()) {
                    // Se instancian los atributos anotados con Cargar
                    if (f.isAnnotationPresent(Cargar.class)) {
                        f.setAccessible(true);
                        f.set(objeto, instanciar(f.getType()));
                    }
                }

                //Iterar cada metodo buscando
                for (Method f : c.getMethods()) {
                    // Se instancian los atributos anotados con Cargar
                    if (f.isAnnotationPresent(PostConstructor.class)) {
                        f.invoke(objeto);
                    }
                }

                return objeto;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
