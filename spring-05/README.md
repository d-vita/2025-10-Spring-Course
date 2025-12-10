<h3>Introduction to the Spring Framework</h3>
<h3>Homework №5:</h3>
<h3>📚 Console Application — Library Book Catalog</h3>

<p>
Develop a <b>single-user console application</b> for managing a library book catalog. 
Each book has an author and at least one genre (depending on the chosen complexity level).  
</p>

<h3>⚙️ Requirements</h3>

<ul>
  <li>Use <b>Spring JDBC</b> and a <b>H2 relational database</b>.</li>
  <li>Use <b>NamedParameterJdbcTemplate</b> for parameterized SQL queries.</li>
  <li>Each entity (Book, Author, Genre) must be stored in its own table.</li>
  <li>The <b>Book → Author</b> relationship is many-to-one (a book has one author; an author can have multiple books).</li>
  <li>The <b>Book → Genre</b> relationship depends on the complexity: either many-to-one (a book has one genre) or many-to-many (a book can have multiple genres).</li>
  <li>The user interface must be implemented using <b>Spring Shell</b>. For the <b>Book</b> entity, provide commands for creating, updating, deleting, listing all books, and fetching a book by id.</li>
  <li>Provide commands to list all <b>Author</b> and <b>Genre</b> entities.</li>
  <li>The database schema and initial data must be created and loaded using <b>schema.sql</b> and <b>data.sql</b>.</li>
  <li>Do not use abstract or generic entities or DAOs.</li>
  <li>Do not create bidirectional relationships (Book has Author, Author does not store Books).</li>
  <li>The N+1 query problem must be solved.</li>
  <li>Use <b>@JdbcTest</b> to write integration tests for all Genre DAO methods using the embedded database.</li>
</ul>