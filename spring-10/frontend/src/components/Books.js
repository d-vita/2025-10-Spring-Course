import React, { useEffect, useState } from 'react';
import axios from 'axios';

import { BooksList } from './BooksList';
import { AddBookForm } from './AddBookForm';
import { EditBookForm } from './EditBookForm';

import './styles/styles.css';

const Books = () => {
    const [genres, setGenres] = useState([]);
    const [authors, setAuthors] = useState([]);
    const [books, setBooks] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [editBook, setEditBook] = useState(null);

    const loadBooks = async () => {
        const response = await axios.get('/api/books');
        setBooks(response.data);
    };

    const loadAuthors = async () => {
        const response = await axios.get('/api/authors');
        setAuthors(response.data);
    };

    const loadGenres = async () => {
        const response = await axios.get('/api/genres');
        setGenres(response.data);
    };

    useEffect(() => {
        loadBooks();
    }, []);

    const deleteBook = async (id) => {
        await axios.delete(`/api/books/${id}`);
        loadBooks();
        loadAuthors();
        loadGenres();
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
                    authors={authors}
                    genres={genres}
                    onSubmit={addBook}
                    onCancel={() => setShowAddForm(false)}
                />
            )}


            {editBook && (
                <EditBookForm
                    book={editBook}
                    authors={authors}
                    genres={genres}
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