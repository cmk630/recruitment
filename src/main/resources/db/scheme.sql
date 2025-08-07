CREATE TABLE company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    company_registration_number VARCHAR(12) NOT NULL UNIQUE
);

CREATE TABLE job_seeker (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE job_description (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    company_id BIGINT,
    FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE
);

CREATE TABLE application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_seeker_id BIGINT,
    company_id BIGINT,
    job_description_id BIGINT,
    FOREIGN KEY (job_seeker_id) REFERENCES job_seeker(id) ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES company(id),
    FOREIGN KEY (job_description_id) REFERENCES job_description(id) ON DELETE CASCADE,
    UNIQUE (job_seeker_id, job_description_id)
);
