package com.github.balintrudas.commentparser.util;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.nio.file.Paths;
import java.util.Optional;

public class NodeUtil {

    /**
     * Chech that the given node is in the configured directory.
     * @param configuration
     * @param node
     * @return Boolean
     */
    public static Boolean isInBoundaries(Configuration configuration, Node node) {
        Optional<CompilationUnit> compUnitOpt = node.findCompilationUnit();
        if (compUnitOpt.isPresent()) {
            Optional<CompilationUnit.Storage> storageOpt = compUnitOpt.get().getStorage();
            storageOpt.ifPresent(CompilationUnit.Storage::getDirectory);
            if (storageOpt.isPresent()) {
                return configuration.getBaseDirs().stream().anyMatch(s -> storageOpt.get().getDirectory().startsWith(Paths.get(s)));
            }
        }
        return false;
    }

    public static Optional<AnnotationExpr> getAnnotation(Node node, String annotationName) {


        if (node instanceof MethodDeclaration && ((MethodDeclaration)node).getAnnotationByName(annotationName).isPresent()) {
            return ((MethodDeclaration)node).getAnnotationByName(annotationName);
        }

        if(node instanceof Comment && ((Comment)node).getCommentedNode().isPresent()
                && ((Comment)node).getCommentedNode().get().getParentNode().isPresent()
                && ((Comment)node).getCommentedNode().get().getParentNode().get() instanceof ClassOrInterfaceDeclaration){
            return ((ClassOrInterfaceDeclaration) ((Comment)node).getCommentedNode().get().getParentNode().get()).getAnnotationByName(annotationName);
        }

        if (node.getParentNode().isPresent() && node.getParentNode().get() instanceof ClassOrInterfaceDeclaration) {
            return ((ClassOrInterfaceDeclaration) node.getParentNode().get()).getAnnotationByName(annotationName);
        }

        return Optional.empty();
    }

    /**
     * Try to get comment for the given node (like method, class, etc).
     * @param node
     * @return Comment
     */
    public static Optional<Comment> getComment(Node node) {

        if (node instanceof MethodDeclaration && node.getComment().isPresent()) {
            return node.getComment();
        }

        if(node instanceof Comment && ((Comment)node).getCommentedNode().isPresent()
                && ((Comment)node).getCommentedNode().get().getParentNode().isPresent()
                && ((Comment)node).getCommentedNode().get().getParentNode().get() instanceof ClassOrInterfaceDeclaration){
            return (((Comment)node).getCommentedNode().get().getParentNode().get()).getComment();
        }

        if (node.getParentNode().isPresent() && node.getParentNode().get() instanceof ClassOrInterfaceDeclaration) {
            return (node.getParentNode().get()).getComment();
        }

        return Optional.empty();
    }

}
