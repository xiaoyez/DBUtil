package com.xiaoye.classgenerator.defenition;

import com.xiaoye.classgenerator.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author xiaoye
 * @create 2021-03-16 1:27
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstarctAnnotatedDefinition extends AbstractDefinition{

    protected List<AnnotationDefinition> annotationDefinitions;

    public void addAnnotationDefinition(AnnotationDefinition annotationDefinition)
    {
        if (annotationDefinitions == null)
            annotationDefinitions = new ArrayList<>();
        annotationDefinitions.add(annotationDefinition);
    }
}
