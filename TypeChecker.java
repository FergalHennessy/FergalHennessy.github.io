package chocopy.pa2;

import chocopy.common.analysis.AbstractNodeAnalyzer;
import chocopy.common.analysis.SymbolTable;
import chocopy.common.analysis.types.ClassValueType;
import chocopy.common.analysis.types.FuncType;
import chocopy.common.analysis.types.ListValueType;
import chocopy.common.analysis.types.Type;
import chocopy.common.analysis.types.ValueType;
import chocopy.common.astnodes.BinaryExpr;
import chocopy.common.astnodes.BooleanLiteral;
import chocopy.common.astnodes.CallExpr;
import chocopy.common.astnodes.Declaration;
import chocopy.common.astnodes.Errors;
import chocopy.common.astnodes.ExprStmt;
import chocopy.common.astnodes.Identifier;
import chocopy.common.astnodes.IntegerLiteral;
import chocopy.common.astnodes.ListExpr;
import chocopy.common.astnodes.Node;
import chocopy.common.astnodes.Program;
import chocopy.common.astnodes.ReturnStmt;
import chocopy.common.astnodes.Stmt;
import chocopy.common.astnodes.StringLiteral;
import chocopy.common.astnodes.UnaryExpr;
import chocopy.common.astnodes.IfExpr;
import chocopy.common.astnodes.Expr;
import chocopy.common.astnodes.IfStmt;
import chocopy.common.astnodes.AssignStmt;
import chocopy.common.astnodes.NoneLiteral;
import chocopy.common.astnodes.VarDef;
import chocopy.common.astnodes.TypeAnnotation;
import chocopy.common.astnodes.TypedVar;
import chocopy.common.astnodes.ClassType;
import chocopy.common.astnodes.ListType;
import chocopy.common.astnodes.WhileStmt;
import chocopy.common.astnodes.IndexExpr;
import chocopy.common.astnodes.ForStmt;
import chocopy.common.astnodes.FuncDef;

import static chocopy.common.analysis.types.Type.BOOL_TYPE;
import static chocopy.common.analysis.types.Type.EMPTY_TYPE;
import static chocopy.common.analysis.types.Type.INT_TYPE;
import static chocopy.common.analysis.types.Type.OBJECT_TYPE;
import static chocopy.common.analysis.types.Type.STR_TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListCellRenderer;

import static chocopy.common.analysis.types.Type.NONE_TYPE;

/** Analyzer that performs ChocoPy type checks on all nodes.  Applied after
 *  collecting declarations. */
public class TypeChecker extends AbstractNodeAnalyzer<Type> {

    /** The current symbol table (changes depending on the function
     *  being analyzed). */
    private SymbolTable<Type> sym;
    /** Collector for errors. */
    private Errors errors;

    /** Store all classes as <String name, ClassValueType>*/
    public static Map<String, ClassValueType> classNames = new HashMap<>();

    public void setClassNamesMap(Map<String, ClassValueType> newClassNames){
        classNames = newClassNames;
    }


    /** Creates a type checker using GLOBALSYMBOLS for the initial global
     *  symbol table and ERRORS0 to receive semantic errors. */
    public TypeChecker(SymbolTable<Type> globalSymbols, Errors errors0) {
        sym = globalSymbols;
        errors = errors0;
    }

    /** Inserts an error message in NODE if there isn't one already.
     *  The message is constructed with MESSAGE and ARGS as for
     *  String.format. */
    private void err(Node node, String message, Object... args) {
        errors.semError(node, message, args);
    }

    /** pass through declarations twice in case defined after use in stmt */
    @Override
    public Type analyze(Program program) {
        for(Declaration decl : program.declarations) {
            decl.dispatch(this);
        }
        for (Declaration decl : program.declarations) {
            decl.dispatch(this);
        }
        for (Stmt stmt : program.statements) {
            stmt.dispatch(this);
        }
        return null;
    }

    @Override
    public Type analyze(ExprStmt s) {
        s.expr.dispatch(this);
        return null;
    }

