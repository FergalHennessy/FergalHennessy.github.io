package chocopy.pa2;

import java.util.Map;

import chocopy.common.analysis.SymbolTable;
import chocopy.common.analysis.types.ClassValueType;
import chocopy.common.analysis.types.Type;
import chocopy.common.astnodes.Program;

/** Top-level class for performing semantic analysis. */
public class StudentAnalysis {

    /** Perform semantic analysis on PROGRAM, adding error messages and
     *  type annotations. Provide debugging output iff DEBUG. Returns modified
     *  tree. */
    public static Program process(Program program, boolean debug) {
        if (program.hasErrors()) {
            return program;
        }

        DeclarationAnalyzer declarationAnalyzer =
            new DeclarationAnalyzer(program.errors);
        program.dispatch(declarationAnalyzer);
        SymbolTable<Type> globalSym =
            declarationAnalyzer.getGlobals();
        Map<String, ClassValueType> allClassNames = declarationAnalyzer.getClassNames();

        if (!program.hasErrors()) {
            TypeChecker typeChecker =
                new TypeChecker(globalSym, program.errors);
            typeChecker.setClassNamesMap(allClassNames);
            program.dispatch(typeChecker);
        }

        return program;
    }
}
