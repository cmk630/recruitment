package com.cmk630.recruitment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobDescription is a Querydsl query type for JobDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobDescription extends EntityPathBase<JobDescription> {

    private static final long serialVersionUID = -904123959L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobDescription jobDescription = new QJobDescription("jobDescription");

    public final ListPath<Application, QApplication> applications = this.<Application, QApplication>createList("applications", Application.class, QApplication.class, PathInits.DIRECT2);

    public final QCompany company;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public QJobDescription(String variable) {
        this(JobDescription.class, forVariable(variable), INITS);
    }

    public QJobDescription(Path<? extends JobDescription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobDescription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobDescription(PathMetadata metadata, PathInits inits) {
        this(JobDescription.class, metadata, inits);
    }

    public QJobDescription(Class<? extends JobDescription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

