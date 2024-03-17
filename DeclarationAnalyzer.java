package chocopy.pa2;

import static chocopy.common.analysis.types.Type.BOOL_TYPE;
import static chocopy.common.analysis.types.Type.INT_TYPE;
import static chocopy.common.analysis.types.Type.NONE_TYPE;
import static chocopy.common.analysis.types.Type.OBJECT_TYPE;
import static chocopy.common.analysis.types.Type.STR_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chocopy.common.analysis.AbstractNodeAnalyzer;
import chocopy.common.analysis.SymbolTable;
import chocopy.common.analysis.types.ClassValueType;
import chocopy.common.analysis.types.FuncType;
import chocopy.common.analysis.types.Type;
import chocopy.common.analysis.types.ValueType;
import chocopy.common.astnodes.ClassDef;
import chocopy.common.astnodes.Declaration;
import chocopy.common.astnodes.Errors;
import chocopy.common.astnodes.FuncDef;
import chocopy.common.astnodes.Identifier;
import chocopy.common.astnodes.Program;
import chocopy.common.astnodes.TypedVar;
import chocopy.common.astnodes.VarDef;

/**
 * Analyzes declarations to create a top-level symbol table.
 */
public class DeclarationAnalyzer extends AbstractNodeAnalyzer<Type> {

    /** Current symbol table.  Changes with new declarative region. */
    private SymbolTable<Type> sym = new SymbolTable<>();
    /** Global symbol table. */
    private final SymbolTable<Type> globals = sym;
    /** Receiver for semantic error messages. */
    private final Errors errors;
    /** Store all classes as <ClassValueType, parent ClassValueType> */
    private static Map<ClassValueType, ClassValueType> classParents = new HashMap<>();
    /** Store all classes as <String name, ClassValueType>*/
    private static Map<String, ClassValueType> classNames = new HashMap<>();

    /** A new declaration analyzer sending errors to ERRORS0. */
    public DeclarationAnalyzer(Errors errors0) {
        errors = errors0;
    }

    public Map<String, ClassValueType> getClassNames(){
        return classNames;
    }


    public SymbolTable<Type> getGlobals() {
        return globals;
    }

    @Override
    public Type analyze(Program program) {
        /** From Chocopy Spec: predefined global functions */
        sym.put("print", new FuncType(new ArrayList<ValueType>
                                        (Arrays.asList(ValueType.INT_TYPE)),
                                        Type.NONE_TYPE));
        sym.put("print", new FuncType(new ArrayList<ValueType>
                                        (Arrays.asList(ValueType.BOOL_TYPE)),
                                        Type.NONE_TYPE));
        sym.put("print", new FuncType(new ArrayList<ValueType>
                                        (Arrays.asList(ValueType.STR_TYPE)),
                                        Type.NONE_TYPE));
        sym.put("print", new FuncType(new ArrayList<ValueType>
                                        (Arrays.asList(ValueType.OBJECT_TYPE)),
                                        Type.NONE_TYPE));
        sym.put("input", Type.STR_TYPE);
        /** T0D0: len function */


        classParents.put(INT_TYPE, OBJECT_TYPE);
        classParents.put(STR_TYPE, OBJECT_TYPE);
        classParents.put(BOOL_TYPE, OBJECT_TYPE);
        classParents.put(OBJECT_TYPE, null);
        classNames.put("int", INT_TYPE);
        classNames.put("str", STR_TYPE);
        classNames.put("bool", BOOL_TYPE);
        classNames.put("object", OBJECT_TYPE);

        for (Declaration decl : program.declarations) {
            Identifier id = decl.getIdentifier();
            String name = id.name;

            Type type = decl.dispatch(this);

            if (type == null) {
                continue;
            }
            if (sym.declares(name)) {
                errors.semError(id,
                                "Duplicate declaration of identifier in same "
                                + "scope: %s",
                                name);
            } else {
                sym.put(name, type);
            }
        }

        return null;
    }

    //Class Definition Symbol Table
    @Override
    public Type analyze(ClassDef classDef) {
        Identifier classId = classDef.getIdentifier();
        String className = classId.name;
        Identifier parentClassId = classDef.superClass;
        String parentClassName = parentClassId != null ? parentClassId.name : null;
        //the parent of our classdef symbol table is the current symbol table sym
        sym = new SymbolTable<>(sym);
        ClassValueType CLASS_TYPE = new ClassValueType(className);

        for(Declaration decl : classDef.declarations) {
            Identifier id = decl.getIdentifier();
            String name = id.name;
            Type type = decl.dispatch(this);

            if (type == null) {
                continue;
            }
            if (globals.declares(name)) {
                errors.semError(id, "Cannot shadow class name: %s", name);
            }
            if (sym.declares(name)) {
                errors.semError(id,
                                "Duplicate declaration of identifier in same "
                                + "scope: %s",
                                name);
            } else {
                sym.put(name, type);
            }
        }
        classNames.put(className, CLASS_TYPE);
        if(parentClassName == null){
            classParents.put(CLASS_TYPE, OBJECT_TYPE);
        }else{
            /** Hardcoded error messages for bad parents */
            if(parentClassName.equals("int") || parentClassName.equals("bool") || parentClassName.equals("string")){
                errors.semError(parentClassId,
                    "Cannot extend special class: %s", parentClassName
                );
            }else if (globals.declares(parentClassName) && !classNames.containsKey(parentClassName)){
                errors.semError(parentClassId,
                    "Super-class must be a class: %s", parentClassName
                );
            }else if(!classNames.containsKey(parentClassName)){
                errors.semError(parentClassId,
                    "Super-class not defined: %s", parentClassName
                );
            }else{
                classParents.put(CLASS_TYPE, classNames.get(parentClassName));
            }
        }
        sym = sym.getParent();
        return CLASS_TYPE;
    }
    @Override
    public Type analyze(FuncDef funcDef){
        //the parent of our funcDef symbol table is the current symbol table, sym
        sym = new SymbolTable<>(sym);
        ValueType funcReturnType = ValueType.annotationToValueType(funcDef.returnType);
        FuncType funcDefType;
        if(funcDef.params.isEmpty()){
            funcDefType = new FuncType(funcReturnType);
        }else{
            List<ValueType> params = new ArrayList<ValueType>();
            for(TypedVar typedVar : funcDef.params){
                Identifier id = typedVar.identifier;
                String name = id.name;
                if (classNames.containsKey(name)){
                    errors.semError(id, "Cannot shadow class name: %s", name);
                }
                ValueType tvarType = ValueType.annotationToValueType(typedVar.type);
                sym.put(typedVar.identifier.name, tvarType);
                params.add(tvarType);
            }
            funcDefType = new FuncType(params, funcReturnType);
        }
        
        

        for(Declaration decl : funcDef.declarations) {
            Identifier id = decl.getIdentifier();
            String name = id.name;
            Type type = decl.dispatch(this);

            if (type == null) {
                continue;
            }
            if (classNames.containsKey(name)) {
                errors.semError(id, "Cannot shadow class name: %s", name);
            }
            if (sym.declares(name)) {
                errors.semError(id,
                                "Duplicate declaration of identifier in same "
                                + "scope: %s",
                                name);
            } else {
                sym.put(name, type);
            }
        }
        sym = sym.getParent();
        return funcDefType;
    }

 

    @Override
    public Type analyze(VarDef varDef) {
        return ValueType.annotationToValueType(varDef.var.type);
    }


}