    @Override
    /**Literals */
    public Type analyze(IntegerLiteral i) {
        return i.setInferredType(Type.INT_TYPE);
    }
    public Type analyze(BooleanLiteral b){
        return b.setInferredType(Type.BOOL_TYPE);
    }
    public Type analyze(StringLiteral s) {
        return s.setInferredType(Type.STR_TYPE);
    }
    public Type analyze(NoneLiteral n) {
        return n.setInferredType(Type.NONE_TYPE);
    }
    
    @Override
    public Type analyze(UnaryExpr e) {
        Type t = e.operand.dispatch(this);
        switch (e.operator){
            case "-": 
                if (INT_TYPE.equals(t)){
                    return e.setInferredType(INT_TYPE);
                } else{
                    err(e, "Cannot apply operator `%s` on type `%s`", e.operator, t);
                    return e.setInferredType(INT_TYPE);
                }
            case "not":
                if(BOOL_TYPE.equals(t)){
                    return e.setInferredType(BOOL_TYPE);
                }else{
                    err(e, "Cannot apply operator `%s` on type `%s`", e.operator, t);
                    return e.setInferredType(BOOL_TYPE);
                }
            default:
            return e.setInferredType(OBJECT_TYPE);
        }
    }

    @Override
    public Type analyze(IfExpr e) {
        Type condType = e.condition.dispatch(this);
        if (!BOOL_TYPE.equals(condType)) {
            err(e.condition, "Condition of an if expression must be a boolean, found type `%s`", condType);
        }

        Type thenBranchType = e.thenExpr.dispatch(this);
        Type elseBranchType = e.elseExpr.dispatch(this);

        if (thenBranchType.equals(elseBranchType)) {
            return e.setInferredType(thenBranchType);
        } else {
            err(e, "Branches of an if expression must have the same type, found `%s` and `%s`", thenBranchType, elseBranchType);
            return e.setInferredType(thenBranchType);
        }
    }

    @Override
    public Type analyze(BinaryExpr e) {
        Type t1 = e.left.dispatch(this);
        Type t2 = e.right.dispatch(this);

        switch (e.operator) {
        
        case "-":
        case "*":
        case "//":
        case "%":
            if (INT_TYPE.equals(t1) && INT_TYPE.equals(t2)) {
                return e.setInferredType(INT_TYPE);
            } else {
                err(e, "Cannot apply operator `%s` on types `%s` and `%s`",
                    e.operator, t1, t2);
                return e.setInferredType(INT_TYPE);
            }
        /** from PA2 spec: infer int when args have >=1 int, otherwise, infer object*/
        case "+":
            if (INT_TYPE.equals(t1) && INT_TYPE.equals(t2)){
                return e.setInferredType(INT_TYPE);
            } else if (STR_TYPE.equals(t1) && STR_TYPE.equals(t2)){
                return e.setInferredType(STR_TYPE);
            } else if(t1.isListType() && t2.isListType()){
                Type leastCommon = leastCommonAncestor(t1.elementType(), t2.elementType());
                return e.setInferredType(new ListValueType(leastCommon));
            }else{
                err(e, "Cannot apply operator `%s` on types `%s` and `%s`",
                    e.operator, t1, t2);
                if(INT_TYPE.equals(t1) || INT_TYPE.equals(t2)){
                    return e.setInferredType(INT_TYPE);
                }
                return e.setInferredType(OBJECT_TYPE);
            }
        case "<":
        case "<=":
        case ">":
        case ">=":
        case "==":
        case "!=":
            return e.setInferredType(BOOL_TYPE);
        case "and":
        case "or":
            if (BOOL_TYPE.equals(t1) && BOOL_TYPE.equals(t2)) {
                return e.setInferredType(BOOL_TYPE);
            } else {
                err(e, "Logical operator `%s` applied to non-boolean types `%s` and `%s`",
                    e.operator, t1, t2);
                return e.setInferredType(BOOL_TYPE);
            }
        default:
            return e.setInferredType(OBJECT_TYPE);
        }
    }


