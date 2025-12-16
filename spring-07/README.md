<h3>Introduction to the Spring Framework</h3>
<h3>Homework №6:</h3>
<h3>📚 Console Application — Library Book Catalog</h3>

<p>
Migrate a library book catalog application to repositories using Spring ORM, JPA, and Hibernate as data access technologies. Supplement the domain model with a book comment entity. This task builds on previous work, but also includes a stub containing tests that verify some of the task requirements. The solution must meet the following requirements. 
</p>

<h3>⚙️ Requirements</h3>

<ul>
  <li>Use Spring ORM, JPA, and Hibernate (EntityManager) as data access technologies.</li>
  <li>Add a "book comment" entity (multiple comments for a single book).</li>
  <li>Implement CRUD operations for the new entity. Retrieving all comments is not necessary. Only a specific comment by ID and all comments for a specific book by its ID.</li>
  <li>LAZY relationships should not be present in equals/hashCode/toString. Including Using @Data</li>
  <li>The N+1 problem should be solved using the same methods as in the previous work, but using the tools provided by the ORM.</li>
  <li>Cover all repositories with tests using the H2 database and @DataJpaTest.</li>
  <li>In repository tests, use TestEntityManager (only methods that do not require writing queries) to check or prepare data. Repositories themselves cannot be used for this purpose.</li>
  <li>Write integration tests for the book and comment services, verifying that access to relationships used outside the service does not cause LazyInitializationException.</li>
</ul>