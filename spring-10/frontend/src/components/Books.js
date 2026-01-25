import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './styles/styles.css';

const Books = () => {
    const [books, setBooks] = useState([]);

    const loadBooks = async () => {
        const response = await axios.get('/api/books');
        setBooks(response.data);
    };

    useEffect(() => {
        loadBooks();
    }, []);

    const deleteBook = async (id) => {
        await axios.delete(`/api/books/${id}`);
        loadBooks();
    };

    return (
        <div>

            <h1>Books</h1>

            {books.length === 0 && <p>No books</p>}

            {/* list of books */}
            <ul>
                {books.map(book => (
                    <li key={book.id}>
                        {book.title}
                        <button
                            className="btn-delete"
                            onClick={() => deleteBook(book.id)}
                        >
                            Delete
                        </button>
                    </li>
                ))}
            </ul>

        </div>
    );
}

export default Books;