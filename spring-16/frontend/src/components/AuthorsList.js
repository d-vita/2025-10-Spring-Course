import React from 'react';

export const AuthorsList = ({ authors, onBack }) => {
    return (
        <div>
            <h3 className="books-header">List of Authors:</h3>

            <table className="general">
                <thead>
                <tr>
                    <th>№</th>
                    <th>Full Name</th>
                </tr>
                </thead>
                <tbody>
                {authors.map((author, index) => (
                    <tr key={author.id}>
                        <td>{index + 1}</td>
                        <td>{author.fullName}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <button
                className="btn-add btn-light-blue btn-bottom"
                onClick={onBack}
            >
                Back to Books
            </button>
        </div>
    );
};
