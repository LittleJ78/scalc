CREATE TABLE IF NOT EXISTS expression_unit(
	expression_unit_id INT PRIMARY KEY AUTO_INCREMENT,
	project_id VARCHAR(30) NOT NULL,
	type_of_operand VARCHAR(10) NOT NULL,
	expression_unit_name VARCHAR(30) NOT NULL,
	default_expression VARCHAR(255) NOT NULL,
	expression_result VARCHAR(10) NOT NULL
	);
