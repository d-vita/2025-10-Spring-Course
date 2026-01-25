import React, { useEffect, useState } from 'react';
import axios from 'axios';

import { BooksList } from './BooksList';
import { AddBookForm } from './AddBookForm';
import { EditBookForm } from './EditBookForm';

import './styles/styles.css';

const Books = () => {
    const [books, setBooks] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [editBook, setEditBook] = useState(null);

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

    const addBook = async (book) => {
        await axios.post('/api/books', book);
        loadBooks();
        setShowAddForm(false);
    };

    const updateBook = async (book) => {
        await axios.put(`/api/books/${book.id}`, book);
        loadBooks();
        setEditBook(null);
    };

    return (
        <div>

            <h1>Books</h1>

            {!showAddForm && !editBook && (
                <button className="btn-add" onClick={() => setShowAddForm(true)}>
                    Add New Book
                </button>
            )}

            {showAddForm && (
                <AddBookForm
                    authors={[]} // authors из API
                    genres={[]}  // genres из API
                    onSubmit={addBook}
                    onCancel={() => setShowAddForm(false)}
                />
            )}


            {editBook && (
                <EditBookForm
                    book={editBook}
                    authors={[]}
                    genres={[]}
                    onSubmit={updateBook}
                    onCancel={() => setEditBook(null)}
                />
            )}

            {!showAddForm && !editBook && (
                <BooksList
                    books={books}
                    onDelete={deleteBook}
                    onEdit={setEditBook}
                />
            )}
        </div>
    );
}

export default Books;