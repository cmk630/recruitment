package com.cmk630.recruitment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = 1762585075L;

    public static final QCompany company = new QCompany("company");

    public final StringPath companyRegistrationNumber = createString("companyRegistrationNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<JobDescription, QJobDescription> jobDescriptions = this.<JobDescription, QJobDescription>createList("jobDescriptions", JobDescription.class, QJobDescription.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

