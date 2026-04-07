CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role (
    user_id BIGINT NOT NULL,
    role VARCHAR(32) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS projects (
    project_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_projects_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS expression_units (
    expression_unit_id SERIAL PRIMARY KEY,
    type_of_operand VARCHAR(64) NOT NULL,
    expression_unit_name VARCHAR(255) NOT NULL,
    default_expression TEXT NOT NULL,
    expression_result VARCHAR(255),
    project_id INT NOT NULL,
    watch_list BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_expression_units_project FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parametrised_expressions (
    parametrised_expression_id SERIAL PRIMARY KEY,
    expression_unit_id INT NOT NULL UNIQUE,
    CONSTRAINT fk_parametrised_expressions_expression_unit FOREIGN KEY (expression_unit_id)
        REFERENCES expression_units (expression_unit_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parameters_for_expressions (
    parameters_for_expressions_id SERIAL PRIMARY KEY,
    parameter VARCHAR(255) NOT NULL,
    parametrised_expression_id INT NOT NULL,
    CONSTRAINT fk_parameters_for_expressions_parametrised_expression FOREIGN KEY (parametrised_expression_id)
        REFERENCES parametrised_expressions (parametrised_expression_id) ON DELETE CASCADE
);
