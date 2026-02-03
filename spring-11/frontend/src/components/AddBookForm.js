import React, { useState } from 'react';

export const AddBookForm = ({ authors, genres, onSubmit, onCancel }) => {
    const [title, setTitle] = useState('');
    const [authorId, setAuthorId] = useState('');
    const [genreId, setGenreId] = useState('');

    return (
        <form
            onSubmit={e => {
                e.preventDefault();
                onSubmit({ title, authorId, genreId });
            }}
            className="form-container"
        >
            <h3>Add New Book</h3>

            <div className="row">
                <label>Title:</label>
                <input
                    type="text"
                    value={title}
                    onChange={e => setTitle(e.target.value)}
                    placeholder="Enter book title"
                />
            </div>

            <div className="row">
                <label>Author:</label>
                <select value={authorId} onChange={e => setAuthorId(e.target.value)}>
                    <option value="">-- choose author --</option>
                    {authors.map(a => (
                        <option key={a.id} value={a.id}>
                            {a.fullName}
                        </option>
                    ))}
                </select>
            </div>

            <div className="row">
                <label>Genre:</label>
                <select value={genreId} onChange={e => setGenreId(e.target.value)}>
                    <option value="">-- choose genre --</option>
                    {genres.map(g => (
                        <option key={g.id} value={g.id}>
                            {g.name}
                        </option>
                    ))}
                </select>
            </div>

            <div className="actions">
                <button type="submit" className="btn-add">
                    Add
                </button>
                <button type="button" className="btn-cancel" onClick={onCancel}>
                    Cancel
                </button>
            </div>
        </form>
    );
};