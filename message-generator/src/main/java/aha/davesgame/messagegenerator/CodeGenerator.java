package aha.davesgame.messagegenerator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

public class CodeGenerator {

    private final Message message;
    private final String name;
    private final List<Property> properties;
    private JCodeModel cm = new JCodeModel();
    private JDefinedClass dc;

    @SuppressWarnings("null")
    public CodeGenerator(Message message) {
        this.message = message;
        this.name = message.getName();
        this.properties = message.getProperties();
    }

    public void generateJavaSource(String destSrcDir) throws JClassAlreadyExistsException, ClassNotFoundException,
            IOException {
        dc = cm._class(message.getPackage() + "." + name);
        JClass iface = cm.directClass(message.getInterface());
        dc._implements(iface);
        for (Property p : properties) {
            generateField(p);
        }
        generateConstructor();
        for (Property p : properties) {
            generateGetter(p);
        }

        generateToString();
        generateHashCode();
        generateEquals();

        File file = new File(destSrcDir);
        cm.build(file);
    }

    private JType getType(String typeName) throws ClassNotFoundException {
        return cm.parseType(typeName);
    }

    private void generateEquals() throws ClassNotFoundException {
        JMethod method = dc.method(JMod.PUBLIC, cm.BOOLEAN, "equals");
        method.annotate(Override.class);
        JVar param = method.param(Object.class, "obj");
        param.annotate(Nullable.class);
        JBlock body = method.body();

        body._if(JExpr._this().eq(param))._then()._return(JExpr.TRUE);
        body._if(param.eq(JExpr._null()))._then()._return(JExpr.FALSE);
        body._if(param._instanceof(dc).not())._then()._return(JExpr.FALSE);
        JVar other = body.decl(dc, "other").init(JExpr.cast(dc, param));
        for (Property p : properties) {
            if (isPrimitive(p)) {
                body._if(JExpr.ref(p.getName()).ne(other.ref(p.getName())))._then()._return(JExpr.FALSE);
            } else {
                body._if(JExpr.ref(p.getName()).invoke("equals").arg(other.ref(p.getName())).not())._then()
                        ._return(JExpr.FALSE);
            }
        }
        body._return(JExpr.TRUE);
    }

    private boolean isPrimitive(Property p) throws ClassNotFoundException {
        return getType(p.getType()).isPrimitive();
    }

    private void generateField(Property p) throws ClassNotFoundException {
        dc.field(JMod.FINAL | JMod.PRIVATE, getType(p.getType()), p.getName());
    }

    private void generateGetter(Property p) throws ClassNotFoundException {
        String getterName = "get" + p.getName().substring(0, 1).toUpperCase() + p.getName().substring(1);
        JMethod getter = dc.method(JMod.PUBLIC, getType(p.getType()), getterName);
        JBlock body = getter.body();
        body._return(JExpr.ref(p.getName()));
    }

    private void generateToString() {
        JMethod method = dc.method(JMod.PUBLIC, String.class, "toString");
        method.annotate(Override.class);
        JInvocation invocation = cm.ref(Objects.class).staticInvoke("toStringHelper").arg(JExpr._this());
        for (Property p : properties) {
            invocation = invocation.invoke("add").arg(p.getName()).arg(JExpr.ref(p.getName()));
        }
        method.body()._return(invocation.invoke("toString"));
    }

    private void generateHashCode() {
        JMethod method = dc.method(JMod.PUBLIC, cm.INT, "hashCode");
        method.annotate(Override.class);
        JInvocation invocation = cm.ref(Objects.class).staticInvoke("hashCode");
        for (Property p : properties) {
            invocation.arg(JExpr.ref(p.getName()));
        }
        method.body()._return(invocation);
    }

    private void generateConstructor() throws ClassNotFoundException {
        JMethod constructor = dc.constructor(JMod.PUBLIC);
        constructor.annotate(JsonCreator.class);
        for (Property p : properties) {
            JVar param = constructor.param(getType(p.getType()), p.getName());
            p.setConstrParam(param);
            param.annotate(JsonProperty.class).param("value", p.getName());
        }
        JBlock body = constructor.body();
        for (Property p : properties) {
            JFieldRef lhs = JExpr.refthis(p.getName());
            JInvocation invocation = cm.ref(Preconditions.class).staticInvoke("checkNotNull");
            invocation.arg(p.getConstrParam());
            body.assign(lhs, invocation);
        }
    }

}
