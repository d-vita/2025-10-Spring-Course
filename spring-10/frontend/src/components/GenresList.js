import React from 'react';

export const GenresList = ({ genres, onBack }) => {
    return (
        <div>
            <h3 className="books-header">List of Genres:</h3>

            <table className="general">
                <thead>
                <tr>
                    <th>№</th>
                    <th>Name</th>
                </tr>
                </thead>
                <tbody>
                {genres.map((genre, index) => (
                    <tr key={genre.id}>
                        <td>{index + 1}</td>
                        <td>{genre.name}</td>
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
