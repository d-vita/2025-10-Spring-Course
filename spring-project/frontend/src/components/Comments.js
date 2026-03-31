import React, { useEffect, useState } from 'react';
import axios from 'axios';

import './styles/styles.css';

export const Comments = ({ bookId, onBack }) => {
    const [book, setBook] = useState(null);
    const [comments, setComments] = useState([]);


    const loadBookData = async () => {
        try {
            const bookResponse = await axios.get(`/api/books/${bookId}`);
            setBook(bookResponse.data);

            const commentsResponse = await axios.get(`/api/books/${bookId}/comments`);
            setComments(commentsResponse.data);
        } catch (err) {
            console.error('Failed to load book or comments', err);
        }
    };

    useEffect(() => {
        loadBookData();
    }, [bookId]);

    if (!book) return <p>Loading book...</p>;

    return (
        <div className="book-card">
            {/* Book info */}
            <h3>{book.title}</h3>

            <div className="book-meta" style={{ display: 'flex', gap: '40px', marginBottom: '24px' }}>
                <div>
                    <strong>Author:</strong> <span>{book.author?.fullName || ''}</span>
                </div>
                <div>
                    <strong>Genre:</strong> <span>{book.genre?.name || ''}</span>
                </div>
            </div>

            {/* Comments */}
            <h3 className="comments-header">Comments</h3>

            <table className="general">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Comment</th>
                </tr>
                </thead>
                <tbody>
                {comments.length > 0 ? (
                    comments.map((comment, index) => (
                        <tr key={comment.id || index}>
                            <td>{index + 1}</td>
                            <td>{comment.message}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="2" className="no-comments">No comments yet</td>
                    </tr>
                )}
                </tbody>
            </table>

            {/* Back button */}
            <div style={{ marginTop: '20px' }}>
                <button className="btn-add btn-light-blue" onClick={onBack}>
                    Back to Books
                </button>
            </div>
        </div>
    );
};
