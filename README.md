Code Exercise 106
- 

Task description
-
BIG COMPANY is employing a lot of employees. Company would like to analyze its organizational
structure and identify potential improvements. Board wants to make sure that every manager earns
at least 20% more than the average salary of its direct subordinates, but no more than 50% more
than that average. Company wants to avoid too long reporting lines, therefore we would like to
identify all employees which have more than 4 managers between them and the CEO.
You are given a CSV file which contains information about all the employees.

Each line represents an employee (CEO included). CEO has no manager specified. Number of rows
can be up to 1000.

Write a simple program which will read the file and report:
- which managers earn less than they should, and by how much
- which managers earn more than they should, and by how much
- which employees have a reporting line which is too long, and by how much


Assumptions made:
-
- Duplicated entries are allowed in the file;
- Order of the entries in the file is not guaranteed (e.g CEO can appear in the end of the file);
- File is a valid CSV as per provided format;
- Report results are not ordered;
- Data expected to be valid and consistent.There is no handling for the cases when:
  - "salary" is not a number;
  - there are more than 1 employee with empty manager id;
  - manager is not found by id;
  - etc.
