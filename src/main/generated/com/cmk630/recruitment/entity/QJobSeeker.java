package com.cmk630.recruitment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobSeeker is a Querydsl query type for JobSeeker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobSeeker extends EntityPathBase<JobSeeker> {

    private static final long serialVersionUID = -139572424L;

    public static final QJobSeeker jobSeeker = new QJobSeeker("jobSeeker");

    public final ListPath<Application, QApplication> applies = this.<Application, QApplication>createList("applies", Application.class, QApplication.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public QJobSeeker(String variable) {
        super(JobSeeker.class, forVariable(variable));
    }

    public QJobSeeker(Path<? extends JobSeeker> path) {
        super(path.getType(), path.getMetadata());
    }

    public QJobSeeker(PathMetadata metadata) {
        super(JobSeeker.class, metadata);
    }

}

