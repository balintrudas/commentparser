package com.github.balintrudas.commentparser.marker.group.annotation;

import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.marker.group.GroupMarkerElementParser;
import com.github.balintrudas.commentparser.util.NodeUtil;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used for collect all annotation based group marker.
 */
public class GroupAnnotationParser implements GroupMarkerElementParser<AnnotationElement> {

    private Configuration configuration;

    public GroupAnnotationParser(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public List<AnnotationElement> parse(Node node) {
        List<AnnotationElement> annotationElements = new ArrayList<>();
        this.configuration.getGroupMarkerConfiguration().getAnnotations().forEach(aClass -> {
            Optional<AnnotationExpr> annotationExprOpt = NodeUtil.getAnnotation(node, aClass);
            if (annotationExprOpt.isPresent()) {
                List<String> groupNames = this.getParameter(
                        annotationExprOpt.get(),
                        this.configuration.getGroupMarkerConfiguration().getDefaultGroupNameKey(),
                        Collections.singletonList(this.configuration.getGroupMarkerConfiguration().getDefaultGroupName())
                );
                List<String> groupInherits = this.getParameter(
                        annotationExprOpt.get(),
                        this.configuration.getGroupMarkerConfiguration().getDefaultGroupInheritKey(),
                        Stream.of(this.configuration.getGroupMarkerConfiguration().getDefaultGroupInherit())
                                .filter(Objects::nonNull).collect(Collectors.toList())
                );
                AnnotationElement annotationElement = new AnnotationElement(aClass, groupNames, groupInherits);
                annotationElement.setNodeDeclaration(node);
                annotationElement.setDescription(this.getDescription(annotationExprOpt));
                annotationElement.setRange(annotationExprOpt.get().getRange().orElse(null));
                annotationElements.add(annotationElement);
            }
        });
        return annotationElements;
    }

    private List<String> getParameter(AnnotationExpr annotationExpr, String parameterName, List<String> defaultValue) {
        try {
            if (annotationExpr instanceof SingleMemberAnnotationExpr) {
                return this.parseExpression(annotationExpr.asSingleMemberAnnotationExpr().getMemberValue());
            } else if (annotationExpr instanceof NormalAnnotationExpr) {
                List<String> valuesOpt = annotationExpr.asNormalAnnotationExpr().getPairs().stream()
                        .filter(memberValuePair -> memberValuePair.getName().asString().equals(parameterName)
                        ).flatMap(memberValuePair -> this.parseExpression(memberValuePair.getValue()).stream())
                        .collect(Collectors.toList());
                return !valuesOpt.isEmpty() ? valuesOpt : defaultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    private List<String> parseExpression(Expression expression) {
        if (expression instanceof StringLiteralExpr) {
            return Collections.singletonList(expression.asStringLiteralExpr().getValue());
        } else if (expression instanceof NameExpr) {
            ResolvedValueDeclaration resolvedValueDeclaration = expression.asNameExpr().resolve();
            if (resolvedValueDeclaration instanceof JavaParserFieldDeclaration) {
                JavaParserFieldDeclaration value = (JavaParserFieldDeclaration) resolvedValueDeclaration.asField();
                FieldDeclaration field = value.getWrappedNode();
                Optional<Expression> initializerOpt = field.getVariables().get(0).getInitializer();
                if (initializerOpt.isPresent()) {
                    return Collections.singletonList(initializerOpt.get().asStringLiteralExpr().getValue());
                }
            }
            return Collections.singletonList(expression.asNameExpr().resolve().getName());
        } else if (expression instanceof ArrayInitializerExpr) {
            return expression.asArrayInitializerExpr().getValues().stream().
                    flatMap(expression1 -> parseExpression(expression1).stream()).collect(Collectors.toList());
        }
        return Collections.singletonList(this.configuration.getGroupMarkerConfiguration().getDefaultGroupName());
    }

    private String getDescription(Optional<AnnotationExpr> annotationExprOpt){
        if(annotationExprOpt.get().getParentNode().isPresent() && annotationExprOpt.get().getParentNode().get().getComment().isPresent()){
            if(annotationExprOpt.get().getParentNode().get().getComment().get() instanceof JavadocComment){
                Javadoc javaDoc = ((JavadocComment) annotationExprOpt.get().getParentNode().get().getComment().get()).parse();
                if(javaDoc != null){
                    return javaDoc.getDescription().toText();
                }
            }else{
                return annotationExprOpt.get().getParentNode().get().getComment().get().getContent();
            }
        }
        return null;
    }

}
