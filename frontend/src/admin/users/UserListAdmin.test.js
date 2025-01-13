import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import UserListAdmin from './UserListAdmin';
import tokenService from '../../services/token.service';
import useFetchState from '../../util/useFetchState';

jest.mock('../../services/token.service');
jest.mock('../../util/useFetchState');

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));

describe('UserListAdmin', () => {
    beforeEach(() => {
        tokenService.getLocalAccessToken.mockReturnValue('mock-jwt-token');
        useFetchState.mockReturnValue([
            {
                content: [
                    { id: 1, username: 'user1', authority: { authority: 'ROLE_USER' } },
                    { id: 2, username: 'user2', authority: { authority: 'ROLE_ADMIN' } },
                ],
                totalPages: 2,
            },
            jest.fn(),
        ]);
    });

    test('renders user list', () => {
        render(
            <Router>
                <UserListAdmin />
            </Router>
        );

        expect(screen.getByText('USUARIOS')).toBeInTheDocument();
        expect(screen.getByText('user1')).toBeInTheDocument();
        expect(screen.getByText('user2')).toBeInTheDocument();
    });

    test('navigates to user edit page on edit button click', () => {
        render(
            <Router>
                <UserListAdmin />
            </Router>
        );

        const editButton = screen.getByLabelText('edit-1');
        fireEvent.click(editButton);

        expect(mockNavigate).toHaveBeenCalledWith('/users/1');
    });

    test('navigates to admin page on delete button click', () => {
        render(
            <Router>
                <UserListAdmin />
            </Router>
        );

        const deleteButton = screen.getByLabelText('delete-1');
        fireEvent.click(deleteButton);

        expect(mockNavigate).toHaveBeenCalledWith('/admin');
    });

    test('navigates to add new user page on add button click', () => {
        render(
            <Router>
                <UserListAdmin />
            </Router>
        );

        const addButton = screen.getByText('Añadir usuario');
        fireEvent.click(addButton);

        expect(mockNavigate).toHaveBeenCalledWith('/users/new');
    });

    test('handles pagination correctly', () => {
        render(
            <Router>
                <UserListAdmin />
            </Router>
        );

        const nextPageButton = screen.getByText('>');
        fireEvent.click(nextPageButton);

        expect(screen.getByText('2 de 2')).toBeInTheDocument();

        const prevPageButton = screen.getByText('<');
        fireEvent.click(prevPageButton);

        expect(screen.getByText('1 de 2')).toBeInTheDocument();
    });
});