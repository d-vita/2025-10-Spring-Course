<h3>Introduction to the Spring Framework</h3>
<h3>Homework №8:</h3>
<h3>📚 Console Application — Library Book Catalog</h3>

<p>
Migrate a library book catalog application to use Spring Data MongoDB repositories as the data access technology and MongoDB for data storage.
</p>

<h3>⚙️ Requirements</h3>

<ul>
  <li>Enable spring-boot-starter-data-mongodb.</li>
  <li>Rewrite all repositories to Spring Data MongoDB repositories.</li>
  <li>Cover all services with integration tests (with an embedded database); other tests can be removed.</li>
  <li>The solution should not contain any traces of other data access technologies (JDBC/JPA).</li>
  <li>Application operation should not result in inconsistent data.</li>
  <li>Consider the absence of cascading operations (including when using @DBRef) and the fact that MongoEventListeners work differently for different methods, and in some cases, will not work at all.</li>
  <li>Excess data should not be pulled from the database unless absolutely necessary (for example, loading all books by an author when the author is deleted or modified). If necessary, you can extend Spring Data repositories with custom functionality using MongoOperations.</li>
</ul>