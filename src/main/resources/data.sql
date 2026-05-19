INSERT IGNORE  INTO book (book_id, book_name, book_author, available, price, quantity, release_date)

VALUES
(54, 'Clean Code', 'Robert C. Martin', true, 45.99, 10, '2008-08-01'),
(87, 'Effective Java', 'Joshua Bloch', true, 55.50, 7, '2018-01-06'),
(213, 'Spring in Action', 'Craig Walls', true, 49.99, 5, '2022-03-15'),
(1223, 'Java Concurrency in Practice', 'Brian Goetz', false, 60.00, 3, '2006-05-19'),
(1234, 'Design Patterns', 'Erich Gamma', true, 39.99, 12, '1994-10-21'),
(7878, 'Head First Java', 'Kathy Sierra', true, 35.75, 8, '2005-02-09'),
(6766, 'Refactoring', 'Martin Fowler', false, 47.20, 4, '2018-11-19'),
(67643, 'The Pragmatic Programmer', 'Andrew Hunt', true, 42.30, 6, '2019-09-13'),
(66743, 'Microservices Patterns', 'Chris Richardson', true, 58.90, 9, '2018-10-27'),
(12, 'Domain-Driven Design', 'Eric Evans', false, 65.00, 2, '2003-08-30') ,
(345, 'Code Complete', 'Steve McConnell', true, 44.90, 11, '2004-06-09'),
(346, 'Working Effectively with Legacy Code', 'Michael Feathers', true, 50.00, 6, '2004-09-30'),
(347, 'Test Driven Development: By Example', 'Kent Beck', true, 38.75, 7, '2002-11-08'),
(348, 'Patterns of Enterprise Application Architecture', 'Martin Fowler', false, 54.60, 4, '2002-11-15'),
(349, 'Clean Architecture', 'Robert C. Martin', true, 46.80, 9, '2017-09-20'),
(350, 'The Mythical Man-Month', 'Frederick P. Brooks Jr.', true, 33.50, 5, '1995-08-02'),
(351, 'Release It!', 'Michael T. Nygard', true, 48.25, 8, '2018-01-30'),
(352, 'Continuous Delivery', 'Jez Humble', true, 57.40, 6, '2010-07-27'),
(353, 'Building Microservices', 'Sam Newman', true, 52.10, 10, '2021-02-12'),
(354, 'Site Reliability Engineering', 'Betsy Beyer', false, 59.90, 3, '2016-03-23')
;

