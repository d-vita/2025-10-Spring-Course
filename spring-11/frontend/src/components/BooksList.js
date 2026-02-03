import React from 'react';

export const BooksList = ({ books, onDelete, onEdit, onViewComments }) => {
    return (
        <table className="general">
            <thead>
            <tr>
                <th>Title</th>
                <th>Author</th>
                <th>Genre</th>
                <th>Comments</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
            </thead>
            <tbody>
            {books.map(book => (
                <tr key={book.id}>
                    <td>{book.title}</td>
                    <td>{book.author?.fullName || ''}</td>
                    <td>{book.genre?.name || ''}</td>
                    <td>
                        <button className="edit-link" onClick={() => onViewComments(book.id)}>
                            Comments
                        </button>
                    </td>
                    <td>
                        <button className="btn-edit" onClick={() => onEdit(book)}>
                            Edit
                        </button>
                    </td>
                    <td>
                        <button className="btn-delete" onClick={() => onDelete(book.id)}>
                            Delete
                        </button>
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
};