    @Override
    public Type analyze(Identifier id) {
        String varName = id.name;
        Type varType = sym.get(varName);

        if (varType != null && varType.isValueType()) {
            return id.setInferredType(varType);
        }

        err(id, "Not a variable: %s", varName);
        return id.setInferredType(ValueType.OBJECT_TYPE);
    }

    @Override
    public Type analyze(IfStmt stmt) {
        Type condType = stmt.condition.dispatch(this);
        if (!BOOL_TYPE.equals(condType)) {
            err(stmt.condition, "Condition of an if statement must be of type bool, found type `%s`", condType);
        }

        for (Stmt thenStmt : stmt.thenBody) {
            thenStmt.dispatch(this);
        }

        if (stmt.elseBody != null) {
            for (Stmt elseStmt : stmt.elseBody) {
                elseStmt.dispatch(this);
            }
        }
        return null;
    }

    @Override
    public Type analyze(AssignStmt stmt) {
        Type valueType = stmt.value.dispatch(this);
        for (Expr target : stmt.targets) {
            if (!(target instanceof Identifier)) {
                err(target, "Invalid assignment target");
                continue;
            }
            Identifier id = (Identifier) target;
            Type targetType = sym.get(id.name);

            if (targetType == null) {
                err(id, "Variable '%s' not declared", id.name);
                continue;
            }
            if (valueType instanceof ListValueType || valueType.equals(Type.NONE_TYPE)) {
                id.setInferredType(valueType);
            } else if (!isCompatible(valueType, targetType)) {
                err(id, "Type mismatch in assignment: cannot assign %s to %s", valueType, targetType);
            } else {
                id.setInferredType(targetType);
            }
        }
        return null;
    }

    @Override
    public Type analyze(ListExpr listExpr) {
        if (listExpr.elements.isEmpty()) {
            return listExpr.setInferredType(Type.EMPTY_TYPE);
        }

        Type commonType = null;
        for (Expr elem : listExpr.elements) {
            Type elemType = elem.dispatch(this);
            if (commonType == null) {
                commonType = elemType;
            } else if (!commonType.equals(elemType)) {
                commonType = Type.OBJECT_TYPE;
                break;
            }
        }

        return listExpr.setInferredType(new ListValueType(commonType));
    }

    /** The smallest class that contains both t1 and t2 (NOT COMPLETE) */
    public Type leastCommonAncestor(Type t1, Type t2){
        if(t1.equals(EMPTY_TYPE)) return t2;
        if(t2.equals(EMPTY_TYPE)) return t1;
        if(t1.equals(t2)){
            return t1;
        }
        return OBJECT_TYPE;
    }

    public boolean isCompatible(Type valueType, Type declaredType) {
        if (declaredType.equals(OBJECT_TYPE)) {
            return true;
        }
        return valueType.equals(declaredType);
    }
    

    @Override
    public Type analyze(VarDef varDef) {
        Type declaredType = resolveTypeNameToType(varDef.var.type);

        if (varDef.value != null) {
            Type valueType = varDef.value.dispatch(this);
            varDef.value.setInferredType(valueType);
        }
        sym.put(varDef.var.identifier.name, declaredType);
        return null;
    }

    public ClassValueType resolveTypeNameToType(TypeAnnotation typeAnnotation) {
        String typeName = getTypeNameFromAnnotation(typeAnnotation);
        
        return classNames.get(typeName);
        /*switch (typeName) {
            case "int":
                return INT_TYPE;
            case "bool":
                return BOOL_TYPE;
            case "str":
                return STR_TYPE;
            default:
                return OBJECT_TYPE;
        }**/
    }

    public String getTypeNameFromAnnotation(TypeAnnotation typeAnnotation) {
        if (typeAnnotation instanceof ClassType) {
            ClassType classType = (ClassType) typeAnnotation;
            return classType.className;
        } else if (typeAnnotation instanceof ListType) {
            ListType listType = (ListType) typeAnnotation;
            String elementTypeName = getTypeNameFromAnnotation(listType.elementType);
            return "[" + elementTypeName + "]";
        }
        return null;
    }


