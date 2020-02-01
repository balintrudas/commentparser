package com.github.balintrudas.commentparser;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.visitor.CommentVisitor;
import com.github.balintrudas.commentparser.visitor.MethodVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Scanner {

    private Configuration configuration;
    private CommentStore commentStore = new CommentStore();
    private Path currentPath;

    public Scanner() {
        this.configuration = new Configuration();
    }

    public Scanner(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Process all java file in the given directory and build environment.a CommentStore.
     *
     * @return CommentStore
     * @throws IOException
     */
    public CommentStore parse() throws IOException {
        List<Path> files = new ArrayList<>();
        this.configuration.getBaseDirs().forEach(s -> {
            try {
                Stream<Path> walk = Files.walk(Paths.get(s));
                files.addAll(walk.filter(path -> path.toString().endsWith(".java")).collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        List<TypeSolver> typeSolvers = new ArrayList<>();
        typeSolvers.add(new ReflectionTypeSolver());
        this.getConfiguration().getSourceRoots().stream()
                .filter(s -> s != null && !s.isEmpty())
                .forEach(s -> typeSolvers.add(new JavaParserTypeSolver(new File(s))));

        TypeSolver[] typeSolversArray = new TypeSolver[typeSolvers.size()];
        typeSolversArray = typeSolvers.toArray(typeSolversArray);
        TypeSolver myTypeSolver = new CombinedTypeSolver(typeSolversArray);

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(myTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        MethodVisitor methodVisitor = new MethodVisitor();
        CommentVisitor commentVisitor = new CommentVisitor();

        //Process each java file
        files.forEach(path -> {
            try {
                this.currentPath = path;
                CompilationUnit compilationUnit = StaticJavaParser.parse(path);
                compilationUnit.accept(methodVisitor, this);
                if (!this.configuration.getCommentMarkerConfiguration().getIncludeOnlyWithinMethods()) {
                    compilationUnit.accept(commentVisitor, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Apply inheritance for the created comment collections
        this.commentStore.processInheritance();

        this.commentStore.sort();

        return this.commentStore;

    }

}
