import React, { useState } from 'react';

export const EditBookForm = ({ book, authors, genres, onSubmit, onCancel }) => {
    const [title, setTitle] = useState(book.title);
    const [authorId, setAuthorId] = useState(book.authorId);
    const [genreId, setGenreId] = useState(book.genreId);

    return (
        <form
            onSubmit={e => {
                e.preventDefault();
                onSubmit({ id: book.id, title, authorId, genreId });
            }}
            className="form-container"
        >
            <h3>Edit Book</h3>

            <div className="row">
                <label>Title:</label>
                <input
                    type="text"
                    value={title}
                    onChange={e => setTitle(e.target.value)}
                />
            </div>

            <div className="row">
                <label>Author:</label>
                <select value={authorId} onChange={e => setAuthorId(e.target.value)}>
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
                    {genres.map(g => (
                        <option key={g.id} value={g.id}>
                            {g.name}
                        </option>
                    ))}
                </select>
            </div>

            <div className="actions">
                <button type="submit" className="btn-save">
                    Save
                </button>
                <button type="button" className="btn-cancel" onClick={onCancel}>
                    Cancel
                </button>
            </div>
        </form>
    );
};