    @Override
    public Type analyze(WhileStmt stmt) {
        Type condType = stmt.condition.dispatch(this);
        if (!BOOL_TYPE.equals(condType)) {
            err(stmt.condition, "Condition of a while statement must be of type bool, found type `%s`", condType);
        } else {
            stmt.condition.setInferredType(BOOL_TYPE);
        }
        for (Stmt bodyStmt : stmt.body) {
            bodyStmt.dispatch(this);
        }
        return null;
    }

   @Override
    public Type analyze(IndexExpr e) {
        Type listType = e.list.dispatch(this);
        e.index.dispatch(this);

        if (listType instanceof ListValueType || listType.equals(STR_TYPE)) {
            if (listType.equals(STR_TYPE)) {
                return e.setInferredType(STR_TYPE);
            } else {
                ListValueType lvType = (ListValueType) listType;
                return e.setInferredType(lvType.elementType());
            }
        } else {
            err(e.list, "Indexing operation not supported on type `%s`", listType);
            return e.setInferredType(OBJECT_TYPE);
        }
    }

    @Override
    public Type analyze(ForStmt stmt) {
        Type iterableType = stmt.iterable.dispatch(this);
        
        if (iterableType.equals(STR_TYPE)) {
            Type elementType = STR_TYPE;
            stmt.identifier.setInferredType(elementType);
            for (Stmt bodyStmt : stmt.body) {
                bodyStmt.dispatch(this);
            }
        } else if (iterableType instanceof ListValueType) {
            ListValueType listValueType = (ListValueType) iterableType;
            Type elementType = listValueType.elementType;
            stmt.identifier.setInferredType(elementType);
            for (Stmt bodyStmt : stmt.body) {
                bodyStmt.dispatch(this);
            }
        } else {
            err(stmt.iterable, "The iterable in a for loop must be a string or a list, found type `%s`", iterableType);
        }
        return null;
    }

    /** place all local variables into the symbol table,
     *  then typecheck the inner declarations, statements */
    @Override
    public Type analyze(FuncDef funcDef){
        sym = new SymbolTable<>(sym);
        ValueType returnType = NONE_TYPE;
        if(funcDef.returnType != null){
            returnType = resolveTypeNameToType(funcDef.returnType);
        }
        sym.put(funcDef.name.name, returnType);
        List<ValueType> params = new ArrayList<ValueType>();
        for(TypedVar param : funcDef.params){
            sym.put(param.identifier.name, resolveTypeNameToType(param.type));
            params.add(resolveTypeNameToType(param.type));
        }

        /** two passes through declarations in case a stmt used 
         * in a declaration calls a declaration defined later */
        for (Declaration decl : funcDef.declarations) {
            decl.dispatch(this);
        }
        for (Declaration decl : funcDef.declarations) {
            decl.dispatch(this);
        }
        for (Stmt stmt : funcDef.statements) {
            stmt.dispatch(this);
        }
        
        FuncType funcDefType = new FuncType(params, returnType);
        sym = sym.getParent();
        sym.put(funcDef.name.name, funcDefType);
        return null;
    }

    //I just assume that the callexpr name is a function, will this cause errors?
    @Override
    public Type analyze(CallExpr callExpr){
        String functionName = callExpr.function.name;
        FuncType functionType = (FuncType) sym.get(functionName);

        //first assign types, later make sure they type check
        for(int i = 0; i < callExpr.args.size(); i++){
            if(i >= functionType.parameters.size()){
                err(callExpr, "This call expression has too many args! (placeholder)");
                break;
            }
            callExpr.args.get(i).setInferredType(functionType.parameters.get(i));
        }

        callExpr.function.setInferredType(functionType);
        
        return callExpr.setInferredType(functionType.returnType);
    }

    @Override
    public Type analyze(ReturnStmt returnStmt){
        returnStmt.value.dispatch(this);
        return null;
    }
}
